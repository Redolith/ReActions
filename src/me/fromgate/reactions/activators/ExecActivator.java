package me.fromgate.reactions.activators;


import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;
import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.ExecEvent;

public class ExecActivator extends Activator {
    
    
    ExecActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }
    
    public ExecActivator(String name, String group) {
        super(name, group);
    }

    public ExecActivator(String name){
        super (name,"activators");
    }
    
    @Override
    public void activate(Event event) {
        if (event instanceof ExecEvent){
            ExecEvent ce  = (ExecEvent) event;
            if (ce.getCommand().equalsIgnoreCase(this.getName())) Actions.executeActivator(ce.getTargetPlayer(), this);
        }
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
        return "exec";
    }

}
