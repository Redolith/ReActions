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


package me.fromgate.reactions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.activators.Activator;
import me.fromgate.reactions.activators.ActivatorType;
import me.fromgate.reactions.activators.Activators;
import me.fromgate.reactions.activators.ButtonActivator;
import me.fromgate.reactions.activators.CommandActivator;
import me.fromgate.reactions.activators.DoorActivator;
import me.fromgate.reactions.activators.FactionActivator;
import me.fromgate.reactions.activators.FactionCreateActivator;
import me.fromgate.reactions.activators.FactionRelationActivator;
import me.fromgate.reactions.activators.ItemClickActivator;
import me.fromgate.reactions.activators.ItemHoldActivator;
import me.fromgate.reactions.activators.ItemWearActivator;
import me.fromgate.reactions.activators.JoinActivator;
import me.fromgate.reactions.activators.LeverActivator;
import me.fromgate.reactions.activators.MobClickActivator;
import me.fromgate.reactions.activators.PVPDeathActivator;
import me.fromgate.reactions.activators.PVPRespawnActivator;
import me.fromgate.reactions.activators.PVPKillActivator;
import me.fromgate.reactions.activators.PlateActivator;
import me.fromgate.reactions.activators.RegionActivator;
import me.fromgate.reactions.activators.RgEnterActivator;
import me.fromgate.reactions.activators.RgLeaveActivator;
import me.fromgate.reactions.activators.ExecActivator;
import me.fromgate.reactions.activators.SignActivator;
import me.fromgate.reactions.event.EventManager;
import me.fromgate.reactions.flags.Flags;
import me.fromgate.reactions.menu.InventoryMenu;
import me.fromgate.reactions.timer.Time;
import me.fromgate.reactions.timer.Timers;
import me.fromgate.reactions.util.Delayer;
import me.fromgate.reactions.util.Locator;
import me.fromgate.reactions.util.ParamUtil;
import me.fromgate.reactions.util.Placeholders;
import me.fromgate.reactions.util.RADebug;
import me.fromgate.reactions.util.Selector;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.Variables;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Cmd implements CommandExecutor{
	ReActions plg;
	RAUtil u;
	public Cmd (ReActions plg){
		this.plg = plg;
		this.u = this.plg.u;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		if ((args.length>0)&&u.checkCmdPerm(sender, args[0])){
			if (args[0].equalsIgnoreCase("add")) {
				return executeCmdAdd (sender, args);
			} else if (args[0].equalsIgnoreCase("set")) {
				return executeCmdSet (sender, args);
			} else if (args[0].equalsIgnoreCase("remove")){
				return executeCmdRemove(sender, args);
			} else if (args[0].equalsIgnoreCase("list")){
				return executCmdList(sender,args);
			} else if (args[0].equalsIgnoreCase("clear")){
				return executeCmdClear(sender, args);
			} else if (args[0].equalsIgnoreCase("debug")){
				return executeCmdDebug(sender, args);
			} else if (args[0].equalsIgnoreCase("run")){
				return executeCmdRun(sender, args);
			} else if (args[0].equalsIgnoreCase("copy")){
				return executeCmdCopy (sender, args);
			} else if (args[0].equalsIgnoreCase("help")){
				return executeCmdHelp (sender, args);
			} else if (args[0].equalsIgnoreCase("info")){
				return executeCmdInfo (sender, args);
			} else if (args[0].equalsIgnoreCase("group")){
				return executeCmdGroup (sender, args);
			} else if (args[0].equalsIgnoreCase("check")){
				return executeCmdCheck (sender, args);
			} else if (args[0].equalsIgnoreCase("reload")){
				return executeCmdReload(sender);
			} else if (args[0].equalsIgnoreCase("select")){
				return executeCmdSelect (sender);
				/*} else if (args[0].equalsIgnoreCase("profile")){ // For test purpose only. Not active in released version.
				long ticks = u.timeToTicks(u.parseTime(args[1]));
				if (ticks == 0) ticks = (long) 20*30;
				Profiler.start(ticks, sender); */
			} else return u.returnMSG(true, sender, "cmd_cmdpermerr",'c');
		}
		return false;
	}


	private boolean executeCmdReload(CommandSender sender) {
		Activators.clear();
		Activators.loadActivators();
		Locator.loadLocs();
		plg.reloadConfig();
		plg.loadCfg();
		Delayer.load();
		Variables.load();
		Timers.load();
		InventoryMenu.load();
		u.printMSG(sender, "msg_cmdreload",Activators.size(),Locator.sizeTpLoc());
		return true;
	}

	private boolean executeCmdSelect(CommandSender sender) {
		Player player = (sender instanceof Player) ? (Player) sender : null;
		if (player == null) return false;
		Selector.selectLocation(player, null);
		u.printMSG(player, "cmd_selected", Locator.locationToStringFormated(Selector.getSelectedLocation(player)));
		return true;
	}

	private boolean executeCmdCheck(CommandSender sender, String[] args) {
		Player player = (sender instanceof Player) ? (Player) sender : null;
		if (player==null) return false;
		int radius = args.length>1 && u.isIntegerGZ(args[1]) ? Integer.parseInt(args[1]) : 8;
		printActivatorsAround(player, radius);
		return true;
	}

	private boolean executeCmdGroup(CommandSender sender, String[] args) {
		String id = args.length>1 ? args[1] : "";
		if (id.isEmpty()) return false;
		String group = args.length>2 ? args[2] :"activators";
		if (Activators.setGroup(id, group)) {
			Activators.saveActivators();
			u.printMSG(sender, "msg_groupset",id, group);
		}
		else u.printMSG(sender, "msg_groupsetfailed",id, group);
		return true;
	}

	// react info <id> [far]
	//       0    1    2 
	private boolean executeCmdInfo(CommandSender sender, String[] args) {
		String id = args.length>1 ? args[1] : "";
		if (id.isEmpty()) return false;
		String far = args.length>2 ? args[2] :"";
		if (Activators.contains(id)){
			printActInfo(sender,id,far);
		} else if(id.equalsIgnoreCase("menu")){
			InventoryMenu.printMenu(sender, far);
		} else u.printMSG(sender, "cmd_unknownbutton",id);
		return true;
	}

	// react help <flag> <page>
	//       0     1     2   
	private boolean executeCmdHelp(CommandSender sender, String[] args) {
		String arg1 = "help";
		int page = 1;
		if (args.length>1)
			for (int i=1; i<Math.min(args.length, 3); i++){
				if (u.isIntegerGZ(args[i])) page = Integer.parseInt(args[i]);
				else arg1 = args[i];
			}
		if (arg1.equalsIgnoreCase("flag")||arg1.equalsIgnoreCase("flags")){
			Flags.listFlags(sender, page);
		} else if (arg1.equalsIgnoreCase("action")||arg1.equalsIgnoreCase("actions")){
			Actions.listActions(sender, page);
		} else if (arg1.equalsIgnoreCase("activator")||arg1.equalsIgnoreCase("activators")){
			ActivatorType.listActivators(sender, page);
		} else if (arg1.equalsIgnoreCase("placeholder")||arg1.equalsIgnoreCase("placeholders")){
			Placeholders.listPlaceholders(sender, page);
		} else {
			if (!arg1.equalsIgnoreCase("help")) page = 1;
			u.PrintHlpList(sender, page, (sender instanceof Player) ? 10 : 1000);
		}
		return true;
	}

	private boolean executeCmdCopy(CommandSender sender, String[] args) {
		if (args.length!=3&&args.length!=4) return false;
		String id1 =args.length ==4 ? args[2] : args[1];
		String id2 =args.length ==4 ? args[3] : args[2];
		String copyMode = args.length == 3 ? "all" : args[1];
		if (copyMode.equalsIgnoreCase("all")){
			if (Activators.copyAll(id1, id2)) {
				u.printMSG(sender, "msg_copyall",id1,id2);
			} else u.printMSG(sender, "msg_copyallfailed",'c','4',id1,id2);
			if (copyMode.equalsIgnoreCase("f")||copyMode.equalsIgnoreCase("flag")){
				if (Activators.copyFlags(id1,id2)) u.printMSG(sender, "msg_copyflags",id1,id2);
				else u.printMSG(sender, "msg_copyflagsfailed",'c','4',id1,id2);
			} else if (copyMode.equalsIgnoreCase("a")||copyMode.equalsIgnoreCase("actions")){
				if (Activators.copyActions(id1,id2)) u.printMSG(sender, "msg_copyactions",id1,id2);
				else u.printMSG(sender, "msg_copyactionsfailed",'c','4',id1,id2);
			} else if (copyMode.equalsIgnoreCase("r")||copyMode.equalsIgnoreCase("reactions")){
				if (Activators.copyReactions(id1,id2)) u.printMSG(sender, "msg_copyreactions",id1,id2);
				else u.printMSG(sender, "msg_copyreactionsfailed",'c','4',id1,id2);
			}
		}
		Activators.saveActivators();
		return true;
	}

	private boolean executeCmdRun(CommandSender sender, String[] args) {
		String param = "";
		if (args.length>1){
			for (int i = 1; i<args.length;i++) 
				param = param+" "+args[i];
			param = param.trim();
		}
		if (EventManager.raiseExecEvent(sender, param)) u.printMSG(sender, "cmd_runplayer",param);
		else u.printMSG(sender, "cmd_runplayerfail",'c','6',param);
		return false;
	}

	private boolean executeCmdDebug(CommandSender sender, String[] args) {
		Player p = (sender instanceof Player) ? (Player) sender : null;
		if (p == null) return false;
		String arg = args.length>=2 ? args[1] : "off";
		if (arg.equalsIgnoreCase("false")) {
			RADebug.setPlayerDebug(p, false);
			u.printMSG(p, "cmd_debugfalse");
		} else if (arg.equalsIgnoreCase("true")) {
			RADebug.setPlayerDebug(p, true);
			u.printMSG(p, "cmd_debugtrue");
		} else {
			RADebug.offPlayerDebug(p);
			u.printMSG(p, "cmd_debugoff");
		}
		return true;
	}

	private boolean executeCmdClear(CommandSender sender, String[] args) {
		String arg1 = args.length >=2 ? args[1] : "";
		if (arg1.isEmpty()) return false;
		String arg2 = args.length >=3 ? args[2] : "";
		//String arg3 = args.length >=4 ? args[3] : "";
		if (Activators.contains(arg1)){
			if (arg2.equalsIgnoreCase("f")||arg2.equalsIgnoreCase("flag")) {
				Activators.clearFlags(arg1);
				Activators.saveActivators();
				u.printMSG(sender, "msg_clearflag", arg1);
			} else if (arg2.equalsIgnoreCase("a")||arg2.equalsIgnoreCase("action")) {
				Activators.clearActions(arg1);
				u.printMSG(sender, "msg_clearact", arg1);
				Activators.saveActivators();
			} else if (arg2.equalsIgnoreCase("r")||arg2.equalsIgnoreCase("reaction")) {
				Activators.clearReactions(arg1);
				u.printMSG(sender, "msg_clearreact", arg1);
				Activators.saveActivators();
			}
			Activators.saveActivators();
		} else return u.returnMSG(true,sender, "cmd_unknownbutton",arg1);

		return false;
	}

	private boolean executCmdList(CommandSender sender, String[] args) {
		Player player = (sender instanceof Player) ? (Player) sender : null;
		int lpp = (player == null) ? 1000 : 15;
		int page = 1; 
		String arg1 = args.length >=2 ? args[1] : "";
		String arg2 = args.length >=3 ? args[2] : "";
		String arg3 = args.length >=4 ? args[3] : "";
		if (u.isIntegerGZ(arg1)) printAct(sender, 1, lpp);
		else {
			String mask = "";
			if (u.isIntegerGZ(arg2)) {
				page = Integer.parseInt(arg2);
				mask = arg3;
			}else if (u.isIntegerGZ(arg3)) {
				page = Integer.parseInt(arg3);
				mask = arg2;
			}

			if (arg1.equalsIgnoreCase("all")){
				printAct(sender, page, lpp);
			} else if (arg1.equalsIgnoreCase("type")){
				printActType(sender, mask, page, lpp);
			} else if (arg1.equalsIgnoreCase("group")){
				printActGroup(sender, mask, page, lpp);
			} else if (arg1.equalsIgnoreCase("timer")||arg1.equalsIgnoreCase("timers")){
				Timers.listTimers(sender, page);
			} else if (arg1.equalsIgnoreCase("delay")||arg1.equalsIgnoreCase("delays")){
				Delayer.printDelayList(sender, page, lpp);
			} else if (arg1.equalsIgnoreCase("loc")||arg1.equalsIgnoreCase("location")){
				Locator.printLocList(sender,page,lpp);
			} else if (arg1.equalsIgnoreCase("var")||arg1.equalsIgnoreCase("variables")||arg1.equalsIgnoreCase("variable")){
				Variables.printList(sender,page,mask);
			} else if (arg1.equalsIgnoreCase("menu")||arg1.equalsIgnoreCase("menus")){
				InventoryMenu.printMenuList(sender,page,mask);
			} else {
				u.printMSG(sender,"msg_listcount",Activators.size(),Locator.sizeTpLoc());
			}
		}



		return true;
	}

	private boolean executeCmdRemove(CommandSender sender, String[] args) {
		if (args.length == 1) return false;
		String arg1 = args[1];
		String arg2 = args.length >=3 ? args[2] : "";
		String arg3 = args.length >=4 ? args[3] : "";
		if (args.length>5){
			for (int i = 4; i<args.length;i++) 
				arg3 = arg3+" "+args[i];
			arg3 = arg3.trim();
		}
		if (arg2.isEmpty()) return false;
		if (arg1.equalsIgnoreCase("act")||	arg1.equalsIgnoreCase("activator")){
			if (Activators.contains(arg2)){
				Activators.removeActivator(arg2);
				u.printMSG(sender, "msg_removebok",arg2);
				Activators.saveActivators();
			} else u.printMSG(sender, "msg_removebnf",arg2);
		} else if (arg1.equalsIgnoreCase("loc")){
			if (Locator.removeTpLoc(arg2)){
				u.printMSG(sender, "msg_removelocok",arg2);
				Locator.saveLocs();
			} else u.printMSG(sender, "msg_removelocnf",arg2);
		} else if (arg1.equalsIgnoreCase("timer")||arg1.equalsIgnoreCase("tmr")){
			Timers.removeTimer (sender,arg2);
		} else if (arg1.equalsIgnoreCase("var")||arg1.equalsIgnoreCase("variable")||arg1.equalsIgnoreCase("variables")){
			removeVariable(sender, arg2+ (arg3.isEmpty() ? "" : " "+arg3));
		} else if (arg1.equalsIgnoreCase("menu")||arg1.equalsIgnoreCase("m")){
			if (InventoryMenu.remove(arg2)) u.printMSG(sender,"msg_removemenu",arg2);
			else u.printMSG(sender,"msg_removemenufail",'c','4',arg2);
		} else if (Activators.contains(arg1)){
			Activator act = Activators.get(arg1);
			if (u.isIntegerGZ(arg3)) {
				int num = Integer.parseInt(arg3);
				if (arg2.equalsIgnoreCase("f")||arg2.equalsIgnoreCase("flag")){
					if (act.removeFlag(num-1)) u.printMSG(sender, "msg_flagremoved",act.getName(),num);
					else u.printMSG(sender, "msg_failedtoremoveflag",act.getName(),num);
				} else if (arg2.equalsIgnoreCase("a")||arg2.equalsIgnoreCase("action")){
					if (act.removeAction(num-1)) u.printMSG(sender, "msg_actionremoved",act.getName(),num);
					else u.printMSG(sender, "msg_failedtoremoveaction",act.getName(),num);
				} else if (arg2.equalsIgnoreCase("r")||arg2.equalsIgnoreCase("reaction")){
					if (act.removeReaction(num-1)) u.printMSG(sender, "msg_reactionremoved",act.getName(),num);
					else u.printMSG(sender, "msg_failedtoremovereaction",act.getName(),num);
				} else return false;
				Activators.saveActivators();
			} else u.printMSG(sender, "msg_wrongnumber",arg3); 
		}
		return true;

	}

	private boolean executeCmdSet(CommandSender sender, String[] args) {
		if (args.length == 1) return false;
		Player player = (sender instanceof Player) ? (Player) sender : null;
		String arg1 = args[1];
		String arg2 = args.length >=3 ? args[2] : "";
		if (args.length>4){
			for (int i = 3; i<args.length;i++) 
				arg2 = arg2+" "+args[i];
			arg2 = arg2.trim();
		}
		return this.setVariable(player, arg1, arg2 /*arg2+(arg3.isEmpty() ? "" : " "+arg3)*/);
	}

	private boolean executeCmdAdd(CommandSender sender, String[] args) {
		if (args.length == 1) return false;
		Player player = (sender instanceof Player) ? (Player) sender : null;
		String arg1 = args[1];
		String arg2 = args.length >=3 ? args[2] : "";
		String arg3 = args.length >=4 ? args[3] : "";
		String arg4 = args.length >=5 ? args[4] : "";
		if (args.length>5){
			for (int i = 5; i<args.length;i++) 
				arg4 = arg4+" "+args[i];
			arg4 = arg4.trim();
		}
		if (ActivatorType.isValid(arg1))  {
			@SuppressWarnings("deprecation")
			Block block = player != null ?  player.getTargetBlock(null, 100) : null;
			return addActivator(player, arg1, arg2, (arg3.isEmpty() ? "":arg3) +  (arg4.isEmpty() ? "" : " "+ arg4), block);
		} else if (arg1.equalsIgnoreCase("loc")){
			if (player == null) return false;
			Locator.addTpLoc(arg2, player.getLocation());
			Locator.saveLocs();
			u.printMSG(sender, "cmd_addtpadded",arg2);
		} else if (arg1.equalsIgnoreCase("timer")){
			Map<String,String> params = ParamUtil.parseParams((arg3.isEmpty() ? "":arg3) +  (arg4.isEmpty() ? "" : " "+ arg4));
			return Timers.addTimer(sender,arg2, params,true);
		} else if (arg1.equalsIgnoreCase("menu")){
			// /react add menu id size sdjkf
			if (InventoryMenu.add(arg2, u.isInteger(arg3) ? Integer.parseInt(arg3) : 9, ((u.isInteger(arg3) ? "":arg3+" ") +  (arg4.isEmpty() ? "" : arg4)).trim()))
				u.printMSG(sender, "cmd_addmenuadded",arg2);
			else u.printMSG(sender, "cmd_addmenuaddfail",arg2);
		} else if (Activators.contains(arg1)){
			String param = Util.replaceStandartLocations(player, arg4); // используется в addActions
			if (arg2.equalsIgnoreCase("a")||arg2.equalsIgnoreCase("action")){
				if (addAction (arg1, arg3, param)){
					Activators.saveActivators();
					u.printMSG(sender, "cmd_actadded", arg3 + " ("+ param+")"); //TODO~
					return true;
				} else u.printMSG(sender, "cmd_actnotadded",arg3 + " ("+ param+")");
			} else if (arg2.equalsIgnoreCase("r")||arg2.equalsIgnoreCase("reaction")){
				if (addReAction (arg1, arg3, param)){
					Activators.saveActivators();
					u.printMSG(sender, "cmd_reactadded",arg3 + " ("+ param+")");	
					return true;
				} else u.printMSG(sender, "cmd_reactnotadded",arg3 + " ("+ param+")");
			} else if (arg2.equalsIgnoreCase("f")||arg2.equalsIgnoreCase("flag")){
				if (addFlag (arg1, arg3, param)){
					Activators.saveActivators();
					u.printMSG(sender, "cmd_flagadded",arg3 + " ("+ param+")");
					return true;					
				} else u.printMSG(sender, "cmd_flagnotadded",arg3 + " ("+ arg4+")");
			} else u.printMSG(sender, "cmd_unknownbutton",arg2);            	
		} else u.printMSG(sender, "cmd_unknownadd",'c');
		return true;
	}




	/*
	 * TODO
	 * нужно добавить проверку на синтаксис, чтобы не добавлялся мусор
	 */
	public boolean addAction(String clicker, String flag, String param){
		if (Actions.isValid(flag)){    
			Activators.addAction(clicker, flag, param);
			return true;
		}
		return false;
	}

	public boolean addReAction(String clicker, String flag, String param){
		if (Actions.isValid(flag)){
			Activators.addReaction(clicker, flag, param);
			return true;
		}
		return false;
	}

	public boolean addFlag(String clicker, String fl, String param){
		String flag=fl.replaceFirst("!", "");
		boolean not = fl.startsWith("!");
		if (Flags.isValid(flag)){
			Activators.addFlag(clicker, flag, param, not); // все эти проверки вынести в соответствующие классы
			return true;
		}
		return false;
	}

	public void printGroupList(CommandSender p, int page, int lpp){
		List<String> grp = new ArrayList<String>();
		if (!Activators.findGroupsFromActivators().isEmpty())
			for (String g : Activators.findGroupsFromActivators())
				grp.add(g);
		u.printPage(p, grp, page, "msg_grouplisttitle", "", true);
	}


	public void printAct (CommandSender p, int page, int lpp){
		List<String> ag = Activators.getActivatorsList();
		u.printPage(p, ag, page, "msg_actlist", "", true);
		u.printMSG(p,"msg_listcount",Activators.size(),Locator.sizeTpLoc());
	}


	public void printActGroup (CommandSender p, String group, int page, int lpp){
		List<String> ag = Activators.getActivatorsListGroup(group);
		u.printPage(p, ag, page, "&6"+u.getMSG("msg_actlistgrp",group), "", true);
	}

	public void printActType(CommandSender p, String type, int page, int lpp){
		List<String> ag = Activators.getActivatorsList(type);
		u.printPage(p, ag, page, "&6"+u.getMSG("msg_actlisttype",type), "", true);
	}

	public void printActivatorsAround(Player p, int radius){
		int xx = p.getLocation().getBlockX();
		int yy = p.getLocation().getBlockY();
		int zz = p.getLocation().getBlockZ();
		Set<String> lst = new HashSet<String>();
		for (int x = xx-radius; x<=xx+radius;x++)
			for (int y = yy-radius; y<=yy+radius;y++)
				for (int z = zz-radius; z<=zz+radius;z++){
					List<Activator> found = Activators.getActivatorInLocation(p.getWorld(), x, y, z);
					if (found.isEmpty()) continue;
					for (int i = 0; i<found.size(); i++)
						lst.add(found.get(i).toString());
				}
		List<String> plst = new ArrayList<String>();
		plst.addAll(lst);
		u.printPage(p, plst, 1, "msg_check", "", true,100);
	}

	private void printActInfo(CommandSender p, String actname, String far) {
		Activator act = Activators.get(actname);
		boolean f = false;
		boolean a = false;
		boolean r = false;
		if (far.isEmpty()){
			f = true;
			a = true;
			r = true;
		} else {
			f = far.contains("f")||far.equalsIgnoreCase("flag")||far.equalsIgnoreCase("flags");
			a = far.contains("a")||far.equalsIgnoreCase("action")||far.equalsIgnoreCase("actions");
			r = far.contains("r")||far.equalsIgnoreCase("reaction")||far.equalsIgnoreCase("reactions");
		}

		u.printMsg(p, "&5☆ &d&l"+u.getMSGnc("msg_actinfotitle")+" &r&5☆");
		u.printMSG(p, "msg_actinfo",act.getName(), act.getType(), act.getGroup());
		u.printMSG(p, "msg_actinfo2",act.getFlags().size(),act.getActions().size(), act.getReactions().size());
		if (f&&(!act.getFlags().isEmpty())){
			List<String> flg = new ArrayList<String>();
			for (int i = 0; i<act.getFlags().size();i++)
				flg.add((act.getFlags().get(i).not ? "&4! &e" : "  &e" ) + act.getFlags().get(i).flag+" &3= &a"+act.getFlags().get(i).value);
			u.printPage(p, flg, 1, "lst_flags", "", true,100);
		} 
		if (a&&(!act.getActions().isEmpty())){
			List<String> flg = new ArrayList<String>();
			for (int i = 0; i<act.getActions().size();i++){
				String action = act.getActions().get(i).flag;
				String param = act.getActions().get(i).value;
				if (action.equalsIgnoreCase("tp")) {
					Location loc = Locator.parseCoordinates(param);//Util.parseLocation(param);
					if (loc!=null) param = Locator.locationToStringFormated(loc);
				}
				flg.add("  &e" + action +" &3= &a"+param);
			}
			u.printPage(p, flg, 1, "lst_actions", "", true,100);
		}
		if (r&&(!act.getReactions().isEmpty())){
			List<String> flg = new ArrayList<String>();
			for (int i = 0; i<act.getReactions().size();i++){
				String action = act.getReactions().get(i).flag;
				String param = act.getReactions().get(i).value;
				if (action.equalsIgnoreCase("tp")) {
					Location loc = Locator.parseCoordinates(param);
					if (loc!=null) param = Locator.locationToStringFormated(loc);
				}
				flg.add("  &e" + action +" &3= &a"+param);
			}
			u.printPage(p, flg, 1, "lst_reactions", "", true,100);
		} 
	}

	private boolean setVariable(CommandSender sender, String var, String param){
		Player p = (sender instanceof Player) ? (Player) sender : null;
		Map<String,String> params = ParamUtil.parseParams(param, "id");
		String id = ParamUtil.getParam(params, "id", "");
		if (id.isEmpty()) return u.returnMSG (true, sender, "msg_needvdmid",'c');

		if (var.equalsIgnoreCase("delay")||var.equalsIgnoreCase("d")){
			String player = ParamUtil.getParam(params, "player", "");
			if (player.equalsIgnoreCase("%player%")&&(p!=null)) player = p.getName();
			Long time = /*System.currentTimeMillis()+*/u.parseTime(ParamUtil.getParam(params,"delay","3s")); //дефолтная задержка три секунды
			if (player.isEmpty()) Delayer.setDelay(id, time);
			else Delayer.setPersonalDelay(player, id, time);
			u.printMSG(sender, "cmd_delayset", player.isEmpty() ? id : player+"."+id, Time.fullTimeToString(System.currentTimeMillis()+time));
		} else if (var.equalsIgnoreCase("var")||var.equalsIgnoreCase("variable")||var.equalsIgnoreCase("v")){
			String value = ParamUtil.getParam(params, "value", "");
			String player = ParamUtil.getParam(params, "player", "");
			Variables.setVar(player, id, value);
			return u.returnMSG(true, sender, "cmd_varset", player.isEmpty() ? id : player+"."+id, Variables.getVar(player, id, ""));
		} else if (var.equalsIgnoreCase("menu")||var.equalsIgnoreCase("m")){
			if (InventoryMenu.set(id, params)) return u.returnMSG(true, sender, "msg_menuparamset",id);
			else return u.returnMSG(true, sender, "msg_menusetfail",'c','4',id);
		} else return false;
		return true;
	}

	private boolean addActivator (Player p, String type, String name, String param, Block b){
		ActivatorType at = ActivatorType.getByName(type);
		if (at==null) return false;
		Activator activator = null;

		switch (at){
		case BUTTON:
			if (b == null) return false;
			if ((b.getType()==Material.STONE_BUTTON)||(b.getType()==Material.WOOD_BUTTON)) {
				activator = new ButtonActivator (name,b);
			} else u.printMSG(p, "cmd_addbreqbut");
			break;
		case COMMAND:
			activator = new CommandActivator (name,param);
			break;
		case EXEC:
			activator = new ExecActivator (name);
			break;
		case PLATE:
			if (b == null) return false;
			if ((b.getType()==Material.STONE_PLATE)||(b.getType()==Material.WOOD_PLATE)) {
				activator = new PlateActivator (name,b);
			} else u.printMSG(p, "cmd_addbreqbut");
			break;
		case PVP_RESPAWN:
			activator = new PVPRespawnActivator (name);
			break;
		case PVP_KILL:
			activator = new PVPKillActivator (name);
			break;
		case PVP_DEATH:
			activator = new PVPDeathActivator (name);
			break;
		case REGION:
			if (param.isEmpty()) u.printMSG(p, "msg_needregion",'c');
			else activator = new RegionActivator (name,param);
			break;
		case REGION_ENTER:
			if (param.isEmpty()) u.printMSG(p, "msg_needregion",'c');
			else activator = new RgEnterActivator (name,param);
			break;
		case REGION_LEAVE:
			if (param.isEmpty()) u.printMSG(p, "msg_needregion",'c');
			else activator = new RgLeaveActivator (name,param);
			break;
		case LEVER:
			if (b == null) return false;
			if (b.getType()==Material.LEVER) {
				activator = new LeverActivator (name,param,b);
			} else u.printMSG(p, "cmd_addbreqbut");
			break;
		case DOOR:
			if (b == null) return false;
			if (Util.isDoorBlock(b)){
				activator = new DoorActivator (name,param,Util.getDoorBottomBlock(b));
			} else u.printMSG(p, "cmd_addbreqbut");
			break;
		case JOIN:
			activator = new JoinActivator(name,param);
			break;
		case MOBCLICK:    
			activator = new MobClickActivator (name, param);
			break;
		case ITEM_CLICK:    
			if (!param.isEmpty()) activator = new ItemClickActivator (name, param);
			break;
		case ITEM_HOLD:    
			if (!param.isEmpty()) activator = new ItemHoldActivator (name, param);
			break;
		case ITEM_WEAR:    
			if (!param.isEmpty()) activator = new ItemWearActivator (name, param);
			break;
		case FCT_CHANGE:    
			activator = new FactionActivator (name, param);
			break;
		case FCT_RELATION:    
			activator = new FactionRelationActivator (name, param);
			break;
		case FCT_CREATE:    
			activator = new FactionCreateActivator (name, param);
			break;
		case FCT_DISBAND:    
			activator = new FactionCreateActivator (name, param);
			break;
		case SIGN:    
			activator = new SignActivator (name, param);
			break;
		default:
			break;
		}
		if (activator == null) return false;
		if (Activators.addActivator(activator)) {
			Activators.saveActivators();
			u.printMSG(p, "cmd_addbadded",activator.toString());
		} else u.printMSG(p, "cmd_notaddbadded",activator.toString());
		return true;
	}

	public boolean removeVariable(CommandSender sender, String param){
		Player p = (sender instanceof Player) ? (Player) sender :null;
		Map<String,String> params = ParamUtil.parseParams(param);
		String player = ParamUtil.getParam(params, "player", "");
		if (player.equalsIgnoreCase("%player%")&&p!=null) player = p.getName();
		String id = ParamUtil.getParam(params, "id", "");
		if (id.isEmpty()) return u.returnMSG(true, sender, "msg_varneedid");
		if (Variables.removeVar(player, id)) return u.returnMSG(true, sender, "msg_varremoved",id);
		return u.returnMSG(true, sender, "msg_varremovefail");
	}



}
