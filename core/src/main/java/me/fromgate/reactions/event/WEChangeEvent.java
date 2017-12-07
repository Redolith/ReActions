/**
 * Created by MaxDikiy on 17/10/2017.
 */
package me.fromgate.reactions.event;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class WEChangeEvent extends RAEvent {
    private Player player;
    private Location location;
    private Material blockType;

    public WEChangeEvent(Player player, Location location, Material blockType) {
        super(player);
        this.player = player;
        this.location = location; //new Location(player.getWorld(), location.getX(), location.getY(), location.getZ());
        this.blockType = blockType;

    }

    public Player getPlayer() {
        return player;
    }

    public Location getLocation() {
        return location;
    }

    /*  public BaseBlock getBlock() {
          return block;
      }

      public void setBlock(BaseBlock block) {
          this.block = block;
      }
  */
    public String getBlockType() {
        return blockType.name();
    }
}
