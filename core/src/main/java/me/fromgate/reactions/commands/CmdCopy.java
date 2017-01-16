package me.fromgate.reactions.commands;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.activators.Activators;
import org.bukkit.command.CommandSender;

@CmdDefine(command = "react", description = "cmd_copy", permission = "reactions.config",
        subCommands = {"copy"}, allowConsole = true, shortDescription = "&3/react copy [flag|actions|reactions] <source> <destination>")
public class CmdCopy extends Cmd {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length != 3 && args.length != 4) return false;
        String id1 = args.length == 4 ? args[2] : args[1];
        String id2 = args.length == 4 ? args[3] : args[2];
        String copyMode = args.length == 3 ? "all" : args[1];
        if (copyMode.equalsIgnoreCase("all")) {
            if (Activators.copyAll(id1, id2)) ReActions.getUtil().printMSG(sender, "msg_copyall", id1, id2);
            else ReActions.getUtil().printMSG(sender, "msg_copyallfailed", 'c', '4', id1, id2);
        } else if (copyMode.equalsIgnoreCase("f") || copyMode.equalsIgnoreCase("flag")) {
            if (Activators.copyFlags(id1, id2)) ReActions.getUtil().printMSG(sender, "msg_copyflags", id1, id2);
            else ReActions.getUtil().printMSG(sender, "msg_copyflagsfailed", 'c', '4', id1, id2);
        } else if (copyMode.equalsIgnoreCase("a") || copyMode.equalsIgnoreCase("actions")) {
            if (Activators.copyActions(id1, id2)) ReActions.getUtil().printMSG(sender, "msg_copyactions", id1, id2);
            else ReActions.getUtil().printMSG(sender, "msg_copyactionsfailed", 'c', '4', id1, id2);
        } else if (copyMode.equalsIgnoreCase("r") || copyMode.equalsIgnoreCase("me/fromgate/reactions")) {
            if (Activators.copyReactions(id1, id2)) ReActions.getUtil().printMSG(sender, "msg_copyreactions", id1, id2);
            else ReActions.getUtil().printMSG(sender, "msg_copyreactionsfailed", 'c', '4', id1, id2);
        }
        Activators.saveActivators();
        return true;
    }
}
