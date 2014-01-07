package me.fromgate.reactions.flags;

import me.fromgate.reactions.util.ItemUtil;
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
        case 0: return ItemUtil.compareItemStr(p.getItemInHand(), itemStr);
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
