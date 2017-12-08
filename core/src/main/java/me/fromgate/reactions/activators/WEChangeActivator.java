/**
 * Created by MaxDikiy on 17/10/2017.
 */
package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.WeChangeEvent;
import me.fromgate.reactions.externals.RaWorldGuard;
import me.fromgate.reactions.util.Locator;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

public class WEChangeActivator extends Activator {
    private String blockType;
    private String region;

    public WEChangeActivator(String name, String param) {
        super(name, "activators");
        Param params = new Param(param);
        blockType = params.getParam("block-type");
        region = params.getParam("region", "");
        blockType();
    }

    public WEChangeActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }

    @Override
    public boolean activate(Event event) {
        if (!(event instanceof WeChangeEvent)) return false;
        WeChangeEvent e = (WeChangeEvent) event;
        String type = e.getBlockType();
        Variables.setTempVar("blocktype", type);
        if (!checkBlockType(type)) return false;
        Variables.setTempVar("blocklocation", Locator.locationToString(e.getLocation()));

        if (!region.isEmpty() && !RaWorldGuard.isLocationInRegion(e.getLocation(), region)) return false;

        return Actions.executeActivator(e.getPlayer(), this);
    }

    public boolean checkBlockType(String blockType) {
        blockType();
        return blockType.isEmpty() || this.blockType.equalsIgnoreCase("ANY") || this.blockType.equalsIgnoreCase(blockType);
    }

    public void blockType() {
        String bType = blockType.toUpperCase();
        if (bType.isEmpty()) blockType = "ANY";
        else if (StringUtils.isNumeric(bType)) {
            int bTypeID = Integer.parseInt(bType);
            //noinspection deprecation
            blockType = Material.getMaterial(bTypeID).name();
        } else if (!bType.equalsIgnoreCase("ANY") && Material.getMaterial(bType) != null)
            blockType = Material.getMaterial(bType).name();
        else blockType = "ANY";
    }

    @Override
    public boolean isLocatedAt(Location loc) {
        return false;
    }

    @Override
    public void save(String root, YamlConfiguration cfg) {
        cfg.set(root + ".block-type", this.blockType);
        cfg.set(root + ".region", this.region);
    }

    @Override
    public void load(String root, YamlConfiguration cfg) {
        this.blockType = cfg.getString(root + ".block-type", "");
        this.region = cfg.getString(root + ".region", "");
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.WE_CHANGE;
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
        sb.append("block-type:").append(blockType.isEmpty() ? "ANY" : blockType.toUpperCase());
        sb.append(" region:").append(region.isEmpty() ? "-" : region.toUpperCase());
        sb.append(")");
        return sb.toString();
    }
}
