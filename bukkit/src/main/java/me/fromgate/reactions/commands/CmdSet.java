package me.fromgate.reactions.commands;

import com.google.common.base.Joiner;
import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.menu.InventoryMenu;
import me.fromgate.reactions.timer.Time;
import me.fromgate.reactions.util.Delayer;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;


@CmdDefine(command = "react", description = "cmd_set", permission = "reactions.config", subCommands = {"set"}, allowConsole = true, shortDescription = "&3/react set delay player:<player> delay:<time> id:<id>")
public class CmdSet extends Cmd {


    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 1) return false;
        String arg1 = args[1];
        String arg2 = args.length > 3 ? Joiner.on(" ").join(Arrays.copyOfRange(args, 2, args.length)) : "";
        return this.setVariable(sender, arg1, arg2);
    }

    private boolean setVariable(CommandSender sender, String var, String param) {
        Player p = (sender instanceof Player) ? (Player) sender : null;
        Param params = new Param(param, "id");
        String id = params.getParam("id", "");
        if (id.isEmpty()) return ReActions.getUtil().returnMSG(true, sender, "msg_needvdmid", 'c');
        if (var.equalsIgnoreCase("delay") || var.equalsIgnoreCase("d")) {
            boolean add = params.getParam("add", false);
            String player = params.getParam("player", "");
            if (player.equalsIgnoreCase("%player%") && (p != null)) player = p.getName();
            Long time = /*System.currentTimeMillis()+*/ReActions.getUtil().parseTime(params.getParam("delay", "3s")); //дефолтная задержка три секунды
            if (player.isEmpty()) Delayer.setDelay(id, time, add);
            else Delayer.setPersonalDelay(player, id, time, add);
            ReActions.getUtil().printMSG(sender, "cmd_delayset", player.isEmpty() ? id : player + "." + id, Time.fullTimeToString(System.currentTimeMillis() + time));
        } else if (var.equalsIgnoreCase("var") || var.equalsIgnoreCase("variable") || var.equalsIgnoreCase("v")) {
            String value = params.getParam("value", "");
            String player = params.getParam("player", "");
            Variables.setVar(player, id, value);
            return ReActions.getUtil().returnMSG(true, sender, "cmd_varset", player.isEmpty() ? id : player + "." + id, Variables.getVar(player, id, ""));
        } else if (var.equalsIgnoreCase("menu") || var.equalsIgnoreCase("m")) {
            if (InventoryMenu.set(id, params))
                return ReActions.getUtil().returnMSG(true, sender, "msg_menuparamset", id);
            else return ReActions.getUtil().returnMSG(true, sender, "msg_menusetfail", 'c', '4', id);
        } else return false;
        return true;
    }

}
