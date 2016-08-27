package me.fromgate.reactions.activators;


import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.ExecEvent;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

public class ExecActivator extends Activator {

    ExecActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }

    public ExecActivator(String name, String group) {
        super(name, group);
    }

    public ExecActivator(String name) {
        super(name, "activators");
    }

    @Override
    public boolean activate(Event event) {
        if (event instanceof ExecEvent) {
            ExecEvent ce = (ExecEvent) event;
            if (ce.getActivatorId().equalsIgnoreCase(this.getName()))
                return Actions.executeActivator(ce.getTargetPlayer(), this);
        }
        return false;
    }

    @Override
    public boolean isLocatedAt(Location loc) {
        return false;
    }

    @Override
    public void save(String root, YamlConfiguration cfg) {
    }

    @Override
    public void load(String root, YamlConfiguration cfg) {
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.EXEC;
    }

}
