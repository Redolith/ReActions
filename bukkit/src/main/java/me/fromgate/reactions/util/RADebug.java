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

package me.fromgate.reactions.util;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class RADebug {
    private static HashMap<String, Boolean> debug = new HashMap<String, Boolean>();

    public static void setPlayerDebug(Player p, boolean debugmode) {
        debug.put(p.getName(), debugmode);
    }

    public static void offPlayerDebug(Player p) {
        if (debug.containsKey(p.getName())) debug.remove(p.getName());
    }

    public static boolean checkFlagAndDebug(Player p, boolean flag) {
        if ((p != null) && debug.containsKey(p.getName())) return (debug.get(p.getName()));
        return flag;
    }

}
