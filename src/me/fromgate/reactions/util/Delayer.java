/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2013, fromgate, fromgate@gmail.com
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

package me.fromgate.reactions.util;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import me.fromgate.reactions.ReActions;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;


public class Delayer {

    public static HashMap<String,Long> delays = new HashMap<String,Long>();

    public static void save(){
        try {
            YamlConfiguration cfg = new YamlConfiguration();
            File f = new File (ReActions.instance.getDataFolder()+File.separator+"delay.yml");
            if (f.exists()) f.delete();
            f.createNewFile();
            for (String key : delays.keySet()){
                long delaytime = delays.get(key);
                if (delaytime>System.currentTimeMillis())
                    cfg.set(key, delaytime);
            }
            cfg.save(f);
        } catch (Exception e){
        }
    }

    public static void load(){
        delays.clear();
        try {
            YamlConfiguration cfg = new YamlConfiguration();
            File f = new File (ReActions.instance.getDataFolder()+File.separator+"delay.yml");
            if (!f.exists()) return;
            cfg.load(f);
            for (String key : cfg.getKeys(true)){
                if (!key.contains(".")) continue;
                long delaytime = cfg.getLong(key);
                if (delaytime>System.currentTimeMillis())
                    delays.put(key, delaytime);
            }
        } catch (Exception e){
        }
    }


    public static boolean checkDelay (String id){
        String idd = id.contains(".") ? id : "global."+id;
        if (!delays.containsKey(idd)) return true;
        return (delays.get(idd)<System.currentTimeMillis());
    }

    public static boolean checkPersonalDelay (Player p, String id){
        return checkDelay (p.getName()+"."+id);
    }

    public static void setDelay(String id, Long seconds){
        setDelay(id,seconds,true);
    }

    public static void setDelay(String id, Long seconds, boolean save){
        delays.put(id.contains(".") ? id : "global."+id, System.currentTimeMillis()+seconds);
        if (save) save();
    }

    public static void setPersonalDelay(Player p, String id, Long seconds){
        setDelay (p.getName()+"."+id, seconds,true);
    }

    public static void setPersonalDelay(String playerName, String id, Long seconds){
        setDelay (playerName+"."+id, seconds,true);
    }

    public static String longTimeToString(long time){
        Date date = new Date(time);
        DateFormat formatter = new SimpleDateFormat("dd-MM-YYYY HH:mm:ss");
        return formatter.format(date);
    }
    
    public static void printDelayList (CommandSender p, int page, int lpp) {
        List<String> lst = new ArrayList<String>();
        for (String key : delays.keySet()){
            long delaytime = delays.get(key);
            if (delaytime<System.currentTimeMillis()) continue;
            String [] ln = key.split("\\.",2);
            if (ln.length!=2) continue;
            lst.add("["+ln[0] + "] "+ln[1]+": "+longTimeToString(delays.get(key)));
        }
        Collections.sort(lst);
        ReActions.util.printPage(p, lst, page, "msg_listdelay", "", true,lpp);
    }


}
