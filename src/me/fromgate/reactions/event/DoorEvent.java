package me.fromgate.reactions.event;

import me.fromgate.reactions.util.Util;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class DoorEvent extends RAEvent{
    private Block door_block;
    
    public DoorEvent (Player p, Block block) {
    	super(p);
        this.door_block = block;
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


}
