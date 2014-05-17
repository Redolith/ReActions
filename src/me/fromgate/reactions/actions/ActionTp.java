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

package me.fromgate.reactions.actions;

import java.util.Map;

import me.fromgate.reactions.externals.RAEffects;
import me.fromgate.reactions.util.Locator;
import me.fromgate.reactions.util.ParamUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ActionTp extends Action{

    @Override
    public boolean execute(Player p, Map<String, String> params) {
        Location loc = teleportPlayer (p,params);
        if (loc!=null) this.setMessageParam(Locator.locationToStringFormated(loc));
        return (loc!=null);
    }

    private Location teleportPlayer (Player p, Map<String,String> params){
        Location loc = null;
        int radius = 0;
        if (params.isEmpty()) return null;
        if (params.containsKey("param")) {
            loc = Locator.parseLocation(ParamUtil.getParam(params, "param", ""), p.getLocation());
        } else { 
            loc = Locator.parseLocation(ParamUtil.getParam(params, "loc", ""), p.getLocation());
            radius = ParamUtil.getParam(params, "radius", 0);
        }
        boolean land = ParamUtil.getParam(params, "land", true);

        if (loc != null){
            if (radius>0) loc = Locator.getRadiusLocation(loc, radius, land); 
            if (plg().isCenterTpLocation()) {
                loc.setX(loc.getBlockX()+0.5);
                loc.setZ(loc.getBlockZ()+0.5);
            }
            try{
                while (!loc.getChunk().isLoaded()) loc.getChunk().load();
            } catch (Exception e) {
            }
            p.teleport(loc);
            String playeffect = ParamUtil.getParam(params, "effect", "");
            if (!playeffect.isEmpty()){
                if (playeffect.equalsIgnoreCase("smoke")&&(!params.containsKey("wind"))) params.put("wind", "all");
                RAEffects.playEffect(loc, playeffect, params);
            }
        }
        return loc;
    }


}
