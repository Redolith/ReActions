/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2014, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/reactions/
 *   * 
 *  This file is part of ReActions.
 *  
 *  ReActions is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ReActions is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with ReActions.  If not, see <http://www.gnorg/licenses/>.
 * 
 */

package me.fromgate.reactions.timer;

import me.fromgate.reactions.RAUtil;
import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.event.EventManager;
import me.fromgate.reactions.util.Param;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Timers {
    /*
        Command example:
	    /react add timer <name> activator:<exec> time:<HH:MM,HH:MM|0_0/5_*_*_*_?> [player:<player>] [world:<world>] 


		Example Cron Expressions
		CronTrigger Example 1 - an expression to create a trigger that simply fires every 5 minutes
		"0 0/5 * * * ?"

		CronTrigger Example 2 - an expression to create a trigger that fires every 5 minutes, at 10 seconds after the
		minute (i.e. 10:00:10 am, 10:05:10 am, etc.).
		"10 0/5 * * * ?"

		CronTrigger Example 3 - an expression to create a trigger that fires at 10:30, 11:30, 12:30, and 13:30, on
		every Wednesday and Friday.

		"0 30 10-13 ? * WED,FRI"
		CronTrigger Example 4 - an expression to create a trigger that fires every half hour between the hours of 8 am
		and 10 am on the 5th and 20th of every month. Note that the trigger will NOT fire at 10:00 am, just at 8:00,
		8:30, 9:00 and 9:30
		"0 0/30 8-9 5,20 * ?"
		Note that some scheduling requirements are too complicated to express with a single trigger - such as "every 5
		minutes between 9:00 am and 10:00 am, and every 20 minutes between 1:00 pm and 10:00 pm". The solution in
		this scenario is to simply create two triggers, and register both of them to run the same job.

	 */

	/*
     * TODO
	 * нужно будет добавить поддержку временных поясов
	 * 	
	 */


    private static BukkitTask ingameTimer = null;
    private static String currentIngameTime;
    private static BukkitTask serverTimer = null;

    private static RAUtil u() {
        return ReActions.util;
    }

    private static Map<String, Timer> timers;
    private static Set<String> timersIngame;


    public static boolean addTimer(String name, Param params) {
        return addTimer(null, name, params, false);
    }

    public static void listTimers(CommandSender sender, int page) {
        List<String> timerList = new ArrayList<String>();
        Map<String, Timer> timers = getIngameTimers();
        for (String id : timers.keySet()) {
            Timer timer = timers.get(id);
            timerList.add((timer.isPaused() ? "&c" : "&2") + id + " &a" + timer.toString());
        }
        timers = getServerTimers();
        for (String id : timers.keySet()) {
            Timer timer = timers.get(id);
            timerList.add((timer.isPaused() ? "&c" : "&2") + id + " &a" + timer.toString());
        }
        u().printPage(sender, timerList, page, "msg_timerlist", "", true, 15);
    }

    public static boolean removeTimer(CommandSender sender, String name) {
        if (name.isEmpty()) return u().returnMSG(false, sender, "msg_timerneedname");
        if (!timers.containsKey(name)) return u().returnMSG(false, sender, "msg_timerunknownname", name);
        timers.remove(name);
        save();
        return u().returnMSG(true, sender, "msg_timerremoved", name);
    }

    public static boolean addTimer(CommandSender sender, String name, Param params, boolean save) {
        if (name.isEmpty()) return false;
        if (timers.containsKey(name)) return u().returnMSG(false, sender, "msg_timerexist", name);
        if (params.isEmpty()) return u().returnMSG(false, sender, "msg_timerneedparams");
        if (params.getParam("activator", "").isEmpty()) return u().returnMSG(false, sender, "msg_timerneedactivator");
        if (!params.isParamsExists("timer-type")) return u().returnMSG(false, sender, "msg_timerneedtype");
        if (!params.isParamsExists("time")) return u().returnMSG(false, sender, "msg_timerneedtime");
        Timer timer = new Timer(params);
        timers.put(name, timer);
        updateIngameTimers();
        if (save) save();
        return u().returnMSG(true, sender, "msg_timeradded", name);
    }

    public static Map<String, Timer> getIngameTimers() {
        Map<String, Timer> ingameTimers = new TreeMap<String, Timer>(String.CASE_INSENSITIVE_ORDER);
        for (String key : timers.keySet()) {
            Timer timer = timers.get(key);
            if (timer.isIngameTimer()) ingameTimers.put(key, timer);
        }
        return ingameTimers;
    }

    public static Map<String, Timer> getServerTimers() {
        Map<String, Timer> serverTimers = new TreeMap<String, Timer>(String.CASE_INSENSITIVE_ORDER);
        for (String key : timers.keySet()) {
            Timer timer = timers.get(key);
            if (!timer.isIngameTimer()) serverTimers.put(key, timer);
        }
        return serverTimers;
    }


    public static void updateIngameTimers() {
        timersIngame.clear();
        Map<String, Timer> ingame = getIngameTimers();
        for (String key : ingame.keySet()) {
            Timer timer = ingame.get(key);
            timersIngame.addAll(timer.getIngameTimes());
        }
    }


    public static void init() {
        currentIngameTime = "";
        timersIngame = new HashSet<String>();
        timers = new TreeMap<String, Timer>(String.CASE_INSENSITIVE_ORDER);
        load();
        initIngameTimer();
        initServerTimer();
    }

    public static void initIngameTimer() {
        if (ingameTimer != null) return;
        ingameTimer = Bukkit.getScheduler().runTaskTimerAsynchronously(ReActions.instance, new Runnable() {
            @Override
            public void run() {
                String currentTime = Time.currentIngameTime();
                if (currentIngameTime.equalsIgnoreCase(currentTime)) return;
                currentIngameTime = currentTime;
                if (!timersIngame.contains(currentIngameTime)) return;
                Map<String, Timer> timers = getIngameTimers();
                for (String key : timers.keySet()) {
                    Timer timer = timers.get(key);
                    if (timer.isTimeToRun())
                        EventManager.raiseExecEvent(null, timer.getParams());
                }
            }
        }, 1, 4); //1 По идее так не упустим, хотя.... ;)
    }

    public static void initServerTimer() {
        if (serverTimer != null) return;
        serverTimer = Bukkit.getScheduler().runTaskTimerAsynchronously(ReActions.instance, new Runnable() {
            @Override
            public void run() {
                for (Timer timer : getServerTimers().values())
                    if (timer.isTimeToRun())
                        EventManager.raiseExecEvent(null, timer.getParams());
				/*
				Map<String,Timer> timers = getServerTimers();
				for (String key : timers.keySet()){
					Timer timer = timers.get(key);
					if (timer.isTimeToRun())
						EventManager.raiseExecEvent(null, timer.getParams());					
				}*/
            }
        }, 1, 20);
    }


    public static String getCurrentIngameTime() {
        return currentIngameTime;
    }

    public static void onDisable() {
        ingameTimer.cancel();
        serverTimer.cancel();
    }

    public static void load() {
        timers.clear();
        YamlConfiguration cfg = new YamlConfiguration();
        File f = new File(ReActions.instance.getDataFolder() + File.separator + "timers.yml");
        if (!f.exists()) return;
        try {
            cfg.load(f);
        } catch (Exception e) {
            u().log("Failed to save timers.yml file");
            return;
        }
        for (String timerType : cfg.getKeys(false)) {
            if (!(timerType.equalsIgnoreCase("INGAME") || timerType.equalsIgnoreCase("SERVER"))) continue;
            ConfigurationSection cs = cfg.getConfigurationSection(timerType);
            if (cs == null) continue;
            for (String timerId : cs.getKeys(false)) {
                ConfigurationSection csParams = cs.getConfigurationSection(timerId);
                if (csParams == null) continue;
                Param params = new Param();
                params.set("timer-type", timerType);
                for (String param : csParams.getKeys(true)) {
                    if (!csParams.isString(param)) continue;
                    params.set(param, csParams.getString(param));
                }
                addTimer(timerId, params);
            }
        }
    }

    public static void save() {
        YamlConfiguration cfg = new YamlConfiguration();
        File f = new File(ReActions.instance.getDataFolder() + File.separator + "timers.yml");
        if (f.exists()) f.delete();
        for (String name : timers.keySet()) {
            Timer timer = timers.get(name);
            Param params = timer.getParams();
            if (params.isEmpty()) continue;
            String timerType = timer.isIngameTimer() ? "INGAME" : "SERVER";
            String root = timerType + "." + name + ".";
            for (String key : params.keySet()) {
                if (key.equalsIgnoreCase("timer-type")) continue;
                if (key.equalsIgnoreCase("param-line")) continue;
                cfg.set(root + key, key.equalsIgnoreCase("time") ? params.getParam(key).replace("_", " ") : params.getParam(key));
            }
        }
        try {
            cfg.save(f);
        } catch (IOException e) {
            u().log("Failed to save timers.yml file");
        }
    }

    public static boolean isTimerExists(String timerName) {
        return timers.containsKey(timerName);
    }

    public static boolean setPause(String timerName, boolean pause) {
        if (timers.isEmpty()) return false;
        if (!(timerName.equalsIgnoreCase("all") || isTimerExists(timerName))) return false;
        if (timerName.equalsIgnoreCase("all")) {
            for (Timer timer : timers.values())
                timer.setPause(pause);
        } else {
            Timer timer = timers.get(timerName);
            timer.setPause(pause);
        }
        return true;
    }

    public static boolean isTimerWorking(String timerName) {
        if (!isTimerExists(timerName)) return false;
        return (!timers.get(timerName).isPaused());
    }

}

