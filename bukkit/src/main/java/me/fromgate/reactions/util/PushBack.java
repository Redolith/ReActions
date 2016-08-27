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

import me.fromgate.reactions.ReActions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;


public class PushBack {

    private static ReActions plg() {
        return ReActions.instance;
    }

    public static boolean teleportToPrev(Player p, int prev) {
        Location loc = null;
        if (prev <= 1) loc = getPlayerPrevLoc1(p);
        else {
            loc = getPlayerPrevLoc2(p);
            if (loc == null) loc = getPlayerPrevLoc1(p);
        }
        if (loc == null) return false;
        return p.teleport(loc);
    }

    private static double distance(Location loc1, Location loc2) {
        if (!loc1.getWorld().equals(loc2.getWorld())) return 1000;
        if (plg().horizontalPushback) {
            double dx = loc2.getX() - loc1.getX();
            double dy = loc2.getZ() - loc1.getZ();
            return Math.sqrt((dx * dx) + (dy * dy));
        } else return loc1.distance(loc2);
    }

    public static void rememberLocations(Player p, Location from, Location to) {
        Location prev1 = getPlayerPrevLoc1(p);
        if (prev1 == null) {
            setPlayerPrevLoc1(p, from);
            setPlayerPrevLoc2(p, from);
            return;
        }
        if (distance(prev1, to) < 1) return;
        setPlayerPrevLoc2(p, prev1);
        setPlayerPrevLoc1(p, from);
    }

    private static void setPlayerPrevLoc2(Player p, Location prev2) {
        p.setMetadata("ra-pb-loc2-world", new FixedMetadataValue(plg(), prev2.getWorld().getName()));
        p.setMetadata("ra-pb-loc2-x", new FixedMetadataValue(plg(), prev2.getX()));
        p.setMetadata("ra-pb-loc2-y", new FixedMetadataValue(plg(), prev2.getY()));
        p.setMetadata("ra-pb-loc2-z", new FixedMetadataValue(plg(), prev2.getZ()));
        p.setMetadata("ra-pb-loc2-yaw", new FixedMetadataValue(plg(), prev2.getYaw()));
        p.setMetadata("ra-pb-loc2-pitch", new FixedMetadataValue(plg(), prev2.getPitch()));
    }

    private static void setPlayerPrevLoc1(Player p, Location prev1) {
        p.setMetadata("ra-pb-loc1-world", new FixedMetadataValue(plg(), prev1.getWorld().getName()));
        p.setMetadata("ra-pb-loc1-x", new FixedMetadataValue(plg(), prev1.getX()));
        p.setMetadata("ra-pb-loc1-y", new FixedMetadataValue(plg(), prev1.getY()));
        p.setMetadata("ra-pb-loc1-z", new FixedMetadataValue(plg(), prev1.getZ()));
        p.setMetadata("ra-pb-loc1-yaw", new FixedMetadataValue(plg(), prev1.getYaw()));
        p.setMetadata("ra-pb-loc1-pitch", new FixedMetadataValue(plg(), prev1.getPitch()));
    }


    public static void clear(Player p) {
        if (p.hasMetadata("ra-pb-loc1-world")) p.removeMetadata("ra-pb-loc1-world", plg());
        if (p.hasMetadata("ra-pb-loc1-x")) p.removeMetadata("ra-pb-loc1-x", plg());
        if (p.hasMetadata("ra-pb-loc1-y")) p.removeMetadata("ra-pb-loc1-y", plg());
        if (p.hasMetadata("ra-pb-loc1-z")) p.removeMetadata("ra-pb-loc1-z", plg());
        if (p.hasMetadata("ra-pb-loc1-yaw")) p.removeMetadata("ra-pb-loc1-yaw", plg());
        if (p.hasMetadata("ra-pb-loc1-pitch")) p.removeMetadata("ra-pb-loc1-pitch", plg());

        if (p.hasMetadata("ra-pb-loc2-world")) p.removeMetadata("ra-pb-loc2-world", plg());
        if (p.hasMetadata("ra-pb-loc2-x")) p.removeMetadata("ra-pb-loc2-x", plg());
        if (p.hasMetadata("ra-pb-loc2-y")) p.removeMetadata("ra-pb-loc2-y", plg());
        if (p.hasMetadata("ra-pb-loc2-z")) p.removeMetadata("ra-pb-loc2-z", plg());
        if (p.hasMetadata("ra-pb-loc2-yaw")) p.removeMetadata("ra-pb-loc2-yaw", plg());
        if (p.hasMetadata("ra-pb-loc2-pitch")) p.removeMetadata("ra-pb-loc2-pitch", plg());

    }

    public static Location getPlayerPrevLoc1(Player p) {
        if (!p.hasMetadata("ra-pb-loc1-world")) return null;
        if (p.getMetadata("ra-pb-loc1-world").isEmpty()) return null;
        if (!p.hasMetadata("ra-pb-loc1-x")) return null;
        if (!p.hasMetadata("ra-pb-loc1-y")) return null;
        if (!p.hasMetadata("ra-pb-loc1-z")) return null;
        if (!p.hasMetadata("ra-pb-loc1-yaw")) return null;
        if (!p.hasMetadata("ra-pb-loc1-pitch")) return null;
        World w = Bukkit.getServer().getWorld(p.getMetadata("ra-pb-loc1-world").get(0).asString());
        if (w == null) return null;
        return new Location(w, p.getMetadata("ra-pb-loc1-x").get(0).asDouble(),
                p.getMetadata("ra-pb-loc1-y").get(0).asDouble(),
                p.getMetadata("ra-pb-loc1-z").get(0).asDouble(),
                p.getMetadata("ra-pb-loc1-yaw").get(0).asFloat(),
                p.getMetadata("ra-pb-loc1-pitch").get(0).asFloat());
    }

    public static Location getPlayerPrevLoc2(Player p) {
        if (!p.hasMetadata("ra-pb-loc2-world")) return null;
        if (p.getMetadata("ra-pb-loc2-world").isEmpty()) return null;
        if (!p.hasMetadata("ra-pb-loc2-x")) return null;
        if (!p.hasMetadata("ra-pb-loc2-y")) return null;
        if (!p.hasMetadata("ra-pb-loc2-z")) return null;
        if (!p.hasMetadata("ra-pb-loc2-yaw")) return null;
        if (!p.hasMetadata("ra-pb-loc2-pitch")) return null;
        World w = Bukkit.getServer().getWorld(p.getMetadata("ra-pb-loc2-world").get(0).asString());
        if (w == null) return null;
        return new Location(w, p.getMetadata("ra-pb-loc2-x").get(0).asDouble(),
                p.getMetadata("ra-pb-loc2-y").get(0).asDouble(),
                p.getMetadata("ra-pb-loc2-z").get(0).asDouble(),
                p.getMetadata("ra-pb-loc2-yaw").get(0).asFloat(),
                p.getMetadata("ra-pb-loc2-pitch").get(0).asFloat());
    }

}
