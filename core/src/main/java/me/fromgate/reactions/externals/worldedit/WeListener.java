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

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.event.extent.EditSessionEvent;
import com.sk89q.worldedit.event.platform.PlayerInputEvent;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.util.eventbus.Subscribe;
import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.event.WeChangeEvent;
import me.fromgate.reactions.event.WeSelectionRegionEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static me.fromgate.reactions.externals.worldedit.RaWorldEdit.getRegionSelector;
import static me.fromgate.reactions.externals.worldedit.RaWorldEdit.getSelection;

public class WeListener {
    private static Region regionSelection = null;

    private static ReActions plg() {
        return ReActions.instance;
    }

    @Subscribe
    public void onEditSessionEvent(EditSessionEvent event) {
        Actor actor = event.getActor();
        if (actor != null && actor.isPlayer()) {
            Player player = Bukkit.getPlayer(actor.getUniqueId());
            Bukkit.getScheduler().runTaskLater(plg(), () -> {
                Selection selection = getSelection(player);
                if (selection != null) {
                    Region region = null;
                    try {
                        region = selection.getRegionSelector().getRegion();
                        if (region != null) {
                            // Check Region Selection
                            checkChangeSelectionRegion(player, selection, region);
                        }
                    } catch (IncompleteRegionException ignored) {
                        // e.printStackTrace();
                    }
                }
            }, 2);

            if (event.getStage() == EditSession.Stage.BEFORE_CHANGE) {
                event.setExtent(new WeDelegateExtent(actor, event.getExtent()));
            }
        }
    }

    @Subscribe
    public void onPlayerInputEvent(PlayerInputEvent event) throws CommandException, IncompleteRegionException {
        Player player = Bukkit.getPlayer(event.getPlayer().getUniqueId());
        if (player != null) {
            Selection selection = getSelection(player);
            if (selection != null) {
                Region region = selection.getRegionSelector().getRegion();
                // Check Region Selection
                checkChangeSelectionRegion(player, selection, region);
            }
        }
    }

    public void checkChangeSelectionRegion(Player player, Selection selection, Region region) {
        if (regionSelection == null || region != null && !region.toString().equals(regionSelection.toString())) {
            regionSelection = region.clone();
            if (raiseChangeSelectionRegionEvent(player, selection, regionSelection)) {
                regionSelection = null;
                RegionSelector rs = getRegionSelector(player);
                if (rs != null) rs.clear();
            }
        }
    }

    public static boolean raiseChangeSelectionRegionEvent(Player player, Selection selection, Region region) {
        WeSelection weSelection = new WeSelection(selection.getRegionSelector().getTypeName(),
                selection.getMinimumPoint(), selection.getMaximumPoint(),
                selection.getArea(), selection.getWorld(), region.toString());
        WeSelectionRegionEvent e = new WeSelectionRegionEvent(player, weSelection);
        Bukkit.getServer().getPluginManager().callEvent(e);
        return e.isCancelled();
    }

    @SuppressWarnings("deprecation")
    public static boolean raiseWEChangeEvent(Player player, Location location, Material blockType) {
        WeChangeEvent e = new WeChangeEvent(player, location, blockType);
        Bukkit.getServer().getPluginManager().callEvent(e);
        return e.isCancelled();
    }

}


