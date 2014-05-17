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

package me.fromgate.reactions.flags;

import java.util.Map;

import me.fromgate.reactions.util.Locator;
import me.fromgate.reactions.util.ParamUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.material.Lever;

public class FlagPowered extends Flag {

    @Override
    public boolean checkFlag(Player p, String param) {
    	Map<String,String> params = ParamUtil.parseParams(param);
    	String locStr = ParamUtil.isParamExists(params, "loc") ? ParamUtil.getParam(params, "loc", "") : param;
    	if (locStr.isEmpty()) return false;
        Location loc = Locator.parseLocation(locStr,null);
        if (loc == null) return false;
        Block b = loc.getBlock();
        if (b.getType()==Material.LEVER){
            Lever lever = (Lever) b.getState().getData();
            return lever.isPowered();
        }
        return b.isBlockIndirectlyPowered();
    }

}
