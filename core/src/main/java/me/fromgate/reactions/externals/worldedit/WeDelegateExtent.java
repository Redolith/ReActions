/*
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2017, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/reactions/
 *
 *  This file is part of ReActions.
 *
 *  ReActions is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ReActions is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with ReActions.  If not, see <http://www.gnorg/licenses/>.
 *
 */

package me.fromgate.reactions.externals.worldedit;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.extent.Extent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;


public class WeDelegateExtent extends AbstractDelegateExtent {
    private final Player player;

    /**
     * Create a new instance.
     *
     * @param actor
     * @param extent the extent
     */
    public WeDelegateExtent(Actor actor, Extent extent) {
        super(extent);
        this.player = Bukkit.getPlayer(actor.getUniqueId());
    }

    @Override
    public boolean setBlock(Vector vector, BaseBlock block) throws WorldEditException {
        Location loc = new Location(player.getWorld(), vector.getX(), vector.getY(), vector.getZ());
        Material blockType = Material.getMaterial(block.getId());
        return !WeListener.raiseWEChangeEvent(player, loc, blockType) && super.setBlock(vector, block);
    }

}
