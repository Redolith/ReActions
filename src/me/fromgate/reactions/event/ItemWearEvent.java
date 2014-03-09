package me.fromgate.reactions.event;

import me.fromgate.reactions.util.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemWearEvent extends RAEvent {

    public ItemWearEvent (Player p){
        super(p);
    }

    public boolean isItemWeared(String itemStr){
        for (ItemStack armour : getPlayer().getInventory().getArmorContents())
            if (ItemUtil.compareItemStr(armour, itemStr)) return true;
        return false;
    }


}