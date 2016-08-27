/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2014, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/reactions/
 *    
 *  This file is part of ReActions.
 *  
 *  ReActions is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ReActions is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with ReActions.  If not, see <http://www.gnorg/licenses/>.
 * 
 */

package me.fromgate.reactions.actions;

import me.fromgate.reactions.RAUtil;
import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.activators.Activator;
import me.fromgate.reactions.util.Param;
import org.bukkit.entity.Player;


public abstract class Action {
    Actions type = null;
    private String messageParam = "";
    private boolean actionExecuting = true;
    private Activator activator;

    ReActions plg() {
        return ReActions.instance;
    }

    RAUtil u() {
        return ReActions.util;
    }


    public void setMessageParam(String msgparam) {
        this.messageParam = msgparam;
    }

    public void init(Actions at) {
        this.type = at;
    }

    public boolean isAction() {
        return this.actionExecuting;
    }

    public String getActivatorName() {
        return this.activator.getName();
    }

	/*
    public Activator getActivator(){
		return this.activator;
	} */

    public boolean executeAction(Player p,/* Activator a,*/ boolean action, Param params) {
        this.actionExecuting = action;
        //this.activator = a;
        if (!params.hasAnyParam("param-line")) params.set("param-line", "");
        setMessageParam(params.getParam("param-line"));
        boolean actionFailed = (!execute(p, params));
        if ((p != null) && (printAction()))
            u().printMSG(p, actionFailed ? "act_" + type.name().toLowerCase() + "fail" : "act_" + type.name().toLowerCase(), messageParam);
        //Залипухи, но похоже по другому - никак...
        //if (a==null) return true;
        //if ((a.getType() == ActivatorType.COMMAND)&&(!((CommandActivator) a).isCommandRegistered())) return true;
        if ((this.type == Actions.CANCEL_EVENT) && (!actionFailed)) return true;
        return false;
    }

    private boolean printAction() {
        return (u().isWordInList(this.type.name(), plg().getActionMsg()) || u().isWordInList(this.type.getAlias(), plg().getActionMsg()));
    }

    public abstract boolean execute(Player p, Param params);
}
