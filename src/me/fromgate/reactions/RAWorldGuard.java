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
import org.bukkit.Location;
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

    public boolean isPlayerInRegion (Player p, String rg){
        List<String> rgs = getRegions(p);
        if (rgs.isEmpty()) return false;
        return rgs.contains(rg);
    }

}
