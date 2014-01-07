/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2014, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/reactions/
 *   * 
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
import me.fromgate.reactions.event.EventManager;
import me.fromgate.reactions.flags.Flags;
import me.fromgate.reactions.timer.Timers;
import me.fromgate.reactions.util.Delayer;
import me.fromgate.reactions.util.ParamUtil;
import me.fromgate.reactions.util.Profiler;
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
			switch (args.length){
			case 1: return ExecuteCmd (sender, args[0]);
			case 2: return ExecuteCmd (sender, args[0],args[1]);
			case 3: return ExecuteCmd (sender, args[0],args[1],args[2]);
			case 4: return ExecuteCmd (sender, args[0],args[1],args[2],args[3]);
			case 5: return ExecuteCmd (sender, args[0],args[1],args[2],args[3],args[4]);
			default:
				if (args.length>=5){
					String arg4 = "";
					for (int i = 4; i<args.length;i++) 
						arg4 = arg4+" "+args[i];
					arg4 = arg4.trim();
					return ExecuteCmd (sender, args[0],args[1],args[2],args[3],arg4);
				}
			}
		} else u.printMSG(sender, "cmd_cmdpermerr",'c');
		return false;
	}


	public boolean ExecuteCmd (CommandSender sender, String cmd){
		Player p = null;
		if (sender instanceof Player) p = (Player) sender;
		if (cmd.equalsIgnoreCase("help")){
			int lpp = 10;
			if (p==null) lpp=1000;
			u.PrintHlpList(sender, 1, lpp);
		} else if (cmd.equalsIgnoreCase("profile")){
			Profiler.start((long) 20*30, sender);
		} else if (cmd.equalsIgnoreCase("select")){
			Selector.selectLocation(p, null);
			u.printMSG(p, "cmd_selected", Util.locationToStringFormated(Selector.getSelectedLocation(p)));
		} else if (cmd.equalsIgnoreCase("debug")){
			if (p == null) return false;
			RADebug.offPlayerDebug(p);
			u.printMSG(p, "cmd_debugoff");
		} else if (cmd.equalsIgnoreCase("list")){
			int lpp = 15;
			if (p==null) lpp=1000;
			printAct(sender, 1, lpp);
		} else if (cmd.equalsIgnoreCase("reload")){
			Activators.clear();
			Activators.loadActivators();
			plg.tports.clear();
			plg.loadLocs();
			plg.reloadConfig();
			plg.loadCfg();
			Delayer.load();
			Variables.load();
			Timers.load();
			u.printMSG(sender, "msg_cmdreload",Activators.size(),plg.tports.size());
		} else if (cmd.equalsIgnoreCase("check")){
			if (p==null) return false;
			printActivatorsAround(p, 8);
		} else return false;
		return true;
	}

	public boolean ExecuteCmd (CommandSender sender, String cmd, String arg){
		Player p = null;
		if (sender instanceof Player) p = (Player) sender;
		if (cmd.equalsIgnoreCase("run")){
			if (EventManager.raiseExecEvent(sender, arg)) u.printMSG(sender, "cmd_runplayer",arg);
			else u.printMSG(sender, "cmd_runplayerfail",'c','6',arg);
		} else if (cmd.equalsIgnoreCase("profile")){
			long ticks = u.timeToTicks(u.parseTime(arg));
			if (ticks == 0) ticks = (long) 20*30;
			Profiler.start(ticks, sender);
		} else if (cmd.equalsIgnoreCase("help")){
			if (arg.equalsIgnoreCase("flag")||arg.equalsIgnoreCase("flags")){
				Flags.listFlags(sender, 1);
			} else if (arg.equalsIgnoreCase("action")||arg.equalsIgnoreCase("actions")){
				Actions.listActions(sender, 1);
			} else if (arg.equalsIgnoreCase("activator")||arg.equalsIgnoreCase("activators")){
				ActivatorType.listActivators(sender, 1);
			} else {
				int lpp = 10;
				int page = 1;
				if (p==null) {
					lpp=1000;
					page = 1;
				}
				if (u.isIntegerGZ(arg)) page = Integer.parseInt(arg);
				u.PrintHlpList(sender, page, lpp);
			}
		} else if (cmd.equalsIgnoreCase("info")){
			if (Activators.contains(arg)){
				printActInfo(sender,arg,"");
			} else u.printMSG(sender, "cmd_unknownbutton",arg);
		} else if (cmd.equalsIgnoreCase("check")){
			if (p==null) return false;
			int radius = 8;
			if (u.isInteger(arg)) radius = Integer.parseInt(arg);
			this.printActivatorsAround(p, radius);
		} else if (cmd.equalsIgnoreCase("debug")){
			if (p==null) return false;
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
		} else if (cmd.equalsIgnoreCase("list")&&u.isIntegerGZ(arg)){
			int page = Integer.parseInt(arg);
			int lpp = 15;
			if (p==null) {
				lpp=1000;
				page = 1;
			}
			printAct(sender, page, lpp);
		} else if (cmd.equalsIgnoreCase("list")){
			int lpp = 15;
			if (p==null) lpp=1000;
			if (arg.equalsIgnoreCase("all")){
				printAct(sender, 1, lpp);
			} else if (arg.equalsIgnoreCase("timer")||arg.equalsIgnoreCase("timers")){
				Timers.listTimers(sender, 1);
			} else if (arg.equalsIgnoreCase("delay")||arg.equalsIgnoreCase("delays")){
				Delayer.printDelayList(sender, 1, lpp);
			} else if (arg.equalsIgnoreCase("loc")||arg.equalsIgnoreCase("location")){
				printLocList(sender,1,lpp);
			} else {
				u.printMSG(sender,"msg_listcount",Activators.size(),plg.tports.size());
			}
		} else return false; 
		return true;
	}


	//rea add cmd <id>

	@SuppressWarnings("deprecation")
	public boolean ExecuteCmd (CommandSender sender, String cmd, String arg1, String arg2){
		Player p = null;
		if (sender instanceof Player) p = (Player) sender;
		// /react run <player|region:<region name>> <activator>
		if (cmd.equalsIgnoreCase("run")){
			if (EventManager.raiseExecEvent(sender, arg1+" "+arg2)) u.printMSG(sender, "cmd_runplayer",arg1+" "+arg2);
			else u.printMSG(sender, "cmd_runplayerfail",'c','6',arg1+" "+arg2);
		} else if (cmd.equalsIgnoreCase("help")){
			int page = u.isIntegerGZ(arg2) ? Integer.parseInt(arg2) : 1;
			if (arg1.equalsIgnoreCase("flag")||arg1.equalsIgnoreCase("flags")){
				Flags.listFlags(sender, page);
			} else if (arg1.equalsIgnoreCase("action")||arg1.equalsIgnoreCase("actions")){
				Actions.listActions(sender, page);
			} else if (arg1.equalsIgnoreCase("activator")||arg1.equalsIgnoreCase("activators")){
				ActivatorType.listActivators(sender, page);
			} else return false;
		} else if (cmd.equalsIgnoreCase("set")){
			return this.setVariable(p, arg1, arg2);
		} else if (cmd.equalsIgnoreCase("add")){
			if (ActivatorType.isValid(arg1))  {
				Block b = p.getTargetBlock(null, 100);
				return addActivator(p, arg1, arg2, "", b);
			} else if (arg1.equalsIgnoreCase("loc")){
				plg.tports.put(arg2, new TpLoc (p.getLocation()));
				plg.saveLocs();
				u.printMSG(p, "cmd_addtpadded",arg2);
			} else u.printMSG(p, "cmd_unknownadd",'c');
		} else if (cmd.equalsIgnoreCase("copy")) {
			if (Activators.copyAll(arg1, arg2)) u.printMSG(sender, "msg_copyall",arg1,arg2);
			else u.printMSG(sender, "msg_copyallfailed",'c','4',arg1,arg2);
		} else if (cmd.equalsIgnoreCase("info")){
			if (Activators.contains(arg1)){
				printActInfo(sender,arg1,arg2);
			} else u.printMSG(sender, "cmd_unknownbutton",arg1);
		} else if (cmd.equalsIgnoreCase("list")) {
			int lpp = 15;
			if (p==null) lpp=1000;
			//   /ra list type <type> [pnum] 4 [5]
			if (arg1.equalsIgnoreCase("type")){
				printActType(p, arg2, 1, lpp);
			} else if (arg1.equalsIgnoreCase("group")){
				printActGroup(p, arg2, 1, lpp);
			} else if (arg1.equalsIgnoreCase("all")&&u.isIntegerGZ(arg2)){
				this.printAct(p, Integer.parseInt(arg2), lpp);
			} else if ((arg1.equalsIgnoreCase("timer")||arg1.equalsIgnoreCase("timers"))&&u.isIntegerGZ(arg2)){
				Timers.listTimers(sender, Integer.parseInt(arg2));
			} else if ((arg1.equalsIgnoreCase("delay")||arg1.equalsIgnoreCase("delays"))&&u.isIntegerGZ(arg2)){
				Delayer.printDelayList(p, Integer.parseInt(arg2), lpp);
			} else if ((arg1.equalsIgnoreCase("loc")||arg1.equalsIgnoreCase("location"))&&u.isIntegerGZ(arg2)){
				printLocList(p,Integer.parseInt(arg2),lpp);
			}
		} else if (cmd.equalsIgnoreCase("remove")){
			if (arg1.equalsIgnoreCase("act")||
					arg1.equalsIgnoreCase("activator")){
				if (Activators.contains(arg2)){
					Activators.removeActivator(arg2);
					u.printMSG(sender, "msg_removebok",arg2);
					Activators.saveActivators();
				} else u.printMSG(sender, "msg_removebnf",arg2);
			} else if (arg1.equalsIgnoreCase("loc")){
				if (plg.tports.containsKey(arg2)){
					plg.tports.remove(arg2);
					u.printMSG(sender, "msg_removelocok",arg2);
					plg.saveLocs();
				} else u.printMSG(sender, "msg_removelocnf",arg2);
			} else if (arg1.equalsIgnoreCase("timer")||arg1.equalsIgnoreCase("tmr")){
				Timers.removeTimer (sender,arg2);
			} 
		} else if (cmd.equalsIgnoreCase("clear")){
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
			} else u.printMSG(sender, "cmd_unknownbutton",arg1);

		} else if (cmd.equalsIgnoreCase("group")) {
			if (Activators.setGroup(arg1, arg2)) {
				Activators.saveActivators();
				u.printMSG(sender, "msg_groupset",arg1, arg2);
			}
			else u.printMSG(sender, "msg_groupsetfailed",arg1, arg2);
		} else	return false;

		return true;
	}


	public boolean addAction(String clicker, String flag, String param){
		//if (u.isWordInList(flag, Actions.atypes)){
		if (Actions.isValid(flag)){    
			if ((flag.equalsIgnoreCase("loc")&&(!plg.tports.containsKey(param)))||
					(flag.equals("dmg")&&(!(param.matches("[0-9]*")||param.isEmpty()))))
				return false;
			Activators.addAction(clicker, flag, param);
			return true;
		}
		return false;
	}

	public boolean addReAction(String clicker, String flag, String param){
		if (Actions.isValid(flag)){
			if ((flag.equalsIgnoreCase("tp")&&(!plg.tports.containsKey(param)))||
					(flag.equals("dmg")&&(!(param.matches("[0-9]*")||param.isEmpty()))))
				return false;
			Activators.addReaction(clicker, flag, param);
			return true;
		}
		return false;
	}

	public boolean addFlag(Player p, String clicker, String fl, String param){
		String flag=fl.replaceFirst("!", "");
		boolean not = fl.startsWith("!");
		param = Util.replaceStandartLocations(p, param); //Util.processLocationInParam(p,param);
		if (Flags.isValid(flag)){
			Activators.addFlag(clicker, flag, param, not); // все эти проверки вынести в соответствующие классы
			return true;
		}
		return false;
	}


	//   /ra add     region        name wg
	//   /ra add      a|r          dmg/msg  value 	
	public boolean ExecuteCmd (CommandSender s, String cmd, String arg1, String arg2, String arg3){
		Player p = null;
		if (s instanceof Player) p = (Player) s;
		if (cmd.equalsIgnoreCase("run")){
			if (EventManager.raiseExecEvent(s, arg1+" "+arg2+" "+arg3)) u.printMSG(s, "cmd_runplayer",arg1+" "+arg2+" "+arg3);
			else u.printMSG(s, "cmd_runplayerfail",'c','6',arg1+" "+arg2+" "+arg3);
		} else if (cmd.equalsIgnoreCase("set")){
			return this.setVariable(p, arg1, arg2+" "+arg3);
		} else if (cmd.equalsIgnoreCase("add")){
			if (ActivatorType.isValid(arg1))  {
				@SuppressWarnings("deprecation")
				Block b = p.getTargetBlock(null, 100);
				return addActivator(p, arg1, arg2, arg3, b);
			} else return false;
		} else if (cmd.equalsIgnoreCase("remove")) {
			if (Activators.contains(arg1)){
				Activator act = Activators.get(arg1);
				if (u.isIntegerGZ(arg3)) {
					int num = Integer.parseInt(arg3);
					if (arg2.equalsIgnoreCase("f")||arg2.equalsIgnoreCase("flag")){
						if (act.removeFlag(num-1)) u.printMSG(p, "msg_flagremoved",act.getName(),num);
						else u.printMSG(p, "msg_failedtoremoveflag",act.getName(),num);
					} else if (arg2.equalsIgnoreCase("a")||arg2.equalsIgnoreCase("action")){
						if (act.removeAction(num-1)) u.printMSG(p, "msg_actionremoved",act.getName(),num);
						else u.printMSG(p, "msg_failedtoremoveaction",act.getName(),num);
					} else if (arg2.equalsIgnoreCase("r")||arg2.equalsIgnoreCase("reaction")){
						if (act.removeReaction(num-1)) u.printMSG(p, "msg_reactionremoved",act.getName(),num);
						else u.printMSG(p, "msg_failedtoremovereaction",act.getName(),num);
					} else return false;
					Activators.saveActivators();
				} else u.printMSG(p, "msg_wrongnumber",arg3); 
			} else u.printMSG(p, "cmd_unknownbutton",arg1);
		} else if (cmd.equalsIgnoreCase("copy")) {
			if (arg1.equalsIgnoreCase("f")||arg1.equalsIgnoreCase("flag")){
				if (Activators.copyFlags(arg2, arg3)) u.printMSG(p, "msg_copyflags",arg2,arg3);
				else u.printMSG(p, "msg_copyflagsfailed",'c','4',arg2,arg3);
			} else if (arg1.equalsIgnoreCase("a")||arg1.equalsIgnoreCase("actions")){
				if (Activators.copyActions(arg2, arg3)) u.printMSG(p, "msg_copyactions",arg2,arg3);
				else u.printMSG(p, "msg_copyactionsfailed",'c','4',arg2,arg3);
			} else if (arg1.equalsIgnoreCase("r")||arg1.equalsIgnoreCase("reactions")){
				if (Activators.copyReactions(arg2, arg3)) u.printMSG(p, "msg_copyreactions",arg2,arg3);
				else u.printMSG(p, "msg_copyreactionsfailed",'c','4',arg2,arg3);
			}
		} else if (cmd.equalsIgnoreCase("list")) {
			if (arg1.equalsIgnoreCase("type")&&u.isIntegerGZ(arg3)){
				printActType(p, arg2, Integer.parseInt(arg3), 15);
			} else if (arg1.equalsIgnoreCase("group")&&u.isIntegerGZ(arg3)){
				printActGroup(p, arg2, Integer.parseInt(arg3), 15);
			}
		} else if (Activators.contains(arg1)){
			if (((arg2.equalsIgnoreCase("a"))||(arg2.equalsIgnoreCase("r")))&&
					(arg3.equalsIgnoreCase("dmg")||arg3.equalsIgnoreCase("msg"))){
				if (arg2.equalsIgnoreCase("a")) {
					if (addAction(arg1, arg3, "")) {
						u.printMSG(p, "cmd_actadded",cmd+" "+arg2+" "+arg1 +" "+arg3);
						Activators.saveActivators();
						return true;
					}
					else u.printMSG(p, "cmd_actnotadded",cmd+" "+arg2+" "+arg1 +" "+arg3);
				} 
				else if (arg2.equalsIgnoreCase("r")) {
					if (addReAction(arg1, arg3, "")) {
						u.printMSG(p, "cmd_reactadded",cmd+" "+arg2+" "+arg1 +" "+arg3);
						Activators.saveActivators();
						return true;
					}
					else u.printMSG(p, "cmd_reactnotadded",cmd+" "+arg2+" "+arg1 +" "+arg3);
				}
			}
		} else return false;
		return true;
	}

	//                                          add          a           <name>   <flag>    <param>
	//                                          add          <name>        a      <flag>    <param>
	public boolean ExecuteCmd (CommandSender s, String cmd, String arg1, String arg2, String arg3, String arg4){
		Player p = null;
		if (s instanceof Player) p = (Player) s;
		if (cmd.equalsIgnoreCase("add")){
			String param = Util.replaceStandartLocations(p, arg4);
			if (ActivatorType.isValid(arg1))  {
				@SuppressWarnings("deprecation")
				Block b = p.getTargetBlock(null, 100);
				return addActivator(p, arg1, arg2, (arg3.isEmpty() ? "":arg3) +  (arg4.isEmpty() ? "" : " "+ arg4), b);
			} else if (arg1.equalsIgnoreCase("timer")){
				Map<String,String> params = ParamUtil.parseParams((arg3.isEmpty() ? "":arg3) +  (arg4.isEmpty() ? "" : " "+ arg4));
				return Timers.addTimer(s,arg2, params,true);
			} else if (Activators.contains(arg1)){
				if (arg2.equalsIgnoreCase("a")||arg2.equalsIgnoreCase("action")){
					if (addAction (arg1, arg3, param)){
						Activators.saveActivators();
						u.printMSG(p, "cmd_actadded", arg3 + " ("+ param+")"); //TODO~
						return true;
					} else u.printMSG(p, "cmd_actnotadded",arg3 + " ("+ param+")");
				} else if (arg2.equalsIgnoreCase("r")||arg2.equalsIgnoreCase("reaction")){
					if (addReAction (arg1, arg3, param)){
						Activators.saveActivators();
						u.printMSG(p, "cmd_reactadded",arg3 + " ("+ param+")");	
						return true;
					} else u.printMSG(p, "cmd_reactnotadded",arg3 + " ("+ param+")");
				} else if (arg2.equalsIgnoreCase("f")||arg2.equalsIgnoreCase("flag")){
					if (addFlag (p, arg1, arg3, arg4)){
						Activators.saveActivators();
						u.printMSG(p, "cmd_flagadded",arg3 + " ("+ Util.replaceStandartLocations(p, arg4)+")");
						return true;					
					} else u.printMSG(p, "cmd_flagnotadded",arg3 + " ("+ arg4+")");
				} else u.printMSG(p, "cmd_unknownbutton",arg2);            	
			} else return false;
		} else if (cmd.equalsIgnoreCase("set")){
			return this.setVariable(p, arg1, arg2+" "+arg3+" "+arg4);
		} else return false;            

		return true;
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
		u.printMSG(p,"msg_listcount",Activators.size(),plg.tports.size());
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
			f = far.contains("f");
			a = far.contains("a");
			r = far.contains("r");
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
					Location loc = Util.parseLocation(param);
					if (loc!=null) param = Util.locationToStringFormated(loc);
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
					Location loc = Util.parseLocation(param);
					if (loc!=null) param = Util.locationToStringFormated(loc);
				}
				flg.add("  &e" + action +" &3= &a"+param);
			}
			u.printPage(p, flg, 1, "lst_reactions", "", true,100);
		} 

	}

	//   /ra add button name
	private void printLocList(CommandSender p, int page, int lpp) {
		List<String> lst = new ArrayList<String>();
		for (String loc : plg.tports.keySet()){
			lst.add("&3"+loc+" &a"+plg.tports.get(loc).toString());
		}
		u.printPage(p, lst, page, "msg_listloc", "", true);
	}

	private boolean setVariable(Player p, String var, String param){
		if (var.equalsIgnoreCase("delay")){
			Map<String,String> params = ParamUtil.parseParams(param, "delay");
			String player = ParamUtil.getParam(params, "player", "");
			if (player.equalsIgnoreCase("%player%")&&(p!=null)) player = p.getName();
			Long time = System.currentTimeMillis()+u.parseTime(ParamUtil.getParam(params,"delay","3s")); //дефолтная задержка три секунды
			String id = ParamUtil.getParam(params, "id", "");
			if (id.isEmpty()) return false;
			if (player.isEmpty()) Delayer.setDelay(id, time);
			else Delayer.setPersonalDelay(player, id, time);
			u.printMSG(p, "cmd_delayset", player.isEmpty() ? id : player+"."+id, Util.timeToString(time,true));
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




}
