package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.DamageByMobEvent;
import me.fromgate.reactions.util.Locator;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.regex.Pattern;

/**
 * Created by MaxDikiy on 2017-06-25.
 */
public class DamageByMobActivator extends Activator {
    private final static Pattern FLOAT = Pattern.compile("[0-9]+(\\.?[0-9]*)?");

    private String damagerName;
    private String damagerType;
    private String entityType;
    private String damageCause;

    public DamageByMobActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }

    public DamageByMobActivator(String name, String param) {
        super(name, "activators");
        this.damagerType = param;
        this.damagerName = "";
        Param params = new Param(param);
        if (params.isParamsExists("type")) {
            this.damagerType = getDamagerTypeByName(params.getParam("type", "ANY"));
            this.damagerName = params.getParam("name");
        } else if (param.contains("$")) {
            this.damagerName = getDamagerTypeByName(this.damagerType.substring(0, this.damagerType.indexOf("$")));
            this.damagerType = this.damagerType.substring(this.damagerName.length() + 1);
        } else {
            this.damagerType = getDamagerTypeByName(params.getParam("type", "ANY"));
        }
        this.entityType = getDamagerTypeByName(params.getParam("etype", "ANY"));
        this.damageCause = getCauseByName(params.getParam("cause", "ANY"));
    }

    @Override
    public boolean activate(Event event) {
        if (!(event instanceof DamageByMobEvent)) return false;
        DamageByMobEvent pde = (DamageByMobEvent) event;
        if (damagerType.isEmpty()) return false;
        LivingEntity damager = pde.getDamager();
        Entity entity = pde.getEntityDamager();
        if (damager != null && !isActivatorDamager(damager)) return false;
        if (entity != null && !isActivatorEntity(entity)) return false;
        if (!damageCauseCheck(pde.getCause())) return false;
        Variables.setTempVar("damagerlocation", (damager != null) ? Locator.locationToString(damager.getLocation()) : "");
        Variables.setTempVar("damagertype", (damager != null) ? damager.getType().name() : "");
        Variables.setTempVar("entitytype", pde.getEntityDamager().getType().name());
        Player player = damager instanceof Player ? (Player) damager : null;
        String damagerName = (player == null) ? ((damager != null) ? damager.getCustomName() : "") : player.getName();
        Variables.setTempVar("damagername", damagerName != null && !damagerName.isEmpty() ? damagerName : ((damager != null) ? damager.getType().name() : ""));
        Variables.setTempVar("damage", Double.toString(pde.getDamage()));
        Variables.setTempVar("cause", pde.getCause().name());
        boolean result = Actions.executeActivator(pde.getPlayer(), this);
        String dmgStr = Variables.getTempVar("damage");
        if (FLOAT.matcher(dmgStr).matches()) pde.setDamage(Double.parseDouble(dmgStr));
        return result;
    }
    private boolean isActivatorDamager(LivingEntity damager) {
        if (!damagerName.isEmpty()) {
            if (!ChatColor.translateAlternateColorCodes('&', damagerName.replace("_", " ")).equals(getMobName(damager)))
                return false;
        } //else if (!getMobName(damager).isEmpty()) return false;
        if (damagerType.equalsIgnoreCase("ANY")) return true;
        return damager.getType().name().equalsIgnoreCase(this.damagerType);
    }

    private boolean isActivatorEntity(Entity entity) {
        if (entityType.equalsIgnoreCase("ANY")) return true;
        return entity.getType().name().equalsIgnoreCase(this.entityType);
    }


    private String getMobName(Entity mob) {
        if (mob.getCustomName() == null) return "";
        return mob.getCustomName();
    }

    @Override
    public boolean isLocatedAt(Location l) {
        return false;
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

    private static String getDamagerTypeByName(String damagerTypeStr) {
        if (damagerTypeStr != null) {
            for (EntityType damager : EntityType.values()) {
                if (damagerTypeStr.equalsIgnoreCase(damager.name())) {
                    return damager.name();
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
        cfg.set(root + ".damager-type", this.damagerType);
        cfg.set(root + ".damager-name", this.damagerName);
        cfg.set(root + ".entity-type", this.entityType);
        cfg.set(root + ".cause", this.damageCause);
    }

    @Override
    public void load(String root, YamlConfiguration cfg) {
        this.damagerType = cfg.getString(root + ".damager-type", "");
        this.damagerName = cfg.getString(root + ".damager-name", "");
        this.entityType = cfg.getString(root + ".entity-type", "");
        this.damageCause = cfg.getString(root + ".cause", "");
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.DAMAGE_BY_MOB;
    }

    @Override
    public boolean isValid() {
        return true;//!Util.emptySting(damagerType);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name).append(" [").append(getType()).append("]");
        if (!getFlags().isEmpty()) sb.append(" F:").append(getFlags().size());
        if (!getActions().isEmpty()) sb.append(" A:").append(getActions().size());
        if (!getReactions().isEmpty()) sb.append(" R:").append(getReactions().size());
        sb.append(" (");
        sb.append("type:").append(damagerType.isEmpty() ? "-" : damagerType.toUpperCase());
        sb.append(" name:").append(damagerName.isEmpty() ? "-" : damagerName.toUpperCase());
        sb.append(" etype:").append(entityType.isEmpty() ? "-" : entityType.toUpperCase());
        sb.append(" cause:").append(damageCause);
        sb.append(")");
        return sb.toString();
    }

}
