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

import org.bukkit.Bukkit;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Time {


    public static long getIngameTime() {
        return Bukkit.getWorlds().get(0).getTime();
    }

    public static String currentIngameTime() {
        return ingameTimeToString(Bukkit.getWorlds().get(0).getTime());
    }

    public Long timeToTicks(Long time) {
        //1000 ms = 20 ticks
        return Math.max(1, (time / 50));
    }

    public static String ingameTimeToString(long ingameTime) {
        return ingameTimeToString(ingameTime, false);
    }

    public static String ingameTimeToString(long time, boolean showms) {
        String timeStr = "";
        int hours = (int) ((time / 1000 + 8) % 24);
        int minutes = (int) (60 * (time % 1000) / 1000);
        timeStr = String.format("%02d:%02d", hours, minutes);
        if (showms && (time < 1000)) timeStr = Long.toString(time) + "ms";
        return timeStr;
    }

    public static String fullTimeToString(long time, String fromat) {
        Date date = new Date(time);
        DateFormat formatter = new SimpleDateFormat(fromat);
        return formatter.format(date);
    }

    public static String fullTimeToString(long time) {
        return fullTimeToString(time, "dd-MM-YYYY HH:mm:ss");
        /*Date date = new Date(time);
        DateFormat formatter = new SimpleDateFormat("dd-MM-YYYY HH:mm:ss");
        return formatter.format(date);*/
    }


}
