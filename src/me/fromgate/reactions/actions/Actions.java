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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.fromgate.reactions.RAUtil;
import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.activators.Activator;
import me.fromgate.reactions.activators.Activator.ActVal;
import me.fromgate.reactions.flags.Flags;
import me.fromgate.reactions.util.ParamUtil;
import me.fromgate.reactions.util.Placeholders;
import me.fromgate.reactions.util.Variables;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum Actions{
    //"tp,velocity,sound,potion,rmvpot,grpadd,grprmv,msg,dmg,townset,townkick,itemrmv,invitemrmv,itemgive,cmdplr,cmdop,cmdsrv,moneypay,moneygive,delay,pdelay,back,mob,effect,run,rgclear";
    TP ("tp",true,new ActionTp()),
    VELOCITY ("velocity",true,new ActionVelocity()),
    VELOCITY_JUMP ("jump",true,new ActionVelocityJump()),
    SOUND ("sound",false,new ActionSound()),    //добавить параметр location
    POTION ("potion",true,new ActionPlayerPotion()),
    POTION_REMOVE ("rmvpot",true,new ActionPlayerPotionRemove()),
    GROUP_ADD ("grpadd",true,new ActionGroupAdd()),
    GROUP_REMOVE ("grprmv",true,new ActionGroupRemove()),
    MESSAGE ("msg",false,new ActionMessage()), //или всё-таки добавить параметры ??
    BROADCAST ("msgall",false,new ActionBroadcast()), //или всё-таки добавить параметры ??
    DAMAGE ("dmg",true,new ActionDamage()),
    TOWN_SET ("townset",true,new ActionTownSet()),
    TOWN_KICK ("townkick",true,new ActionTownKick()),
    ITEM_GIVE ("itemgive",true,new ActionItems(0)),
    ITEM_REMOVE ("itemrmv",true,new ActionItems(1)),
    ITEM_REMOVE_INVENTORY ("invitemrmv",true,new ActionItems(2)),
    ITEM_DROP ("itemdrop",true,new ActionItems(3)),
    ITEM_WEAR ("itemdrop",true,new ActionItems(4)),
    CMD ("cmdplr",true,new ActionCommand()),
    CMD_OP("cmdop",false,new ActionCommandOp()),
    CMD_CONSOLE ("cmdsrv",false,new ActionCommandConsole()),
    MONEY_PAY ("moneypay",false,new ActionMoneyPay()),
    MONEY_GIVE ("moneygive",false,new ActionMoneyGive()),
    DELAY ("delay",false,new ActionDelay(false)),
    DELAY_PLAYER ("pdelay",true,new ActionDelay(true)),
    BACK("back",true,new ActionBack()),
    MOB_SPAWN ("mob",false,new ActionMobSpawn()),
    EFFECT("effect",false,new ActionEffect()),
    EXECUTE ("run",false,new ActionExecute()),  /// ???? не уверен
    EXECUTE_STOP ("stop",false,new ActionExecStop()),  /// ???? не уверен
    EXECUTE_UNSTOP ("unstop",false,new ActionExecUnstop()),  /// ???? не уверен
    REGION_CLEAR("rgclear",false,new ActionClearRegion()),
    HEAL("heal",true,new ActionHeal()),
    BLOCK_SET("block",false,new ActionBlockSet()),
    BLOCK_FILL("blockfill",false,new ActionBlockFill()),
    SIGN_SET_LINE("sign",false,new ActionSignSet()),
    POWER_SET("power",false,new ActionPowerSet()),
    SHOOT("shoot",true,new ActionShoot()),
    VAR_SET("varset",false,new ActionVar(0,false)),
    VAR_PLAYER_SET("varpset",true,new ActionVar(0,true)),
    VAR_CLEAR("varclr",false,new ActionVar(1,false)),
    VAR_PLAYER_CLEAR("varpclr",true,new ActionVar(1,true)),
    VAR_INC("varinc",false,new ActionVar(2,false)),
    VAR_PLAYER_INC("varpinc",true,new ActionVar(2,true)),
    VAR_DEC("vardec",false,new ActionVar(3,false)),
    VAR_PLAYER_DEC("varpdec",true,new ActionVar(3,true)),
    RNC_SET_RACE("setrace",true,new ActionRacesAndClasses(true)),
    RNC_SET_CLASS("setclass",true,new ActionRacesAndClasses(false)),
    TIMER_STOP("timerstop",false,new ActionTimer(true)),
    TIMER_RESUME("timerresume",false,new ActionTimer(false)),
    CANCEL_EVENT ("cancel",false, new ActionCancelEvent()),
    SQL_SELECT ("sqlselect",false, new ActionSQL(0)),
    SQL_UPDATE("sqlupdate",false, new ActionSQL(2)),
    SQL_INSERT("sqlinsert",false, new ActionSQL(1)),
    SQL_DELETE("sqldelete",false, new ActionSQL(3)),
    ACTION_DELAYED ("actdelay",false, new ActionDelayed()),
    MENU_ITEM ("itemmenu",true, new ActionMenuItem()),
    FACTIONS_POWER_ADD("factaddpower", false, new ActionFactionsPowerAdd());

    private String alias;
    private boolean requireplayer;
    private Action action;

    Actions (String alias, boolean requireplayer, Action action){
        this.alias = alias;
        this.requireplayer = requireplayer;
        this.action = action;
        this.action.init(this);
    }

    static ReActions plg(){
        return ReActions.instance;
    }

    static RAUtil u(){
        return ReActions.util;
    }

    public String getAlias(){
        return this.alias;
    }


    public static Actions getByName(String name){
        for (Actions at : Actions.values())
            if (at.name().equalsIgnoreCase(name)
                    ||at.getAlias().equalsIgnoreCase(name)) return at;
        return null;
    }
    
    public static String getValidName (String name){
        for (Actions at : Actions.values())
            if (at.getAlias().equalsIgnoreCase(name)) return at.name();
       return name;
    }
    
    /*
     *  не знаю ещё зачем..... 
     */
    /*public static boolean executeActivator (Player p, Activator act){
        boolean action = Flags.checkFlags(p, act);
        boolean rst = true;
        List<ActVal> actions = action ? act.getActions() : act.getReactions();
        if (actions.isEmpty()) return true;
        for (int i = 0; i<actions.size(); i++){
            if (!Actions.isValid(actions.get(i).flag)) continue;
            Actions at = Actions.getByName(actions.get(i).flag);
            Map<String,String> params = Placeholders.replaceAllPlaceholders(p, act, ParamUtil.parseParams(actions.get(i).value));
            if (!at.performAction(p, act, action, params)) rst = false;
        }
        return rst;
    }*/
    /*public static boolean executeActivator (Player p, Activator act){
        boolean action = Flags.checkFlags(p, act);
        List<ActVal> actions = action ? act.getActions() : act.getReactions();
        if (actions.isEmpty()) return false;
        boolean cancelParentEvent = false;
        for (int i = 0; i<actions.size(); i++){
            if (!Actions.isValid(actions.get(i).flag)) continue;
            Actions at = Actions.getByName(actions.get(i).flag);
            Map<String,String> params = Placeholders.replaceAllPlaceholders(p, act, ParamUtil.parseParams(actions.get(i).value));
            if (at.performAction(p, act, action, params)) cancelParentEvent = true;
        }
        return cancelParentEvent;
    }*/
    
    public static boolean executeActivator (Player p, Activator act){
        boolean isAction = Flags.checkFlags(p, act);
        List<ActVal> actions = isAction ? act.getActions() : act.getReactions();
        if (actions.isEmpty()) return false;
        boolean cancelParentEvent = false;
        for (ActVal action : actions) {
            if (!Actions.isValid(action.flag)) continue;
            Actions at = Actions.getByName(action.flag);
            Map<String,String> params = Placeholders.replaceAllPlaceholders(p, act, ParamUtil.parseParams(action.value));
            if (at.performAction(p, act, isAction, params)) cancelParentEvent = true;
        }
        Variables.clearAllTempVar();
        return cancelParentEvent;
    }
    
    public boolean performAction(Player p, Activator a, boolean action, Map<String,String> params){
        if ((p==null)&&this.requireplayer) return false;
        return this.action.executeAction(p, a, action, params);
    }

    public static boolean isValid(String name){
        for (Actions at : Actions.values()){
            if (at.name().equalsIgnoreCase(name)) return true;
            if (at.getAlias().equalsIgnoreCase(name)) return true;
        }
        return false;
    }

    public static void listActions (CommandSender sender, int pageNum) {
    	List<String> actionList = new ArrayList<String>();
    	for (Actions actionType : Actions.values()){
    		String name = actionType.name(); 
    		String alias = actionType.getAlias().equalsIgnoreCase(name) ? " " : " ("+actionType.getAlias()+") ";
    		String description = ReActions.util.getMSGnc("action_"+name);
    		actionList.add("&6"+name+"&e"+alias+"&3: &a"+description);
    	}
    	ReActions.util.printPage(sender, actionList, pageNum, "msg_actionlisttitle", "", false, sender instanceof Player ? 10 : 1000);
    }
    
    
    public Player getNearestPlayer (Player player){
    	Player nearest = null;
    	for (Player onlinePlayer : Bukkit.getOnlinePlayers()){
    		if (!player.getWorld().equals(onlinePlayer.getWorld())) continue; // Check only players in player's world
    		if ((nearest!=null)&&
    				(player.getLocation().distance(nearest.getLocation())<player.getLocation().distance(onlinePlayer.getLocation()))) continue;
    		nearest = onlinePlayer;
    	}
		return nearest;
    }
    

}
