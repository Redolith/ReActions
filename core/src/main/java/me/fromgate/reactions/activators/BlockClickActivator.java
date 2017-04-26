package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.BlockClickEvent;
import me.fromgate.reactions.util.Locator;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;
import org.bukkit.block.Block;

public class BlockClickActivator extends Activator {
    private String blockType;
    private String blockLocation;
    private ClickType click;

    public BlockClickActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }

    public BlockClickActivator(String name, String param) {
        super(name, "activators");
        this.blockType = param;
        this.blockLocation = "";
        Param params = new Param(param);
        if (params.isParamsExists("type")) {
            this.blockType = params.getParam("type");
            this.blockLocation = params.getParam("loc");
        }
        this.click = ClickType.getByName(params.getParam("click", "ANY"));
    }


    @Override
    public boolean activate(Event event) {
        if (!(event instanceof BlockClickEvent )) return false;
        BlockClickEvent  bce = (BlockClickEvent ) event;
        if (bce.getBlockClick() == null) return false;
        if (!isActivatorBlock(bce.getBlockClick())) return false;
        if (!clickCheck(bce.isLeftClicked())) return false;
        Variables.setTempVar("blocklocation", Locator.locationToString(bce.getBlockClick().getLocation()));
        Variables.setTempVar("blocktype", bce.getBlockClick().getType().name());
        Variables.setTempVar("click", bce.isLeftClicked() ? "left" : "right");
        return Actions.executeActivator(bce.getPlayer(), this);
    }

    private boolean checkLocations(Block block) {
        if (this.blockLocation.isEmpty()) return true;
        return this.isLocatedAt(block.getLocation());
    }

    private boolean isActivatorBlock(Block block) {
        if (!(this.blockType).isEmpty() && !(block.getType()).toString().equalsIgnoreCase(this.blockType)) return false;
        if (!checkLocations(block)) return false;
        return true;
    }


    @Override
    public boolean isLocatedAt(Location l) {
        if (this.blockLocation.isEmpty()) return false;
        Location loc = Locator.parseCoordinates(this.blockLocation);
        if (loc == null) return false;
        return l.getWorld().equals(loc.getWorld()) &&
                l.getBlockX() == loc.getBlockX() &&
                l.getBlockY() == loc.getBlockY() &&
                l.getBlockZ() == loc.getBlockZ();
    }


    @Override
    public void save(String root, YamlConfiguration cfg) {
        cfg.set(root + ".block-type", this.blockType);
        cfg.set(root + ".click-type", click.name());
        cfg.set(root + ".location", this.blockLocation.isEmpty() ? null : this.blockLocation);
    }

    @Override
    public void load(String root, YamlConfiguration cfg) {
        this.blockType = cfg.getString(root + ".block-type", "");
        click = ClickType.getByName(cfg.getString(root + ".click-type", "ANY"));
        this.blockLocation = cfg.getString(root + ".location", "");
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.BLOCK_CLICK;
    }

    enum ClickType {
        RIGHT,
        LEFT,
        ANY;

        public static ClickType getByName(String clickStr) {
            if (clickStr.equalsIgnoreCase("left")) return ClickType.LEFT;
            if (clickStr.equalsIgnoreCase("any")) return ClickType.ANY;
            return ClickType.RIGHT;
        }
    }

    private boolean clickCheck(boolean leftClick) {
        switch (click) {
            case ANY:
                return true;
            case LEFT:
                return leftClick;
            case RIGHT:
                return !leftClick;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name).append(" [").append(getType()).append("]");
        if (!getFlags().isEmpty()) sb.append(" F:").append(getFlags().size());
        if (!getActions().isEmpty()) sb.append(" A:").append(getActions().size());
        if (!getReactions().isEmpty()) sb.append(" R:").append(getReactions().size());
        sb.append(" (");
        sb.append("type:").append(blockType.isEmpty() ? "-" : blockType.toUpperCase());
        sb.append(" click:").append(this.click.name());
        sb.append(" loc:").append(blockLocation.isEmpty() ? "-" : blockLocation);
        sb.append(")");
        return sb.toString();
    }

}
