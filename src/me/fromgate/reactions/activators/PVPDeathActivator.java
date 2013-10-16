package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.PVPDeathEvent;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

public class PVPDeathActivator extends Activator {

    public PVPDeathActivator(String name) {
        super (name,"activators");
    }
    
    public PVPDeathActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }

    @Override
    public void activate(Event event) {
        if (!(event instanceof PVPDeathEvent)) return;
        PVPDeathEvent pe = (PVPDeathEvent) event;
        Actions.executeActivator(pe.getPlayer(), this);
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
    public String getType() {
        return "pvpdeath";
    }

}