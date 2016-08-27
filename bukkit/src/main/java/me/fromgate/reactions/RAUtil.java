/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2015, fromgate, fromgate@gmail.com
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

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.ChatPaginator;
import org.bukkit.util.ChatPaginator.ChatPage;

public class RAUtil extends FGUtilCore {
    ReActions plg;

    public RAUtil(ReActions plugin, boolean savelng, String language, String plgcmd) {
        super(plugin, savelng, language, plgcmd, "me/fromgate/reactions");
        this.plg = plugin;
        FillMSG();
        if (savelng) this.SaveMSG();
    }

    public void FillMSG() {
        addMSG("msg_listclicker", "List of activators:");
        addMSG("msg_listloc", "List of stored locations:");
        addMSG("msg_listdelay", "List of active delays:");
        addMSG("cmd_addbadded", "Activator %1% successfully defined");
        addMSG("cmd_delayset", "New delay value saved. Id: %1% Delay time: %2%");
        addMSG("cmd_notaddbadded", "Failed to create activator %1%");
        addMSG("cmd_addbreqbut", "You need to look at button to create new button-activator");
        addMSG("cmd_addtpadded", "Location %1% added");
        addMSG("cmd_addmenuadded", "New inventory menu created: %1%");
        addMSG("cmd_addmenuaddfail", "Failed to create new inventory-menu %1%");
        addMSG("msg_removemenu", "Inventory-menu removed: %1%");
        addMSG("msg_removemenufail", "Failed to remove inventory-menu %1%");
        addMSG("msg_menuparamset", "Parameters of inventory-menu %1% changed!");
        addMSG("msg_menusetfail", "Failed to change parameters of inventory-menu %1%");
        addMSG("msg_menulist", "Inventory-menu list");
        addMSG("msg_menuidfail", "There's no inventory-menu %1% defined!");
        addMSG("msg_menuinfotitle", "Menu: %1% (size: %2%) Title: %3%");
        addMSG("msg_menuinfoslot", "slot%1% : Activator: %2% Item: %3%");
        addMSG("cmd_unknownadd", "Unknown type of activator");
        addMSG("cmd_actadded", "Action was added: %1%");
        addMSG("cmd_actnotadded", "Action was not added: %1%");
        addMSG("cmd_reactadded", "Reaction was added: %1%");
        addMSG("cmd_reactnotadded", "Reaction was not added: %1%");
        addMSG("cmd_flagadded", "Flag was added: %1%");
        addMSG("cmd_flagnotadded", "Flag was not added: %1%");
        addMSG("cmd_unknownbutton", "Activator %1% was not found");
        addMSG("loc_unknown", "Unknown location");
        addMSG("cmd_add", "%1% - main command to add new activator, locations, add flags, actions and reactions to activator");
        addMSG("cmd_set", "%1% - set delay that could be checked with DELAY and DELAY_PLAYER flags");
        addMSG("cmd_run", "%1% - Execute the <exec-activator> for a defined player after <delay>");
        addMSG("cmd_list", "%1% - display list of activators (all, groupped by type or group name), list of stored locations");
        addMSG("cmd_info", "%1% - display full info about activator (or display flags, actions and reactions of it)");
        addMSG("cmd_remove", "%1% - remove stored activator or location");
        addMSG("cmd_clear", "%1% - clear flags/actions/reactions bounded with activator with defined id");
        addMSG("cmd_debug", "%1% - switches debug mode (all checks - true, all checks - false, disabled)");
        addMSG("cmd_check", "%1% - check is you looking at block (button) with bounded activator, or find activators around you (radius)");
        addMSG("cmd_reload", "%1% - reload stored locations and activators from configuration files");
        addMSG("cmd_debugtrue", "Debug mode enabled (always - true)");
        addMSG("cmd_debugfalse", "Debug mode enabled (always - false)");
        addMSG("cmd_debugoff", "Debug mode disabled");
        addMSG("cmd_select", "%1% - selects block in your view point. You can use this selection as \"selection\" (or \"sel\") keyword when defining locations");
        addMSG("cmd_selected", "Location %1% selected!");
        addMSG("cmd_varset", "Variable %1% was set to %2%");
        addMSG("msg_listcount", "There's %1% configured activators and %2% stored locations");
        addMSG("msg_removebnf", "Activator %1% not found and not removed");
        addMSG("msg_removelocnf", "Stored location %1% not found and not removed");
        addMSG("msg_removebok", "Activator %1% was removed");
        addMSG("msg_removelocok", "Stored location %1% was removed");
        addMSG("msg_clearflag", "Flags of activator %1% were cleared");
        addMSG("msg_clearact", "Actions of activator %1% were cleared");
        addMSG("msg_clearreact", "Reactions of activator %1% were cleared");
        addMSG("cmd_checkmsg", "Found %1% activators around you (radius %2%)");
        addMSG("cmd_checkfail", "Activators around you was not found (radius %1%)");
        addMSG("cmd_checkneednumber", "Wrong number: %1%");
        addMSG("msg_cmdreload", "Reload completed. Loaded %1% activators, %2% locations.");
        addMSG("cmd_copy", "%1% - to copy all parameters (or flags, actions, reactions) from <source> activator to <destination>");
        addMSG("msg_copyall", "All parameters of activator %1% was copied to %2%");
        addMSG("msg_copyallfailed", "Failed to copy parameters from activator %1% to %2%");
        addMSG("msg_copyflags", "Flags of activator %1% was copied to %2%");
        addMSG("msg_copyflagsfailed", "Failed to copy flags from activator %1% to %2%");
        addMSG("msg_copyactions", "Actions of activator %1% was copied to %2%");
        addMSG("msg_copyactionsfailed", "Failed to copy actions from activator %1% to %2%");
        addMSG("msg_copyreactions", "Reactions of activator %1% was copied to %2%");
        addMSG("msg_copyreactionsfailed", "Failed to copy reactions from activator %1% to %2%");
        addMSG("msg_grouplisttitle", "Groups of activators");
        addMSG("msg_actlist", "Activators");
        addMSG("msg_actlistgrp", "Activators (Group: %1%)");
        addMSG("msg_actlisttype", "Activators (Type: %1%)");
        addMSG("msg_listloc", "List of stored locations");
        addMSG("cmd_group", "%1% - set the group of <activator> to group <group>");
        addMSG("msg_groupset", "Activator %1% was moved to group %2%");
        addMSG("msg_groupsetfailed", "Failed to move activator %1% to group %2%");
        addMSG("msg_actinfotitle", "Activator info");
        addMSG("msg_actinfo", "Id: %1% (Type: %2% Group: %3%)");
        addMSG("msg_actinfo2", "Flags: %1% Actions: %2% Reactions: %3%");
        addMSG("lst_flags", "Flags");
        addMSG("lst_actions", "Actions");
        addMSG("lst_reactions", "Reactions");
        addMSG("msg_check", "Activators around you");
        addMSG("msg_flagremoved", "Flag No.%2% of activator %1% was removed!");
        addMSG("msg_failedtoremoveflag", "Failed to remove flag No.%2% of actiovator %1%");
        addMSG("msg_actionremoved", "Action No.%2% of activator %1% was removed!");
        addMSG("msg_failedtoremoveaction", "Failed to remove action No.%2% of actiovator %1%");
        addMSG("msg_reactionremoved", "Reaction No.%2% of activator %1% was removed!");
        addMSG("msg_failedtoremovereaction", "Failed to remove reaction No.%2% of actiovator %1%");
        addMSG("msg_wrongnumber", "Wrong number %1%!");
        addMSG("cmd_runplayer", "Activator execution started (parameters: %1%)");
        addMSG("cmd_runplayerfail", "Failed to execute activator (parameters: %1%)");
        addMSG("msg_mobbounty", "You received %1% for killing %2%");
        addMSG("msg_needregion", "You must define region for this activator");
        addMSG("act_tp", "You was teleported to %1%");
        addMSG("act_tpfail", "Failed teleportation to %1% (wrong location?)");
        addMSG("act_velocity", "Your movement direction was changed");
        addMSG("act_velocityfail", "Failed to change your movement direction");
        addMSG("act_sound", "You hear strange sounds (%1%) around you");
        addMSG("act_soundfail", "Failed to play sound %1%");
        addMSG("act_potion", "You have strange feeling of %1%. Maybe you're bewitched?");
        addMSG("act_potionfail", "Failed to set potion effect %1%");
        addMSG("act_potion_remove", "Potion effect was removed: %1%");
        addMSG("act_potion_removefail", "There's no potion effect to remove");
        addMSG("act_group_add", "You were added to group %1%");
        addMSG("act_group_addfail", "Cannot add you in group %1%");
        addMSG("act_group_remove", "You were excluded from group %1%");
        addMSG("act_group_removefail", "Cannot remove you from group %1%");
        addMSG("act_message", "Message: %1%");
        addMSG("act_messagefail", "Failed to send message");
        addMSG("act_broadcast", "Broadcast message: %1%");
        addMSG("act_broadcastfail", "Failed to send global message");
        addMSG("act_damage", "You receive a damage. %1% hit points lost");
        addMSG("act_town_set", "You was added to town %1%");
        addMSG("act_town_setfail", "Failed to add you into town %1%");
        addMSG("act_town_kick", "You was kicked from town!");
        addMSG("act_town_kickfail", "Failed to kick you from town!");
        addMSG("act_item_remove", "You lost item: %1%");
        addMSG("act_item_removefail", "Failed to remove item: %1%");
        addMSG("act_item_remove_inventory", "You lost item from the inventory: %1%");
        addMSG("act_item_remove_inventoryfail", "Failed to remove item %1% from your inventory");
        addMSG("act_item_give", "You receive item(s): %1%");
        addMSG("act_item_givefail", "Failed to give item(s): %1%");
        addMSG("act_item_drop", "New item(s) spawned: %1%");
        addMSG("act_item_dropfail", "Failed to spawn item(s): %1%");
        addMSG("act_cmd", "Command executed (by player): %1%");
        addMSG("act_cmdfail", "Failed to execute command");
        addMSG("act_cmd_console", "Command executed (by console): %1%");
        addMSG("act_cmd_op", "Command executed (as OP): %1%");
        addMSG("act_cmd_opfail", "Failed to execute command as OP");
        addMSG("act_money_pay", "You paid %1%");
        addMSG("act_money_payfail", "Failed to remove %1% from your account");
        addMSG("act_money_give", "You credited by %1%");
        addMSG("act_money_givefail", "Failed to credit your account with %1%");
        addMSG("act_delay", "You will wait some time to do it again!");
        addMSG("act_delayfail", "Failed to setup activator delay");
        addMSG("act_delay_player", "You will wait some time to do it again!");
        addMSG("act_delay_playerfail", "Failed to setup activator delay");
        addMSG("act_back", "You were pushed back!");
        addMSG("act_backfail", "Failed to push you back!");
        addMSG("act_mob_spawn", "Ooops! Mobs was spawned!");
        addMSG("act_effect", "You noticed interesting visual effect provided by plugin PlayEffect!");
        addMSG("act_execute", "Activator %1% started!");
        addMSG("act_executefail", "Failed to execute activator");
        addMSG("act_execute_stop", "Activator %1% stopped");
        addMSG("act_execute_unstop", "Activator %1% restored and could be executed next time");
        addMSG("act_execute_unstopfail", "Fail to restore activator %1%. Is it stopped?");
        addMSG("act_power_set", "Power state of block %1% was changed");
        addMSG("act_power_setfail", "Power state of block %1% was not changed");
        addMSG("act_region_clear", "Region was cleared. %1% mobs was killed!");
        addMSG("act_region_clearfail", "Failed to clear region");
        addMSG("act_heal", "You was healed by %1%!");
        addMSG("act_healfailed", "Failed to perform healing...");
        addMSG("act_block_set", "Block placed: %1%!");
        addMSG("act_block_setfailed", "Failed to place block: %1%");
        addMSG("msg_timerlist", "Timers");
        addMSG("msg_timerneedname", "You must define name for the timer");
        addMSG("msg_timerunknownname", "Could not find timer %1%");
        addMSG("msg_timerremoved", "Timer %1% removed");
        addMSG("msg_timerexist", "Timer with %1% name already exists");
        addMSG("msg_timerneedparams", "You need to define parameters (activator, time, timer-type)!");
        addMSG("msg_timerneedactivator", "You need to define activator (activator:<EXEC-activator)!");
        addMSG("msg_timerneedtype", "You need to define timer type (timer-type:<INGAME|SERVER>)!");
        addMSG("msg_timerneedtime", "You need to define execution time (time:<HH:MM,HH:MM|cron format: * * * * * *>!");
        addMSG("msg_timeradded", "New timer created: %1%");
        addMSG("msg_varneedid", "You need to define variable Id (and player name for personal variables)");
        addMSG("msg_varremoved", "Variable was removed");
        addMSG("msg_varremovefail", "Failed to remove variable");
        addMSG("msg_varlist", "Variables");
        addMSG("msg_signforbidden", "You're not permitted to set signs, that subscribed to activator %1%");


		/*
         *  Action description messages
		 */
        addMSG("msg_actionlisttitle", "Actions");
        addMSG("action_TP", "Teleport player to defined location. Parameters: loc:<location or location's name> radius:<radius> land:<true/false> effect:<effect type>");
        addMSG("action_VELOCITY", "Set player's velocity (you can force player to jump or change movement direction). Parameters: vector:<x,y,z> kick:<true/false>");
        addMSG("action_SOUND", "Play sound effect. Parameters: type:<sound> pitch:<pitch> volume:<volume>");
        addMSG("action_POTION", "Add defined potion effect to player. Parameters: type:<potion type> level:<power> time:<duration time> ambient:<true/false>");
        addMSG("action_POTION_REMOVE", "Remove potion effect. Parameters: <POTION1,POTION2,..>");
        addMSG("action_GROUP_ADD", "Add player to group. Parameter: <group name>");
        addMSG("action_GROUP_REMOVE", "Kick player from group. Parameter: <group name>");
        addMSG("action_MESSAGE", "Display message to player. Parameters: region:<region> group:<group> perm:<permission> world:<world> player:<player>");
        addMSG("action_BROADCAST", "Send message to every online player. Parameter - message.");
        addMSG("action_DAMAGE", "Hit player. Parameter: <damage amount>");
        addMSG("action_TOWN_SET", "Move player to Towny's town. Parameters: <town name>");
        addMSG("action_TOWN_KICK", "Exclude player from the Towny's town. Parameters: <town name>");
        addMSG("action_ITEM_GIVE", "Give item to player. Parameter: <item>");
        addMSG("action_ITEM_REMOVE", "Remove item from the player's hand. Parameter: <item>");
        addMSG("action_ITEM_REMOVE_INVENTORY", "Remove item from the player's inventory. Parameter: <item>");
        addMSG("action_ITEM_DROP", "Drop items around defined location. Parameters: loc:<location> radius:<radius> scatter:<true/false> land:<true/false>");
        addMSG("action_ITEM_WEAR", "Wear item. Parameters: item:<item> slot:<auto/chestplate/helmet/leggins/boots>");
        addMSG("action_ITEM_UNWEAR", "Unwear item. Parameters: item:<item> slot:<auto/chestplate/helmet/leggins/boots> [item-action:<remove|drop|inventory>]");
        addMSG("action_CMD", "Execute command as player. Parameter: <command>");
        addMSG("action_CMD_OP", "Execute command as OP. Parameter: <command>");
        addMSG("action_CMD_CONSOLE", "Execute command as server console. Parameter: <command>");
        addMSG("action_MONEY_PAY", "Debit player's account (and credit target player if defined). Parameter: <amount>[/<target>]");
        addMSG("action_MONEY_GIVE", "Credit player's account (and debit source player if defined). Parameter: <amount>[/<source>]");
        addMSG("action_DELAY", "Set global delay variable. Parameter: <time>/<id>");
        addMSG("action_DELAY_PLAYER", "Set personal delay variable. Parameter: <time>/<id>");
        addMSG("action_BACK", "Pushback player to one or two of previously stored locations. Parameters: <1 or 2>");
        addMSG("action_MOB_SPAWN", "Spawn mob. Parameter: type:<mob type> (read more about this action at dev.bukkit.org)");
        addMSG("action_EFFECT", "Play visual effect. Parameter: eff:<effect> loc:<location>. PlayEffect plugin require for additional effects.");
        addMSG("action_EXECUTE", "Execute a predefined EXEC-activator. Parameters: activator:<exec-activator> delay:<time> player:<all|null|player1,player2...> world:<world1,world2,..> region:<region1,region2,..>");
        addMSG("action_EXECUTE_STOP", "Stop executing of delayed activator. Parameter: activator:<exec-activator> player:<player>");
        addMSG("action_EXECUTE_UNSTOP", "Resume executing of stopped activator. Parameters: activator:<exec-activator> player:<player>");
        addMSG("action_REGION_CLEAR", "Remove entities (mobs or items) in region. Paramters: region:<region id> type:<entity_type|all|mobs|items>");
        addMSG("action_HEAL", "Heal player. Parameter: <hit points amount>");
        addMSG("action_BLOCK_SET", "Place block in location. Parameters: loc:<location> block:<type[:data]>");
        addMSG("action_BLOCK_FILL", "Fill area define by parameters loc1, loc2 or region with defined block type. Parameters: <loc1:<world,x1,y1,z1> loc2:<world,x2,y2,z2>|region:<RegionId>> block:<Type:Data> chance:<Chance>");
        addMSG("action_POWER_SET", "Set redstone power of the block (supported levers and doors). Parameters: loc:<location> power:<on|off|toggle>");
        addMSG("action_SHOOT", "Shoot (without projectile) in player view direction. Parameters: distance:<distance> singlehit:<true/false> damage:<damage amount>");
        addMSG("action_VAR_SET", "Create global variable. Parameters: id:<id> value:<value>");
        addMSG("action_VAR_PLAYER_SET", "Create personal variable. Parameters: id:<id> value:<value>");
        addMSG("action_VAR_CLEAR", "Remove global varibale. Parameter: id:<id>");
        addMSG("action_VAR_PLAYER_CLEAR", "Remove personal varibale. Parameter: id:<id>");
        addMSG("action_VAR_INC", "Increase global variable value. Parameters: id:<id> value:<value>");
        addMSG("action_VAR_PLAYER_INC", "Increase personal variable value. Parameters: id:<id> value:<value>");
        addMSG("action_VAR_DEC", "Decrease global variable value. Parameters: id:<id> value:<value>");
        addMSG("action_VAR_PLAYER_DEC", "Decrease personal variable value. Parameters: id:<id> value:<value>");
        addMSG("action_RNC_SET_RACE", "Set player's race (RacesAndClass plugin required). Parameter: race:<race>");
        addMSG("action_RNC_SET_CLASS", "Set player's class (RacesAndClass plugin required). Parameter: class:<class>");
        addMSG("action_TIMER_STOP", "Stops execution of timer. Parameter: timer:<timer id>");
        addMSG("action_TIMER_RESUME", "Resumes execution of stopped timer. Parameter: timer:<timer id>");
        addMSG("action_VELOCITY_JUMP", "Jump to locations. Parameter: loc:<location>");
        addMSG("action_CANCEL_EVENT", "Cancel bukkit event, that initiates current activator (not all activators could be cancelled). Parameter: TRUE");
        addMSG("action_SQL_SELECT", "Execute SQL query and store field located in first row (and defined column) at variable. Parameters: query:{SELECT... } variable:<variable id> player:<varibale owner> column:<column>");
        addMSG("action_SQL_UPDATE", "Execute update-query (update field of table at MySQL database) Parameter: query:{UPDATE... }");
        addMSG("action_SQL_INSERT", "Execute insert-query (insert new row in table at MySQL database) Parameter: query:{INSERT... }");
        addMSG("action_SQL_DELETE", "Execute delete-query (delete record in MySQL database table) Parameter: query:{DELETE... }");
        addMSG("action_SIGN_SET_LINE", "Set (or clear) one or more line of sign. Parameters: loc:<location> line1:<text>...line4:<text> clear:<1,2..4>");
        addMSG("action_FCT_POWER_ADD", "Add power to player's faction power value. Prameters: value:<Value>");
        addMSG("action_ACTION_DELAYED", "Execute another action after delay. Parameters: time:<time> action:{<another action with parameters>}");
        addMSG("action_WAIT", "Wait some time before exucute another actions. Parameter: time:<time>");
        addMSG("action_MENU_ITEM", "Create and show GUI (item menu) to player. Parameters: menu:<MenuId>");

		/*
         * Flag description messages
		 */
        addMSG("msg_flaglisttitle", "Flags");
        addMSG("flag_GROUP", "Check player's group. Parameter: <group>");
        addMSG("flag_PERM", "Check player's permission. Parameter: <permission>");
        addMSG("flag_TIME", "Check in-game time in player's world. Parameter: <time in hours>");
        addMSG("flag_ITEM", "Check item in hand. Parameter: <item>");
        addMSG("flag_ITEM_INVENTORY", "Finding item in inventory. Parameter: <item>");
        addMSG("flag_ITEM_WEAR", "Finding item in armour slot. Parameter: <item>");
        addMSG("flag_TOWN", "Check player's town. Parameter: <town>");
        addMSG("flag_MONEY", "Check player account. Parameter: <money amount>");
        addMSG("flag_CHANCE", "Roll dice with defined chance. Parameter:<chance>");
        addMSG("flag_PVP", "Checks is player was in PVP-action during last <time in seconds> seconds. Parameter: <time>");
        addMSG("flag_ONLINE", "Number of player online. Parameter: <required online>");
        addMSG("flag_DELAY", "Check the countdown of delay variable. Parameter: <delay id>");
        addMSG("flag_DELAY_PLAYER", "Check the countdown of personal delay variable. Parameter: <delay id>");
        addMSG("flag_STATE", "Check player's state. Parameter: <STAND/SNEAK/SPRINT/VEHICLE/VEHICLE_MINECART/VEHICLE_BOAT/VEHICLE_PIG/VEHICLE_HORSE");
        addMSG("flag_REGION", "Is player in region? Parameter: <region>");
        addMSG("flag_REGION_PLAYERS", "This flag returns true when there <count> (or more) players located in region <region>. Parameter: <region>/<count>");
        addMSG("flag_REGION_MEMBER", "Is player member of region? Parameter: <region>");
        addMSG("flag_REGION_OWNER", "Is player owner of region? Parameter: <region>");
        addMSG("flag_GAMEMODE", "Check gamemode. Parameter:  <survival/creative/adventure>");
        addMSG("flag_FOODLEVEL", "Check food level. Parameter: <food level>");
        addMSG("flag_XP", "Check player total experience. Parameter: <xp>");
        addMSG("flag_LEVEL", "Check player experience level. Parameter: <level>");
        addMSG("flag_POWER", "Check redstone power state of block. Parameter: <location>");
        addMSG("flag_WORLD", "Player in world? Parameter: <world>");
        addMSG("flag_BIOME", "Player in biome? Parameter: <biome>");
        addMSG("flag_BLOCK", "Check block type. Parameters: loc:<Location> block:<BlockType>");
        addMSG("flag_LIGHT_LEVEL", "Player is in dark place? Parameter: <light level, 1..20>");
        addMSG("flag_WALK_BLOCK", "Player is walking on the defined block? Parameter: <block type>");
        addMSG("flag_DIRECTION", "Player directed to...? Parameter: <NORTH/NORTHEAST/NORTHWEST/SOUTH/SOUTHEAST/SOUTHWEST/EAST/WEST>");
        addMSG("flag_FLAG_SET", "Check all flags in the list and return true if any. Parameter: <[!]<flag1>=<value1> [!]<flag2>=<value2> ...>");
        addMSG("flag_EXECUTE_STOP", "Check stopped-state of delayed EXEC activator. Parameter: <activator id>");
        addMSG("flag_VAR_EXIST", "Is global variable exists? Parameter: <id>");
        addMSG("flag_VAR_PLAYER_EXIST", "Is personal variable exists? Parameter: <id>");
        addMSG("flag_VAR_COMPARE", "Compare global variable with value. Parameters: id:<id> value:<value>");
        addMSG("flag_VAR_PLAYER_COMPARE", "Compare personal variable with value. Parameters: id:<id> value:<value>");
        addMSG("flag_VAR_GREATER", "Is global variable greater than given value? Parameters: id:<id> value:<value>");
        addMSG("flag_VAR_PLAYER_GREATER", "Is personal variable greater than given value? Parameters: id:<id> value:<value>");
        addMSG("flag_VAR_LOWER", "Is global variable lower than given value? Parameters: id:<id> value:<value>");
        addMSG("flag_VAR_PLAYER_LOWER", "Is personal variable lower than given value? Parameters: id:<id> value:<value>");
        addMSG("flag_VAR_MATCH", "Check is value of provided variable is match (regex) to provided value. Parameters: id:<id> value:<value>");
        addMSG("flag_VAR_PLAYER_MATCH", "Check is value of provided variable is match (regex) to provided value. Parameters: id:<id> value:<value>");
        addMSG("flag_RNC_RACE", "Check player's race (Requires RacesAndClasses plugin). Parameter: <race>");
        addMSG("flag_RNC_CLASS", "Check player's class (Requires RacesAndClasses plugin). Parameter: <class>");
        addMSG("flag_WEATHER", "Check weather state around player. Parameter: <rain/clear>");
        addMSG("flag_TIMER_ACTIVE", "Check active state of defined timer. Returns false if timer is paused. Parameter: <timer id>");
        addMSG("flag_FCT_PLAYER", "Check player's faction. Parameter: Faction's name");
        addMSG("flag_SQL_CHECK", "Compares result of SQL-query with provided value. Parameters: query:{SELECT...} value:<value> column:<column>");
        addMSG("flag_SQL_RESULT", "Check is result of SQL-query returns data. If result is empty flag will return false. Parameter: query:{SELECT...}");
        addMSG("flag_COMPARE", "Compare provide parameter with list of variable. True if parameter is equal to one of the provided variables. Parameters: param:<parameter> value1:<vaule1> vaule2:<value2>...");
        addMSG("flag_FCT_AT_ZONE_REL", "Check is player in faction with defined relation");
        addMSG("flag_FCT_IS_REL_PLAYER_AROUND", "Check is there anyone with defined relation around the player");
        addMSG("flag_FCT_ARE_PLAYERS_IN_REL", "Check is players are in defined relations. Parameters: <player1> <player2> <relation>. Where <realtion> could be: LEADER, OFFICER, MEMBER, RECRUIT, ALLY, TRUCE, NEUTRAL or ENEMY");

		/*
         *  Activators!
		 */
        addMSG("msg_activatorlisttitle", "Activators");
        addMSG("activator_BUTTON", "This activator is linked to stone or wooden button. Command to create:  /react add button <id>");
        addMSG("activator_PLATE", "This activator is linked to stone or woode plate. Command to create: /react add plate <id>");
        addMSG("activator_REGION", "This activator is linked to Worldguard region (activates while player is in region)."
                + " Command: /react add region <id> <region id>");
        addMSG("activator_REGION_ENTER", "This activator is linked to Worldguard region (activates when player move into region)."
                + " Command: /react add region_enter <id> <region id>");
        addMSG("activator_REGION_LEAVE", "This activator is linked to Worldguard region (activates when player move out from region)."
                + " Command: /react add region_leave <id> <region id>");
        addMSG("activator_EXEC", "This is standalone activator (it is not bounded to any item or event)."
                + " EXEC activator could be executed by any other activator, built-in timer and command (/react run <activator> [parameters[)."
                + " Command to create: /react add exec <id>");
        addMSG("activator_COMMAND", "This activator is initiates when player typed a defined command. "
                + "Command: /react add command <id> <command>");

        addMSG("activator_MESSAGE", "This activator is initiates when defined message appears in chat input, console input, server log, chat screen. "
                + "Command: /react add message <id> type:<Type> source:<Source> mask:<MessageMask>");

        addMSG("activator_PVP_KILL", "This activator is activating when one player is killing another player. Command: /react add pvp_death <id>");
        addMSG("activator_PVP_DEATH", "This activator is activating after player death, if he was murdered by another player. Command: /react add pvp_death <id>");
        addMSG("activator_PVP_RESPAWN", "This activator is activating after respawn of dead player if he was murdered by another player. Command: /react add pvp_respawn <id>");
        addMSG("activator_LEVER", "This activator is linked to lever block and executing when player triggers this lever. It supports lever states - \"on\" and \"off\". Command: /react add lever <id> [ON/OFF/ANY]");
        addMSG("activator_DOOR", "This activators could be linked to any kind of doors (wooden door, fence gates and trap doors). Command: /react add door <id> [OPEN/CLOSE/ANY]");
        addMSG("activator_JOIN", "This activator is executing when player joins ther server. Command: /react add join <id> [FIRST]");
        addMSG("activator_QUIT", "This activator is executing when player leaves the server. Command: /react add join <id>");
        addMSG("activator_MOB_CLICK", "This activator is executing when player right-clicking mob. You can define mob type (name supported too) for this activators. Command: /react add mobclick <id> &6Mob_Name$MOB_TYPE");
        addMSG("activator_MOB_KILL", "This activator is executing when player killing the mob. You can define mob type (name supported too) for this activators. Command: /react add  mobclick <id> Mob_Name$MOB_TYPE");
        addMSG("activator_ITEM_CLICK", "This activator is linked to right-clicking with defined item. /react add item_click <id> <item (name supported)>");
        addMSG("activator_ITEM_CONSUME", "This activator is linked to eating (drinking) the event. /react add item_consume <id> <item (name supported)>");
        addMSG("activator_ITEM_HOLD", "This activator is linked to defined item, while player hold it in hand. /react add <id> item_hold <item (name supported)>");
        addMSG("activator_ITEM_WEAR", "This activator is linked to defined item, while player wears an item. /react add item_wear <id> <item (name supported)>");
        addMSG("activator_FCT_CHANGE", "This activator is initiates when player moved from one faction to another. /react add fct_change <id> faction:<New faction|ANY> oldfaction:<Old faction|ANY>");
        addMSG("activator_FCT_RELATION", "This activator is initiates when relationship between two factions is changed. /react add fct_relation <id>  faction1:<faction name|ANY> faction2:<faction name|ANY> newrealtion:<New relation|ANY> oldrealtion:<New relation|ANY>");
        addMSG("activator_SIGN", "This activator is initiates player clicks (right-click) sign defined as activator./react add SIGN <id> line1:<text in line1>...line4:<text in line4>");
        addMSG("activator_FCT_CREATE", "This activator is initiates when someone creates a new faction /react add fct_create <id>");
        addMSG("activator_FCT_DISBAND", "This activator is initiates when faction is disbanded /react add fct_disband <id>");
        addMSG("activator_VARIABLE", "This activator is initiates when variable value is changed /react add variable id:<VariableId> personal:<false/true>. Local variables provided by this activator: %var_id%, %var_old%, %var_new%");
        addMSG("msg_placeholderlisttitle", "Placeholders");
        addMSG("placeholder_TIME_SERVER", "Server (system) time");
        addMSG("placeholder_TIME_INGAME", "In-game time. If player is unknonw will show time in default world");
        addMSG("placeholder_PLAYER_LOC", "Player current location");
        addMSG("placeholder_PLAYER_LOC_EYE", "Player eye (head) location");
        addMSG("placeholder_PLAYER_LOC_VIEW", "Player point of view location");
        addMSG("placeholder_PLAYER_NAME", "Player name");
        addMSG("placeholder_PLAYER_DISPLAY", "Player display name");
        addMSG("placeholder_HEALTH", "Player health");
        addMSG("placeholder_PLAYER_LOC_DEATH", "Player death locations (PVP only)");
        addMSG("placeholder_TARGET_PLAYER", "Target player name (not all activators support this)");
        addMSG("placeholder_MONEY", "Player balance. Use \"%money.<world>%\" or \"%money.<world>.<currency>%\" to get balance of player defined by currency in defined world");
        addMSG("placeholder_RANDOM", "Random value. \"%rnd:100%\" - will show random number 0...99, \"%rnd:1-10%\" - will show random number 1..10, \"%rnd:word1,word2,word3%\" - will show random word in list (word1 for example)");
        addMSG("placeholder_TIME", "Flag-based placeholder. Returns time that defined in activator's flag.");
        addMSG("placeholder_CHANCE", "Flag-based placeholder. Returns chance that defined in activator's flag.");
        addMSG("placeholder_VAR", "Variable value. Use syntax %var:<id>% to get global variable value and %varp:<id>% to get personal variable value");
        addMSG("placeholder_CALC", "Calculates the expression and provide it's result. For example: \"%CALC:1+2%\" will be replaced to \"3\"");
        addMSG("placeholder_SIGNAct", "Activator-based placeholders. Provides SIGN activator locations and text-lines");
        addMSG("placeholder_COMMANDAct", "Activator-based placeholders. Provides COMMAND activator parameters (arguments)");
        addMSG("msg_needvdmid", "You need to define id of element (variable, delay or menu)");


    }

    @Override
    public void printMsg(CommandSender sender, String msg) {
        String message = ChatColor.translateAlternateColorCodes('&', msg);
        if ((!(sender instanceof Player)) && (!colorconsole)) message = ChatColor.stripColor(message);
        ChatPage chatPage = null;
        try {
            chatPage = ChatPaginator.paginate(message, 1, ReActions.getPlugin().getChatLineLength(), 10000);
        } catch (Exception ignore) {
        }
        ;
        if (chatPage != null) {
            for (String line : chatPage.getLines())
                sender.sendMessage(line);
        } else sender.sendMessage(message);
    }

    @Override
    public void printMSG(CommandSender p, Object... s) {
        String message = getMSG(s);
        if ((!(p instanceof Player)) && (!colorconsole)) message = ChatColor.stripColor(message);
        ChatPage chatPage = ChatPaginator.paginate(message, 1, ReActions.getPlugin().getChatLineLength(), 10000);
        for (String line : chatPage.getLines())
            p.sendMessage(line);
    }

	/*
	 * 	public static void printHelp(CommandSender sender, int page) {
		List<String> helpList = new ArrayList<String>();
		for (Cmd cmd : commands){
			helpList.add(cmd.getFullDescription());
		}
		int pageHeight = (sender instanceof Player) ? 9 : 1000;

		ReActions.getUtil().printMsg(sender, "&6&lReActions v"+ReActions.getPlugin().getDescription().getVersion()+" &r&6| "+ReActions.getUtil().getMSG("hlp_help",'6'));
		ChatPage chatPage = paginate (helpList, page,55,pageHeight);

		for (String str : chatPage.getLines())
			sender.sendMessage(str);

		if (pageHeight == 9)
			ReActions.getUtil().printMSG(sender, "lst_footer",'e','6', chatPage.getPageNumber(),chatPage.getTotalPages());
	}

	public static ChatPage paginate(List<String> unpaginatedStrings, int pageNumber, int lineLength, int pageHeight){
		List<String> lines = new ArrayList<String>();
		for (String str : unpaginatedStrings){
			lines.addAll(Arrays.asList(ChatPaginator.wordWrap(str, lineLength)));
		}
		int totalPages = lines.size()/ pageHeight + (lines.size() % pageHeight == 0 ? 0 : 1);
		int actualPageNumber = pageNumber <= totalPages ? pageNumber : totalPages;
		int from = (actualPageNumber - 1) * pageHeight;
		int to = from + pageHeight <= lines.size()? from + pageHeight : lines.size();
		String[] selectedLines = (String[])Java15Compat.Arrays_copyOfRange((String[])lines.toArray(new String[lines.size()]), from, to);
		return new ChatPage(selectedLines, actualPageNumber, totalPages);
	}
	 */


}
