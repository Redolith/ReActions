package me.fromgate.reactions.commands;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.activators.*;
import me.fromgate.reactions.externals.RAWorldGuard;
import me.fromgate.reactions.flags.Flags;
import me.fromgate.reactions.menu.InventoryMenu;
import me.fromgate.reactions.timer.Timers;
import me.fromgate.reactions.util.FakeCmd;
import me.fromgate.reactions.util.Locator;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Util;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

@CmdDefine(command = "react", description = "cmd_add", permission = "reactions.config",
        subCommands = {"add"}, allowConsole = true,
        shortDescription = "&3/react add [<activator> <Id>|loc <Id>|<Id> f <flag> <param>|<Id> <a|r> <action> <param>")
public class CmdAdd extends Cmd {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 1) return false;
        Player player = (sender instanceof Player) ? (Player) sender : null;
        String arg1 = args[1];
        String arg2 = args.length >= 3 ? args[2] : "";
        String arg3 = args.length >= 4 ? args[3] : "";
        String arg4 = args.length >= 5 ? args[4] : "";
        if (args.length > 5) {
            for (int i = 5; i < args.length; i++)
                arg4 = arg4 + " " + args[i];
            arg4 = arg4.trim();
        }
        if (ActivatorType.isValid(arg1)) {
            @SuppressWarnings("deprecation")
            Block block = player != null ? player.getTargetBlock((Set<Material>) null, 100) : null;
            return addActivator(player, arg1, arg2, (arg3.isEmpty() ? "" : arg3) + (arg4.isEmpty() ? "" : " " + arg4), block);
        } else if (arg1.equalsIgnoreCase("loc")) {
            if (player == null) return false;
            if (!Locator.addTpLoc(arg2, player.getLocation())) return false;
            Locator.saveLocs();
            ReActions.getUtil().printMSG(sender, "cmd_addtpadded", arg2);
        } else if (arg1.equalsIgnoreCase("timer")) {
            Param params = Param.parseParams((arg3.isEmpty() ? "" : arg3) + (arg4.isEmpty() ? "" : " " + arg4));
            return Timers.addTimer(sender, arg2, params, true);
        } else if (arg1.equalsIgnoreCase("menu")) {
            // /react add menu id size sdjkf
            if (InventoryMenu.add(arg2, ReActions.getUtil().isInteger(arg3) ? Integer.parseInt(arg3) : 9, ((ReActions.getUtil().isInteger(arg3) ? "" : arg3 + " ") + (arg4.isEmpty() ? "" : arg4)).trim()))
                ReActions.getUtil().printMSG(sender, "cmd_addmenuadded", arg2);
            else ReActions.getUtil().printMSG(sender, "cmd_addmenuaddfail", arg2);
        } else if (Activators.contains(arg1)) {
            String param = Util.replaceStandartLocations(player, arg4); // используется в addActions
            if (arg2.equalsIgnoreCase("a") || arg2.equalsIgnoreCase("action")) {
                if (addAction(arg1, arg3, param)) {
                    Activators.saveActivators();
                    ReActions.getUtil().printMSG(sender, "cmd_actadded", arg3 + " (" + param + ")"); //TODO~
                    return true;
                } else ReActions.getUtil().printMSG(sender, "cmd_actnotadded", arg3 + " (" + param + ")");
            } else if (arg2.equalsIgnoreCase("r") || arg2.equalsIgnoreCase("reaction")) {
                if (addReAction(arg1, arg3, param)) {
                    Activators.saveActivators();
                    ReActions.getUtil().printMSG(sender, "cmd_reactadded", arg3 + " (" + param + ")");
                    return true;
                } else ReActions.getUtil().printMSG(sender, "cmd_reactnotadded", arg3 + " (" + param + ")");
            } else if (arg2.equalsIgnoreCase("f") || arg2.equalsIgnoreCase("flag")) {
                if (addFlag(arg1, arg3, param)) {
                    Activators.saveActivators();
                    ReActions.getUtil().printMSG(sender, "cmd_flagadded", arg3 + " (" + param + ")");
                    return true;
                } else ReActions.getUtil().printMSG(sender, "cmd_flagnotadded", arg3 + " (" + arg4 + ")");
            } else ReActions.getUtil().printMSG(sender, "cmd_unknownbutton", arg2);
        } else ReActions.getUtil().printMSG(sender, "cmd_unknownadd", 'c');
        return true;
    }

    public boolean addAction(String clicker, String flag, String param) {
        if (Actions.isValid(flag)) {
            Activators.addAction(clicker, flag, param);
            return true;
        }
        return false;
    }

    public boolean addReAction(String clicker, String flag, String param) {
        if (Actions.isValid(flag)) {
            Activators.addReaction(clicker, flag, param);
            return true;
        }
        return false;
    }

    public boolean addFlag(String clicker, String fl, String param) {
        String flag = fl.replaceFirst("!", "");
        boolean not = fl.startsWith("!");
        if (Flags.isValid(flag)) {
            Activators.addFlag(clicker, flag, param, not); // все эти проверки вынести в соответствующие классы
            return true;
        }
        return false;
    }

    private boolean addActivator(Player p, String type, String name, String param, Block b) {
        ActivatorType at = ActivatorType.getByName(type);
        if (at == null) return false;
        Activator activator = null;

        switch (at) {
            case BUTTON:
                if (b == null) return false;
                if ((b.getType() == Material.STONE_BUTTON) || (b.getType() == Material.WOOD_BUTTON)) {
                    activator = new ButtonActivator(name, b);
                } else ReActions.getUtil().printMSG(p, "cmd_addbreqbut");
                break;
            case COMMAND:
                Param cmdParam = new Param(param);
                String command = param;
                boolean override = true;
                boolean regex = false;
                if (cmdParam.isParamsExists("command")) {
                    command = cmdParam.getParam("command");
                    override = cmdParam.getParam("override", true);
                    regex = cmdParam.getParam("regex", false);
                }
                activator = new CommandActivator(name, command, override, regex);
                break;
            case MESSAGE:
                activator = new MessageActivator(name, param);
                break;
            case EXEC:
                activator = new ExecActivator(name);
                break;
            case PLATE:
                if (b == null) return false;
                if ((b.getType() == Material.STONE_PLATE) || (b.getType() == Material.WOOD_PLATE)) {
                    activator = new PlateActivator(name, b);
                } else ReActions.getUtil().printMSG(p, "cmd_addbreqbut");
                break;
            case PLAYER_RESPAWN:
                activator = new PlayerRespawnActivator(name, param);
                break;
            case PVP_KILL:
                activator = new PVPKillActivator(name);
                break;
            case PLAYER_DEATH:
                activator = new PlayerDeathActivator(name, param);
                break;
            case REGION:
                if (param.isEmpty()) ReActions.getUtil().printMSG(p, "msg_needregion", 'c');
                else activator = new RegionActivator(name, param);
                break;
            case REGION_ENTER:
                if (param.isEmpty()) ReActions.getUtil().printMSG(p, "msg_needregion", 'c');
                else activator = new RgEnterActivator(name, param);
                break;
            case REGION_LEAVE:
                if (param.isEmpty()) ReActions.getUtil().printMSG(p, "msg_needregion", 'c');
                else activator = new RgLeaveActivator(name, param);
                break;
            case LEVER:
                if (b == null) return false;
                if (b.getType() == Material.LEVER) {
                    activator = new LeverActivator(name, param, b);
                } else ReActions.getUtil().printMSG(p, "cmd_addbreqbut");
                break;
            case DOOR:
                if (b == null) return false;
                if (Util.isDoorBlock(b)) {
                    activator = new DoorActivator(name, param, Util.getDoorBottomBlock(b));
                } else ReActions.getUtil().printMSG(p, "cmd_addbreqbut");
                break;
            case JOIN:
                activator = new JoinActivator(name, param);
                break;
            case QUIT:
                activator = new QuitActivator(name);
                break;
            case MOB_CLICK:
                activator = new MobClickActivator(name, param.replace("%here%", Locator.locationToString(p.getLocation())));
                break;
            case MOB_KILL:
                activator = new MobKillActivator(name, param);
            case MOB_DAMAGE:
                activator = new MobDamageActivator(name, param);
                break;
            case ITEM_CLICK:
                if (!param.isEmpty()) activator = new ItemClickActivator(name, param);
                break;
            case ITEM_CONSUME:
                if (!param.isEmpty()) activator = new ItemConsumeActivator(name, param);
                break;
            case ITEM_HOLD:
                if (!param.isEmpty()) activator = new ItemHoldActivator(name, param);
                break;
            case ITEM_WEAR:
                if (!param.isEmpty()) activator = new ItemWearActivator(name, param);
                break;
            case FCT_CHANGE:
                activator = new FactionActivator(name, param);
                break;
            case FCT_RELATION:
                activator = new FactionRelationActivator(name, param);
                break;
            case FCT_CREATE:
                activator = new FactionCreateActivator(name, param);
                break;
            case FCT_DISBAND:
                activator = new FactionCreateActivator(name, param);
                break;
            case SIGN:
                Sign sign = null;
                if (b != null && (b.getType() == Material.SIGN_POST || b.getType() == Material.WALL_SIGN))
                    sign = (Sign) b.getState();
                if (sign != null) activator = new SignActivator(name, param, sign);
                else activator = new SignActivator(name, param);
                break;
            case VARIABLE:
                activator = new VariableActivator(name, param);
                break;

            default:
                break;
        }
        if (activator == null) return false;
        if (Activators.addActivator(activator)) {
            Activators.saveActivators();
            ReActions.getUtil().printMSG(p, "cmd_addbadded", activator.toString());
        } else ReActions.getUtil().printMSG(p, "cmd_notaddbadded", activator.toString());
        FakeCmd.updateAllCommands();
        RAWorldGuard.updateRegionCache();
        return true;
    }


}
