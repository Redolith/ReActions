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

package me.fromgate.reactions.flags;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.activators.Activator;
import me.fromgate.reactions.flags.factions.FlagAtFactionZoneRel;
import me.fromgate.reactions.flags.factions.FlagFaction;
import me.fromgate.reactions.flags.factions.FlagIsFactionRelPlayerAround;
import me.fromgate.reactions.flags.factions.FlagPlayersInRel;
import me.fromgate.reactions.placeholders.Placeholders;
import me.fromgate.reactions.util.FlagVal;
import me.fromgate.reactions.util.RADebug;
import me.fromgate.reactions.util.Variables;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public enum Flags {
    GROUP("group", true, new FlagGroup()),
    PERM("perm", true, new FlagPerm()),
    TIME("time", false, new FlagTime()),
    ITEM("item", true, new FlagItem(0)),
    ITEM_INVENTORY("invitem", true, new FlagItem(1)),
    ITEM_WEAR("invwear", true, new FlagItem(2)),
    BLOCK("blockcheck", false, new FlagBlock()),
    TOWN("town", true, new FlagTown()),
    MONEY("money", false, new FlagMoney()),
    CHANCE("chance", false, new FlagChance()),
    PVP("pvp", true, new FlagPVP()),
    ONLINE("online", false, new FlagOnline()),
    DELAY("delay", false, new FlagDelay(true)),
    DELAY_PLAYER("pdelay", true, new FlagDelay(false)),
    STATE("pose", true, new FlagState()),
    REGION("region", true, new FlagRegion(0)),
    REGION_PLAYERS("rgplayer", false, new FlagRegion(1)),
    REGION_MEMBER("rgmember", false, new FlagRegion(2)),
    REGION_OWNER("rgowner", false, new FlagRegion(3)),
    GAMEMODE("gamemode", true, new FlagGameMode()),
    FOODLEVEL("food", true, new FlagFoodlevel()),
    XP("xp", true, new FlagXP()),
    LEVEL("level", true, new FlagLevel()),
    POWER("powered", false, new FlagPowered()),
    WORLD("world", true, new FlagWorld()),
    BIOME("biome", true, new FlagBiome()),
    LIGHT_LEVEL("light", true, new FlagLightLevel()),
    WALK_BLOCK("walk", true, new FlagWalkBlock()),
    DIRECTION("dir", true, new FlagDirection()),
    FLAG_SET("flagset", false, new FlagFlagSet()),
    EXECUTE_STOP("stopped", false, new FlagExecStop()),
    VAR_EXIST("varexist", false, new FlagVar(0, false)),
    VAR_PLAYER_EXIST("varpexist", true, new FlagVar(0, true)),
    VAR_COMPARE("varcmp", false, new FlagVar(1, false)),
    VAR_PLAYER_COMPARE("varpcmp", true, new FlagVar(1, true)),
    VAR_GREATER("vargrt", false, new FlagVar(2, false)),
    VAR_PLAYER_GREATER("varpgrt", true, new FlagVar(2, true)),
    VAR_LOWER("varlwr", false, new FlagVar(3, false)),
    VAR_PLAYER_LOWER("varplwr", true, new FlagVar(3, true)),
    VAR_MATCH("varmatch", false, new FlagVar(4, false)),
    VAR_PLAYER_MATCH("varpmatch", true, new FlagVar(4, true)),
    COMPARE("cmp", false, new FlagCompare()),
    RNC_RACE("rncrace", true, new FlagRacesAndClasses(true)),
    RNC_CLASS("rncclass", true, new FlagRacesAndClasses(false)),
    WEATHER("weather", true, new FlagWeather()),
    TIMER_ACTIVE("timeract", false, new FlagTimerActive()),
    FCT_PLAYER("playerfaction", false, new FlagFaction()),
    FCT_AT_ZONE_REL("atfactionzonerel", true, new FlagAtFactionZoneRel()),
    FCT_IS_REL_PLAYER_AROUND("isfactionrelplayeraround", true, new FlagIsFactionRelPlayerAround()),
    FCT_ARE_PLAYERS_IN_REL("areplayersinfactionsrel", false, new FlagPlayersInRel()),
    SQL_CHECK("sqlcheck", false, new FlagSQL(true)),
    SQL_RESULT("sqlhasresult", false, new FlagSQL(false));


    private String alias;
    private boolean require_player = true;
    private Flag flag;


    Flags(String alias, boolean needplayer, Flag flag) {
        this.alias = alias;
        this.require_player = needplayer;
        this.flag = flag;
    }

    public boolean check(Player player, String param) {
        if (this.require_player && (player == null)) return false;
        return flag.checkFlag(player, param);
    }


    public static boolean isValid(String name) {
        for (Flags ft : Flags.values()) {
            if (ft.name().equalsIgnoreCase(name)) return true;
            if (ft.getAlias().equalsIgnoreCase(name)) return true;
        }
        return false;
    }

    public static Flags getByName(String name) {
        for (Flags ft : Flags.values()) {
            if (ft.name().equalsIgnoreCase(name)) return ft;
            if (ft.getAlias().equalsIgnoreCase(name)) return ft;
        }
        return null;
    }

    public String getAlias() {
        return this.alias;
    }

    public static boolean checkFlag(Player p, String flag, String param, boolean not) {
        Flags ft = Flags.getByName(flag);
        if (ft == null) return false;
        boolean check = ft.check(p, param);
        if (not) return !check;
        return check;
    }

    public static boolean checkFlags(Player p, Activator c) {
        return RADebug.checkFlagAndDebug(p, checkAllFlags(p, c));
    }

    public static boolean checkAllFlags(Player p, Activator c) {
        if (c.getFlags().size() > 0)
            for (int i = 0; i < c.getFlags().size(); i++) {
                FlagVal f = c.getFlags().get(i);
                Variables.setTempVar(new StringBuilder(f.flag).append("_flag").toString().toUpperCase(), f.value);
                if (!checkFlag(p, f.flag, Placeholders.replacePlaceholders(p, f.value), f.not)) return false;
            }
        return true;
    }

    public static String getFtypes() {
        String str = "";
        for (Flags f : Flags.values()) {
            str = (str.isEmpty() ? f.name() : str + "," + f.name());
            str = (str.isEmpty() ? f.getAlias() : str + "," + f.getAlias());
        }
        return str;
    }

    public static String getValidName(String flag) {
        for (Flags f : Flags.values())
            if (f.getAlias().equalsIgnoreCase(flag)) return f.name();
        return flag;
    }

    public static void listFlags(CommandSender sender, int pageNum) {
        List<String> flagList = new ArrayList<String>();
        for (Flags flagType : Flags.values()) {
            String flagName = flagType.name();
            String alias = flagType.getAlias().equalsIgnoreCase(flagName) ? " " : " (" + flagType.getAlias() + ") ";
            String description = ReActions.util.getMSGnc("flag_" + flagName);
            flagList.add("&6" + flagName + "&e" + alias + "&3: &a" + description);
        }
        ReActions.util.printPage(sender, flagList, pageNum, "msg_flaglisttitle", "", false, sender instanceof Player ? 10 : 1000);
    }


}
