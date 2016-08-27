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

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.util.Param;
import org.quartz.CronExpression;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Timer {

    private boolean timerType;
    private Set<String> timesIngame;
    private CronExpression timeServer;
    private Param params;
    private boolean pause;

    public Timer(Param params2) {
        this.timesIngame = new HashSet<String>();
        this.params = params2;
        this.timerType = params2.getParam("timer-type", "ingame").equalsIgnoreCase("ingame");
        this.pause = params2.getParam("paused", false);
        params2.set("paused", String.valueOf(this.pause));
        this.parseTime();

    }

    /**
     * @return true = ingame, false - server
     */
    public boolean getTimerType() {
        return this.timerType;
    }

    public Param getParams() {
        return this.params;
    }

    public void parseTime() {
        if (this.timerType) {
            this.timesIngame = new HashSet<String>();
            for (String time : params.getParam("time", "").split(",\\S*")) {
                this.timesIngame.add(time);
            }
        } else {
            String time = params.getParam("time", "").replace("_", " ");
            try {
                this.timeServer = new CronExpression(time);
            } catch (ParseException e) {
                ReActions.util.logOnce(time, "Failed to parse cron format: " + time);
                this.timeServer = null;
                e.printStackTrace();
            }
        }
    }


    public boolean isTimeToRun() {
        if (isPaused()) return false;
        return this.timerType ? isIngameTimeToRun() : isServerTimeToRun();
    }

    private boolean isServerTimeToRun() {
        if (this.timerType) return false;
        if (this.timeServer == null) return false;
        return (this.timeServer.isSatisfiedBy(new Date()));
    }

    private boolean isIngameTimeToRun() {
        return timerType && timesIngame.contains(Time.currentIngameTime());
    }

    public boolean isIngameTimer() {
        return this.timerType;
    }

    public Set<String> getIngameTimes() {
        return this.timesIngame;
    }

    public String toString() {
        return params.getParam("activator", "Undefined") + " : " + params.getParam("time", "Undefined") + (this.isIngameTimer() ? " (ingame)" : " (server)");
    }

    public boolean isPaused() {
        return this.pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

}
