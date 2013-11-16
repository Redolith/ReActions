package me.fromgate.reactions.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PVPRespawnEvent extends Event{
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private Player deadplayer;


    public PVPRespawnEvent (Player player, Player killedplayer){
        this.player = player;
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
