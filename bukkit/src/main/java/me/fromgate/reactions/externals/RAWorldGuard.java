/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2015, fromgate, fromgate@gmail.com
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

import me.fromgate.reactions.activators.*;
import me.fromgate.reactions.module.wgbridge.WGBridge;
import me.fromgate.reactions.module.wgbridge.WGBridge6x;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RAWorldGuard {

    private static WGBridge bridge = null;
    private static Set<String> regionActivators = null;

    public static boolean init() {
        bridge = getWGBridge();
        updateRegionCache();
        return isConnected();
    }

    public static WGBridge getWGBridge() {
        Plugin twn = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        if (twn == null) return null;
        if (!twn.getClass().getName().equals("com.sk89q.worldguard.bukkit.WorldGuardPlugin")) return null;
        String version = twn.getDescription().getVersion().replaceAll("[^0-9]", "");
        if (version.startsWith("5")) {
            try {
                Class<?> c = Class.forName("me.fromgate.reactions.module.wgbridge.WGBridge5x");
                return (WGBridge) c.newInstance();
            } catch (Exception e) {
            }
        }
        return new WGBridge6x();
    }

    public static void updateRegionCache() {
        regionActivators = new HashSet<String>();
        for (Activator a : Activators.getActivators(ActivatorType.REGION)) {
            RegionActivator r = (RegionActivator) a;
            regionActivators.add(r.getRegion());
        }
        for (Activator a : Activators.getActivators(ActivatorType.REGION_ENTER)) {
            RgEnterActivator r = (RgEnterActivator) a;
            regionActivators.add(r.getRegion());
        }
        for (Activator a : Activators.getActivators(ActivatorType.REGION_LEAVE)) {
            RgLeaveActivator r = (RgLeaveActivator) a;
            regionActivators.add(r.getRegion());
        }
    }

    public static List<String> getRegions(Location loc) {
        List<String> regions = new ArrayList<String>();
        for (String rg : regionActivators) {
            if (bridge.isLocationInRegion(loc, rg))
                regions.add(WGBridge6x.getFullRegionName(rg));
        }
        return regions;
    }

    public static List<String> getRegions(Player p) {
        return getRegions(p.getLocation());
    }

    public static int countPlayersInRegion(String rg) {
        return bridge.countPlayersInRegion(rg);
    }

    public static List<Player> playersInRegion(String rg) {
        return bridge.playersInRegion(rg);
    }

    public static boolean isPlayerInRegion(Player p, String rg) {
        return bridge.isPlayerInRegion(p, rg);
    }

    public static boolean isRegionExists(String rg) {
        return bridge.isRegionExists(rg);
    }

    public static List<Location> getRegionMinMaxLocations(String rg) {
        return bridge.getRegionMinMaxLocations(rg);
    }

    public static List<Location> getRegionLocations(String rg, boolean land) {
        return bridge.getRegionLocations(rg, land);
    }

    public static boolean isPlayerIsMemberOrOwner(Player p, String region) {
        return bridge.isPlayerIsMemberOrOwner(p, region);
    }

    public static boolean isPlayerIsOwner(Player p, String region) {
        return bridge.isPlayerIsOwner(p, region);
    }

    public static boolean isPlayerIsMember(Player p, String region) {
        return bridge.isPlayerIsMember(p, region);
    }

    public static boolean isConnected() {
        return bridge != null && bridge.isConnected();
    }
}
