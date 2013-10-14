package me.fromgate.reactions.actions;

import java.util.Map;

import me.fromgate.reactions.RAUtil;
import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.activators.Activator;
import org.bukkit.entity.Player;


public abstract class Action {
    Actions type = null;
    private boolean success = false;
    private String message_param="";
    private boolean action_executing = true;
    private Activator activator;
    
    ReActions plg(){
        return ReActions.instance;
    }

    RAUtil u(){
        return ReActions.util;
    }
    

    public void setMessageParam (String msgparam){
        this.message_param = msgparam;
    }
    
    public void init(Actions at){
        this.type = at;
    }
    
    public boolean isAction(){
        return this.action_executing;
    }
    public String getActivatorName(){
        return this.activator.getName();
    }
    
    public Activator getActivator(){
        return this.activator;
    }
    
    public boolean executeAction (Player p, Activator a, boolean action, Map<String, String> params){
        this.action_executing = action;
        this.activator = a;
        if (!params.containsKey("param-line")) params.put("param-line", "");
        setMessageParam(params.get("param-line"));
        //if (params.containsKey("param")) setMessageParam(params.get("param"));
        boolean activator_fail = (!execute (p,params)); 
        if (activator_fail) this.success = false;
        if ((p!=null)&&(printAction())) 
            u().printMSG(p, activator_fail ? "act_"+type.name().toLowerCase()+"fail" : "act_"+type.name().toLowerCase(), message_param);
        return this.success;
    }
    
    private boolean printAction() {
        return (u().isWordInList(this.type.name(), plg().getActionMsg()));
    }

    public abstract boolean execute(Player p, Map<String, String> params);
}
