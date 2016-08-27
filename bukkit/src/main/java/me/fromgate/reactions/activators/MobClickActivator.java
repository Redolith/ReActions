package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.MobClickEvent;
import me.fromgate.reactions.util.Locator;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;

public class MobClickActivator extends Activator {
    private String mobName;
    private String mobType;
    private String mobLocation;

    public MobClickActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }

    public MobClickActivator(String name, String param) {
        super(name, "activators");
        this.mobType = param;
        this.mobName = "";
        this.mobLocation = "";
        Param params = new Param(param);
        if (params.isParamsExists("type")) {
            this.mobType = params.getParam("type");
            this.mobName = params.getParam("name");
            this.mobLocation = params.getParam("loc");
        } else if (param.contains("$")) {
            this.mobName = this.mobType.substring(0, this.mobType.indexOf("$"));
            this.mobType = this.mobType.substring(this.mobName.length() + 1);
        }
    }


    @Override
    public boolean activate(Event event) {
        if (!(event instanceof MobClickEvent)) return false;
        MobClickEvent me = (MobClickEvent) event;
        if (mobType.isEmpty()) return false;
        if (me.getMob() == null) return false;

        if (!isActivatorMob(me.getMob())) return false;
        Variables.setTempVar("moblocation", Locator.locationToString(me.getMob().getLocation()));
        Variables.setTempVar("mobtype", me.getMob().getType().name());
        String mobName = getMobName(me.getMob());
        Variables.setTempVar("mobname", mobName != null && !mobName.isEmpty() ? mobName : me.getMob().getType().name());
        return Actions.executeActivator(me.getPlayer(), this);
    }

    private boolean checkLocations(LivingEntity mob) {
        if (this.mobLocation.isEmpty()) return true;
        return this.isLocatedAt(mob.getLocation());
    }

    private boolean isActivatorMob(LivingEntity mob) {
        if (!mob.getType().name().equalsIgnoreCase(this.mobType)) return false;
        if (!mobName.isEmpty()) {
            if (!ChatColor.translateAlternateColorCodes('&', mobName.replace("_", " ")).equals(getMobName(mob)))
                return false;
        } else if (!getMobName(mob).isEmpty()) return false;
        if (!checkLocations(mob)) return false;
        return true;
    }


    private String getMobName(LivingEntity mob) {
        if (mob.getCustomName() == null) return "";
        return mob.getCustomName();
    }

    @Override
    public boolean isLocatedAt(Location l) {
        if (this.mobLocation.isEmpty()) return false;
        Location loc = Locator.parseCoordinates(this.mobLocation);
        if (loc == null) return false;
        return l.getWorld().equals(loc.getWorld()) &&
                l.getBlockX() == loc.getBlockX() &&
                l.getBlockY() == loc.getBlockY() &&
                l.getBlockZ() == loc.getBlockZ();
    }


    @Override
    public void save(String root, YamlConfiguration cfg) {
        cfg.set(root + ".mob-type", this.mobType);
        cfg.set(root + ".mob-name", this.mobName.isEmpty() ? null : this.mobName);
        cfg.set(root + ".location", this.mobLocation.isEmpty() ? null : this.mobLocation);
    }

    @Override
    public void load(String root, YamlConfiguration cfg) {
        this.mobType = cfg.getString(root + ".mob-type", "");
        this.mobName = cfg.getString(root + ".mob-name", "");
        this.mobLocation = cfg.getString(root + ".location", "");
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.MOB_CLICK;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name).append(" [").append(getType()).append("]");
        if (!getFlags().isEmpty()) sb.append(" F:").append(getFlags().size());
        if (!getActions().isEmpty()) sb.append(" A:").append(getActions().size());
        if (!getReactions().isEmpty()) sb.append(" R:").append(getReactions().size());
        sb.append(" (");
        sb.append("type:").append(mobType.isEmpty() ? "-" : mobType.toUpperCase());
        sb.append(" name:").append(mobName.isEmpty() ? "-" : mobName);
        sb.append(" loc:").append(mobLocation.isEmpty() ? "-" : mobLocation);
        sb.append(")");
        return sb.toString();
    }

}
