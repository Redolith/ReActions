package me.fromgate.reactions.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TimeIngameEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private String currentTime;
    private Player player;

    public TimeIngameEvent (Player p, String currentTime) {
        this.player = p;
        this.currentTime = currentTime;
    }

    public String getIngameTime(){
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