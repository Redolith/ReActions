package me.fromgate.reactions.commands;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.activators.Activator;
import me.fromgate.reactions.activators.Activators;
import me.fromgate.reactions.menu.InventoryMenu;
import me.fromgate.reactions.timer.Timers;
import me.fromgate.reactions.util.Locator;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CmdDefine(command = "react", description = "cmd_remove", permission = "reactions.config",
        subCommands = {"remove|rmv|del|delete"}, allowConsole = true, shortDescription = "&3/react remove [loc|activator] <id>")
public class CmdRemove extends Cmd {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 1) return false;
        String arg1 = args[1];
        String arg2 = args.length >= 3 ? args[2] : "";
        String arg3 = args.length >= 4 ? args[3] : "";
        if (args.length > 5) {
            for (int i = 4; i < args.length; i++)
                arg3 = arg3 + " " + args[i];
            arg3 = arg3.trim();
        }
        if (arg2.isEmpty()) return false;
        if (arg1.equalsIgnoreCase("act") || arg1.equalsIgnoreCase("activator")) {
            if (Activators.contains(arg2)) {
                Activators.removeActivator(arg2);
                ReActions.getUtil().printMSG(sender, "msg_removebok", arg2);
                Activators.saveActivators();
            } else ReActions.getUtil().printMSG(sender, "msg_removebnf", arg2);
        } else if (arg1.equalsIgnoreCase("loc")) {
            if (Locator.removeTpLoc(arg2)) {
                ReActions.getUtil().printMSG(sender, "msg_removelocok", arg2);
                Locator.saveLocs();
            } else ReActions.getUtil().printMSG(sender, "msg_removelocnf", arg2);
        } else if (arg1.equalsIgnoreCase("timer") || arg1.equalsIgnoreCase("tmr")) {
            Timers.removeTimer(sender, arg2);
        } else if (arg1.equalsIgnoreCase("var") || arg1.equalsIgnoreCase("variable") || arg1.equalsIgnoreCase("variables")) {
            removeVariable(sender, arg2 + (arg3.isEmpty() ? "" : " " + arg3));
        } else if (arg1.equalsIgnoreCase("menu") || arg1.equalsIgnoreCase("m")) {
            if (InventoryMenu.remove(arg2)) ReActions.getUtil().printMSG(sender, "msg_removemenu", arg2);
            else ReActions.getUtil().printMSG(sender, "msg_removemenufail", 'c', '4', arg2);
        } else if (Activators.contains(arg1)) {
            Activator act = Activators.get(arg1);
            if (ReActions.getUtil().isIntegerGZ(arg3)) {
                int num = Integer.parseInt(arg3);
                if (arg2.equalsIgnoreCase("f") || arg2.equalsIgnoreCase("flag")) {
                    if (act.removeFlag(num - 1))
                        ReActions.getUtil().printMSG(sender, "msg_flagremoved", act.getName(), num);
                    else ReActions.getUtil().printMSG(sender, "msg_failedtoremoveflag", act.getName(), num);
                } else if (arg2.equalsIgnoreCase("a") || arg2.equalsIgnoreCase("action")) {
                    if (act.removeAction(num - 1))
                        ReActions.getUtil().printMSG(sender, "msg_actionremoved", act.getName(), num);
                    else ReActions.getUtil().printMSG(sender, "msg_failedtoremoveaction", act.getName(), num);
                } else if (arg2.equalsIgnoreCase("r") || arg2.equalsIgnoreCase("reaction")) {
                    if (act.removeReaction(num - 1))
                        ReActions.getUtil().printMSG(sender, "msg_reactionremoved", act.getName(), num);
                    else ReActions.getUtil().printMSG(sender, "msg_failedtoremovereaction", act.getName(), num);
                } else return false;
                Activators.saveActivators();
            } else ReActions.getUtil().printMSG(sender, "msg_wrongnumber", arg3);
        }
        return true;
    }

    public boolean removeVariable(CommandSender sender, String param) {
        Player p = (sender instanceof Player) ? (Player) sender : null;
        Param params = new Param(param);
        String player = params.getParam("player", "");
        if (player.equalsIgnoreCase("%player%") && p != null) player = p.getName();
        String id = params.getParam("id", "");
        if (id.isEmpty()) return ReActions.getUtil().returnMSG(true, sender, "msg_varneedid");
        if (Variables.clearVar(player, id)) return ReActions.getUtil().returnMSG(true, sender, "msg_varremoved", id);
        return ReActions.getUtil().returnMSG(true, sender, "msg_varremovefail");
    }

}
