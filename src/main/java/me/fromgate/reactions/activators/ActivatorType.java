package me.fromgate.reactions.activators;

import org.bukkit.event.Event;
import me.fromgate.reactions.event.*;

public enum ActivatorType {
    // алиас, класс активатора, класс события
    BUTTON ("b",ButtonActivator.class,ButtonEvent.class),
    PLATE ("plt",PlateActivator.class,PlateEvent.class),
    REGION ("rg",RegionActivator.class,RegionEvent.class),
    REGION_ENTER ("rgenter",RgEnterActivator.class,RegionEnterEvent.class),
    REGION_LEAVE ("rgleave",RgLeaveActivator.class,RegionLeaveEvent.class),
    EXEC ("exe",ExecActivator.class,ExecEvent.class),
    COMMAND ("cmd",CommandActivator.class,CommandEvent.class),
    PVP_KILL ("pvpkill",PVPKillActivator.class, PVPKillEvent.class),
    PVP_DEATH ("pvpdeath",PVPDeathActivator.class, PVPDeathEvent.class),
    PVP_RESPAWN("pvprespawn",PVPRespawnActivator.class, PVPRespawnEvent.class),
    LEVER ("lvr",LeverActivator.class,LeverEvent.class),
    DOOR ("door",DoorActivator.class,DoorEvent.class),
    JOIN ("join",JoinActivator.class,JoinEvent.class),
    MOBCLICK ("mobclick",MobClickActivator.class,MobClickEvent.class),
    ITEM_CLICK ("itemclick",ItemClickActivator.class,ItemClickEvent.class),
    ITEM_HOLD ("itemhold",ItemHoldActivator.class,ItemHoldEvent.class);

    private String alias;
    private Class<? extends Activator> aclass;
    private Class<? extends Event> eclass;
    
    ActivatorType(String alias, Class<? extends Activator> actclass, Class<? extends Event> evntclass){
        this.alias = alias;
        this.aclass = actclass;
        this.eclass = evntclass;
    }
    
    public Class<? extends Activator> getActivatorClass(){
        return aclass;
    }
    
    public Class<? extends Event> getEventClass(){
        return eclass;
    }
    
    
    public String getAlias(){
        return this.alias;
    }
    
    public static boolean isValid (String str){
        for (ActivatorType at : ActivatorType.values()) 
            if (at.name().equalsIgnoreCase(str)||at.alias.equalsIgnoreCase(str)) return true;
        return false;
    }
    
    public boolean isValidEvent(Event event){
        return eclass.isInstance(event);
    }
    
    public static ActivatorType getByName(String name){
        for (ActivatorType at : ActivatorType.values())
            if (at.name().equalsIgnoreCase(name)||at.getAlias().equalsIgnoreCase(name)) return at;
        return null;
    }
    

}
