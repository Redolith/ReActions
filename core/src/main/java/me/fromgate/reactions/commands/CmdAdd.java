package me.fromgate.reactions.commands;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.activators.Activator;
import me.fromgate.reactions.activators.ActivatorType;
import me.fromgate.reactions.activators.Activators;
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
            return addActivator(sender, player, arg1, arg2, (arg3.isEmpty() ? "" : arg3) + (arg4.isEmpty() ? "" : " " + arg4), block);
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

    private boolean addActivator(CommandSender sender, Player player, String type, String name, String param, Block targetBlock) {
        ActivatorType at = ActivatorType.getByName(type);
        if (at == null) return false;
        Activator activator = at.create(name, targetBlock, param);
        if (activator == null || !activator.isValid()) {
            ReActions.getUtil().printMSG(sender, "cmd_notaddbaddedsyntax", name, type);
            return true;
        }
        if (Activators.addActivator(activator)) {
            Activators.saveActivators();
            ReActions.getUtil().printMSG(sender, "cmd_addbadded", activator.toString());
        } else {
            ReActions.getUtil().printMSG(sender, "cmd_notaddbadded", activator.toString());
        }
        FakeCmd.updateAllCommands();
        RAWorldGuard.updateRegionCache();
        return true;
    }


}
