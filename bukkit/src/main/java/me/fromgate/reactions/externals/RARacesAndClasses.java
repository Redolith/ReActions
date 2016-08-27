/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2014, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/reactions/
 *    
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

package me.fromgate.reactions.externals;

import de.tobiyas.racesandclasses.APIs.ClassAPI;
import de.tobiyas.racesandclasses.APIs.RaceAPI;
import me.fromgate.reactions.ReActions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class RARacesAndClasses {

    private static boolean enabled = false;

    public static boolean isEnabled() {
        return enabled;
    }

    public static void init() {
        try {
            enabled = isRacesAndClassesInstalled();
        } catch (Exception e) {
        }
        if (enabled) ReActions.util.log("RacesAndClasses found");
    }

    private static boolean isRacesAndClassesInstalled() {
        Plugin pe = Bukkit.getServer().getPluginManager().getPlugin("RacesAndClasses");
        return (pe != null);
    }

    // Флаг RNC_RACE
    public static boolean checkRace(Player player, String race) {
        if (!enabled) return false;
        return RaceAPI.getRaceNameOfPlayer(player).equalsIgnoreCase(race);
    }

    // Действие RNC_SET_RACE
    public static boolean setRace(Player player, String race) {
        if (!enabled) return false;
        if (player == null) return false;
        return RaceAPI.addPlayerToRace(player, race);
    }

    // Флаг RNC_CLASS
    public static boolean checkClass(Player player, String className) {
        if (!enabled) return false;
        return ClassAPI.getClassNameOfPlayer(player).equalsIgnoreCase(className);
    }

    // Действие RNC_SET_CLASS
    public static boolean setClass(Player player, String className) {
        if (!enabled) return false;
        if (player == null) return false;
        return ClassAPI.addPlayerToClass(player, className);
    }


}
