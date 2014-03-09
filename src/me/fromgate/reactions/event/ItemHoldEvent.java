package me.fromgate.reactions.event;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemHoldEvent extends RAEvent {
    public ItemHoldEvent (Player p){
        super(p);
    }
    
    public ItemStack getItem() {
        return this.getPlayer().getItemInHand();
    }

}