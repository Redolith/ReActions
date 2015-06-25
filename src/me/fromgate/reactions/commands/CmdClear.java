package me.fromgate.reactions.commands;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.activators.Activators;

import org.bukkit.command.CommandSender;

@CmdDefine(command = "react", description = "cmd_clear", permission = "reactions.config", 
subCommands = { "clear" }, allowConsole=true, shortDescription = "&3/react clear <id> [f|a|r]")
public class CmdClear extends Cmd {
	@Override
	public boolean execute(CommandSender sender, String [] args){
		String arg1 = args.length >=2 ? args[1] : "";
		if (arg1.isEmpty()) return false;
		String arg2 = args.length >=3 ? args[2] : "";
		if (Activators.contains(arg1)){
			if (arg2.equalsIgnoreCase("f")||arg2.equalsIgnoreCase("flag")) {
				Activators.clearFlags(arg1);
				Activators.saveActivators();
				ReActions.getUtil().printMSG(sender, "msg_clearflag", arg1);
			} else if (arg2.equalsIgnoreCase("a")||arg2.equalsIgnoreCase("action")) {
				Activators.clearActions(arg1);
				ReActions.getUtil().printMSG(sender, "msg_clearact", arg1);
				Activators.saveActivators();
			} else if (arg2.equalsIgnoreCase("r")||arg2.equalsIgnoreCase("reaction")) {
				Activators.clearReactions(arg1);
				ReActions.getUtil().printMSG(sender, "msg_clearreact", arg1);
				Activators.saveActivators();
			}
			Activators.saveActivators();
		} else return ReActions.getUtil().returnMSG(true,sender, "cmd_unknownbutton",arg1);
		return false;
	}
}
