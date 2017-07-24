package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.DamageByBlockEvent;
import me.fromgate.reactions.util.Locator;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.item.ItemUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Created by MaxDikiy on 2017-07-23.
 */
public class DamageByBlockActivator extends Activator {
    private String blockStr;
    private String blockLocation;
    private String damageCause;

    public DamageByBlockActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }

    public DamageByBlockActivator(String name, Block targetBlock, String param) {
        super(name, "activators");
        this.blockLocation = "";
        this.blockStr = "";
        Param params = new Param(param);
        if (targetBlock != null) {
            this.blockLocation = Locator.locationToString(targetBlock.getLocation());
            this.blockStr = (targetBlock.getType()).toString();
        }
        String bt = params.getParam("block", "");
        if (this.blockStr.isEmpty() || this.blockStr.equals("AIR") || !bt.isEmpty() && !this.blockStr.equalsIgnoreCase(bt)) {
            this.blockStr = bt;
            this.blockLocation = params.getParam("loc", "");
        }
        this.damageCause = getCauseByName(params.getParam("cause", "ANY"));
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean activate(Event event) {
        if (!(event instanceof DamageByBlockEvent)) return false;
        DamageByBlockEvent db = (DamageByBlockEvent) event;
        Block damagerBlock = db.getBlockDamager();
        if (damagerBlock == null) return false;
        if (!isActivatorBlock(damagerBlock)) return false;
        if (!damageCauseCheck(db.getCause())) return false;
        Variables.setTempVar("blocklocation", Locator.locationToString(db.getBlockBreakLocation()));
        Variables.setTempVar("blocktype", damagerBlock.getType().name());
        Variables.setTempVar("blockdata", String.valueOf(damagerBlock.getData()));
        Variables.setTempVar("block", ItemUtil.itemFromBlock(damagerBlock).toString());
        Variables.setTempVar("damage", Double.toString(db.getDamage()));
        Variables.setTempVar("cause", db.getCause().name());
        return Actions.executeActivator(db.getPlayer(), this);
    }

    private boolean checkLocations(Block block) {
        if (this.blockLocation.isEmpty()) return true;
        return this.isLocatedAt(block.getLocation());
    }

    private boolean isActivatorBlock(Block block) {
        if (!this.blockStr.isEmpty() && !ItemUtil.compareItemStr(block, this.blockStr)) return false;
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

    private static String getCauseByName(String damageCauseStr) {
        if (damageCauseStr != null) {
            for (EntityDamageEvent.DamageCause damageCause : EntityDamageEvent.DamageCause.values()) {
                if (damageCauseStr.equalsIgnoreCase(damageCause.name())) {
                    return damageCause.name();
                }
            }
        }
        return "ANY";
    }

    private boolean damageCauseCheck(EntityDamageEvent.DamageCause dc) {
        if (damageCause.equals("ANY")) return true;
        return dc.name().equals(damageCause);
    }

    @Override
    public void save(String root, YamlConfiguration cfg) {
        cfg.set(root + ".block", this.blockStr);
        cfg.set(root + ".location", this.blockLocation.isEmpty() ? null : this.blockLocation);
        cfg.set(root + ".cause", this.damageCause);
    }

    @Override
    public void load(String root, YamlConfiguration cfg) {
        this.blockStr = cfg.getString(root + ".block", "");
        this.blockLocation = cfg.getString(root + ".location", "");
        this.damageCause = cfg.getString(root + ".cause", "");
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.DAMAGE_BY_BLOCK;
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
        sb.append("block:").append(blockStr.isEmpty() ? "-" : blockStr);
        sb.append(" loc:").append(blockLocation.isEmpty() ? "-" : blockLocation);
        sb.append(" cause:").append(damageCause);
        sb.append(")");
        return sb.toString();
    }

}
