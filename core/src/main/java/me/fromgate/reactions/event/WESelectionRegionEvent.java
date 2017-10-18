package me.fromgate.reactions.event;

import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.entity.Player;

public class WESelectionRegionEvent extends RAEvent {
    private Region region;
    private Selection selection;

    public WESelectionRegionEvent(Player player, Selection selection, Region region) {
        super(player);
        this.selection = selection;
        this.region = region;
    }

    public Selection getSelection() {
        return this.selection;
    }

    public Region getRegion() {
        return this.region;
    }
}
