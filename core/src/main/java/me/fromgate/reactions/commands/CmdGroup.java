package me.fromgate.reactions.commands;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.activators.Activators;
import org.bukkit.command.CommandSender;

@CmdDefine(command = "react", description = "cmd_group", permission = "reactions.config",
        subCommands = {"group"}, allowConsole = true, shortDescription = "&3/react group <activator> <groupname>")
public class CmdGroup extends Cmd {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        String id = args.length > 1 ? args[1] : "";
        if (id.isEmpty()) return false;
        String group = args.length > 2 ? args[2] : "activators";
        if (Activators.setGroup(id, group)) {
            Activators.saveActivators();
            ReActions.getUtil().printMSG(sender, "msg_groupset", id, group);
        } else ReActions.getUtil().printMSG(sender, "msg_groupsetfailed", id, group);
        return true;
    }
}
