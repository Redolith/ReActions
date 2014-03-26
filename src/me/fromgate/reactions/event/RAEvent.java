package me.fromgate.reactions.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RAEvent extends Event /*implements Cancellable */ {
	
	private static final HandlerList handlers = new HandlerList();
	protected Player player;
	private boolean cancelled = false;
	
    public Player getPlayer() {
        return this.player;
    }

    public RAEvent (Player player){
    	this.player = player;
    }


	
	public boolean isCancelled() {
		return this.cancelled;
	}

	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

    public static HandlerList getHandlerList() {
        return handlers;
    }
	
	public HandlerList getHandlers() {
		return handlers;
	}
	
	
	


}
