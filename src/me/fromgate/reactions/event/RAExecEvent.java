package me.fromgate.reactions.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RAExecEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private String activator; //// ?????
    private Player player;
    private Player targetPlayer;

    public RAExecEvent (Player p, Player targetPlayer, String activator) {
        this.player = p;
        this.targetPlayer = targetPlayer;
        this.activator = activator;
    }

    public String getCommand() {
        return this.activator;
    }

    public Player getPlayer() {
        return this.player;
    }
    
    public Player getTargetPlayer(){
        return this.targetPlayer;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}