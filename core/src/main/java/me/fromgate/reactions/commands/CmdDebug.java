package me.fromgate.reactions.commands;

import me.fromgate.reactions.util.RADebug;
import me.fromgate.reactions.util.message.M;
import org.bukkit.entity.Player;

@CmdDefine(command = "react", description = M.CMD_DEBUG, permission = "reactions.debug",
        subCommands = {"debug", "off|true|false"}, allowConsole = false, shortDescription = "&3/react debug [true|false|off]")
public class CmdDebug extends Cmd {
    @Override
    public boolean execute(Player player, String[] args) {
        String arg = args.length >= 2 ? args[1] : "off";
        if (arg.equalsIgnoreCase("false")) {
            RADebug.setPlayerDebug(player, false);
            M.printMSG(player, "cmd_debugfalse");
        } else if (arg.equalsIgnoreCase("true")) {
            RADebug.setPlayerDebug(player, true);
            M.printMSG(player, "cmd_debugtrue");
        } else {
            RADebug.offPlayerDebug(player);
            M.printMSG(player, "cmd_debugoff");
        }
        return true;
    }
}
