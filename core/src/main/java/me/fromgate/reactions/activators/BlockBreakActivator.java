package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.PlayerBlockBreakEvent;
import me.fromgate.reactions.util.Locator;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

/**
 * Created by MaxDikiy on 2017-05-14.
 */
public class BlockBreakActivator extends Activator {
    private String blockType;
    private String blockLocation;

    public BlockBreakActivator(String name, Block targetBlock, String param) {
        super(name, "activators");
        this.blockLocation = "";
        this.blockType = "";
        Param params = new Param(param);
        if (targetBlock != null) {
            this.blockLocation = Locator.locationToString(targetBlock.getLocation());
            this.blockType = (targetBlock.getType()).toString();
        }
        String bt = params.getParam("type", "");
        if (this.blockType.isEmpty() || this.blockType.equals("AIR") || !bt.isEmpty() && !this.blockType.equalsIgnoreCase(bt)) {
            this.blockType = bt;
            this.blockLocation = params.getParam("loc", "");
        }
    }

    public BlockBreakActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }

    @Override
    public boolean activate(Event event) {
        if (!(event instanceof PlayerBlockBreakEvent)) return false;
        PlayerBlockBreakEvent bbe = (PlayerBlockBreakEvent) event;
        if (bbe.getBlockBreak() == null) return false;
        if (!isActivatorBlock(bbe.getBlockBreak())) return false;
        Variables.setTempVar("blocklocation", Locator.locationToString(bbe.getBlockBreakLocation()));
        Variables.setTempVar("blocktype", bbe.getBlockBreak().getType().name());
        return Actions.executeActivator(bbe.getPlayer(), this);
    }

    private boolean checkLocations(Block block) {
        if (this.blockLocation.isEmpty()) return true;
        return this.isLocatedAt(block.getLocation());
    }

    private boolean isActivatorBlock(Block block) {
        if (!(this.blockType).isEmpty() && !(block.getType()).toString().equalsIgnoreCase(this.blockType)) return false;
        return checkLocations(block);
    }

    @Override
    public boolean isLocatedAt(Location l) {
        if (this.blockLocation.isEmpty()) return false;
        Location loc = Locator.parseLocation(this.blockLocation, null);
        if (loc == null) return false;
        return l.getWorld().equals(loc.getWorld()) &&
                l.getBlockX() == loc.getBlockX() &&
                l.getBlockY() == loc.getBlockY() &&
                l.getBlockZ() == loc.getBlockZ();
    }

    @Override
    public void save(String root, YamlConfiguration cfg) {
        cfg.set(root + ".block-type", this.blockType);
        cfg.set(root + ".location", this.blockLocation.isEmpty() ? null : this.blockLocation);
    }

    @Override
    public void load(String root, YamlConfiguration cfg) {
        this.blockType = cfg.getString(root + ".block-type", "");
        this.blockLocation = cfg.getString(root + ".location", "");
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.BLOCK_BREAK;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name).append(" [").append(getType()).append("]");
        if (!getFlags().isEmpty()) sb.append(" F:").append(getFlags().size());
        if (!getActions().isEmpty()) sb.append(" A:").append(getActions().size());
        if (!getReactions().isEmpty()) sb.append(" R:").append(getReactions().size());
        sb.append(" (");
        sb.append("type:").append(blockType.isEmpty() ? "-" : blockType.toUpperCase());
        sb.append(" loc:").append(blockLocation.isEmpty() ? "-" : blockLocation);
        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
