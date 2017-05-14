/*
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2017, fromgate, fromgate@gmail.com
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

package me.fromgate.reactions.util.message;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public enum M {

    //Default (lang) messages
    LNG_LOAD_FAIL("Failed to load languages from file. Default message used"),
    LNG_SAVE_FAIL("Failed to save lang file"),
    LNG_PRINT_FAIL("Failed to print message %1%. Sender object is null."),
    LNG_CONFIG("[MESSAGES] Messages: %1% Language: %2% Save translate file: %1% Debug mode: %3%"),
    LNG_PRINT_FAIL_M("Failed to print message. Unknown key %1%"),

    WORD_UNKNOWN("Unknown"),
    WRONG_PERMISSION("You have not enough permissions to execute this command"),
    PERMISSION_FAIL("You have not enough permissions to execute this command", 'c'),
    PLAYER_COMMAD_ONLY("You can use this command in-game only!", 'c'),
    CMD_REGISTERED("Command registered: %1%"),
    CMD_FAILED("Failed to execute command. Type %1% to get help!"),
    HLP_TITLE("%1% | Help"),

    MSG_OUTDATED("%1% is outdated! Recommended version is %2%", 'e', '6'),
    MSG_PLEASEDOWNLOAD("Please download new version from:"),

    HLP_HELP("Help"),
    HLP_THISHELP("%1% - this help"),
    HLP_EXECCMD("%1% - execute command"),
    HLP_TYPECMD("Type %1% - to get additional help"),
    HLP_TYPECMDPAGE("Type %1% - to see another page of this help"),

    HLP_CMDPARAM_COMMAND("command"),
    HLP_CMDPARAM_PAGE("page"),
    HLP_CMDPARAM_PARAMETER("parameter"),
    CMD_PLAY("%1% - play effect"),

    CMD_UNKNOWN("Unknown command: %1%"),
    CMD_CMDPERMERR("Something wrong (check command, permissions)"),
    ENABLED("enabled"),
    DISABLED("disabled"),
    LST_TITLE("String list:"),
    LST_FOOTER("Page: [%1% / %2%]"),
    LST_LISTISEMPTY("List is empty"),

    MSG_LISTCLICKER("List of activators:"),
    MSG_LISTLOC("List of stored locations:"),
    MSG_LISTDELAY("List of active delays:"),
    CMD_ADDBADDED("Activator %1% successfully defined"),
    CMD_DELAYSET("New delay value saved. Id: %1% Delay time: %2%"),
    CMD_NOTADDBADDED("Failed to create activator %1%"),
    CMD_NOTADDBADDEDSYNTAX("Failed to create activator %1% (%2%). Please check syntax and try again."),
    CMD_ADDBREQBUT("You need to look at button to create new button-activator"),
    CMD_ADDTPADDED("Location %1% added"),
    CMD_ADDMENUADDED("New inventory menu created: %1%"),
    CMD_ADDMENUADDFAIL("Failed to create new inventory-menu %1%"),
    MSG_REMOVEMENU("Inventory-menu removed: %1%"),
    MSG_REMOVEMENUFAIL("Failed to remove inventory-menu %1%"),
    MSG_MENUPARAMSET("Parameters of inventory-menu %1% changed!"),
    MSG_MENUSETFAIL("Failed to change parameters of inventory-menu %1%"),
    MSG_MENULIST("Inventory-menu list"),
    MSG_MENUIDFAIL("There's no inventory-menu %1% defined!"),
    MSG_MENUINFOTITLE("Menu: %1% (size: %2%) Title: %3%"),
    MSG_MENUINFOSLOT("slot%1% : Activator: %2% Item: %3%"),
    CMD_UNKNOWNADD("Unknown type of activator"),
    CMD_ACTADDED("Action was added: %1%"),
    CMD_ACTNOTADDED("Action was not added: %1%"),
    CMD_REACTADDED("Reaction was added: %1%"),
    CMD_REACTNOTADDED("Reaction was not added: %1%"),
    CMD_FLAGADDED("Flag was added: %1%"),
    CMD_FLAGNOTADDED("Flag was not added: %1%"),
    CMD_UNKNOWNBUTTON("Activator %1% was not found"),
    LOC_UNKNOWN("Unknown location"),
    CMD_ADD("%1% - main command to add new activator, locations, add flags, actions and reactions to activator"),
    CMD_SET("%1% - set delay that could be checked with DELAY and DELAY_PLAYER flags"),
    CMD_RUN("%1% - Execute the <exec-activator> for a defined player after <delay>"),
    CMD_LIST("%1% - display list of activators (all, groupped by type or group name), list of stored locations"),
    CMD_INFO("%1% - display full info about activator (or display flags, actions and reactions of it)"),
    CMD_REMOVE("%1% - remove stored activator or location"),
    CMD_CLEAR("%1% - clear flags/actions/reactions bounded with activator with defined id"),
    CMD_DEBUG("%1% - switches debug mode (all checks - true, all checks - false, disabled)"),
    CMD_CHECK("%1% - check is you looking at block (button) with bounded activator, or find activators around you (radius)"),
    CMD_RELOAD("%1% - reload stored locations and activators from configuration files"),
    CMD_EXEC("%1% - execute EXEC-activator"),
    CMD_DEBUGTRUE("Debug mode enabled (always - true)"),
    CMD_DEBUGFALSE("Debug mode enabled (always - false)"),
    CMD_DEBUGOFF("Debug mode disabled"),
    CMD_SELECT("%1% - selects block in your view point. You can use this selection as \"selection\" (or \"sel\") keyword when defining locations"),
    CMD_SELECTED("Location %1% selected!"),
    CMD_VARSET("Variable %1% was set to %2%"),
    MSG_LISTCOUNT("There's %1% configured activators and %2% stored locations"),
    MSG_REMOVEBNF("Activator %1% not found and not removed"),
    MSG_REMOVELOCNF("Stored location %1% not found and not removed"),
    MSG_REMOVEBOK("Activator %1% was removed"),
    MSG_REMOVELOCOK("Stored location %1% was removed"),
    MSG_CLEARFLAG("Flags of activator %1% were cleared"),
    MSG_CLEARACT("Actions of activator %1% were cleared"),
    MSG_CLEARREACT("Reactions of activator %1% were cleared"),
    CMD_CHECKMSG("Found %1% activators around you (radius %2%)"),
    CMD_CHECKFAIL("Activators around you was not found (radius %1%)"),
    CMD_CHECKNEEDNUMBER("Wrong number: %1%"),
    MSG_CMDRELOAD("Reload completed. Loaded %1% activators, %2% locations."),
    CMD_COPY("%1% - to copy all parameters (or flags, actions, reactions) from <source> activator to <destination>"),
    MSG_COPYALL("All parameters of activator %1% was copied to %2%"),
    MSG_COPYALLFAILED("Failed to copy parameters from activator %1% to %2%"),
    MSG_COPYFLAGS("Flags of activator %1% was copied to %2%"),
    MSG_COPYFLAGSFAILED("Failed to copy flags from activator %1% to %2%"),
    MSG_COPYACTIONS("Actions of activator %1% was copied to %2%"),
    MSG_COPYACTIONSFAILED("Failed to copy actions from activator %1% to %2%"),
    MSG_COPYREACTIONS("Reactions of activator %1% was copied to %2%"),
    MSG_COPYREACTIONSFAILED("Failed to copy reactions from activator %1% to %2%"),
    MSG_GROUPLISTTITLE("Groups of activators"),
    MSG_ACTLIST("Activators"),
    MSG_ACTLISTGRP("Activators (Group: %1%)"),
    MSG_ACTLISTTYPE("Activators (Type: %1%)"),
    CMD_GROUP("%1% - set the group of <activator> to group <group>"),
    MSG_GROUPSET("Activator %1% was moved to group %2%"),
    MSG_GROUPSETFAILED("Failed to move activator %1% to group %2%"),
    MSG_ACTINFOTITLE("Activator info"),
    MSG_ACTINFO("Id: %1% (Type: %2% Group: %3%)"),
    MSG_ACTINFO2("Flags: %1% Actions: %2% Reactions: %3%"),
    LST_FLAGS("Flags"),
    LST_ACTIONS("Actions"),
    LST_REACTIONS("Reactions"),
    MSG_CHECK("Activators around you"),
    MSG_FLAGREMOVED("Flag No.%2% of activator %1% was removed!"),
    MSG_FAILEDTOREMOVEFLAG("Failed to remove flag No.%2% of actiovator %1%"),
    MSG_ACTIONREMOVED("Action No.%2% of activator %1% was removed!"),
    MSG_FAILEDTOREMOVEACTION("Failed to remove action No.%2% of actiovator %1%"),
    MSG_REACTIONREMOVED("Reaction No.%2% of activator %1% was removed!"),
    MSG_FAILEDTOREMOVEREACTION("Failed to remove reaction No.%2% of actiovator %1%"),
    MSG_WRONGNUMBER("Wrong number %1%!"),
    CMD_RUNPLAYER("Activator execution started (parameters: %1%)"),
    CMD_RUNPLAYERFAIL("Failed to execute activator (parameters: %1%)"),
    MSG_MOBBOUNTY("You received %1% for killing %2%"),
    MSG_NEEDREGION("You must define region for this activator"),
    ACT_TP("You was teleported to %1%"),
    ACT_TPFAIL("Failed teleportation to %1% (wrong location?)"),
    ACT_VELOCITY("Your movement direction was changed"),
    ACT_VELOCITYFAIL("Failed to change your movement direction"),
    ACT_SOUND("You hear strange sounds (%1%) around you"),
    ACT_SOUNDFAIL("Failed to play sound %1%"),
    ACT_POTION("You have strange feeling of %1%. Maybe you're bewitched?"),
    ACT_POTIONFAIL("Failed to set potion effect %1%"),
    ACT_POTION_REMOVE("Potion effect was removed: %1%"),
    ACT_POTION_REMOVEFAIL("There's no potion effect to remove"),
    ACT_GROUP_ADD("You were added to group %1%"),
    ACT_GROUP_ADDFAIL("Cannot add you in group %1%"),
    ACT_GROUP_REMOVE("You were excluded from group %1%"),
    ACT_GROUP_REMOVEFAIL("Cannot remove you from group %1%"),
    ACT_MESSAGE("Message: %1%"),
    ACT_MESSAGEFAIL("Failed to send message"),
    ACT_BROADCAST("Broadcast message: %1%"),
    ACT_BROADCASTFAIL("Failed to send global message"),
    ACT_DAMAGE("You receive a damage. %1% hit points lost"),
    ACT_TOWN_SET("You was added to town %1%"),
    ACT_TOWN_SETFAIL("Failed to add you into town %1%"),
    ACT_TOWN_KICK("You was kicked from town!"),
    ACT_TOWN_KICKFAIL("Failed to kick you from town!"),
    ACT_ITEM_REMOVE("You lost item: %1%"),
    ACT_ITEM_REMOVE_OFFHAND("You lost offhand-item: %1%"),
    ACT_ITEM_REMOVEFAIL("Failed to remove item: %1%"),
    ACT_ITEM_REMOVE_OFFHANDFAIL("Failed to remove offhand-item: %1%"),
    ACT_ITEM_REMOVE_INVENTORY("You lost item from the inventory: %1%"),
    ACT_ITEM_REMOVE_INVENTORYFAIL("Failed to remove item %1% from your inventory"),
    ACT_ITEM_GIVE("You receive item(s): %1%"),
    ACT_ITEM_GIVEFAIL("Failed to give item(s): %1%"),
    ACT_ITEM_DROP("New item(s) spawned: %1%"),
    ACT_ITEM_DROPFAIL("Failed to spawn item(s): %1%"),
    ACT_CMD("Command executed (by player): %1%"),
    ACT_CMDFAIL("Failed to execute command"),
    ACT_CMD_CONSOLE("Command executed (by console): %1%"),
    ACT_CMD_OP("Command executed (as OP): %1%"),
    ACT_CMD_OPFAIL("Failed to execute command as OP"),
    ACT_MONEY_PAY("You paid %1%"),
    ACT_MONEY_PAYFAIL("Failed to remove %1% from your account"),
    ACT_MONEY_GIVE("You credited by %1%"),
    ACT_MONEY_GIVEFAIL("Failed to credit your account with %1%"),
    ACT_DELAY("You will wait some time to do it again!"),
    ACT_DELAYFAIL("Failed to setup activator delay"),
    ACT_DELAY_PLAYER("You will wait some time to do it again!"),
    ACT_DELAY_PLAYERFAIL("Failed to setup activator delay"),
    ACT_BACK("You were pushed back!"),
    ACT_BACKFAIL("Failed to push you back!"),
    ACT_MOB_SPAWN("Ooops! Mobs was spawned!"),
    ACT_EFFECT("You noticed interesting visual effect provided by plugin PlayEffect!"),
    ACT_EXECUTE("Activator %1% started!"),
    ACT_EXECUTEFAIL("Failed to execute activator"),
    ACT_EXECUTE_STOP("Activator %1% stopped"),
    ACT_EXECUTE_UNSTOP("Activator %1% restored and could be executed next time"),
    ACT_EXECUTE_UNSTOPFAIL("Fail to restore activator %1%. Is it stopped?"),
    ACT_POWER_SET("Power state of block %1% was changed"),
    ACT_POWER_SETFAIL("Power state of block %1% was not changed"),
    ACT_REGION_CLEAR("Region was cleared. %1% mobs was killed!"),
    ACT_REGION_CLEARFAIL("Failed to clear region"),
    ACT_HEAL("You was healed by %1%!"),
    ACT_HEALFAILED("Failed to perform healing..."),
    ACT_BLOCK_SET("Block placed: %1%!"),
    ACT_BLOCK_SETFAILED("Failed to place block: %1%"),
    MSG_TIMERLIST("Timers"),
    MSG_TIMERNEEDNAME("You must define name for the timer"),
    MSG_TIMERUNKNOWNNAME("Could not find timer %1%"),
    MSG_TIMERREMOVED("Timer %1% removed"),
    MSG_TIMEREXIST("Timer with %1% name already exists"),
    MSG_TIMERNEEDPARAMS("You need to define parameters (activator, time, timer-type)!"),
    MSG_TIMERNEEDACTIVATOR("You need to define activator (activator:<EXEC-activator)!"),
    MSG_TIMERNEEDTYPE("You need to define timer type (timer-type:<INGAME|SERVER>)!"),
    MSG_TIMERNEEDTIME("You need to define execution time (time:<HH:MM,HH:MM|cron format: * * * * * *>!"),
    MSG_TIMERADDED("New timer created: %1%"),
    MSG_VARNEEDID("You need to define variable Id (and player name for personal variables)"),
    MSG_VARREMOVED("Variable was removed"),
    MSG_VARREMOVEFAIL("Failed to remove variable"),
    MSG_VARLIST("Variables"),
    MSG_SIGNFORBIDDEN("You're not permitted to set signs, that subscribed to activator %1%"),


    /*
    ON DESCRIPTION MESSAGES
     */
    MSG_ACTIONLISTTITLE("Actions"),
    ACTION_TP("Teleport player to defined location. Parameters: loc:<location or location's name> radius:<radius> land:<true/false> effect:<effect type>"),
    ACTION_VELOCITY("Set player's velocity (you can force player to jump or change movement direction). Parameters: vector:<x,y,z> kick:<true/false>"),
    ACTION_SOUND("Play sound effect. Parameters: type:<sound> pitch:<pitch> volume:<volume>"),
    ACTION_POTION("Add defined potion effect to player. Parameters: type:<potion type> level:<power> time:<duration time> ambient:<true/false>"),
    ACTION_POTION_REMOVE("Remove potion effect. Parameters: <POTION1,POTION2,..>"),
    ACTION_GROUP_ADD("Add player to group. Parameter: <group name>"),
    ACTION_GROUP_REMOVE("Kick player from group. Parameter: <group name>"),
    ACTION_MESSAGE("Display message to player. Parameters: region:<region> group:<group> perm:<permission> world:<world> player:<player>"),
    ACTION_BROADCAST("Send message to every online player. Parameter - message."),
    ACTION_DAMAGE("Hit player. Parameter: <damage amount>"),
    ACTION_TOWN_SET("Move player to Towny's town. Parameters: <town name>"),
    ACTION_TOWN_KICK("Exclude player from the Towny's town. Parameters: <town name>"),
    ACTION_ITEM_GIVE("Give item to player. Parameter: <item>"),
    ACTION_ITEM_REMOVE("Remove item from the player's hand. Parameter: <item>"),
    ACTION_ITEM_REMOVE_OFFHAND("Remove item from the player's offhand. Parameter: <item>"),
    ACTION_ITEM_REMOVE_INVENTORY("Remove item from the player's inventory. Parameter: <item>"),
    ACTION_ITEM_DROP("Drop items around defined location. Parameters: loc:<location> radius:<radius> scatter:<true/false> land:<true/false>"),
    ACTION_ITEM_WEAR("Wear item. Parameters: item:<item> slot:<auto/chestplate/helmet/leggins/boots>"),
    ACTION_ITEM_UNWEAR("Unwear item. Parameters: item:<item> slot:<auto/chestplate/helmet/leggins/boots> [item-action:<remove|drop|inventory>]"),
    ACTION_CMD("Execute command as player. Parameter: <command>"),
    ACTION_CMD_OP("Execute command as OP. Parameter: <command>"),
    ACTION_CMD_CONSOLE("Execute command as server console. Parameter: <command>"),
    ACTION_MONEY_PAY("Debit player's account (and credit target player if defined). Parameter: <amount>[/<target>]"),
    ACTION_MONEY_GIVE("Credit player's account (and debit source player if defined). Parameter: <amount>[/<source>]"),
    ACTION_DELAY("Set global delay variable. Parameter: <time>/<id>"),
    ACTION_DELAY_PLAYER("Set personal delay variable. Parameter: <time>/<id>"),
    ACTION_BACK("Pushback player to one or two of previously stored locations. Parameters: <1 or 2>"),
    ACTION_MOB_SPAWN("Spawn mob. Parameter: type:<mob type> (read more about this action at dev.bukkit.org)"),
    ACTION_EFFECT("Play visual effect. Parameter: eff:<effect> loc:<location>. PlayEffect plugin require for additional effects."),
    ACTION_EXECUTE("Execute a predefined EXEC-activator. Parameters: activator:<exec-activator> delay:<time> player:<all|null|player1,player2...> world:<world1,world2,..> region:<region1,region2,..>"),
    ACTION_EXECUTE_STOP("Stop executing of delayed activator. Parameter: activator:<exec-activator> player:<player>"),
    ACTION_EXECUTE_UNSTOP("Resume executing of stopped activator. Parameters: activator:<exec-activator> player:<player>"),
    ACTION_REGION_CLEAR("Remove entities (mobs or items) in region. Paramters: region:<region id> type:<entity_type|all|mobs|items>"),
    ACTION_HEAL("Heal player. Parameter: <hit points amount>"),
    ACTION_BLOCK_SET("Place block in location. Parameters: loc:<location> block:<type[:data]>"),
    ACTION_BLOCK_FILL("Fill area define by parameters loc1, loc2 or region with defined block type. Parameters: <loc1:<world,x1,y1,z1> loc2:<world,x2,y2,z2>|region:<RegionId>> block:<Type:Data> chance:<Chance>"),
    ACTION_POWER_SET("Set redstone power of the block (supported levers and doors). Parameters: loc:<location> power:<on|off|toggle>"),
    ACTION_SHOOT("Shoot (without projectile) in player view direction. Parameters: distance:<distance> singlehit:<true/false> damage:<damage amount>"),
    ACTION_VAR_SET("Create global variable. Parameters: id:<id> value:<value>"),
    ACTION_VAR_PLAYER_SET("Create personal variable. Parameters: id:<id> value:<value>"),
    ACTION_VAR_TEMP_SET("Create temporary variable. Parameters: id:<id> value:<value>"),
    ACTION_VAR_CLEAR("Remove global varibale. Parameter: id:<id>"),
    ACTION_VAR_PLAYER_CLEAR("Remove personal varibale. Parameter: id:<id>"),
    ACTION_VAR_INC("Increase global variable value. Parameters: id:<id> value:<value>"),
    ACTION_VAR_PLAYER_INC("Increase personal variable value. Parameters: id:<id> value:<value>"),
    ACTION_VAR_DEC("Decrease global variable value. Parameters: id:<id> value:<value>"),
    ACTION_VAR_PLAYER_DEC("Decrease personal variable value. Parameters: id:<id> value:<value>"),
    ACTION_RNC_SET_RACE("Set player's race (RacesAndClass plugin required). Parameter: race:<race>"),
    ACTION_RNC_SET_CLASS("Set player's class (RacesAndClass plugin required). Parameter: class:<class>"),
    ACTION_TIMER_STOP("Stops execution of timer. Parameter: timer:<timer id>"),
    ACTION_TIMER_RESUME("Resumes execution of stopped timer. Parameter: timer:<timer id>"),
    ACTION_VELOCITY_JUMP("Jump to locations. Parameter: loc:<location>"),
    ACTION_CANCEL_EVENT("Cancel bukkit event, that initiates current activator (not all activators could be cancelled). Parameter: TRUE"),
    ACTION_SQL_SELECT("Execute SQL query and store field located in first row (and defined column) at variable. Parameters: query:{SELECT... } variable:<variable id> player:<varibale owner> column:<column>"),
    ACTION_SQL_UPDATE("Execute update-query (update field of table at MySQL database) Parameter: query:{UPDATE... }"),
    ACTION_SQL_INSERT("Execute insert-query (insert new row in table at MySQL database) Parameter: query:{INSERT... }"),
    ACTION_SQL_DELETE("Execute delete-query (delete record in MySQL database table) Parameter: query:{DELETE... }"),
    ACTION_SIGN_SET_LINE("Set (or clear) one or more line of sign. Parameters: loc:<location> line1:<text>...line4:<text> clear:<1,2..4>"),
    ACTION_FCT_POWER_ADD("Add power to player's faction power value. Prameters: value:<Value>"),
    ACTION_ACTION_DELAYED("Execute another action after delay. Parameters: time:<time> action:{<another action with parameters>}"),
    ACTION_WAIT("Wait some time before exucute another actions. Parameter: time:<time>"),
    ACTION_MENU_ITEM("Create and show GUI (item menu) to player. Parameters: menu:<MenuId>"),
    ACTION_ITEM_SLOT("Place item into provided inventory slot. Parameters: item:<Item> slot:<Number:0..35> [exist:<drop | undress | keep>]"),
    ACTION_REGEX("This action is used when you need to pull out point data from the text. Parameters: input:<Text> regex:<RegExp> [prefix<Prefix>]"),
    ACTION_LOG("Write message into server log file. You can use it as additional debug tool for your activators. Parameters: message:<Text> prefix:<true/False> color:<true/False>"),
    ACTION_PLAYER_ID("This action is used when it is necessary to get the UUID of the player by its nickname and vice versa - the nickname of the player by its UUID. Parameters: varid:<VariableUUID>"),
    ACTION_FILE("Actions on the server files. Parameters: action:remove filename:[Path]<File.Ext>"),
    ACTION_GLIDE("Set the flight mode on Elytra. Parameters: glide:<true/false> [player:<Name>]"),

    /*
    DESCRIPTION MESSAGES
     */
    MSG_FLAGLISTTITLE("Flags"),
    FLAG_GROUP("Check player's group. Parameter: <group>"),
    FLAG_PERM("Check player's permission. Parameter: <permission>"),
    FLAG_TIME("Check in-game time in player's world. Parameter: <time in hours>"),
    FLAG_ITEM("Check item in hand. Parameter: <item>"),
    FLAG_ITEM_INVENTORY("Finding item in inventory. Parameter: <item>"),
    FLAG_ITEM_WEAR("Finding item in armour slot. Parameter: <item>"),
    FLAG_ITEM_OFFHAND("Check item in offhand. Parameter: <item>"),
    FLAG_TOWN("Check player's town. Parameter: <town>"),
    FLAG_MONEY("Check player account. Parameter: <money amount>"),
    FLAG_CHANCE("Roll dice with defined chance. Parameter:<chance>"),
    FLAG_PVP("Checks is player was in PVP-action during last <time in seconds> seconds. Parameter: <time>"),
    FLAG_ONLINE("Number of player online. Parameter: <required online>"),
    FLAG_DELAY("Check the countdown of delay variable. Parameter: <delay id>"),
    FLAG_DELAY_PLAYER("Check the countdown of personal delay variable. Parameter: <delay id>"),
    FLAG_STATE("Check player's state. Parameter: <STAND/SNEAK/SPRINT/VEHICLE/VEHICLE_MINECART/VEHICLE_BOAT/VEHICLE_PIG/VEHICLE_HORSE"),
    FLAG_REGION("Is player in region? Parameter: <region>"),
    FLAG_REGION_PLAYERS("This flag returns true when there <count> (or more) players located in region <region>. Parameter: <region>/<count>"),
    FLAG_REGION_MEMBER("Is player member of region? Parameter: <region>"),
    FLAG_REGION_OWNER("Is player owner of region? Parameter: <region>"),
    FLAG_REGION_STATE("Check flag value in the region. Parameters: [<World>.]<RegionName.FlagName.FlagValue>[/<GroupName>]"),
    FLAG_GAMEMODE("Check gamemode. Parameter:  <survival/creative/adventure>"),
    FLAG_FOODLEVEL("Check food level. Parameter: <food level>"),
    FLAG_XP("Check player total experience. Parameter: <xp>"),
    FLAG_LEVEL("Check player experience level. Parameter: <level>"),
    FLAG_POWER("Check redstone power state of block. Parameter: <location>"),
    FLAG_WORLD("Player in world? Parameter: <world>"),
    FLAG_BIOME("Player in biome? Parameter: <biome>"),
    FLAG_BLOCK("Check block type. Parameters: loc:<Location> block:<BlockType>"),
    FLAG_LIGHT_LEVEL("Player is in dark place? Parameter: <light level, 1..20>"),
    FLAG_WALK_BLOCK("Player is walking on the defined block? Parameter: <block type>"),
    FLAG_DIRECTION("Player directed to...? Parameter: <NORTH/NORTHEAST/NORTHWEST/SOUTH/SOUTHEAST/SOUTHWEST/EAST/WEST>"),
    FLAG_FLAG_SET("Check all flags in the list and return true if any. Parameter: <[!]<flag1>=<value1> [!]<flag2>=<value2> ...>"),
    FLAG_EXECUTE_STOP("Check stopped-state of delayed EXEC activator. Parameter: <activator id>"),
    FLAG_VAR_EXIST("Is global variable exists? Parameter: <id>"),
    FLAG_VAR_PLAYER_EXIST("Is personal variable exists? Parameter: <id>"),
    FLAG_VAR_COMPARE("Compare global variable with value. Parameters: id:<id> value:<value>"),
    FLAG_VAR_PLAYER_COMPARE("Compare personal variable with value. Parameters: id:<id> value:<value>"),
    FLAG_VAR_GREATER("Is global variable greater than given value? Parameters: id:<id> value:<value>"),
    FLAG_VAR_PLAYER_GREATER("Is personal variable greater than given value? Parameters: id:<id> value:<value>"),
    FLAG_VAR_LOWER("Is global variable lower than given value? Parameters: id:<id> value:<value>"),
    FLAG_VAR_PLAYER_LOWER("Is personal variable lower than given value? Parameters: id:<id> value:<value>"),
    FLAG_VAR_MATCH("Check is value of provided variable is match (regex) to provided value. Parameters: id:<id> value:<value>"),
    FLAG_VAR_PLAYER_MATCH("Check is value of provided variable is match (regex) to provided value. Parameters: id:<id> value:<value>"),
    FLAG_RNC_RACE("Check player's race (Requires RacesAndClasses plugin). Parameter: <race>"),
    FLAG_RNC_CLASS("Check player's class (Requires RacesAndClasses plugin). Parameter: <class>"),
    FLAG_WEATHER("Check weather state around player. Parameter: <rain/clear>"),
    FLAG_TIMER_ACTIVE("Check active state of defined timer. Returns false if timer is paused. Parameter: <timer id>"),
    FLAG_FCT_PLAYER("Check player's faction. Parameter: Faction's name"),
    FLAG_SQL_CHECK("Compares result of SQL-query with provided value. Parameters: query:{SELECT...} value:<value> column:<column>"),
    FLAG_SQL_RESULT("Check is result of SQL-query returns data. If result is empty flag will return false. Parameter: query:{SELECT...}"),
    FLAG_COMPARE("Compare provide parameter with list of variable. True if parameter is equal to one of the provided variables. Parameters: param:<parameter> value1:<vaule1> vaule2:<value2>..."),
    FLAG_FCT_AT_ZONE_REL("Check is player in faction with defined relation"),
    FLAG_FCT_IS_REL_PLAYER_AROUND("Check is there anyone with defined relation around the player"),
    FLAG_FCT_ARE_PLAYERS_IN_REL("Check is players are in defined relations. Parameters: <player1> <player2> <relation>. Where <realtion> could be: LEADER, OFFICER, MEMBER, RECRUIT, ALLY, TRUCE, NEUTRAL or ENEMY"),
    FLAG_FLY_SPEED("Check is player fly speed higher then provided value or not"),
    FLAG_WALK_SPEED("Check is player walk speed higher then provided value or not"),

    /*
            ACTIVATORS!
     */
    MSG_ACTIVATORLISTTITLE("Activators"),
    ACTIVATOR_BUTTON("This activator is linked to stone or wooden button. Command to create:  /react add button <id>"),
    ACTIVATOR_PLATE("This activator is linked to stone or woode plate. Command to create: /react add plate <id>"),
    ACTIVATOR_REGION("This activator is linked to Worldguard region (activates while player is in region)." + " Command: /react add region <id> <region id>"),
    ACTIVATOR_REGION_ENTER("This activator is linked to Worldguard region (activates when player move into region)." + " Command: /react add region_enter <id> <region id>"),
    ACTIVATOR_REGION_LEAVE("This activator is linked to Worldguard region (activates when player move out from region)." + " Command: /react add region_leave <id> <region id>"),
    ACTIVATOR_EXEC("This is standalone activator (it is not bounded to any item or event)." + " EXEC activator could be executed by any other activator, built-in timer and command (/react run <activator> [parameters[)." + " Command to create: /react add exec <id>"),
    ACTIVATOR_COMMAND("This activator is initiates when player typed a defined command. " + "Command: /react add command <id> <command>"),

    ACTIVATOR_MESSAGE("This activator is initiates when defined message appears in chat input, console input, server log, chat screen. " + "Command: /react add message <id> type:<Type> source:<Source> mask:<MessageMask>"),

    ACTIVATOR_PVP_KILL("This activator is activating when one player is killing another player. Command: /react add pvp_death <id>"),
    ACTIVATOR_PVP_DEATH("This activator is activating after player death, if he was murdered by another player. Command: /react add pvp_death <id>"),
    ACTIVATOR_PVP_RESPAWN("This activator is activating after respawn of dead player if he was murdered by another player. Command: /react add pvp_respawn <id>"),
    ACTIVATOR_LEVER("This activator is linked to lever block and executing when player triggers this lever. It supports lever states - \"on\" and \"off\". Command: /react add lever <id> [ON/OFF/ANY]"),
    ACTIVATOR_DOOR("This activators could be linked to any kind of doors (wooden door, fence gates and trap doors). Command: /react add door <id> [OPEN/CLOSE/ANY]"),
    ACTIVATOR_JOIN("This activator is executing when player joins ther server. Command: /react add join <id> [FIRST]"),
    ACTIVATOR_QUIT("This activator is executing when player leaves the server. Command: /react add join <id>"),
    ACTIVATOR_MOB_CLICK("This activator is executing when player right-clicking mob. You can define mob type (name supported too) for this activators. Command: /react add mobclick <id> &6Mob_Name$MOB_TYPE"),
    ACTIVATOR_MOB_KILL("This activator is executing when player killing the mob. You can define mob type (name supported too) for this activators. Command: /react add  mobclick <id> Mob_Name$MOB_TYPE"),
    ACTIVATOR_ITEM_CLICK("This activator is linked to right-clicking with defined item. /react add item_click <id> <item (name supported)>"),
    ACTIVATOR_ITEM_CONSUME("This activator is linked to eating (drinking) the event. /react add item_consume <id> <item (name supported)>"),
    ACTIVATOR_ITEM_HOLD("This activator is linked to defined item, while player hold it in hand. /react add <id> item_hold <item (name supported)>"),
    ACTIVATOR_ITEM_WEAR("This activator is linked to defined item, while player wears an item. /react add item_wear <id> <item (name supported)>"),
    ACTIVATOR_FCT_CHANGE("This activator is initiates when player moved from one faction to another. /react add fct_change <id> faction:<New faction|ANY> oldfaction:<Old faction|ANY>"),
    ACTIVATOR_FCT_RELATION("This activator is initiates when relationship between two factions is changed. /react add fct_relation <id>  faction1:<faction name|ANY> faction2:<faction name|ANY> newrealtion:<New relation|ANY> oldrealtion:<New relation|ANY>"),
    ACTIVATOR_SIGN("This activator is initiates player clicks (right-click) sign defined as activator./react add SIGN <id> line1:<text in line1>...line4:<text in line4>"),
    ACTIVATOR_FCT_CREATE("This activator is initiates when someone creates a new faction /react add fct_create <id>"),
    ACTIVATOR_FCT_DISBAND("This activator is initiates when faction is disbanded /react add fct_disband <id>"),
    ACTIVATOR_VARIABLE("This activator is initiates when variable value is changed /react add variable id:<VariableId> personal:<false/true>. Local variables provided by this activator: %var_id%, %var_old%, %var_new%"),
    ACTIVATOR_PLAYER_DEATH("This activator is activating after player death. All flags and actions will bound to the \"dead player\". Activator may be linked to different reason of player death: PVP, PVE or OTHER"),
    ACTIVATOR_PLAYER_RESPAWN("This activator is activating after respawn of dead player. All flags and actions will bound to the \"deadplayer\" player."),
    ACTIVATOR_MOB_DAMAGE("This activator executing when player left-clicking or shooting mob. You can define mob type (name supported too) and item (in player's hand) for this activators"),
    ACTIVATOR_BLOCK_CLICK("This activator works when the player clicks on the block with the left or right mouse button."),
    ACTIVATOR_INVENTORY_CLICK("This activator works when the player performs actions with the inventory using the mouse or keys"),
    ACTIVATOR_DROP("This activator initiates when player drop out items"),
    ACTIVATOR_FLIGHT("This activators initiates when player's flight-mode changed"),
    ACTIVATOR_ENTITY_CLICK("This activator is initiated when the player performs a right-click on an entity. /react add entity_click <id> [entity-type]"),
    MSG_PLACEHOLDERLISTTITLE("Placeholders"),
    PLACEHOLDER_TIME_SERVER("Server (system) time"),
    PLACEHOLDER_TIME_INGAME("In-game time. If player is unknonw will show time in default world"),
    PLACEHOLDER_PLAYER_LOC("Player current location"),
    PLACEHOLDER_PLAYER_LOC_EYE("Player eye (head) location"),
    PLACEHOLDER_PLAYER_LOC_VIEW("Player point of view location"),
    PLACEHOLDER_PLAYER_NAME("Player name"),
    PLACEHOLDER_PLAYER_DISPLAY("Player display name"),
    PLACEHOLDER_HEALTH("Player health"),
    PLACEHOLDER_PLAYER_LOC_DEATH("Player death locations (PVP only)"),
    PLACEHOLDER_TARGET_PLAYER("Target player name (not all activators support this)"),
    PLACEHOLDER_MONEY("Player balance. Use \"%money.<world>%\" or \"%money.<world>.<currency>%\" to get balance of player defined by currency in defined world"),
    PLACEHOLDER_RANDOM("Random value. \"%rnd:100%\" - will show random number 0...99, \"%rnd:1-10%\" - will show random number 1..10, \"%rnd:word1,word2,word3%\" - will show random word in list (word1 for example)"),
    PLACEHOLDER_TIME("Flag-based placeholder. Returns time that defined in activator's flag."),
    PLACEHOLDER_CHANCE("Flag-based placeholder. Returns chance that defined in activator's flag."),
    PLACEHOLDER_VAR("Variable value. Use syntax %var:<id>% to get global variable value and %varp:<id>% to get personal variable value"),
    PLACEHOLDER_CALC("Calculates the expression and provide it's result. For example: \"%CALC:1+2%\" will be replaced to \"3\""),
    PLACEHOLDER_SIGNACT("Activator-based placeholders. Provides SIGN activator locations and text-lines"),
    PLACEHOLDER_COMMANDACT("Activator-based placeholders. Provides COMMAND activator parameters (arguments)"),
    MSG_NEEDVDMID("You need to define id of element (variable, delay or menu)"),
    LNG_MISSED_ACTIVATOR_DESC("Activator description undefined: %1%"),
    LNG_FAIL_ACTION_MSG("Action message undefined: %1%"),
    LNG_FAIL_ACTION_DESC("Action description undefined: %1%"),
    LNG_FAIL_FLAG_DESC("Flag description undefined: %1%"),
    LNG_FAIL_PLACEHOLDER_DESC("Placeholder description undefined: %1%");

    private static Messenger messenger;

    private static boolean debugMode = false;
    private static String language = "default";
    private static String pluginName;
    private static Set onceLog = new HashSet();


    public static String colorize(String text) {
        return messenger.colorize(text);
    }


    /**
     * This is my favorite debug routine :) I use it everywhere to print out variable values
     *
     * @param s - array of any object that you need to print out.
     *          Example:
     *          Message.BC ("variable 1:",var1,"variable 2:",var2)
     */
    public static void BC(Object... s) {
        if (!debugMode) return;
        if (s.length == 0) return;
        StringBuilder sb = new StringBuilder("&3[").append(pluginName).append("]&f ");
        for (Object str : s) {
            sb.append(str == null ? "null" : str.toString()).append(" ");
        }

        messenger.broadcast(colorize(sb.toString().trim()));
    }

    /**
     * Send current message to log files
     *
     * @param s
     * @return — always returns true.
     * Examples:
     * Message.ERROR_MESSAGE.log(variable1); // just print in log
     * return Message.ERROR_MESSAGE.log(variable1); // print in log and return value true
     */
    public boolean log(Object... s) {
        M.logMessage(getText(s));
        return true;
    }

    /**
     * Same as log, but will printout nothing if debug mode is disabled
     *
     * @param s
     * @return — always returns true.
     */
    public boolean debug(Object... s) {
        if (debugMode)
            log(messenger.clean(getText(s)));
        return true;
    }

    /**
     * Show a message to player in center of screen (this routine unfinished yet)
     *
     * @param seconds — how much time (in seconds) to show message
     * @param sender  — Player
     * @param s
     * @return — always returns true.
     */
    public boolean tip(int seconds, Object sender, Object... s) {
        return messenger.tip(seconds, sender, getText(s));
    }
    /*
    public boolean tip(int seconds, CommandSender sender, Object... s) {
        if (sender == null) return Message.LNG_PRINT_FAIL.log(this.name());
        final Player player = sender instanceof Player ? (Player) sender : null;
        final String message = getText(s);
        if (player == null) sender.sendMessage(message);
        else for (int i = 0; i < seconds; i++)
            Server.getInstance().getScheduler().scheduleDelayedTask(new Runnable() {
                public void run() {
                    if (player.isOnline()) player.sendTip(message);
                }
            }, 20 * i);
        return true;
    } */

    /**
     * Show a message to player in center of screen
     *
     * @param sender — Player
     * @param s
     * @return — always returns true.
     */
    public boolean tip(Object sender, Object... s) {
        return messenger.tip(sender, getText(s));
    }

    /**
     * Send message to Player or to ConsoleSender
     *
     * @param sender
     * @param s
     * @return — always returns true.
     */
    public boolean print(Object sender, Object... s) {
        if (sender == null) return M.LNG_PRINT_FAIL.log(this.name());
        return messenger.print(sender, getText(s));
    }

    /**
     * Send message to all players or to players with defined permission
     *
     * @param permission
     * @param s
     * @return — always returns true.
     * <p>
     * Examples:
     * Message.MSG_BROADCAST.broadcast ("pluginname.broadcast"); // send message to all players with permission "pluginname.broadcast"
     * Message.MSG_BROADCAST.broadcast (null); // send message to all players
     */
    public boolean broadcast(String permission, Object... s) {
        return messenger.broadcast(permission, getText(s));
    }


    /**
     * Get formated text.
     *
     * @param keys * Keys - are parameters for message and control-codes.
     *             Parameters will be shown in position in original message according for position.
     *             This keys are used in every method that prints or sends message.
     *             <p>
     *             Example:
     *             <p>
     *             EXAMPLE_MESSAGE ("Message with parameters: %1%, %2% and %3%");
     *             Message.EXAMPLE_MESSAGE.getText("one","two","three"); //will return text "Message with parameters: one, two and three"
     *             <p>
     *             * Color codes
     *             You can use two colors to define color of message, just use character symbol related for color.
     *             <p>
     *             Message.EXAMPLE_MESSAGE.getText("one","two","three",'c','4');  // this message will be red, but word one, two, three - dark red
     *             <p>
     *             * Control codes
     *             Control codes are text parameteres, that will be ignored and don't shown as ordinary parameter
     *             - "SKIPCOLOR" - use this to disable colorizing of parameters
     *             - "NOCOLOR" (or "NOCOLORS") - return uncolored text, clear all colors in text
     *             - "FULLFLOAT" - show full float number, by default it limit by two symbols after point (0.15 instead of 0.1483294829)
     * @return
     */
    public String getText(Object... keys) {
        char c2 = '2';
        char c1 = 'a';
        char[] colors = new char[]{color1 == null ? c1 : color1, color2 == null ? c2 : color2};
        if (keys.length == 0) {
            return colorize("&" + colors[0] + this.message);
        }
        String str = this.message;
        boolean noColors = false;
        boolean skipDefaultColors = false;
        boolean fullFloat = false;
        String prefix = "";
        int count = 1;
        int c = 0;
        DecimalFormat fmt = new DecimalFormat("####0.##");
        for (Object key : keys) {
            String s = messenger.toString(key, fullFloat);//keys[i].toString();
            if (c < 2 && key instanceof Character) {
                colors[c] = (Character) key;
                c++;
                continue;
            } else if (s.startsWith("prefix:")) {
                prefix = s.replace("prefix:", "");
                continue;
            } else if (s.equals("SKIPCOLOR")) {
                skipDefaultColors = true;
                continue;
            } else if (s.equals("NOCOLORS") || s.equals("NOCOLOR")) {
                noColors = true;
                continue;
            } else if (s.equals("FULLFLOAT")) {
                fullFloat = true;
                continue;
            } else if (key instanceof Double) {
                if (!fullFloat) s = fmt.format((Double) key);
            } else if (key instanceof Float) {
                if (!fullFloat) s = fmt.format((Float) key);
            }

            String from = (new StringBuilder("%").append(count).append("%")).toString();
            String to = skipDefaultColors ? s : (new StringBuilder("&").append(colors[1]).append(s).append("&").append(colors[0])).toString();
            str = str.replace(from, to);
            count++;
        }
        str = colorize(prefix.isEmpty() ? "&" + colors[0] + str : prefix + " " + "&" + colors[0] + str);
        if (noColors) str = clean(str);
        return str;
    }

    public static String clean(String str) {
        return messenger.clean(str);
    }

    private void initMessage(String message) {
        this.message = message;
    }

    private String message;
    private Character color1;
    private Character color2;

    M(String msg) {
        message = msg;
        this.color1 = null;
        this.color2 = null;
    }

    M(String msg, char color1, char color2) {
        this.message = msg;
        this.color1 = color1;
        this.color2 = color2;
    }

    M(String msg, char color) {
        this(msg, color, color);
    }

    @Override
    public String toString() {
        return this.getText("NOCOLOR");
    }

    /**
     * Initialize current class, load messages, etc.
     * Call this file in onEnable method after initializing plugin configuration
     */
    public static void init(String pluginName, Messenger mess, String lang, boolean debug, boolean save) {
        M.pluginName = pluginName;
        messenger = mess;
        language = lang;
        debugMode = debug;
        boolean saveLanguage = save;
        initMessages();
        if (saveLanguage) saveMessages();
        LNG_CONFIG.debug(M.values().length, language, true, debugMode);
    }

    /**
     * Enable debugMode
     *
     * @param debug
     */
    public static void setDebugMode(boolean debug) {
        debugMode = debug;
    }

    public static boolean isDebug() {
        return debugMode;
    }


    private static void initMessages() {
        Map<String, String> lng = messenger.load(language);
        for (M key : M.values()) {
            if (lng.containsKey(key.name().toLowerCase())) {
                key.initMessage(lng.get(key.name().toLowerCase()));
            }
        }
    }

    private static void saveMessages() {
        Map<String, String> messages = new LinkedHashMap<>();
        for (M msg : M.values()) {
            messages.put(msg.name().toLowerCase(), msg.message);
        }
        messenger.save(language, messages);
    }

    /**
     * Send message (formed using join method) to server log if debug mode is enabled
     *
     * @param s
     */
    public static boolean debugMessage(Object... s) {
        if (debugMode) messenger.log(clean(join(s)));
        return true;
    }

    /**
     * Join object array to string (separated by space)
     *
     * @param s
     */
    public static String join(Object... s) {
        StringBuilder sb = new StringBuilder();
        for (Object o : s) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(messenger.toString(o, false));
        }
        return sb.toString();
    }

    public static void printLines(Object sender, Collection<String> lines) {
        for (String l : lines) {
            messenger.print(sender, colorize(l));
        }
    }

    public static void printPage(Object sender, List<String> lines, M title, int pageNum, int linesPerPage) {
        printPage(sender, lines, title, pageNum, linesPerPage, false);
    }

    public static void printPage(Object sender, List<String> lines, M title, int pageNum, int linesPerPage, boolean showNum) {
        printPage(sender, lines, title, null, pageNum, linesPerPage, showNum);
    }

    public static void printPage(Object sender, List<String> lines, M title, M footer, int pageNum, int linesPerPage) {
        printPage(sender, lines, title, footer, pageNum, linesPerPage, false);

    }

    public static void printPage(Object sender, List<String> lines, M title, M footer, int pageNum, int linesPerPage, boolean showNum) {
        if (lines == null || lines.isEmpty()) return;
        List<String> page = new ArrayList<>();
        if (title != null) page.add(title.getText('e', '6', pluginName));

        int pageCount = lines.size() / linesPerPage + 1;
        if (pageCount * linesPerPage == lines.size()) pageCount = pageCount - 1;

        int num = pageNum <= pageCount ? pageNum : 1;

        for (int i = linesPerPage * (num - 1); i < Math.min(lines.size(), num * linesPerPage); i++) {
            page.add((showNum ? (i + 1) : "") + lines.get(i));
        }
        if (footer != null) page.add(footer.getText('e', 'e', num, pageCount));
        printLines(sender, page);
    }

    public static boolean logMessage(Object... s) {
        messenger.log(clean(join(s)));
        return true;
    }


    public static M getByName(String name) {
        for (M m : values()) {
            if (m.name().equalsIgnoreCase(name)) return m;
        }
        return null;
    }

    public static String enDis(boolean value) {
        return value ? M.ENABLED.toString() : M.DISABLED.toString();
    }

    public static boolean printMSG(Object sender, String key, Object... s) {
        M m = getByName(key.toUpperCase());
        if (m == null) {
            LNG_PRINT_FAIL_M.print(sender, key);
            return LNG_PRINT_FAIL_M.log(sender, key);
        } else {
            return m.print(sender, s);
        }
    }


    public static void logOnce(String key, Object... s) {
        if (onceLog.contains(key)) return;
        onceLog.add(key);
        M.logMessage(s);
    }

    public static void printMessage(Object sender, String message) {
        if (messenger.isValidSender(sender)) {
            messenger.print(sender, colorize(message));
        }
    }
}
