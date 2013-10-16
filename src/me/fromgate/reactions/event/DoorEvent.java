package me.fromgate.reactions.event;

import me.fromgate.reactions.util.Util;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DoorEvent extends Event{
    private static final HandlerList handlers = new HandlerList();
    private Block door_block;
    private Player player;
    
    
    public DoorEvent (Player p, Block block) {
        this.door_block = block;
        this.player = p;
    }

    public Block getDoorBlock(){
        if (Util.isDoorBlock(door_block)) return door_block;
        return null;
    }
    
    public boolean isDoorOpened(){
        return Util.isOpen(door_block);
    }
    
    public Location getDoorLocation() {
        return door_block.getLocation();
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
