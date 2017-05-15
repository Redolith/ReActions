package me.fromgate.reactions.event;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 2017-05-14.
 */
public class PlayerBlockBreakEvent extends RAEvent {
    private Block block;

    public PlayerBlockBreakEvent(Player p, Block block) {
        super(p);
        this.block = block;
    }

    public Block getBlockBreak() {
        return this.block;
    }

    public Location getBlockBreakLocation() {
        return block.getLocation();
    }

}
