package me.fromgate.reactions.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class ItemClickEvent extends Event {
    private Player player;
    private static final HandlerList handlers = new HandlerList();
    
    public ItemClickEvent (Player p){
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

    public ItemStack getItem() {
        return player.getItemInHand();
    }

}
