package me.fromgate.reactions.event;

import me.fromgate.reactions.util.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class ItemWearEvent extends Event {
    private Player player;
    private static final HandlerList handlers = new HandlerList();

    public ItemWearEvent (Player p){
        this.player = p;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }


    public Player getPlayer() {
        return player;
    }

    public boolean isItemWeared(String itemStr){
        for (ItemStack armour : this.player.getInventory().getArmorContents())
            if (ItemUtil.compareItemStr(armour, itemStr)) return true;
        return false;
    }


}