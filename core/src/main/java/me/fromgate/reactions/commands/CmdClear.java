package me.fromgate.reactions.commands;

import me.fromgate.reactions.activators.Activators;
import me.fromgate.reactions.util.message.M;
import org.bukkit.command.CommandSender;

@CmdDefine(command = "react", description = M.CMD_CLEAR, permission = "reactions.config",
        subCommands = {"clear"}, allowConsole = true, shortDescription = "&3/react clear <id> [f|a|r]")
public class CmdClear extends Cmd {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        String activatorId = args.length >= 2 ? args[1] : "";
        if (activatorId.isEmpty()) return false;
        String arg2 = args.length >= 3 ? args[2] : "";
        if (Activators.contains(activatorId)) {
            if (arg2.equalsIgnoreCase("f") || arg2.equalsIgnoreCase("flag")) {
                Activators.clearFlags(activatorId);
                Activators.saveActivators();
                M.MSG_CLEARFLAG.print(sender, activatorId);
            } else if (arg2.equalsIgnoreCase("a") || arg2.equalsIgnoreCase("action")) {
                Activators.clearActions(activatorId);
                M.MSG_CLEARACT.print(sender, activatorId);
                Activators.saveActivators();
            } else if (arg2.equalsIgnoreCase("r") || arg2.equalsIgnoreCase("reaction")) {
                Activators.clearReactions(activatorId);
                M.MSG_CLEARREACT.print(sender, activatorId);
                Activators.saveActivators();
            }
            Activators.saveActivators();
        } else return M.CMD_UNKNOWNBUTTON.print(sender, activatorId);
        return false;
    }
}
