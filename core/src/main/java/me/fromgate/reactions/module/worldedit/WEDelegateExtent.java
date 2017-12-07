/**
 * Created by MaxDikiy on 17/10/2017.
 */
package me.fromgate.reactions.module.worldedit;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.extent.Extent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class WEDelegateExtent extends AbstractDelegateExtent {
    private final Player player;

    /**
     * Create a new instance.
     *
     * @param actor
     * @param extent the extent
     */
    public WEDelegateExtent(Actor actor, Extent extent) {
        super(extent);
        this.player = Bukkit.getPlayer(actor.getUniqueId());
    }

    @Override
    public boolean setBlock(Vector location, BaseBlock block) throws WorldEditException {
        return !WEListener.raiseWEChangeEvent(player, location, block) && super.setBlock(location, block);
    }

}
