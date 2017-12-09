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

package me.fromgate.reactions.actions;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.actions.ActionItems.ItemActionType;
import me.fromgate.reactions.activators.Activator;
import me.fromgate.reactions.flags.Flags;
import me.fromgate.reactions.placeholders.Placeholders;
import me.fromgate.reactions.util.ActVal;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.message.M;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


public enum Actions {
    //"tp,velocity,sound,potion,rmvpot,grpadd,grprmv,msg,dmg,townset,townkick,itemrmv,invitemrmv,itemgive,cmdplr,cmdop,cmdsrv,moneypay,moneygive,delay,pdelay,back,mob,effect,run,rgclear";
    TP("tp", true, new ActionTp()),
    VELOCITY("velocity", true, new ActionVelocity()),
    VELOCITY_JUMP("jump", true, new ActionVelocityJump()),
    SOUND("sound", false, new ActionSound()),
    POTION("potion", true, new ActionPlayerPotion()),
    POTION_REMOVE("rmvpot", true, new ActionPlayerPotionRemove()),
    GROUP_ADD("grpadd", true, new ActionGroupAdd()),
    GROUP_REMOVE("grprmv", true, new ActionGroupRemove()),
    MESSAGE("msg", false, new ActionMessage()),
    CHAT_MESSAGE("chatmsg", false, new ActionChatMessage()),
    BROADCAST("msgall", false, new ActionBroadcast()),
    DAMAGE("dmg", false, new ActionDamage()),
    TOWN_SET("townset", true, new ActionTownSet()),
    TOWN_KICK("townkick", true, new ActionTownKick()),
    ITEM_GIVE("itemgive", true, new ActionItems(ItemActionType.GIVE_ITEM)),
    ITEM_REMOVE("itemrmv", true, new ActionItems(ItemActionType.REMOVE_ITEM_HAND)),
    ITEM_REMOVE_OFFHAND("itemrmvoffhand", true, new ActionItems(ItemActionType.REMOVE_ITEM_OFFHAND)),
    ITEM_REMOVE_INVENTORY("invitemrmv", true, new ActionItems(ItemActionType.REMOVE_ITEM_INVENTORY)),
    ITEM_DROP("itemdrop", true, new ActionItems(ItemActionType.DROP_ITEM)),
    ITEM_WEAR("itemwear", true, new ActionItems(ItemActionType.WEAR_ITEM)),
    ITEM_UNWEAR("itemundress", true, new ActionItems(ItemActionType.UNWEAR_ITEM)),
    ITEM_SLOT("itemslot", true, new ActionItems(ItemActionType.SET_INVENTORY)),
    ITEM_SLOT_VIEW("itemslotview", true, new ActionItems(ItemActionType.GET_INVENTORY)),
    CMD("cmdplr", true, new ActionCommand(ActionCommand.NORMAL)),
    CMD_OP("cmdop", false, new ActionCommand(ActionCommand.OP)),
    CMD_CONSOLE("cmdsrv", false, new ActionCommand(ActionCommand.CONSOLE)),
    CMD_CHAT("cmdchat", false, new ActionCommand(ActionCommand.CHAT)),
    MONEY_PAY("moneypay", false, new ActionMoneyPay()),
    MONEY_GIVE("moneygive", false, new ActionMoneyGive()),
    DELAY("delay", false, new ActionDelay(true)),
    DELAY_PLAYER("pdelay", true, new ActionDelay(false)),
    BACK("back", true, new ActionBack()),
    MOB_SPAWN("mob", false, new ActionMobSpawn()),
    EFFECT("effect", false, new ActionEffect()),
    EXECUTE("run", false, new ActionExecute()),  /// ???? не уверен
    EXECUTE_STOP("stop", false, new ActionExecStop()),  /// ???? не уверен
    EXECUTE_UNSTOP("unstop", false, new ActionExecUnstop()),  /// ???? не уверен
    REGION_CLEAR("rgclear", false, new ActionClearRegion()),
    HEAL("heal", false, new ActionHeal()),
    BLOCK_SET("block", false, new ActionBlockSet()),
    BLOCK_FILL("blockfill", false, new ActionBlockFill()),
    SIGN_SET_LINE("sign", false, new ActionSignSet()),
    POWER_SET("power", false, new ActionPowerSet()),
    SHOOT("shoot", true, new ActionShoot()),
    VAR_SET("varset", false, new ActionVar(0, false)),
    VAR_PLAYER_SET("varpset", true, new ActionVar(0, true)),
    VAR_CLEAR("varclr", false, new ActionVar(1, false)),
    VAR_PLAYER_CLEAR("varpclr", true, new ActionVar(1, true)),
    VAR_INC("varinc", false, new ActionVar(2, false)),
    VAR_PLAYER_INC("varpinc", true, new ActionVar(2, true)),
    VAR_DEC("vardec", false, new ActionVar(3, false)),
    VAR_PLAYER_DEC("varpdec", true, new ActionVar(3, true)),
    VAR_TEMP_SET("vartempset", false, new ActionVar(4, false)),
    RNC_SET_RACE("setrace", true, new ActionRacesAndClasses(true)),
    RNC_SET_CLASS("setclass", true, new ActionRacesAndClasses(false)),
    TIMER_STOP("timerstop", false, new ActionTimer(true)),
    TIMER_RESUME("timerresume", false, new ActionTimer(false)),
    CANCEL_EVENT("cancel", false, new ActionCancelEvent()),
    SQL_SELECT("sqlselect", false, new ActionSql(0)),
    SQL_UPDATE("sqlupdate", false, new ActionSql(2)),
    SQL_INSERT("sqlinsert", false, new ActionSql(1)),
    SQL_DELETE("sqldelete", false, new ActionSql(3)),
    SQL_SET("sqlset", false, new ActionSql(4)),
    REGEX("regex", false, new ActionRegex()),
    ACTION_DELAYED("actdelay", false, new ActionDelayed()),
    MENU_ITEM("itemmenu", true, new ActionMenuItem()),
    FCT_POWER_ADD("factaddpower", false, new ActionFactionsPowerAdd()),
    WAIT("wait", false, new ActionWait()),
    LOG("log", false, new ActionLog()),
    PLAYER_ID("playerid", false, new ActionPlayerId()),
    FILE("file", false, new ActionFile()),
    FLY("fly", false, new ActionFly()),
    GLIDE("glide", false, new ActionGlide()),
    WALK_SPEED("walkspeed", false, new ActionWalkSpeed()),
    FLY_SPEED("flyspeed", false, new ActionFlySpeed()),
    IF_ELSE("ifelse", false, new ActionIfElse()),
    WE_TOOLCONTROL("wetoolcontrol", true, new ActionWeToolControl()),
    WE_SUPERPICKAXE("wesuperpickaxe", true, new ActionWeSuperPickaxe()),
    RADIUS_CLEAR("clearradius", true, new ActionClearRadius());

    private String alias;
    private boolean requireplayer;
    private Action action;

    Actions(String alias, boolean requireplayer, Action action) {
        this.alias = alias;
        this.requireplayer = requireplayer;
        this.action = action;
        this.action.init(this);
    }

    static ReActions plg() {
        return ReActions.instance;
    }

    public String getAlias() {
        return this.alias;
    }


    public static Actions getByName(String name) {
        for (Actions at : Actions.values())
            if (at.name().equalsIgnoreCase(name)
                    || at.getAlias().equalsIgnoreCase(name)) return at;
        return null;
    }

    public static String getValidName(String name) {
        for (Actions at : Actions.values())
            if (at.getAlias().equalsIgnoreCase(name)) return at.name();
        return name;
    }

    public static boolean executeActivator(Player player, Activator act) {
        boolean isAction = Flags.checkFlags(player, act);
        List<ActVal> actions = isAction ? act.getActions() : act.getReactions();
        if (actions.isEmpty()) return false;
        return executeActions(player, actions, isAction);
    }

    public static boolean executeActions(Player player, List<ActVal> actions, boolean isAction) {
        boolean cancelParentEvent = false;
        if (actions == null || actions.isEmpty()) return false;
        for (int i = 0; i < actions.size(); i++) {
            ActVal av = actions.get(i);
            if (!Actions.isValid(av.flag)) continue;
            Actions at = Actions.getByName(av.flag);
            if (at == Actions.WAIT) {
                if (i == actions.size() - 1) continue;
                ActionWait aw = (ActionWait) at.action;
                Param param = new Param(Placeholders.replacePlaceholderButRaw(player, av.value), "time");
                String timeStr = param.getParam("time", "0");
                long time = Util.parseTime(timeStr);
                if (time == 0) continue;
                List<ActVal> futureList = new ArrayList<>();
                futureList.addAll(actions.subList(i + 1, actions.size()));
                aw.executeDelayed(player, futureList, isAction, time);
                return cancelParentEvent;
            }
            if (at != null && at.performAction(player, isAction, new Param(Placeholders.replacePlaceholderButRaw(player, av.value)))) {
                cancelParentEvent = true;
            }
        }
        return cancelParentEvent;
    }

    public boolean performAction(Player p, /*Activator a,*/ boolean action, Param actionParam) {
        if ((p == null) && this.requireplayer) return false;
        return this.action.executeAction(p,/* a,*/ action, actionParam);
    }

    public static boolean isValid(String name) {
        for (Actions at : Actions.values()) {
            if (at.name().equalsIgnoreCase(name)) return true;
            if (at.getAlias().equalsIgnoreCase(name)) return true;
        }
        return false;
    }

    public static void listActions(CommandSender sender, int pageNum) {
        List<String> actionList = new ArrayList<>();
        for (Actions actionType : Actions.values()) {
            String name = actionType.name();
            String alias = actionType.getAlias().equalsIgnoreCase(name) ? " " : " (" + actionType.getAlias() + ") ";
            M msg = M.getByName("action_" + name);
            if (msg == null) {
                M.LNG_FAIL_ACTION_DESC.log(name);
            } else {
                actionList.add("&6" + name + "&e" + alias + "&3: &a" + msg.getText("NOCOLOR"));
            }
        }
        Util.printPage(sender, actionList, M.MSG_ACTIONLISTTITLE, pageNum);
    }


    public Player getNearestPlayer(Player player) {
        Player nearest = null;
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!player.getWorld().equals(onlinePlayer.getWorld())) continue; // Check only players in player's world
            if ((nearest != null) &&
                    (player.getLocation().distance(nearest.getLocation()) < player.getLocation().distance(onlinePlayer.getLocation())))
                continue;
            nearest = onlinePlayer;
        }
        return nearest;
    }
}
