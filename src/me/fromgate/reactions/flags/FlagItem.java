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

import me.fromgate.reactions.util.ItemUtil;
import me.fromgate.reactions.util.Variables;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FlagItem extends Flag{
    private int flagType = 0;
    
    public FlagItem (int flagType){
        this.flagType = flagType;
    }

    @Override
    public boolean checkFlag(Player p, String itemStr) {
        switch (flagType){
        case 0: 
        	Variables.setTempVar("item_amount", p.getItemInHand() == null ? "0" : String.valueOf(p.getItemInHand().getAmount()));
        	ItemUtil.compareItemStr(p.getItemInHand(), itemStr);
        case 1: return ItemUtil.hasItemInInventory(p, itemStr);
        case 2: return isItemWeared (p,itemStr); 
        }
        return false;
    }
    
    public boolean isItemWeared(Player player, String itemStr){
        for (ItemStack armour : player.getInventory().getArmorContents())
            if (ItemUtil.compareItemStr(armour, itemStr)) return true;
        return false;
    }

        

}
