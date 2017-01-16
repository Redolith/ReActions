package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.MobKillEvent;
import me.fromgate.reactions.util.Locator;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;

public class MobKillActivator extends Activator {
    private String mob_name;
    private String mob_type;

    public MobKillActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }

    public MobKillActivator(String name, String param) {
        super(name, "activators");
        this.mob_type = param;
        this.mob_name = "";
        Param params = new Param(param);
        if (params.isParamsExists("type")) {
            this.mob_type = params.getParam("type");
            this.mob_name = params.getParam("name");
        } else if (param.contains("$")) {
            this.mob_name = this.mob_type.substring(0, this.mob_type.indexOf("$"));
            this.mob_type = this.mob_type.substring(this.mob_name.length() + 1);
        }
    }


    @Override
    public boolean activate(Event event) {
        if (!(event instanceof MobKillEvent)) return false;
        MobKillEvent me = (MobKillEvent) event;
        if (mob_type.isEmpty()) return false;
        if (me.getMob() == null) return false;
        if (!isActivatorMob(me.getMob())) return false;
        Variables.setTempVar("moblocation", Locator.locationToString(me.getMob().getLocation()));
        Variables.setTempVar("mobkiller", me.getPlayer() == null ? "" : me.getPlayer().getName());
        Variables.setTempVar("mobtype", me.getMob().getType().name());
        String mobName = me.getMob().getCustomName();
        Variables.setTempVar("mobname", mobName != null && !mobName.isEmpty() ? mobName : me.getMob().getType().name());
        return Actions.executeActivator(me.getPlayer(), this);
    }


    private boolean isActivatorMob(LivingEntity mob) {
        if (!mob_name.isEmpty()) {
            if (!ChatColor.translateAlternateColorCodes('&', mob_name.replace("_", " ")).equals(getMobName(mob)))
                return false;
        } else if (!getMobName(mob).isEmpty()) return false;
        return mob.getType().name().equalsIgnoreCase(this.mob_type);
    }


    private String getMobName(LivingEntity mob) {
        if (mob.getCustomName() == null) return "";
        return mob.getCustomName();
    }

    @Override
    public boolean isLocatedAt(Location l) {
        return false;
    }


    @Override
    public void save(String root, YamlConfiguration cfg) {
        cfg.set(root + ".mob-type", this.mob_type);
        cfg.set(root + ".mob-name", this.mob_name);
    }

    @Override
    public void load(String root, YamlConfiguration cfg) {
        this.mob_type = cfg.getString(root + ".mob-type", "");
        this.mob_name = cfg.getString(root + ".mob-name", "");
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.MOB_KILL;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name).append(" [").append(getType()).append("]");
        if (!getFlags().isEmpty()) sb.append(" F:").append(getFlags().size());
        if (!getActions().isEmpty()) sb.append(" A:").append(getActions().size());
        if (!getReactions().isEmpty()) sb.append(" R:").append(getReactions().size());
        sb.append(" (");
        sb.append("type:").append(mob_type.isEmpty() ? "-" : mob_type.toUpperCase());
        sb.append(" name:").append(mob_name.isEmpty() ? "-" : mob_name.isEmpty());
        sb.append(")");
        return sb.toString();
    }
}
