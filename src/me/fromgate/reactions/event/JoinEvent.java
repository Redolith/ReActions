package me.fromgate.reactions.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class JoinEvent extends Event {
    private boolean firstjoin = false;
    private Player player;
    private static final HandlerList handlers = new HandlerList();
    
    public JoinEvent (Player p, boolean firstjoin){
        this.player = p;
        this.firstjoin = firstjoin;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    
    public Player getPlayer(){
        return this.player;
    }
    public boolean isFirstJoin(){
        return this.firstjoin;
    }
}
