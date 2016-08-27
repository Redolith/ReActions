package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.FactionCreateEvent;
import me.fromgate.reactions.util.Variables;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

public class FactionCreateActivator extends Activator {

    public FactionCreateActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }

    public FactionCreateActivator(String name, String param) {
        super(name, "activators");
    }

    @Override
    public boolean activate(Event event) {
        if (!(event instanceof FactionCreateEvent)) return false;
        FactionCreateEvent fe = (FactionCreateEvent) event;
        Variables.setTempVar("faction", fe.getFaction());
        return Actions.executeActivator(fe.getPlayer(), this);
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
        return ActivatorType.FCT_CREATE;
    }
}
