package me.fromgate.reactions.actions;

import java.util.Map;

import me.fromgate.reactions.RAUtil;
import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.activators.Activator;
import me.fromgate.reactions.activators.CommandActivator;
import me.fromgate.reactions.activators.ActivatorType;

import org.bukkit.entity.Player;


public abstract class Action {
	Actions type = null;
	private String message_param="";
	private boolean actionExecuting = true;
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
		return this.actionExecuting;
	}
	public String getActivatorName(){
		return this.activator.getName();
	}

	public Activator getActivator(){
		return this.activator;
	}

	public boolean executeAction (Player p, Activator a, boolean action, Map<String, String> params){
		this.actionExecuting = action;
		this.activator = a;
		if (!params.containsKey("param-line")) params.put("param-line", "");
		setMessageParam(params.get("param-line"));
		boolean actionFailed = (!execute (p,params));
		if ((p!=null)&&(printAction())) 
			u().printMSG(p, actionFailed ? "act_"+type.name().toLowerCase()+"fail" : "act_"+type.name().toLowerCase(), message_param);
		//Залипухи, но похоже по другому - никак...
		if ((a.getType() == ActivatorType.COMMAND)&&(!((CommandActivator) a).isCommandRegistered())) return true;
		if ((this.type==Actions.CANCEL_EVENT)&&(!actionFailed)) return true;
		return false;
	}

	private boolean printAction() {
		return (u().isWordInList(this.type.name(), plg().getActionMsg())||u().isWordInList(this.type.getAlias(), plg().getActionMsg()));
	}

	public abstract boolean execute(Player p, Map<String, String> params);
}
