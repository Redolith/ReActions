package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.JoinEvent;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

public class JoinActivator extends Activator {

    private boolean firstJoin;

    JoinActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }

    public JoinActivator(String name, String join) {
        super(name, "activators");
        this.firstJoin = join.equalsIgnoreCase("first") || join.equalsIgnoreCase("firstjoin");
    }

    @Override
    public boolean activate(Event event) {
        if (event instanceof JoinEvent) {
            JoinEvent ce = (JoinEvent) event;
            if (isJoinActivate(ce.isFirstJoin())) return Actions.executeActivator(ce.getPlayer(), this);
        }
        return false;
    }

    private boolean isJoinActivate(boolean join_first_time) {
        if (this.firstJoin) return join_first_time;
        return true;
    }

    @Override
    public boolean isLocatedAt(Location loc) {
        return false;
    }

    @Override
    public void save(String root, YamlConfiguration cfg) {
        cfg.set(root + ".join-state", (firstJoin ? "FIRST" : "ANY"));
    }

    @Override
    public void load(String root, YamlConfiguration cfg) {
        this.firstJoin = cfg.getString(root + ".join-state", "ANY").equalsIgnoreCase("first") ? true : false;
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.JOIN;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name).append(" [").append(getType()).append("]");
        if (!getFlags().isEmpty()) sb.append(" F:").append(getFlags().size());
        if (!getActions().isEmpty()) sb.append(" A:").append(getActions().size());
        if (!getReactions().isEmpty()) sb.append(" R:").append(getReactions().size());
        sb.append(" (first join:").append(this.firstJoin).append(")");
        return sb.toString();
    }

}
