package me.fromgate.reactions.event;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MobClickEvent extends Event {
    
    
    private static final HandlerList handlers = new HandlerList();
    private LivingEntity mob;
    private Player player;
    
    
    public MobClickEvent (Player p,LivingEntity mob) {
        this.mob = mob;
        this.player = p;
    }

    public LivingEntity getMob(){
        return this.mob;
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
