package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.CommandEvent;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

public class CommandActivator extends Activator {
    
    String command;
    
    
    CommandActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }
    
    public CommandActivator(String name, String group, String command) {
        super(name, group);
    }

    public CommandActivator(String name, String command){
        super (name,"activators");
        this.command = command;
    }

    @Override
    public void activate(Event event) {
        if (!(event instanceof CommandEvent)) return;
        CommandEvent ce = (CommandEvent) event;
        if (!ce.getCommand().toLowerCase().startsWith(command.toLowerCase())) return;
        Actions.executeActivator(ce.getPlayer(), this);
    }

    @Override
    public boolean isLocatedAt(Location loc) {
        return false;
    }

    @Override
    public void save(String root, YamlConfiguration cfg) {
        cfg.set(root+".command",command);
    }

    @Override
    public void load(String root, YamlConfiguration cfg) {
       command = cfg.getString(root+".command"); 
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.COMMAND;
    }
    

}
