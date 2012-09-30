package fromgate.reactions;
/*  
 *  WeatherMan, Minecraft bukkit plugin
 *  (c)2012, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/weatherman/
 *   * 
 *  This file is part of WeatherMan.
 *  
 *  WeatherMan is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  WeatherMan is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with WeatherMan.  If not, see <http://www.gnorg/licenses/>.
 * 
 */


import org.bukkit.entity.Player;


public class RAUtil extends FGUtilCore {
	ReActions plg;

	public RAUtil(ReActions plugin, boolean vcheck, boolean savelng, String language, String devbukkitname, String version_name, String plgcmd, String px){
		super (plugin, vcheck, savelng, language, devbukkitname, version_name, plgcmd, px);
		this.plg = plugin;
		
		FillMSG();
		InitCmd();
		
		
		if (savelng) this.SaveMSG();
	}

	public void PrintCfg(Player p){
		
		
		/*PrintMsg(p, "&6&lWeatherMan "+des.getVersion()+" &r&6| "+MSG("msg_config", "",'6','6'));
		PrintMSG(p, "cfg_plgconfig");
		PrintMSG(p, "cfg_wanditem",Integer.toString(plg.wand)+" "+Material.getMaterial(plg.wand).name());
		PrintMSG(p, "cfg_melt",EnDis(plg.meltsnow)+";"+EnDis(plg.meltice));
		PrintEnDis(p, "cfg_mobspawn",plg.nethermob);
		PrintMsg(p, MSG("cfg_defbiome",plg.Biome2Str(plg.dbiome))+" "+ MSG("cfg_defradius",Integer.toString(plg.dradius)));
		PrintMSG(p, "cfg_maxradius");
		PrintMsg(p, MSG("cfg_maxrcmd",Integer.toString(plg.maxrcmd))+" "+MSG("cfg_maxrwand",Integer.toString(plg.maxrwand))+" "+
				MSG("cfg_maxrsign",Integer.toString(plg.maxrsign)));
		if (plg.unsnowbiomes.isEmpty()){
			PrintMSG(p, "cfg_nosnow_empty");
		} else {
			PrintMSG(p, "cfg_nosnow", plg.unsnowbiomes);
		}

		if (plg.unicebiomes.isEmpty()){
			PrintMSG(p, "cfg_noice_empty");
		} else {
			PrintMSG(p, "cfg_noice", plg.unicebiomes);
		}

		PrintMSG(p, "cfg_player");
		PrintMSG(p, "cfg_wandbiomeradius",EnDis(plg.pcfg.get(p.getName()).wand)+";"+plg.Biome2Str(plg.pcfg.get(p.getName()).biome)+";"+Integer.toString(plg.pcfg.get(p.getName()).radius));
		
		} else u.PrintPxMSG(p, "cmd_unknownadd",'c');
		} else u.PrintMsg(p, "cmd_addbreqbut");
		u.PrintPxMSG(p, "cmd_addbadded",arg2);
		u.PrintPxMSG(p, "cmd_addtpadded",arg2);
		
		u.PrintPxMSG(p, "cmd_unknownbutton",arg);
		
		*/
	}



	public void InitCmd(){
		cmds.clear();
		cmdlist = "";
		AddCmd("help", "config",MSG("cmd_help","&3/react help [command]",'b'));
		AddCmd("add", "config",MSG("cmd_add","&3/react add [b <id>|loc <id>|<id> f <flag> <param>|<id> r <action> <param>|<id> r <reaction> <param>",'b'));
		AddCmd("list", "config",MSG("cmd_list","&3/react list [loc|b]",'b'));
		AddCmd("remove", "config",MSG("cmd_remove","&3/react remove [loc|b] <id>",'b'));
		AddCmd("clear", "config",MSG("cmd_clear","&3/react clear <id> [f|a|r]",'b'));
		AddCmd("debug", "config",MSG("cmd_debug","&3/react debug [true|false|off]",'b'));
		//AddCmd("edit", "config",MSG("cmd_edit","&3/react edit <id>",'b'));
	
	}

	public void FillMSG(){
		addMSG ("msg_listclicker", "List of buttons:");
		addMSG ("msg_listloc", "List of store locations:");
		addMSG ("cmd_addbadded", "Events successfully defined");
		addMSG ("cmd_addbreqbut", "You can define events only for buttons");
		addMSG ("cmd_addtpadded", "Location %1% added");
		addMSG ("cmd_unknownadd", "You can add only locations (loc) and buttons (b)");
		addMSG ("cmd_actadded", "Action was added: %1%");
		addMSG ("cmd_actnotadded", "Action was not added: %1%");
		addMSG ("cmd_reactadded", "Reaction was added: %1%");
		addMSG ("cmd_reactnotadded", "Reaction was not added: %1%");
		addMSG ("cmd_flagadded", "Flag was added: %1%");
		addMSG ("cmd_flagnotadded", "Flag was not added: %1%");
		addMSG ("cmd_unknownbutton", "Button %1% is not found");
		addMSG ("loc_unknown", "Unknown location");
		addMSG ("cmd_add", "%1% - main command to add new triggers, locations, add flags, actions and reactions to current triggers");
		addMSG ("cmd_list", "%1% - list all triggers or locations");
		addMSG ("cmd_remove", "%1% - remove stored trigger or location");
		addMSG ("cmd_clear", "%1% - clear flags/actions/reactions bounded with trigger with defined id");
		addMSG ("cmd_debug", "%1% - switches debug mode (all checks - true, all checks - false, disabled)");
		addMSG ("cmd_debugtrue", "Debug mode enabled (always - true)");
		addMSG ("cmd_debugfalse", "Debug mode enabled (always - false)");
		addMSG ("cmd_debugoff", "Debug mode disabled");
		
		
		
		//tp,grpadd,grprmv,msg,dmg,townset,townkick,itemrmv,itemgive,cmdplr,cmdsrv,moneypay,moneygive
		addMSG ("act_tp", "You was teleported to %1%");
		addMSG ("act_grpadd", "You were added to group %1%");
		addMSG ("act_grpaddfail", "Cannot add you in group %1%");
		addMSG ("act_grprmv", "You were excluded from group %1%");
		addMSG ("act_grprmvfail", "Cannot remove you from group %1%");
		addMSG ("act_msg", "Personal message: %1%");
		addMSG ("act_dmg", "You receive a damage. %1% hit points lost");
		addMSG ("act_dmghurt", "You receive a slap! It hurts!"); 
		addMSG ("act_townset", "You was added to town %1%");
		addMSG ("act_townkick", "You was kicked from town!");
		addMSG ("act_itemrmv", "You lost item: %1%");
		addMSG ("act_itemgive", "You receive item: %1%");
		addMSG ("act_cmdplr", "Command executed (by player): %1%");
		addMSG ("act_cmdsrv", "Command executed (by console): %1%");
		addMSG ("act_moneypay", "You paid %1% %2%");
		addMSG ("act_moneygive", "You credited by %1% %2%");
		addMSG ("msg_listcount", "There's %1% configured buttons and %2% stored locations");
		addMSG ("msg_removebnf", "Configured button %1% not found and not removed");
		addMSG ("msg_removelocnf", "Stored location %1% not found and not removed");
		addMSG ("msg_removebok", "Configured button %1% was removed");
		addMSG ("msg_removelocok", "Stored location %1% was removed");
		addMSG ("msg_clearflag", "Flags cleared for button %1%");
		addMSG ("msg_clearact", "Flags cleared for button %1%");
		addMSG ("msg_cleareract", "Flags cleared for button %1%");
		
		/*
		
		addMSG ("", "");
		addMSG ("", "");
		addMSG ("", "");
		addMSG ("", "");
		*/
		
		
		
	
		
	}
}
