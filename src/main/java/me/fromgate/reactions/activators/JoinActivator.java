package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.JoinEvent;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

public class JoinActivator extends Activator {
    
    private boolean first_join;

    JoinActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }

    public JoinActivator(String name, String join){
        super (name,"activators");
        this.first_join = join.equalsIgnoreCase("first")||join.equalsIgnoreCase("firstjoin");
    }

    @Override
    public void activate(Event event) {
        if (event instanceof JoinEvent){
            JoinEvent ce  = (JoinEvent) event;
            if (isJoinActivate(ce.isFirstJoin())) Actions.executeActivator(ce.getPlayer(), this);
        }
    }

    private boolean isJoinActivate(boolean join_first_time){
        if (this.first_join) return join_first_time;
        return true;
    }
    
    @Override
    public boolean isLocatedAt(Location loc) {
        return false;
    }

    @Override
    public void save(String root, YamlConfiguration cfg) {
        cfg.set(root+".join-state",(first_join ? "FIRST" : "ANY"));
    }

    @Override
    public void load(String root, YamlConfiguration cfg) {
        this.first_join = cfg.getString(root+".join-state","ANY").equalsIgnoreCase("first") ? true : false;
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.JOIN;
    }

}
