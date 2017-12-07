package me.fromgate.reactions.event;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 2017-05-14.
 */
public class PlayerBlockBreakEvent extends RAEvent {
    private Block block;
    private Boolean isDropItems;

    public PlayerBlockBreakEvent(Player p, Block block, Boolean isDropItems) {
        super(p);
        this.block = block;
        this.isDropItems = isDropItems;
    }

    public Block getBlockBreak() {
        return this.block;
    }

    public Boolean isDropItems() {
        return this.isDropItems;
    }

    public void setDropItems(Boolean isDropItems) {
        this.isDropItems = isDropItems;
    }

    public Location getBlockBreakLocation() {
        return block.getLocation();
    }

}
