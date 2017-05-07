package me.fromgate.reactions.commands;

import com.google.common.base.Joiner;
import me.fromgate.reactions.event.EventManager;
import me.fromgate.reactions.util.message.M;
import org.bukkit.command.CommandSender;

@CmdDefine(command = "react", description = M.CMD_RUN, permission = "reactions.run",
        subCommands = {"run"}, allowConsole = true, shortDescription = "&3/react run <ExecActivator> [TargetPlayer] [Delay]")
public class CmdRun extends Cmd {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        String param = Joiner.on(" ").join(args); //sb.toString();
        if (EventManager.raiseExecEvent(sender, param)) {
            M.CMD_RUNPLAYER.print(sender, param);
        } else {
            M.CMD_RUNPLAYERFAIL.print(sender, 'c', '6', param);
        }
        return true;
    }
}
