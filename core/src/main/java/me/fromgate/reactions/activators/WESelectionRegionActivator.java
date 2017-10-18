package me.fromgate.reactions.activators;

import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.regions.Region;
import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.WESelectionRegionEvent;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

public class WESelectionRegionActivator extends  Activator {
    private int maxBlocks;
    private int minBlocks;
    private String typeSelection;

    public WESelectionRegionActivator(String name, String param) {
        super(name, "activators");
        Param params = new Param(param);
        minBlocks = params.getParam("minblocks", 0);
        maxBlocks = params.getParam("maxblocks", 0);
        typeSelection = params.getParam("type", "ANY");
    }

    public WESelectionRegionActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }

    @Override
    public boolean activate(Event event) {
        if (!(event instanceof WESelectionRegionEvent)) return false;
        WESelectionRegionEvent e = (WESelectionRegionEvent) event;
        Selection selection = e.getSelection();
        if (selection == null) return false;

        int selectionBlocks = selection.getArea();
        Variables.setTempVar("selblocks", Integer.toString(selectionBlocks));
        if (selectionBlocks < minBlocks) return false;
        if (selectionBlocks > maxBlocks && maxBlocks != 0) return false;

        String selType = selection.getRegionSelector().getTypeName();
        Variables.setTempVar("seltype", selType);
        if (!checkTypeSelection(selType)) return false;

        Region region = e.getRegion();
        if (region == null) return false;

        World world = selection.getWorld();
        Variables.setTempVar("world", (world != null) ? world.getName() : "");

        Variables.setTempVar("region", region.toString());
        return Actions.executeActivator(e.getPlayer(), this);
    }

    public boolean checkTypeSelection (String selType) {
        return typeSelection.isEmpty() || typeSelection.equalsIgnoreCase("ANY") || typeSelection.equalsIgnoreCase(selType);
    }

    @Override
    public boolean isLocatedAt(Location loc) {
        return false;
    }

    @Override
    public void save(String root, YamlConfiguration cfg) {
        cfg.set(root + ".min-blocks", this.minBlocks);
        cfg.set(root + ".max-blocks", this.maxBlocks);
        cfg.set(root + ".type", this.typeSelection);
    }

    @Override
    public void load(String root, YamlConfiguration cfg) {
        this.minBlocks = cfg.getInt(root + ".min-blocks", 0);
        this.maxBlocks = cfg.getInt(root + ".max-blocks", 0);
        this.typeSelection = cfg.getString(root + ".type", "ANY");
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.WE_SELECTION_REGION;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name).append(" [").append(getType()).append("]");
        if (!getFlags().isEmpty()) sb.append(" F:").append(getFlags().size());
        if (!getActions().isEmpty()) sb.append(" A:").append(getActions().size());
        if (!getReactions().isEmpty()) sb.append(" R:").append(getReactions().size());
        sb.append(" (");
        sb.append("minblocks:").append(minBlocks);
        sb.append(" maxblocks:").append(maxBlocks);
        sb.append(" type:").append(typeSelection);
        sb.append(")");
        return sb.toString();
    }
}
