package me.fromgate.reactions.event;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemConsumeEvent extends RAEvent {

    public ItemConsumeEvent(Player p) {
        super(p);
    }

    @SuppressWarnings("deprecation")
    public ItemStack getItem() {
        return this.getPlayer().getItemInHand();
    }

}
