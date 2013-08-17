package me.fromgate.reactions.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RACommandEvent extends Event implements Cancellable {
    //TODO Реализовать возможность отмены событий из активатора...

    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private String command;
    private Player player;

    public RACommandEvent (Player p, String command) {
        this.command = command;
        this.player = p;
    }

    public String getCommand() {
        return this.command;
    }

    public Player getPlayer() {
        return this.player;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel; 
    }
    
}
