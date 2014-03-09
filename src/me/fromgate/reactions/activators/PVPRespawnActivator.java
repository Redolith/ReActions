package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.PVPRespawnEvent;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

public class PVPRespawnActivator extends Activator {
    
    private String targetplayer;

    public PVPRespawnActivator(String name) {
        super (name,"activators");
    }
    
    public PVPRespawnActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }

    @Override
    public boolean activate(Event event) {
        if (!(event instanceof PVPRespawnEvent)) return false;
        PVPRespawnEvent pe = (PVPRespawnEvent) event;
        targetplayer = pe.getKiller().getName();
        return Actions.executeActivator(pe.getPlayer(), this);
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
        return ActivatorType.PVP_RESPAWN;
    }

    @Override
    public String getTargetPlayer(){
        return this.targetplayer;
    }
}