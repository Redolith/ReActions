package me.fromgate.reactions.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TimeServerEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private long currentTime;
    private Player player;

    public TimeServerEvent (Player p, long currentTime) {
        this.player = p;
        this.currentTime = currentTime;
    }

    public long getTime(){
    	return this.currentTime;
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

}