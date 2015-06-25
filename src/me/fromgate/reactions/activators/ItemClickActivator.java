package me.fromgate.reactions.activators;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.ItemClickEvent;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.item.ItemUtil;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

public class ItemClickActivator extends Activator {
    private String item;
    

    public ItemClickActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }
    
    public ItemClickActivator(String name, String item){
        super (name,"activators");
        this.item = item;
    }
    

    @Override
    public boolean activate(Event event) {
        if (item.isEmpty()||(ItemUtil.parseItemStack(item)==null)) {
            ReActions.util.logOnce(this.name+"activatoritemempty", "Failed to parse item of activator "+this.name);
            return false;
        }
        if (event instanceof ItemClickEvent){
            ItemClickEvent ie  = (ItemClickEvent) event;
            if (ItemUtil.compareItemStr(ie.getItem(), this.item)){
            	Variables.setTempVar("item", ItemUtil.itemToString(ie.getItem()));
            
            	return Actions.executeActivator(ie.getPlayer(), this);
            }
                
        }
        return false;
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
        return ActivatorType.ITEM_CLICK;
    }

}
