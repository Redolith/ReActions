package me.fromgate.reactions.commands;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.activators.Activator;
import me.fromgate.reactions.activators.ActivatorType;
import me.fromgate.reactions.activators.Activators;
import me.fromgate.reactions.externals.RaWorldGuard;
import me.fromgate.reactions.flags.Flags;
import me.fromgate.reactions.menu.InventoryMenu;
import me.fromgate.reactions.timer.Timers;
import me.fromgate.reactions.util.FakeCmd;
import me.fromgate.reactions.util.Locator;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.message.M;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CmdDefine(command = "react", description = M.CMD_ADD, permission = "reactions.config",
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
        StringBuilder arg4 = new StringBuilder(args.length >= 5 ? args[4] : "");
        if (args.length > 5) {
            for (int i = 5; i < args.length; i++)
                arg4.append(" ").append(args[i]);
            arg4 = new StringBuilder(arg4.toString().trim());
        }
        if (ActivatorType.isValid(arg1)) {
            @SuppressWarnings("deprecation")
            Block block = player != null ? player.getTargetBlock(null, 100) : null;
            return addActivator(sender, player, arg1, arg2, (arg3.isEmpty() ? "" : arg3) + ((arg4.length() == 0) ? "" : " " + arg4), block);
        } else if (arg1.equalsIgnoreCase("loc")) {
            if (player == null) return false;
            if (!Locator.addTpLoc(arg2, player.getLocation())) return false;
            Locator.saveLocs();
            M.CMD_ADDTPADDED.print(sender, arg2);
        } else if (arg1.equalsIgnoreCase("timer")) {
            Param params = Param.parseParams((arg3.isEmpty() ? "" : arg3) + ((arg4.length() == 0) ? "" : " " + arg4));
            return Timers.addTimer(sender, arg2, params, true);
        } else if (arg1.equalsIgnoreCase("menu")) {
            // /react add menu id size sdjkf
            if (InventoryMenu.add(arg2, Util.isInteger(arg3) ? Integer.parseInt(arg3) : 9, ((Util.isInteger(arg3) ? "" : arg3 + " ") + ((arg4.length() == 0) ? "" : arg4.toString())).trim())) {
                M.CMD_ADDMENUADDED.print(sender, arg2);
            } else {
                M.CMD_ADDMENUADDFAIL.print(sender, arg2);
            }
        } else if (Activators.contains(arg1)) {
            String param = Util.replaceStandartLocations(player, arg4.toString()); // используется в addActions
            if (arg2.equalsIgnoreCase("a") || arg2.equalsIgnoreCase("action")) {
                if (addAction(arg1, arg3, param)) {
                    Activators.saveActivators();
                    M.CMD_ACTADDED.print(sender, arg3 + " (" + param + ")"); //TODO~
                    return true;
                } else {
                    M.CMD_ACTNOTADDED.print(sender, arg3 + " (" + param + ")");
                }
            } else if (arg2.equalsIgnoreCase("r") || arg2.equalsIgnoreCase("reaction")) {
                if (addReAction(arg1, arg3, param)) {
                    Activators.saveActivators();
                    return M.CMD_REACTADDED.print(sender, arg3 + " (" + param + ")");
                } else {
                    M.CMD_REACTADDED.print(sender, arg3 + " (" + param + ")");
                }
            } else if (arg2.equalsIgnoreCase("f") || arg2.equalsIgnoreCase("flag")) {
                if (addFlag(arg1, arg3, param)) {
                    Activators.saveActivators();
                    return M.CMD_FLAGADDED.print(sender, arg3 + " (" + param + ")");
                } else {
                    M.CMD_FLAGNOTADDED.print(sender, arg3 + " (" + arg4 + ")");
                }
            } else {
                M.CMD_UNKNOWNBUTTON.print(sender, arg2);
            }
        } else {
            M.CMD_UNKNOWNADD.print(sender, 'c');
        }
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
            M.CMD_NOTADDBADDEDSYNTAX.print(sender, name, type);
            return true;
        }
        if (Activators.addActivator(activator)) {
            Activators.saveActivators();
            M.CMD_ADDBADDED.print(sender, activator.toString());
        } else {
            M.CMD_NOTADDBADDED.print(sender, activator.toString());
        }
        FakeCmd.updateAllCommands();
        RaWorldGuard.updateRegionCache();
        return true;
    }


}
