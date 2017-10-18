/**
 * Created by MaxDikiy on 17/10/2017.
 */
package me.fromgate.reactions.event;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WEChangeEvent extends RAEvent {
    private Player player;
    private Location location;
    private BaseBlock block;

    public WEChangeEvent(Player player, Vector location, BaseBlock block) {
        super(player);
        this.player = player;
        this.location = new Location(player.getWorld(), location.getX(), location.getY(), location.getZ());
        this.block = block;
    }

    public Player getPlayer() {
        return player;
    }

    public Location getLocation() {
        return location;
    }

    public BaseBlock getBlock() {
        return block;
    }

    public void setBlock(BaseBlock block) {
        this.block = block;
    }
}
