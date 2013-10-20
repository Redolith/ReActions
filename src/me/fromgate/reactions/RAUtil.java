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

import me.fromgate.reactions.util.Util;

import org.bukkit.inventory.ItemStack;

public class RAUtil extends FGUtilCore {
    ReActions plg;

    public RAUtil(ReActions plugin, boolean savelng, String language, String plgcmd){
        super (plugin, savelng, language, plgcmd);
        this.plg = plugin;
        FillMSG();
        InitCmd();
        if (savelng) this.SaveMSG();
    }




    public void InitCmd(){
        cmds.clear();
        cmdlist = "";
        addCmd("help", "config", "hlp_thishelp","&3/react help [command]",'b',true);
        addCmd("run", "run","cmd_run","&3/react run <exec-activator> [target player] [delay]",'b',true);
        addCmd("add", "config","cmd_add","&3/react add [b <id>|loc <id>|<id> f <flag> <param>|<id> r <action> <param>|<id> r <reaction> <param>",'b');
        addCmd("copy", "config","cmd_copy","&3/react copy [flag|actions|reactions] <source> <destination>",'b');
        addCmd("list", "config","cmd_list","&3/react list [loc|group|type] [page]",'b');
        addCmd("info", "config","cmd_info","&3/react info <activator> [f|a|r]",'b');
        addCmd("group", "config","cmd_group","&3/react group <activator> <groupname>",'b');
        addCmd("remove", "config","cmd_remove","&3/react remove [loc|activator] <id>",'b');
        addCmd("clear", "config","cmd_clear","&3/react clear <id> [f|a|r]",'b');
        addCmd("select", "select","cmd_select","&3/react select",'b');
        addCmd("debug", "debug","cmd_debug","&3/react debug [true|false|off]",'b');
        addCmd("check", "config","cmd_check","&3/react check [radius]",'b');
        addCmd("reload", "config","cmd_reload","&3/react reload",'b',true);
    }

    public void FillMSG(){
        addMSG ("msg_listclicker", "List of activators:");
        addMSG ("msg_listloc", "List of store locations:");
        addMSG ("cmd_addbadded", "Activator %1% successfully defined");
        addMSG ("cmd_notaddbadded", "Failed to create activator %1%");
        addMSG ("cmd_addbreqbut", "You need to look at button to create new button-activator");
        addMSG ("cmd_addtpadded", "Location %1% added");
        addMSG ("cmd_unknownadd", "Unknown type of activator");
        addMSG ("cmd_actadded", "Action was added: %1%");
        addMSG ("cmd_actnotadded", "Action was not added: %1%");
        addMSG ("cmd_reactadded", "Reaction was added: %1%");
        addMSG ("cmd_reactnotadded", "Reaction was not added: %1%");
        addMSG ("cmd_flagadded", "Flag was added: %1%");
        addMSG ("cmd_flagnotadded", "Flag was not added: %1%");
        addMSG ("cmd_unknownbutton", "Activator %1% was not found");
        addMSG ("loc_unknown", "Unknown location");
        addMSG ("cmd_add", "%1% - main command to add new activator, locations, add flags, actions and reactions to activator");
        addMSG ("cmd_run", "%1% - Execute the <exec-activator> for a defined player after <delay>");
        addMSG ("cmd_list", "%1% - display list of activators (all, groupped by type or group name), list of stored locations");
        addMSG ("cmd_info", "%1% - display full info about activator (or display flags, actions and reactions of it)");
        addMSG ("cmd_remove", "%1% - remove stored activator or location");
        addMSG ("cmd_clear", "%1% - clear flags/actions/reactions bounded with activator with defined id");
        addMSG ("cmd_debug", "%1% - switches debug mode (all checks - true, all checks - false, disabled)");
        addMSG ("cmd_check", "%1% - check is you looking at block (button) with bounded activator, or find activators around you (radius)");
        addMSG ("cmd_reload", "%1% - reload stored locations and activators from configuration files");
        addMSG ("cmd_debugtrue", "Debug mode enabled (always - true)");
        addMSG ("cmd_debugfalse", "Debug mode enabled (always - false)");
        addMSG ("cmd_debugoff", "Debug mode disabled");
        addMSG ("cmd_select", "%1% - selects block in your view point. You can use this selection as \"selection\" (or \"sel\") keyword when defining locations");
        addMSG ("cmd_selected", "Location %1% selected!");
        addMSG ("msg_listcount", "There's %1% configured activators and %2% stored locations");
        addMSG ("msg_removebnf", "Activator %1% not found and not removed");
        addMSG ("msg_removelocnf", "Stored location %1% not found and not removed");
        addMSG ("msg_removebok", "Activator %1% was removed");
        addMSG ("msg_removelocok", "Stored location %1% was removed");
        addMSG ("msg_clearflag", "Flags of activator %1% were cleared");
        addMSG ("msg_clearact", "Actions of activator %1% were cleared");
        addMSG ("msg_clearreact", "Reactions of activator %1% were cleared");
        addMSG ("cmd_checkmsg", "Found %1% activators around you (radius %2%)");
        addMSG ("cmd_checkfail", "Activators around you was not found (radius %1%)");
        addMSG ("cmd_checkneednumber", "Wrong number: %1%");
        addMSG ("msg_cmdreload", "Reload completed. Loaded %1% activators, %2% locations.");
        addMSG ("cmd_copy", "%1% - to copy all parameters (or flags, actions, reactions) from <source> activator to <destination>");
        addMSG ("msg_copyall", "All parameters of activator %1% was copied to %2%");
        addMSG ("msg_copyallfailed", "Failed to copy parameters from activator %1% to %2%");
        addMSG ("msg_copyflags", "Flags of activator %1% was copied to %2%");
        addMSG ("msg_copyflagsfailed", "Failed to copy flags from activator %1% to %2%");
        addMSG ("msg_copyactions", "Actions of activator %1% was copied to %2%");
        addMSG ("msg_copyactionsfailed", "Failed to copy actions from activator %1% to %2%");
        addMSG ("msg_copyreactions", "Reactions of activator %1% was copied to %2%");
        addMSG ("msg_copyreactionsfailed", "Failed to copy reactions from activator %1% to %2%");
        addMSG ("msg_grouplisttitle", "Groups of activators");
        addMSG ("msg_actlist", "Activators");
        addMSG ("msg_actlistgrp", "Activators (Group: %1%)");
        addMSG ("msg_actlisttype", "Activators (Type: %1%)");
        addMSG ("msg_listloc", "List of stored locations");
        addMSG ("cmd_group", "%1% - set the group of <activator> to group <group>");
        addMSG ("msg_groupset", "Activator %1% was moved to group %2%");
        addMSG ("msg_groupsetfailed", "Failed to move activator %1% to group %2%");
        addMSG ("msg_actinfotitle", "Activator info");
        addMSG ("msg_actinfo", "Id: %1% (Type: %2% Group: %3%)");
        addMSG ("msg_actinfo2", "Flags: %1% Actions: %2% Reactions: %3%");
        addMSG ("lst_flags", "Flags");
        addMSG ("lst_actions", "Actions");
        addMSG ("lst_reactions", "Reactions");
        addMSG ("msg_check", "Activators around you");
        addMSG ("msg_flagremoved", "Flag No.%2% of activator %1% was removed!");
        addMSG ("msg_failedtoremoveflag", "Failed to remove flag No.%2% of actiovator %1%");
        addMSG ("msg_actionremoved", "Action No.%2% of activator %1% was removed!");
        addMSG ("msg_failedtoremoveaction", "Failed to remove action No.%2% of actiovator %1%");
        addMSG ("msg_reactionremoved", "Reaction No.%2% of activator %1% was removed!");
        addMSG ("msg_failedtoremovereaction", "Failed to remove reaction No.%2% of actiovator %1%");
        addMSG ("msg_wrongnumber", "Wrong number %1%!");
        addMSG ("cmd_runplayer", "Activator %1% executed (for player %2%)");
        addMSG ("cmd_runplayerfail", "Failed to run activator %1%");
        addMSG ("cmd_rundelayplayer", "Execution of activator %1% for player %2% will start after %3% ticks");
        addMSG ("cmd_runplayerunknown", "Failed to run activator %1%. Player %2% is unknown");
        addMSG ("msg_mobbounty", "You received %1% for killing %2%");
             
        addMSG ("act_tp", "You was teleported to %1%");
        addMSG ("act_tpfail", "Failed teleportation to %1% (wrong location?)");
        addMSG ("act_velocity", "Your movement direction was changed");
        addMSG ("act_velocityfail", "Failed to change your movement direction");
        addMSG ("act_sound", "You hear strange sounds (%1%) around you");
        addMSG ("act_soundfail", "Failed to play sound %1%");
        addMSG ("act_potion", "You have strange feeling of %1%. Maybe you're bewitched?");
        addMSG ("act_potionfail", "Failed to set potion effect %1%");
        addMSG ("act_potion_remove", "Potion effect was removed: %1%");
        addMSG ("act_potion_removefail", "There's no potion effect to remove");
        addMSG ("act_group_add", "You were added to group %1%");
        addMSG ("act_group_addfail", "Cannot add you in group %1%");
        addMSG ("act_group_remove", "You were excluded from group %1%");
        addMSG ("act_group_removefail", "Cannot remove you from group %1%");
        addMSG ("act_message", "Message: %1%");
        addMSG ("act_messagefail", "Failed to send message");
        addMSG ("act_broadcast", "Broadcast message: %1%");
        addMSG ("act_broadcastfail", "Failed to send global message");
        addMSG ("act_damage", "You receive a damage. %1% hit points lost");
        addMSG ("act_town_set", "You was added to town %1%");
        addMSG ("act_town_setfail", "Failed to add you into town %1%");
        addMSG ("act_town_kick", "You was kicked from town!");
        addMSG ("act_town_kickfail", "Failed to kick you from town!");
        addMSG ("act_item_remove", "You lost item: %1%");
        addMSG ("act_item_removefail", "Failed to remove item: %1%");
        addMSG ("act_item_remove_inventory", "You lost item from the inventory: %1%");
        addMSG ("act_item_remove_inventoryfail", "Failed to remove item %1% from your inventory");
        addMSG ("act_item_give", "You receive item(s): %1%");
        addMSG ("act_item_givefail", "Failed to give item(s): %1%");
        addMSG ("act_item_drop", "New item(s) spawned: %1%");
        addMSG ("act_item_dropfail", "Failed to spawn item(s): %1%");
        addMSG ("act_cmd", "Command executed (by player): %1%");
        addMSG ("act_cmdfail", "Failed to execute command");
        addMSG ("act_cmd_console", "Command executed (by console): %1%");
        addMSG ("act_cmd_op", "Command executed (as OP): %1%");
        addMSG ("act_cmd_opfail", "Failed to execute command as OP");
        addMSG ("act_money_pay", "You paid %1%");
        addMSG ("act_money_payfail", "Failed to remove %1% from your account");
        addMSG ("act_money_give", "You credited by %1%");
        addMSG ("act_money_givefail", "Failed to credit your account with %1%");
        addMSG ("act_delay", "You will wait some time to do it again!");
        addMSG ("act_delayfail", "Failed to setup activator delay");
        addMSG ("act_delay_player", "You will wait some time to do it again!");
        addMSG ("act_delay_playerfail", "Failed to setup activator delay");
        addMSG ("act_back", "You were pushed back!");
        addMSG ("act_backfail", "Failed to push you back!");
        addMSG ("act_mob_spawn", "Ooops! Mobs was spawned!");
        addMSG ("act_effect", "You noticed interesting visual effect provided by plugin PlayEffect!");
        addMSG ("act_execute", "Activator %1% started!");
        addMSG ("act_executefail", "Failed to execute activator");
        addMSG ("act_region_clear", "Region was cleared. %1% mobs was killed!");
        addMSG ("act_region_clearfail", "Failed to clear region");
        addMSG ("act_heal", "You was healed by %1%!");
        addMSG ("act_healfailed", "Failed to perform healing...");
    }
    
    @Override
    public ItemStack parseItemStack (String itemstr){
        return Util.parseItemStack(itemstr);
    }
    
}
