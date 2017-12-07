/**
 * Created by MaxDikiy on 12/10/2017.
 */
package me.fromgate.reactions.module.worldedit;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.event.extent.EditSessionEvent;
import com.sk89q.worldedit.event.platform.PlayerInputEvent;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.util.eventbus.Subscribe;
import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.event.WEChangeEvent;
import me.fromgate.reactions.event.WESelectionRegionEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static me.fromgate.reactions.externals.RAWorldEdit.getRegionSelector;
import static me.fromgate.reactions.externals.RAWorldEdit.getSelection;

public class WEListener {
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
                    } catch (IncompleteRegionException e) {
                        // e.printStackTrace();
                    }
                }
            }, 2);

            if (event.getStage() == EditSession.Stage.BEFORE_CHANGE) {
                event.setExtent(new WEDelegateExtent(actor, event.getExtent()));
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
        WESelectionRegionEvent e = new WESelectionRegionEvent(player, selection, region);
        Bukkit.getServer().getPluginManager().callEvent(e);
        return e.isCancelled();
    }

    @SuppressWarnings("deprecation")
    public static boolean raiseWEChangeEvent(Player player, Vector location, BaseBlock block) {
        WEChangeEvent e = new WEChangeEvent(player, location, block);
        Bukkit.getServer().getPluginManager().callEvent(e);
        return e.isCancelled();
    }

}


