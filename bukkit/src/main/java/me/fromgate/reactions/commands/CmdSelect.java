package me.fromgate.reactions.commands;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.util.Locator;
import me.fromgate.reactions.util.Selector;
import org.bukkit.entity.Player;

@CmdDefine(command = "react", description = "cmd_select", permission = "reactions.select",
        subCommands = {"select|sel"}, allowConsole = false, shortDescription = "&3/react select")
public class CmdSelect extends Cmd {
    @Override
    public boolean execute(Player player, String[] args) {
        Selector.selectLocation(player, null);
        ReActions.getUtil().printMSG(player, "cmd_selected", Locator.locationToStringFormated(Selector.getSelectedLocation(player)));
        return true;
    }
}
