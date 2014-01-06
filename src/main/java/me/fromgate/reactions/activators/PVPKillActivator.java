package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.PVPKillEvent;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

public class PVPKillActivator extends Activator {
    private String targetplayer;
    
    public PVPKillActivator(String name) {
        super(name, "activators");
    }

    public PVPKillActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }

    @Override
    public void activate(Event event) {
        if (!(event instanceof PVPKillEvent)) return;
        PVPKillEvent pe = (PVPKillEvent) event;
        this.targetplayer = pe.getKilledPlayer().getName();
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
    public ActivatorType getType() {
        return ActivatorType.PVP_KILL;
    }

    @Override
    public String getTargetPlayer(){
        return targetplayer; 
    }
}
