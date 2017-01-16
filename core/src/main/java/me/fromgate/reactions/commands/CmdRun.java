package me.fromgate.reactions.commands;

import com.google.common.base.Joiner;
import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.event.EventManager;
import org.bukkit.command.CommandSender;

@CmdDefine(command = "react", description = "cmd_run", permission = "reactions.run",
        subCommands = {"run"}, allowConsole = true, shortDescription = "&3/react run <ExecActivator> [TargetPlayer] [Delay]")
public class CmdRun extends Cmd {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        String param = Joiner.on(" ").join(args); //sb.toString();
        if (EventManager.raiseExecEvent(sender, param)) ReActions.getUtil().printMSG(sender, "cmd_runplayer", param);
        else ReActions.getUtil().printMSG(sender, "cmd_runplayerfail", 'c', '6', param);
        return true;
    }
}
