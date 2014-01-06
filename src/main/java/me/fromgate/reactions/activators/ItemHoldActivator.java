package me.fromgate.reactions.activators;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.ItemHoldEvent;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

public class ItemHoldActivator extends Activator {
    private String item;

    public ItemHoldActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }
    
    public ItemHoldActivator(String name, String item){
        super (name,"activators");
        this.item = item;
    }
    

    @Override
    public void activate(Event event) {
        if (item.isEmpty()||(ReActions.util.parseItemStack(item)==null)) {
            ReActions.util.logOnce(this.name+"activatorholdempty", "Failed to parse item of activator "+this.name);
            return;
        }
        if (event instanceof ItemHoldEvent){
            ItemHoldEvent ie  = (ItemHoldEvent) event;
            if (ReActions.util.compareItemStr(ie.getItem(), this.item))
                 Actions.executeActivator(ie.getPlayer(), this);
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
        return ActivatorType.ITEM_HOLD;
    }
    
    public String getItemStr(){
        return this.item;
    }
}

