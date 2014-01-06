package me.fromgate.reactions.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PVPDeathEvent extends Event {
    
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private Player deadplayer;


    public PVPDeathEvent (Player killer, Player killedplayer){
        this.player = killer;
        this.deadplayer = killedplayer;
    }


    public Player getPlayer() {
        return this.deadplayer;
    }

    public Player getKiller() {
        return this.player;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
