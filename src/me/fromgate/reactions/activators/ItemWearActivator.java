package me.fromgate.reactions.activators;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.ItemWearEvent;
import me.fromgate.reactions.util.ItemUtil;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

public class ItemWearActivator extends Activator {
    private String item;

    public ItemWearActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }
    
    public ItemWearActivator(String name, String item){
        super (name,"activators");
        this.item = item;
    }
    

    @Override
    public void activate(Event event) {
        if (item.isEmpty()||(ItemUtil.parseItemStack(item)==null)) {
            ReActions.util.logOnce(this.name+"activatorwearempty", "Failed to parse item of activator "+this.name);
            return;
        }
        if (event instanceof ItemWearEvent){
            ItemWearEvent iw  = (ItemWearEvent) event;
            if (iw.isItemWeared(this.item))
                Actions.executeActivator(iw.getPlayer(), this);
        }
        
    }

    @Override
    public boolean isLocatedAt(Location loc) {
        return false;
    }

    @Override
    public void save(String root, YamlConfiguration cfg) {
        cfg.set(root+".item",this.item);
    }

    @Override
    public void load(String root, YamlConfiguration cfg) {
        this.item=cfg.getString(root+".item");
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.ITEM_WEAR;
    }
    
    public String getItemStr(){
        return this.item;
    }
}

