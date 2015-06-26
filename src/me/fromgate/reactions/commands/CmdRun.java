package me.fromgate.reactions.commands;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.event.EventManager;

import org.bukkit.command.CommandSender;

@CmdDefine(command = "react", description = "cmd_run", permission = "reactions.run",
subCommands = { "run" }, allowConsole=true, shortDescription = "&3/react run <exec-activator> [target player] [delay]")
public class CmdRun extends Cmd {
	@Override
	public boolean execute(CommandSender sender, String [] args){
		StringBuilder sb = new StringBuilder();
		for (String s : args){
			if (sb.length()>0) sb.append(" ");
			sb.append(s);
		}
		String param = sb.toString();
		if (EventManager.raiseExecEvent(sender, param)) ReActions.getUtil().printMSG(sender, "cmd_runplayer",param);
		else ReActions.getUtil().printMSG(sender, "cmd_runplayerfail",'c','6',param);
		return true;
	}
}
