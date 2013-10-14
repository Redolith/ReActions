package me.fromgate.reactions.activators;

import org.bukkit.event.Event;
import me.fromgate.reactions.event.*;

public enum ActivatorType {
    // алиас, класс активатора, класс события
    BUTTON ("b",ButtonActivator.class,RAButtonEvent.class),
    PLATE ("plt",PlateActivator.class,RAPlateEvent.class),
    REGION ("rg",RegionActivator.class,RARegionEvent.class),
    RGENTER ("rge",RgEnterActivator.class,RARegionEnterEvent.class),
    RGLEAVE ("rgl",RgLeaveActivator.class,RARegionLeaveEvent.class),
    EXEC ("exe",ExecActivator.class,RAExecEvent.class),
    COMMAND ("cmd",CommandActivator.class,RACommandEvent.class),
    PVPKILL ("pvpk",PVPKillActivator.class, RAPVPKillEvent.class),
    PVPDEATH("pvpd",PVPDeathActivator.class, RAPVPDeathEvent.class);

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
            if (at.name().equalsIgnoreCase(name)||at.alias.equalsIgnoreCase(name)) return at;
        return null;
    }
    

}
