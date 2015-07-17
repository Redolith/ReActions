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
import me.fromgate.reactions.util.item.ItemUtil;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ActionBlockSet extends Action {

    @SuppressWarnings("deprecation")
    @Override
    public boolean execute(Player p, Param params) {
        String istr = params.getParam("block", "");
        boolean phys = params.getParam("physics", true);
        if (istr.isEmpty()) return false;
        ItemStack item = ItemUtil.parseItemStack(istr); 
        if ((item==null)||((!item.getType().isBlock()))){
            u().logOnce("wrongblock"+istr, "Failed to execute action BLOCK_SET. Wrong block "+istr.toUpperCase());
            return false;
        }
        Location loc = Locator.parseLocation(params.getParam("loc", ""),null);
        if (loc == null) return false;
        Block b = loc.getBlock();
        b.setType(item.getType());
        b.setData(item.getData().getData(),phys);
        setMessageParam(ItemUtil.itemToString(item));
        return true;
    }

}
