/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2013, fromgate, fromgate@gmail.com
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
import java.util.Set;
import me.fromgate.reactions.activators.Activator;
import me.fromgate.reactions.activators.ButtonActivator;
import me.fromgate.reactions.activators.PlateActivator;
import me.fromgate.reactions.activators.RegionActivator;
import me.fromgate.reactions.activators.RgEnterActivator;
import me.fromgate.reactions.activators.RgLeaveActivator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;



public class RACmd implements CommandExecutor{
    ReActions plg;
    RAUtil u;
    public RACmd (ReActions plg){
        this.plg = plg;
        this.u = this.plg.u;
    }

    /*
     *  /react copy act1 act2
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        if (sender instanceof Player){
            Player p = (Player) sender;

            if ((args.length>0)&&u.checkCmdPerm(p, args[0])){
                if (args.length==1) return ExecuteCmd (p, args[0]);
                else if (args.length==2) return ExecuteCmd (p, args[0],args[1]);
                else if (args.length==3) return ExecuteCmd (p, args[0],args[1],args[2]);
                else if (args.length==4) return ExecuteCmd (p, args[0],args[1],args[2],args[3]);
                else if (args.length==5) return ExecuteCmd (p, args[0],args[1],args[2],args[3],args[4]);
                //else if ((args.length==6)&&args[0].equalsIgnoreCase("add")&&args[3].equalsIgnoreCase("tp")&&(args[4].equalsIgnoreCase("here"))) return ExecuteCmd (p, args[0],args[1],args[2],args[3],args[4],args[5]);
                else if (args.length>=5){
                    String arg4 = "";
                    for (int i = 4; i<args.length;i++) 
                        arg4 = arg4+" "+args[i];
                    arg4 = arg4.trim();
                    return ExecuteCmd (p, args[0],args[1],args[2],args[3],arg4);
                }
            } else u.printMSG(p, "cmd_cmdpermerr",'c');
            return true;
        } 
        return false;
    }

/*    private boolean ExecuteCmd(Player p, String cmd, String arg1,String arg2, String arg3, String arg4, String arg5) {
        if (cmd.equalsIgnoreCase("add")&&plg.activators.contains(arg1)){
            String param = arg4;
            int radius = 0;
            if (arg3.equalsIgnoreCase("tp")){
                if (param.equalsIgnoreCase("here")) param = Util.locationToString(p.getLocation());
                if (u.isInteger(arg5)) radius = Integer.parseInt(arg5); 
                param = param+"@"+Integer.toString(radius);
                if (arg2.equalsIgnoreCase("a")){
                    if (addAction (arg1, arg3, param)){
                        plg.activators.saveActivators();
                        u.printMSG(p, "cmd_actadded", arg3 + " ("+ param+")"); //TODO~
                        return true;
                    } else u.printMSG(p, "cmd_actnotadded",arg3 + " ("+ param+")");
                } else if (arg2.equalsIgnoreCase("r")){
                    if (addReAction (arg1, arg3, param)){
                        plg.activators.saveActivators();
                        u.printMSG(p, "cmd_reactadded",arg3 + " ("+ param+")"); 
                        return true;
                    }
                }
            }
        }
        return false;
    } */

    public boolean ExecuteCmd (Player p, String cmd){
        if (cmd.equalsIgnoreCase("help")){
            u.PrintHlpList(p, 1, 10);
        } else if (cmd.equalsIgnoreCase("debug")){
            plg.debug.offPlayerDebug(p);
            u.printMSG(p, "cmd_debugoff");
        } else if (cmd.equalsIgnoreCase("list")){
            printAct(p, 1, 15);
        } else if (cmd.equalsIgnoreCase("reload")){
            plg.activators.clear();
            plg.activators.loadActivators();
            plg.tports.clear();
            plg.loadLocs();
            u.printMSG(p, "msg_cmdreload",plg.activators.size(),plg.tports.size());
        } else if (cmd.equalsIgnoreCase("check")){
            printActivatorsAround(p, 8);
        } else return false;
        return true;
    }

    public boolean ExecuteCmd (Player p, String cmd, String arg){
        if (cmd.equalsIgnoreCase("help")){
            int page = 1;
            if (u.isIntegerGZ(arg)) page = Integer.parseInt(arg);
            u.PrintHlpList(p, page, 10);
        } else if (cmd.equalsIgnoreCase("info")){
            if (plg.activators.contains(arg)){
                printActInfo(p,arg,"");
            } else u.printMSG(p, "cmd_unknownbutton",arg);
        } else if (cmd.equalsIgnoreCase("list")&&u.isIntegerGZ(arg)){
            printAct(p, Integer.parseInt(arg), 15);
        } else if (cmd.equalsIgnoreCase("check")){
            int radius = 8;
            if (u.isInteger(arg)) radius = Integer.parseInt(arg);
            this.printActivatorsAround(p, radius);
        } else if (cmd.equalsIgnoreCase("debug")){
            if (arg.equalsIgnoreCase("false")) {
                plg.debug.setPlayerDebug(p, false);
                u.printMSG(p, "cmd_debugfalse");
            } else if (arg.equalsIgnoreCase("true")) {
                plg.debug.setPlayerDebug(p, true);
                u.printMSG(p, "cmd_debugtrue");
            } else {
                plg.debug.offPlayerDebug(p);
                u.printMSG(p, "cmd_debugoff");
            }
        } else if (cmd.equalsIgnoreCase("list")){
            if (arg.equalsIgnoreCase("all")){
                printAct(p, 1, 15);
            } else if (arg.equalsIgnoreCase("loc")){
                printLocList(p,1,15);
            } else {
                u.printMSG(p,"msg_listcount",plg.activators.size()+";"+plg.tports.size());
            }
        } else return false; 
        return true;
    }


    public boolean ExecuteCmd (Player p, String cmd, String arg1, String arg2){
        if (cmd.equalsIgnoreCase("add")){
            if (arg1.equalsIgnoreCase("button")||arg1.equalsIgnoreCase("b")){
                Block b = p.getTargetBlock(null, 100); 
                if ((b != null)&&((b.getType()==Material.STONE_BUTTON)||
                        (b.getType()==Material.WOOD_BUTTON))){
                    ButtonActivator ba = new ButtonActivator (arg2, b);
                    if (plg.activators.addActivator(ba)) {
                        plg.activators.saveActivators();
                        u.printMSG(p, "cmd_addbadded",ba.toString());
                    } else u.printMSG(p, "cmd_notaddbadded",ba.toString());
                } else u.printMSG(p, "cmd_addbreqbut");
                return true;
            } else if (arg1.equalsIgnoreCase("plate")||arg1.equalsIgnoreCase("p")){
                Block b = p.getTargetBlock(null, 100); 
                if ((b != null)&&((b.getType()==Material.STONE_PLATE)||
                        (b.getType()==Material.WOOD_PLATE))){
                    PlateActivator ba = new PlateActivator (arg2, b);
                    if (plg.activators.addActivator(ba)) {
                        plg.activators.saveActivators();
                        u.printMSG(p, "cmd_addbadded",ba.toString());
                    } else u.printMSG(p, "cmd_notaddbadded",ba.toString());
                } else u.printMSG(p, "cmd_addbreqbut");
                return true;
            } else if (arg1.equalsIgnoreCase("loc")){
                plg.tports.put(arg2, new RALoc (p.getLocation()));
                plg.saveLocs();
                u.printMSG(p, "cmd_addtpadded",arg2);
            } else u.printMSG(p, "cmd_unknownadd",'c');
        } else if (cmd.equalsIgnoreCase("copy")) {
            if (plg.activators.copyAll(arg1, arg2)) u.printMSG(p, "msg_copyall",arg1,arg2);
            else u.printMSG(p, "msg_copyallfailed",'c','4',arg1,arg2);
        } else if (cmd.equalsIgnoreCase("info")){
            if (plg.activators.contains(arg1)){
                printActInfo(p,arg1,arg2);
            } else u.printMSG(p, "cmd_unknownbutton",arg1);
        } else if (cmd.equalsIgnoreCase("list")) {
            //   /ra list type <type> [pnum] 4 [5]
            if (arg1.equalsIgnoreCase("type")){
                printActType(p, arg2, 1, 15);
            } else if (arg1.equalsIgnoreCase("group")){
                printActGroup(p, arg2, 1, 15);
            } else if (arg1.equalsIgnoreCase("all")&&u.isIntegerGZ(arg2)){
                this.printAct(p, Integer.parseInt(arg2), 15);
            } else if (arg1.equalsIgnoreCase("loc")&&u.isIntegerGZ(arg2)){
                printLocList(p,Integer.parseInt(arg2),15);
            }
        } else if (cmd.equalsIgnoreCase("remove")){
            if (arg1.equalsIgnoreCase("act")||
                    arg1.equalsIgnoreCase("activator")){
                if (plg.activators.contains(arg2)){
                    plg.activators.removeActivator(arg2);
                    u.printMSG(p, "msg_removebok",arg2);
                    plg.activators.saveActivators();
                } else u.printMSG(p, "msg_removebnf",arg2);
            } else if (arg1.equalsIgnoreCase("loc")){
                if (plg.tports.containsKey(arg2)){
                    plg.tports.remove(arg2);
                    u.printMSG(p, "msg_removelocok",arg2);
                    plg.saveLocs();
                } else u.printMSG(p, "msg_removelocnf",arg2);
            } 
        } else if (cmd.equalsIgnoreCase("clear")){
            if (plg.activators.contains(arg1)){
                if (arg2.equalsIgnoreCase("f")) {
                    plg.activators.clearFlags(arg1);
                    plg.activators.saveActivators();
                    u.printMSG(p, "msg_clearflag", arg1);
                } else if (arg2.equalsIgnoreCase("a")) {
                    plg.activators.clearActions(arg1);
                    u.printMSG(p, "msg_clearact", arg1);
                    plg.activators.saveActivators();
                } else if (arg2.equalsIgnoreCase("r")) {
                    plg.activators.clearReactions(arg1);
                    u.printMSG(p, "msg_clearreact", arg1);
                    plg.activators.saveActivators();
                }
                plg.activators.saveActivators();
            } else u.printMSG(p, "cmd_unknownbutton");

        } else if (cmd.equalsIgnoreCase("group")) {
            if (plg.activators.setGroup(arg1, arg2)) {
                plg.activators.saveActivators();
                u.printMSG(p, "msg_groupset",arg1, arg2);
            }
            else u.printMSG(p, "msg_groupsetfailed",arg1, arg2);
        } else	return false;

        return true;
    }


    public boolean addAction(String clicker, String flag, String param){
        if (u.isWordInList(flag, Actions.atypes)){
            if ((flag.equalsIgnoreCase("loc")&&(!plg.tports.containsKey(param)))||
                    (flag.equals("dmg")&&(!(param.matches("[0-9]*")||param.isEmpty()))))
                return false;
            plg.activators.addAction(clicker, flag, param);
            return true;
        }
        return false;
    }

    public boolean addReAction(String clicker, String flag, String param){
        if (u.isWordInList(flag, Actions.atypes)){
            if ((flag.equalsIgnoreCase("tp")&&(!plg.tports.containsKey(param)))||
                    (flag.equals("dmg")&&(!(param.matches("[0-9]*")||param.isEmpty()))))
                return false;
            plg.activators.addReaction(clicker, flag, param);
            return true;
        }
        return false;
    }

    public boolean addFlag(String clicker, String fl, String param){
        String flag=fl.replaceFirst("!", "");
        boolean not = fl.startsWith("!");
        if (u.isWordInList(flag, Flag.ftypes)){
            plg.activators.addFlag(clicker, flag, param, not); // все эти проверки вынести в соответствующие классы
            return true;
        }
        return false;
    }


    //   /ra add     region        name wg
    //   /ra add      a|r          dmg/msg  value 	
    public boolean ExecuteCmd (Player p, String cmd, String arg1, String arg2, String arg3){
        if (cmd.equalsIgnoreCase("add")){
            if (arg1.equalsIgnoreCase("region")||arg1.equalsIgnoreCase("rg")){
                if (!plg.worldguard_conected) u.printMSG(p, "cmd_wgnotfound",'c');
                else {
                    RegionActivator wga = new RegionActivator (arg2,arg3);
                    plg.activators.addActivator(wga);
                    plg.activators.saveActivators();
                    u.printMSG(p, "cmd_addbadded",wga.toString());
                }
            } else if (arg1.equalsIgnoreCase("rgenter")||arg1.equalsIgnoreCase("rge")){
                if (!plg.worldguard_conected) u.printMSG(p, "cmd_wgnotfound",'c');
                else {
                    RgEnterActivator wga = new RgEnterActivator (arg2,arg3);
                    plg.activators.addActivator(wga);
                    plg.activators.saveActivators();
                    u.printMSG(p, "cmd_addbadded",wga.toString());
                }
            } else if (arg1.equalsIgnoreCase("rgleave")||arg1.equalsIgnoreCase("rgl")){
                if (!plg.worldguard_conected) u.printMSG(p, "cmd_wgnotfound",'c');
                else {
                    RgLeaveActivator wga = new RgLeaveActivator (arg2,arg3);
                    plg.activators.addActivator(wga);
                    plg.activators.saveActivators();
                    u.printMSG(p, "cmd_addbadded",wga.toString());
                }
            /*} else if (arg1.equalsIgnoreCase("loc")){
                int radius = 0;
                if (u.isInteger(arg3)) radius = Integer.parseInt(arg3);
                plg.tports.put(arg2, new RALoc (p.getLocation(),radius));
                plg.saveLocs();
                u.printMSG(p, "cmd_addtpadded",arg2); */
            }
        } else if (cmd.equalsIgnoreCase("remove")) {
            if (plg.activators.contains(arg1)){
                Activator act = plg.activators.get(arg1);
                if (u.isIntegerGZ(arg3)) {
                    int num = Integer.parseInt(arg3);
                    if (arg2.equalsIgnoreCase("f")){
                        if (act.removeFlag(num-1)) u.printMSG(p, "msg_flagremoved",act.getName(),num);
                        else u.printMSG(p, "msg_failedtoremoveflag",act.getName(),num);
                    } else if (arg2.equalsIgnoreCase("a")){
                        if (act.removeAction(num-1)) u.printMSG(p, "msg_actionremoved",act.getName(),num);
                        else u.printMSG(p, "msg_failedtoremoveaction",act.getName(),num);
                    } else if (arg2.equalsIgnoreCase("r")){
                        if (act.removeReaction(num-1)) u.printMSG(p, "msg_reactionremoved",act.getName(),num);
                        else u.printMSG(p, "msg_failedtoremovereaction",act.getName(),num);
                    } else return false;
                } else u.printMSG(p, "msg_wrongnumber",arg3); 
            } else u.printMSG(p, "cmd_unknownbutton",arg1);
        } else if (cmd.equalsIgnoreCase("copy")) {
            if (arg1.equalsIgnoreCase("f")||arg1.equalsIgnoreCase("flag")){
                if (plg.activators.copyFlags(arg2, arg3)) u.printMSG(p, "msg_copyflags",arg2,arg3);
                else u.printMSG(p, "msg_copyflagsfailed",'c','4',arg2,arg3);
            } else if (arg1.equalsIgnoreCase("a")||arg1.equalsIgnoreCase("actions")){
                if (plg.activators.copyActions(arg2, arg3)) u.printMSG(p, "msg_copyactions",arg2,arg3);
                else u.printMSG(p, "msg_copyactionsfailed",'c','4',arg2,arg3);
            } else if (arg1.equalsIgnoreCase("r")||arg1.equalsIgnoreCase("reactions")){
                if (plg.activators.copyReactions(arg2, arg3)) u.printMSG(p, "msg_copyreactions",arg2,arg3);
                else u.printMSG(p, "msg_copyreactionsfailed",'c','4',arg2,arg3);
            }
        } else if (cmd.equalsIgnoreCase("list")) {
            if (arg1.equalsIgnoreCase("type")&&u.isIntegerGZ(arg3)){
                printActType(p, arg2, Integer.parseInt(arg3), 15);
            } else if (arg1.equalsIgnoreCase("group")&&u.isIntegerGZ(arg3)){
                printActGroup(p, arg2, Integer.parseInt(arg3), 15);
            }
        } else if (plg.activators.contains(arg1)){
            if (((arg2.equalsIgnoreCase("a"))||(arg2.equalsIgnoreCase("r")))&&
                    (arg3.equalsIgnoreCase("dmg")||arg3.equalsIgnoreCase("msg"))){
                if (arg2.equalsIgnoreCase("a")) {
                    if (addAction(arg1, arg3, "")) {
                        u.printMSG(p, "cmd_actadded",cmd+" "+arg2+" "+arg1 +" "+arg3);
                        plg.activators.saveActivators();
                        return true;
                    }
                    else u.printMSG(p, "cmd_actnotadded",cmd+" "+arg2+" "+arg1 +" "+arg3);
                } 
                else if (arg2.equalsIgnoreCase("r")) {
                    if (addReAction(arg1, arg3, "")) {
                        u.printMSG(p, "cmd_reactadded",cmd+" "+arg2+" "+arg1 +" "+arg3);
                        plg.activators.saveActivators();
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
    public boolean ExecuteCmd (Player p, String cmd, String arg1, String arg2, String arg3, String arg4){
        if (cmd.equalsIgnoreCase("add")&&plg.activators.contains(arg1)){
            String param = arg4;
            
            if (!u.isWordInList(arg3, "msg,msgall,cmdsrv,cmdplr,cmdop")&&param.contains("here"))
                param = param.replace("here", Util.locationToString(p.getLocation()));
            if (arg2.equalsIgnoreCase("a")){
                if (addAction (arg1, arg3, param)){
                    plg.activators.saveActivators();
                    u.printMSG(p, "cmd_actadded", arg3 + " ("+ param+")"); //TODO~
                    return true;
                } else u.printMSG(p, "cmd_actnotadded",arg3 + " ("+ param+")");
            } else if (arg2.equalsIgnoreCase("r")){
                if (addReAction (arg1, arg3, param)){
                    plg.activators.saveActivators();
                    u.printMSG(p, "cmd_reactadded",arg3 + " ("+ param+")");	
                    return true;
                } else u.printMSG(p, "cmd_reactnotadded",arg3 + " ("+ param+")");



            } else if (arg2.equalsIgnoreCase("f"))
                if (addFlag (arg1, arg3, arg4)){
                    plg.activators.saveActivators();
                    u.printMSG(p, "cmd_flagadded",arg3 + " ("+ arg4+")");
                    return true;					

                } else u.printMSG(p, "cmd_flagnotadded",arg3 + " ("+ arg4+")");
            else u.printMSG(p, "cmd_unknownbutton",arg2);
            return true;
        }
        return false;
    }



    public void printGroupList(CommandSender p, int page, int lpp){
        List<String> grp = new ArrayList<String>();
        if (!plg.activators.findGroupsFromActivators().isEmpty())
            for (String g : plg.activators.findGroupsFromActivators())
                grp.add(g);
        u.printPage(p, grp, page, "msg_grouplisttitle", "", true);
    }


    public void printAct (CommandSender p, int page, int lpp){
        List<String> ag = plg.activators.getActivatorsList();
        u.printPage(p, ag, page, "msg_actlist", "", true);
        u.printMSG(p,"msg_listcount",plg.activators.size(),plg.tports.size());
    }


    public void printActGroup (CommandSender p, String group, int page, int lpp){
        List<String> ag = plg.activators.getActivatorsListGroup(group);
        u.printPage(p, ag, page, "&6"+u.getMSG("msg_actlistgrp",group), "", true);
    }

    public void printActType(CommandSender p, String type, int page, int lpp){
        List<String> ag = plg.activators.getActivatorsList(type);
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
                    List<Activator> found = plg.activators.getActivatorInLocation(p.getWorld(), x, y, z);
                    if (found.isEmpty()) continue;
                    for (int i = 0; i<found.size(); i++)
                        lst.add(found.get(i).toString());
                }
        List<String> plst = new ArrayList<String>();
        plst.addAll(lst);
        u.printPage(p, plst, 1, "msg_check", "", true,100);
    }

    private void printActInfo(CommandSender p, String actname, String far) {
        Activator act = plg.activators.get(actname);
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


}
