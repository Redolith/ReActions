package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.GodEvent;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

/**
 * Created by MaxDikiy on 2017-10-28.
 */
public class GodActivator extends Activator {
    private GodType god;

    public GodActivator(String name, String param) {
        super(name, "activators");
        Param params = new Param(param);
        this.god = GodType.getByName(params.getParam("god", "ANY"));
    }

    public GodActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }

    @Override
    public boolean activate(Event event) {
        if (!(event instanceof GodEvent)) return false;
        GodEvent e = (GodEvent) event;
        if (!checkGod(e.isGod())) return false;
        Variables.setTempVar("god", e.isGod() ? "TRUE" : "FALSE");
        return Actions.executeActivator(e.getPlayer(), this);
    }

    @Override
    public boolean isLocatedAt(Location loc) {
        return false;
    }

    @Override
    public void save(String root, YamlConfiguration cfg) {
        cfg.set(root + ".god", god.name());
    }

    @Override
    public void load(String root, YamlConfiguration cfg) {
        god = GodType.getByName(cfg.getString(root + ".god", "ANY"));
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.GOD;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    private boolean checkGod(Boolean isGod) {
        switch (god) {
            case ANY:
                return true;
            case TRUE:
                return isGod;
            case FALSE:
                return !isGod;
        }
        return false;
    }

    enum GodType {
        TRUE,
        FALSE,
        ANY;

        public static GodType getByName(String godStr) {
            if (godStr.equalsIgnoreCase("true")) return GodType.TRUE;
            if (godStr.equalsIgnoreCase("any")) return GodType.ANY;
            return GodType.FALSE;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name).append(" [").append(getType()).append("]");
        if (!getFlags().isEmpty()) sb.append(" F:").append(getFlags().size());
        if (!getActions().isEmpty()) sb.append(" A:").append(getActions().size());
        if (!getReactions().isEmpty()) sb.append(" R:").append(getReactions().size());
        sb.append(" (");
        sb.append("god:").append(this.god.name());
        sb.append(")");
        return sb.toString();
    }

}
