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

package me.fromgate.reactions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class RAWorldGuard {

    private ReActions plg;
    private WorldGuardPlugin worldguard;
    boolean connected = false;

    public RAWorldGuard (ReActions plg){
        this.plg = plg;
        this.connected = connectToWorldGuard();
    }

    private boolean connectToWorldGuard(){
        Plugin twn = plg.getServer().getPluginManager().getPlugin("WorldGuard");
        if ((twn != null)&&(twn instanceof WorldGuardPlugin)){
            this.worldguard = (WorldGuardPlugin) twn;
            return true;
        }
        return false;
    }

    public List<String> getRegions(Location loc){
        List<String> rgs = new ArrayList<String>();
        if (!this.connected) return rgs; //Empty!!!
        ApplicableRegionSet rset = worldguard.getRegionManager(loc.getWorld()).getApplicableRegions(loc);
        if ((rset == null)||(rset.size()==0)) return rgs; //Empty!!!
        for (ProtectedRegion rg : rset ) rgs.add(rg.getId());
        return rgs; 
    }

    public List<String> getRegions (Player p){
        return getRegions (p.getLocation());
    }

    public int countPlayersInRegion (String rg){
        if (!this.connected) return 0;
        int count = 0;
        for (Player p : Bukkit.getOnlinePlayers())
            if (isPlayerInRegion (p, rg)) count++;
        return count;
    }

    public List<Player> playersInRegion (String rg){
        List<Player> plrs = new ArrayList<Player>();
        if (!this.connected) return plrs;
        for (Player p : Bukkit.getOnlinePlayers())
            if (isPlayerInRegion (p, rg)) plrs.add(p);
        return plrs;
    }


    public boolean isPlayerInRegion (Player p, String rg){
        if (!this.connected) return false;
        List<String> rgs = getRegions(p);
        if (rgs.isEmpty()) return false;
        return rgs.contains(rg);
    }

    public boolean isRegionExists(String rg){
        if (!this.connected) return false;
        if (rg.isEmpty()) return false;
        for (World w : Bukkit.getWorlds()){
            if (worldguard.getRegionManager(w).getRegions().containsKey(rg)) return true;
        }
        return false;
    }

    public List<Location> getRegionMinMaxLocations(String rg){
        List<Location> locs = new ArrayList<Location>();
        if (!this.connected) return locs;
        ProtectedRegion prg = null;
        World world = null;
        for (World w : Bukkit.getWorlds()){
            if (worldguard.getRegionManager(w).getRegions().containsKey(rg)){
                prg = worldguard.getRegionManager(w).getRegionExact(rg);
                world = w;
                break;
            }
        }
        if (world == null) return locs;
        if (prg== null) return locs;
        locs.add(new Location (world, prg.getMinimumPoint().getX(),prg.getMinimumPoint().getY(),prg.getMinimumPoint().getZ()));
        locs.add(new Location (world, prg.getMaximumPoint().getX(),prg.getMaximumPoint().getY(),prg.getMaximumPoint().getZ()));
        return locs;
    }
    
    public List<Location> getRegionLocations(String rg, boolean land){
        List<Location> locs = new ArrayList<Location>();
        if (!this.connected) return locs;
        ProtectedRegion prg = null;
        World world = null;
        for (World w : Bukkit.getWorlds()){
            if (worldguard.getRegionManager(w).getRegions().containsKey(rg)){
                prg = worldguard.getRegionManager(w).getRegionExact(rg);
                world = w;
                break;
            }
        }
        if(prg != null){
            for (int x = prg.getMinimumPoint().getBlockX(); x<=prg.getMaximumPoint().getBlockX(); x++)
                for (int y = prg.getMinimumPoint().getBlockY(); y<=prg.getMaximumPoint().getBlockY(); y++)
                    for (int z = prg.getMinimumPoint().getBlockZ(); z<=prg.getMaximumPoint().getBlockZ(); z++){
                        Location t = new Location (world,x,y,z);
                        if (t.getBlock().isEmpty()&&t.getBlock().getRelative(BlockFace.UP).isEmpty()){
                            if (land&&t.getBlock().getRelative(BlockFace.DOWN).isEmpty()) continue;
                            t.add(0.5, 0, 0.5);
                            locs.add(t);
                        }
                    }
        }
        return locs;
    }

}
