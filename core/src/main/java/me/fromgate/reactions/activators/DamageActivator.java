package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.DamageEvent;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Created by MaxDikiy on 2017-07-23.
 */
public class DamageActivator extends Activator {
    private String damageCause;
    private SourceType source;

    public DamageActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }

    public DamageActivator(String name, String param) {
        super(name, "activators");
        Param params = new Param(param);
        this.damageCause = getCauseByName(params.getParam("cause", "ANY"));
        this.source = SourceType.getByName(params.getParam("source", "ANY"));
    }

    @Override
    public boolean activate(Event event) {
        if (!(event instanceof DamageEvent)) return false;
        DamageEvent de = (DamageEvent) event;
        if (!damageCauseCheck(de.getCause())) return false;
        if (!sourceCheck(de.getSource())) return false;
        Variables.setTempVar("damage", Double.toString(de.getDamage()));
        Variables.setTempVar("cause", de.getCause().name());
        Variables.setTempVar("source", de.getSource());
        return Actions.executeActivator(de.getPlayer(), this);
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

    enum SourceType {
        BLOCK,
        ENTITY,
        OTHER,
        ANY;

        public static SourceType getByName(String sourceStr) {
            if (sourceStr != null) {
                for (SourceType SourceType : values()) {
                    if (sourceStr.equalsIgnoreCase(SourceType.name())) {
                        return SourceType;
                    }
                }
            }
            return SourceType.ANY;
        }
    }

    private boolean sourceCheck(String st) {
        if (source.name().equals("ANY")) return true;
        return st.equals(source.name());
    }

    @Override
    public boolean isLocatedAt(Location loc) {
        return false;
    }

    @Override
    public void save(String root, YamlConfiguration cfg) {
        cfg.set(root + ".cause", this.damageCause);
        cfg.set(root + ".source", this.source.name());
    }

    @Override
    public void load(String root, YamlConfiguration cfg) {
        this.damageCause = cfg.getString(root + ".cause", "ANY");
        this.source = SourceType.getByName(cfg.getString(root + ".source", "ANY"));
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.DAMAGE;
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
        sb.append("cause:").append(this.damageCause);
        sb.append(" source:").append(this.source.name());
        sb.append(")");
        return sb.toString();
    }

}
