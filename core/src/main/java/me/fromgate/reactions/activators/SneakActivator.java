package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.SneakEvent;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

/**
 * Created by MaxDikiy on 2017-05-16.
 */
public class SneakActivator extends Activator {
    private SneakType sneak;

    public SneakActivator(String name, String param) {
        super(name, "activators");
        Param params = new Param(param);
        this.sneak = SneakType.getByName(params.getParam("sneak", "ANY"));
    }

    public SneakActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }

    @Override
    public boolean activate(Event event) {
        if (!(event instanceof SneakEvent)) return false;
        SneakEvent se = (SneakEvent) event;
        if (!checkSneak(se.getSneak())) return false;
        Variables.setTempVar("sneak", se.getSneak() ? "TRUE" : "FALSE");
        return Actions.executeActivator(se.getPlayer(), this);
    }

    @Override
    public boolean isLocatedAt(Location loc) {
        return false;
    }

    @Override
    public void save(String root, YamlConfiguration cfg) {
        cfg.set(root + ".sneak", sneak.name());
    }

    @Override
    public void load(String root, YamlConfiguration cfg) {
        sneak = SneakType.getByName(cfg.getString(root + ".sneak", "ANY"));
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.SNEAK;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    private boolean checkSneak(Boolean isSneak) {
        switch (sneak) {
            case ANY:
                return true;
            case TRUE:
                return isSneak;
            case FALSE:
                return !isSneak;
        }
        return false;
    }

    enum SneakType {
        TRUE,
        FALSE,
        ANY;

        public static SneakType getByName(String sneakStr) {
            if (sneakStr.equalsIgnoreCase("true")) return SneakType.TRUE;
            if (sneakStr.equalsIgnoreCase("any")) return SneakType.ANY;
            return SneakType.FALSE;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name).append(" [").append(getType()).append("]");
        if (!getFlags().isEmpty()) sb.append(" F:").append(getFlags().size());
        if (!getActions().isEmpty()) sb.append(" A:").append(getActions().size());
        if (!getReactions().isEmpty()) sb.append(" R:").append(getReactions().size());
        sb.append(" (");
        sb.append("sneak:").append(this.sneak.name());
        sb.append(")");
        return sb.toString();
    }

}
