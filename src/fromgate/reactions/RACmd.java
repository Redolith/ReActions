package fromgate.reactions;

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

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		if (sender instanceof Player){
			Player p = (Player) sender;

			if ((args.length>0)&&u.CheckCmdPerm(p, args[0])){
				/*
				 * 	if (!plg.pset.containsKey(p.getName())) plg.pset.put(p.getName(), new PCfg(p));
				 * */
				if (args.length==1) return ExecuteCmd (p, args[0]);
				else if (args.length==2) return ExecuteCmd (p, args[0],args[1]);
				else if (args.length==3) return ExecuteCmd (p, args[0],args[1],args[2]);
				else if (args.length==4) return ExecuteCmd (p, args[0],args[1],args[2],args[3]);
				else if (args.length==5) return ExecuteCmd (p, args[0],args[1],args[2],args[3],args[4]);
				else if (args.length>=5){
					String arg4 = "";
					for (int i = 4; i<args.length;i++) 
						arg4 = arg4+" "+args[i];
					arg4 = arg4.trim();
					return ExecuteCmd (p, args[0],args[1],args[2],args[3],arg4);
				}


			} else u.PrintPxMSG(p, "cmd_cmdpermerr",'c');
			return true;
		} 
		return false;
	}



	public boolean ExecuteCmd (Player p, String cmd){
		if (cmd.equalsIgnoreCase("help")){
			u.PrintHLP(p);
			return true;
		} else if (cmd.equalsIgnoreCase("debug")){
			plg.debug.offPlayerDebug(p);
			u.PrintMSG(p, "cmd_debugoff");
			return true;
		} else if (cmd.equalsIgnoreCase("list")){
			u.PrintMSG(p,"msg_listcount",plg.clickers.size()+";"+plg.tports.size());
			return true;
		}
		return false;
	}

	public boolean ExecuteCmd (Player p, String cmd, String arg){
		if (cmd.equalsIgnoreCase("help")){
			u.PrintHLP(p,arg);
			return true;
		} else if (cmd.equalsIgnoreCase("debug")){
			if (arg.equalsIgnoreCase("false")) {
				plg.debug.setPlayerDebug(p, false);
				u.PrintMSG(p, "cmd_debugtrue");
			} else if (arg.equalsIgnoreCase("true")) {
				plg.debug.setPlayerDebug(p, true);
				u.PrintMSG(p, "cmd_debugfalse");
			} else {
				plg.debug.offPlayerDebug(p);
				u.PrintMSG(p, "cmd_debugoff");
			}
			return true;
		} else if (cmd.equalsIgnoreCase("edit")){
			if (plg.clickers.containsKey(arg)){
				if ((p.getItemInHand().getType()==Material.WRITTEN_BOOK)||
						(p.getItemInHand().getType()==Material.BOOK_AND_QUILL)){
					Book book = new Book ("ReActions:arg","fromgate",plg.clickers.get(arg).toBookCfg());
					p.setItemInHand(book.generateItemStack());
				} else u.PrintMSG(p, "msg_editneedbook",arg);
			} else u.PrintMSG(p, "msg_editunknown",arg);
			return true;
		} else if (cmd.equalsIgnoreCase("list")){
			if (arg.equalsIgnoreCase("b")){
				u.PrintMSG(p, "msg_listclicker", '6');
				for (String clicker : plg.clickers.keySet()){
					u.PrintMsg(p, "&6"+ clicker+" : &e"+plg.clickers.get(clicker).toString()); 
				}
				return true;
			} else if (arg.equalsIgnoreCase("loc")){
				u.PrintMSG(p, "msg_listloc", '6');
				for (String loc : plg.tports.keySet()){
					RALoc ml = plg.tports.get(loc);
					u.PrintMsg(p, "&3"+loc+" &a["+ml.world+"] "+ml.x+", "+ml.y+", "+ml.z);
				}
				return true;
			} else {
				u.PrintMSG(p,"msg_listcount",plg.clickers.size()+";"+plg.tports.size());
				return true;
			}
		} 
		return false;
	}



	public boolean ExecuteCmd (Player p, String cmd, String arg1, String arg2){
		if (cmd.equalsIgnoreCase("add")){
			if (arg1.equalsIgnoreCase("b")){
				Block b = p.getTargetBlock(null, 100); 
				if ((b != null)&&(b.getType()==Material.STONE_BUTTON)){
					plg.clickers.put(arg2, new Clicker (b.getLocation()));
					plg.saveClickers();
					u.PrintPxMSG(p, "cmd_addbadded",arg2);
				} else u.PrintPxMSG(p, "cmd_addbreqbut");
				return true;
			} else if (arg1.equalsIgnoreCase("loc")){
				plg.tports.put(arg2, new RALoc (p.getLocation()));
				plg.saveLocs();
				u.PrintPxMSG(p, "cmd_addtpadded",arg2);
				return true;
			} else u.PrintPxMSG(p, "cmd_unknownadd",'c');
		} else if (cmd.equalsIgnoreCase("remove")){
			if (arg1.equalsIgnoreCase("b")){
				if (plg.clickers.containsKey(arg2)){
					plg.clickers.remove(arg2);
					u.PrintMSG(p, "msg_removebok",arg2);
					return true;
				} else u.PrintMSG(p, "msg_removebnf",arg2);
			} else if (arg1.equalsIgnoreCase("loc")){
				if (plg.tports.containsKey(arg2)){
					plg.tports.remove(arg2);
					u.PrintMSG(p, "msg_removelocok",arg2);
					return true;					
				} else u.PrintMSG(p, "msg_removelocnf",arg2);

			} 
		}else if (cmd.equalsIgnoreCase("clear")){
			if (plg.clickers.containsKey(arg1)){
				if (arg2.equalsIgnoreCase("f")) {
					plg.clickers.get(arg1).flags.clear();
					u.PrintMSG(p, "msg_clearflag", arg1);
				} else if (arg2.equalsIgnoreCase("a")) {
					plg.clickers.get(arg1).actions.clear();
					u.PrintMSG(p, "msg_clearact", arg1);
				} else if (arg2.equalsIgnoreCase("r")) {
					plg.clickers.get(arg1).reactions.clear();
					u.PrintMSG(p, "msg_clearreact", arg1);
				}
				plg.saveClickers();
			} else u.PrintMSG(p, "cmd_unknownbutton");
			
			return true;
			
			//AddCmd("clear", "config",MSG("cmd_clear","&3/react clear <id> [f|a|r]",'b'));

		}

		return false;
	}


	public boolean addAction(String clicker, String flag, String param){
		if (u.isWordInList(flag, plg.atypes)){
			if ((flag.equalsIgnoreCase("loc")&&(!plg.tports.containsKey(param)))||
					(flag.equals("dmg")&&(!(param.matches("[0-9]*")||param.isEmpty()))))
				return false;

			plg.clickers.get(clicker).addAction(flag, param);
			return true;
		}
		return false;
	}

	public boolean addReAction(String clicker, String flag, String param){
		if (u.isWordInList(flag, plg.atypes)){
			if ((flag.equalsIgnoreCase("tp")&&(!plg.tports.containsKey(param)))||
					(flag.equals("dmg")&&(!(param.matches("[0-9]*")||param.isEmpty()))))
				return false;
			plg.clickers.get(clicker).addReAction(flag, param);
			return true;
		}
		return false;
	}

	public boolean addFlag(String clicker, String flag, String param){
		if (u.isWordInList(flag, plg.ftypes)){
			plg.clickers.get(clicker).flags.put(flag, param);
			return true;
		}
		return false;
	}

	//add         a/r            dmg/msg 	
	public boolean ExecuteCmd (Player p, String cmd, String arg1, String arg2, String arg3){
		if (cmd.equalsIgnoreCase("add")&&plg.clickers.containsKey(arg1)){
			if (((arg2.equalsIgnoreCase("a"))||(arg2.equalsIgnoreCase("r")))&&
					(arg3.equalsIgnoreCase("dmg")||arg3.equalsIgnoreCase("msg"))){
				if (arg2.equalsIgnoreCase("a")) {
					if (addAction(arg1, arg3, "")) {
						u.PrintMSG(p, "cmd_actadded",cmd+" "+arg2+" "+arg1 +" "+arg3);
						plg.saveClickers();
						return true;
					}
					else u.PrintMSG(p, "cmd_actnotadded",cmd+" "+arg2+" "+arg1 +" "+arg3);
				} 
				else if (arg2.equalsIgnoreCase("r")) {
					if (addReAction(arg1, arg3, "")) {
						u.PrintMSG(p, "cmd_reactadded",cmd+" "+arg2+" "+arg1 +" "+arg3);
						plg.saveClickers();
						return true;
					}
					else u.PrintMSG(p, "cmd_reactnotadded",cmd+" "+arg2+" "+arg1 +" "+arg3);
				}
			}
		}
		return false;
	}



	//                                          add          a           <name>   <flag>    <param>
	//                                          add          <name>        a      <flag>    <param>
	public boolean ExecuteCmd (Player p, String cmd, String arg1, String arg2, String arg3, String arg4){
		if (cmd.equalsIgnoreCase("add")&&plg.clickers.containsKey(arg1)){
			if (arg2.equalsIgnoreCase("a"))
				if (addAction (arg1, arg3, arg4)){
					plg.saveClickers();
					u.PrintMSG(p, "cmd_actadded",cmd+" "+arg2+" "+arg1 +" "+arg3 + " "+ arg4);
					return true;
				} else u.PrintMSG(p, "cmd_actnotadded",cmd+" "+arg2+" "+arg1 +" "+arg3 + " "+ arg4);

			else if (arg2.equalsIgnoreCase("r"))

				if (addReAction (arg1, arg3, arg4)){
					plg.saveClickers();
					u.PrintMSG(p, "cmd_reactadded",cmd+" "+arg2+" "+arg1 +" "+arg3 + " "+ arg4);	
					return true;
				} else u.PrintMSG(p, "cmd_reactnotadded",cmd+" "+arg2+" "+arg1 +" "+arg3 + " "+ arg4);



			else if (arg2.equalsIgnoreCase("f"))
				if (addFlag (arg1, arg3, arg4)){
					plg.saveClickers();
					u.PrintMSG(p, "cmd_flagadded",cmd+" "+arg1+" "+arg2 +" "+arg3 + " "+ arg4);
					return true;					

				} else u.PrintMSG(p, "cmd_flagnotadded",cmd+" "+arg2+" "+arg1 +" "+arg3 + " "+ arg4);
			else u.PrintPxMSG(p, "cmd_unknownbutton",arg2);
			return true;
		}
		return false;
	}

}
