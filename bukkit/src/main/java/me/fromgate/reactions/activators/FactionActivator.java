package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.FactionEvent;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

public class FactionActivator extends Activator {

    private String newFaction;
    private String oldFaction;

    public FactionActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }

    public FactionActivator(String name, String param) {
        super(name, "activators");
        Param params = new Param(param, "newfaction");
        this.newFaction = params.getParam("newfaction", params.getParam("faction", "ANY"));
        this.oldFaction = params.getParam("oldfaction", "ANY");
    }

    @Override
    public boolean activate(Event event) {
        if (!(event instanceof FactionEvent)) return false;
        FactionEvent fe = (FactionEvent) event;
        if (!(newFaction.isEmpty() || newFaction.equalsIgnoreCase("any") || fe.getNewFaction().equalsIgnoreCase(newFaction)))
            return false;
        if (!(oldFaction.isEmpty() || oldFaction.equalsIgnoreCase("any") || fe.getOldFaction().equalsIgnoreCase(oldFaction)))
            return false;
        Variables.setTempVar("newfaction", fe.getNewFaction());
        Variables.setTempVar("oldfaction", fe.getOldFaction());
        return Actions.executeActivator(fe.getPlayer(), this);
    }

    @Override
    public boolean isLocatedAt(Location loc) {
        return false;
    }

    @Override
    public void save(String root, YamlConfiguration cfg) {
        cfg.set(root + ".new-faction", newFaction.isEmpty() ? "ANY" : newFaction);
        cfg.set(root + ".old-faction", oldFaction.isEmpty() ? "ANY" : oldFaction);
    }

    @Override
    public void load(String root, YamlConfiguration cfg) {
        this.newFaction = cfg.getString(root + ".new-faction", "ANY");
        this.oldFaction = cfg.getString(root + ".old-faction", "ANY");
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.FCT_CHANGE;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name).append(" [").append(getType()).append("]");
        if (!getFlags().isEmpty()) sb.append(" F:").append(getFlags().size());
        if (!getActions().isEmpty()) sb.append(" A:").append(getActions().size());
        if (!getReactions().isEmpty()) sb.append(" R:").append(getReactions().size());
        sb.append(" (old faction:").append(this.oldFaction).append(" new faction:").append(this.newFaction).append(")");
        return sb.toString();
    }


}
