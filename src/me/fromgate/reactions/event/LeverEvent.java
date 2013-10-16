package me.fromgate.reactions.event;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.material.Lever;

public class LeverEvent extends Event{
    private static final HandlerList handlers = new HandlerList();
    private Block lever_block;
    private Player player;
    
    
    public LeverEvent (Player p, Block block) {
        this.lever_block = block;
        this.player = p;
    }

    public Lever getLever(){
        if (lever_block.getType() !=Material.LEVER) return null;
        return (Lever) lever_block.getState().getData();
    }
    
    public boolean isLeverPowered(){
        Lever lever = getLever();
        if (lever==null) return false;
        return lever.isPowered();
    }
    
    public Location getLeverLocation() {
        return lever_block.getLocation();
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
