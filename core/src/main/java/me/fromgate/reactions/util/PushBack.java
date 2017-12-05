/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2017, fromgate, fromgate@gmail.com
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
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;


public class PushBack {

    private static Map<String, Location> prevLocs1 = new HashMap<>();
    private static Map<String, Location> prevLocs2 = new HashMap<>();

    public static boolean teleportToPrev(Player player, int prev) {
        Location loc;
        if (prev <= 1) loc = getPlayerPrevLoc1(player);
        else {
            loc = getPlayerPrevLoc2(player);
            if (loc == null) loc = getPlayerPrevLoc1(player);
        }
        if (loc == null) return false;
        return player.teleport(loc);
    }

    private static double distance(Location loc1, Location loc2) {
        if (!loc1.getWorld().equals(loc2.getWorld())) return 1000;
        if (Cfg.horizontalPushback) {
            double dx = loc2.getX() - loc1.getX();
            double dy = loc2.getZ() - loc1.getZ();
            return Math.sqrt((dx * dx) + (dy * dy));
        } else return loc1.distance(loc2);
    }

    private static boolean isSameBlock(Location loc1, Location loc2) {
        if (Cfg.horizontalPushback) {
            return loc1.getWorld().equals(loc2.getWorld()) && loc1.getBlockX() == loc2.getBlockX() && loc1.getBlockZ() == loc2.getBlockZ();
        } else {
            return Util.isSameBlock(loc1, loc2);
        }
    }

    public static void rememberLocations(Player player, Location from, Location to) {
        Location prev1 = getPlayerPrevLoc1(player);
        if (prev1 == null) {
            setPlayerPrevLoc1(player, from);
            setPlayerPrevLoc2(player, from);
        } else if (isSameBlock(prev1, to)) {
            setPlayerPrevLoc2(player, prev1);
            setPlayerPrevLoc1(player, from);
        }
    }

    private static void setPlayerPrevLoc1(Player player, Location prev1) {
        prevLocs1.put(player.getName(), prev1);
    }

    private static void setPlayerPrevLoc2(Player player, Location prev2) {
        prevLocs2.put(player.getName(), prev2);
    }

    public static void clear(Player player) {
        if (prevLocs1.containsKey(player.getName())) {
            prevLocs1.remove(player.getName());
        }
        if (prevLocs2.containsKey(player.getName())) {
            prevLocs2.remove(player.getName());
        }
    }

    public static Location getPlayerPrevLoc1(Player player) {
        return prevLocs1.getOrDefault(player.getName(), null);
    }

    public static Location getPlayerPrevLoc2(Player player) {
        return prevLocs2.getOrDefault(player.getName(), null);
    }
}