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

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Selector {
    private static Map<String, Location> locs = new HashMap<String, Location>();

    @SuppressWarnings("deprecation")
    public static void selectLocation(Player p, Location loc) {
        if (p == null) return;
        if (loc == null) loc = p.getTargetBlock((Set<Material>) null, 100).getLocation();
        locs.put(p.getName(), loc);
    }

    public static Location getSelectedLocation(Player p) {
        if (p != null && locs.containsKey(p.getName())) return locs.get(p.getName());
        return null;
    }

    public static String getSelectedStrLoc(Player p) {
        Location loc = getSelectedLocation(p);
        if (loc == null) return "";
        return Locator.locationToString(loc);
    }

}
