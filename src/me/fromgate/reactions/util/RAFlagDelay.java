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

import java.util.HashMap;

import org.bukkit.entity.Player;


public class RAFlagDelay {

    public static HashMap<String,Long> delays = new HashMap<String,Long>();

    public static boolean checkDelay (String id){
        if (!delays.containsKey(id)) return true;
        return (delays.get(id)<System.currentTimeMillis());
    }

    public static boolean checkPersonalDelay (Player p, String id){
        return checkDelay (p.getName()+"#"+id);
    }

    public static void setDelay(String id, Long seconds){
        delays.put(id, System.currentTimeMillis()+seconds);
    }

    public static void setPersonalDelay(Player p, String id, Long seconds){
        setDelay (p.getName()+"#"+id, seconds);
    }
}
