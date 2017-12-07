package me.fromgate.reactions.event;

import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.regions.Region;
import me.fromgate.reactions.externals.worldedit.WeSelection;
import org.bukkit.entity.Player;

public class WESelectionRegionEvent extends RAEvent {

    private WeSelection selection;

    /*private Region region;
    private Selection selection; */

    public WESelectionRegionEvent(Player player, Selection selection, Region region) {
        super(player);
        // this.selection = selection;
        // this.region = region;
    }

    public WESelectionRegionEvent(Player player, WeSelection weSelection) {
        super(player);
        this.selection = weSelection;
    }

    public WeSelection getSelection() {
        return this.selection;
    }

}
