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

import me.fromgate.reactions.util.Locator;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class ActionSignSet extends Action {

    @Override
    public boolean execute(Player p, Param params) {
        // loc:world,x,y,z line1:text line2:text line3:text line4:text clear:1,2,3,4
        String locStr = params.getParam("loc", Variables.getTempVar("sign_loc"));
        if (locStr.isEmpty()) return false;
        Location loc = Locator.parseCoordinates(locStr);
        if (loc == null) return false;
        boolean chunkLoad = params.getParam("loadchunk", false);
        if (!chunkLoad && !loc.getChunk().isLoaded()) return false;
        Block block = loc.getBlock();
        if (block.getType() != Material.SIGN_POST && block.getType() != Material.WALL_SIGN) return false;
        Sign sign = (Sign) block.getState();
        for (int i = 1; i <= 4; i++) {
            String line = params.getParam("line" + Integer.toString(i), "");
            if (line.isEmpty()) continue;
            if (line.length() > 15) line = line.substring(0, 15);
            sign.setLine(i - 1, ChatColor.translateAlternateColorCodes('&', line));
        }

        String clear = params.getParam("clear", "");
        if (!clear.isEmpty()) {
            String[] ln = clear.split(",");
            for (String cl : ln) {
                if (!u().isInteger(cl)) continue;
                int num = Integer.parseInt(cl) - 1;
                if (num < 0) continue;
                if (num >= 4) continue;
                sign.setLine(num, "");
            }
        }
        sign.update(true);
        return true;
    }

}
