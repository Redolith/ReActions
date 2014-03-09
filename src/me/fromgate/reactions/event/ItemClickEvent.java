package me.fromgate.reactions.event;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemClickEvent extends RAEvent {
    public ItemClickEvent (Player p){
        super(p);
    }

    public ItemStack getItem() {
    	return this.getPlayer().getItemInHand();
    }

}
