package me.fromgate.reactions.event;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.material.Lever;

public class LeverEvent extends RAEvent{
    private Block lever_block;
    
    
    public LeverEvent (Player p, Block block) {
    	super(p);
        this.lever_block = block;
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

}
