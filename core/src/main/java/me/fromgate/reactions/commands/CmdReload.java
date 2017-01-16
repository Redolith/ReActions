package me.fromgate.reactions.commands;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.activators.Activators;
import me.fromgate.reactions.menu.InventoryMenu;
import me.fromgate.reactions.timer.Timers;
import me.fromgate.reactions.util.Delayer;
import me.fromgate.reactions.util.FakeCmd;
import me.fromgate.reactions.util.Locator;
import me.fromgate.reactions.util.Variables;
import org.bukkit.command.CommandSender;

@CmdDefine(command = "react", description = "cmd_reload", permission = "reactions.config",
        subCommands = {"reload"}, allowConsole = true, shortDescription = "&3/react reload")
public class CmdReload extends Cmd {

    @Override
    public boolean execute(CommandSender sender, String[] params) {
        Activators.clear();
        Activators.loadActivators();
        Locator.loadLocs();
        ReActions.getPlugin().reloadConfig();
        ReActions.getPlugin().loadCfg();
        Delayer.load();
        Variables.load();
        Timers.init();
        InventoryMenu.load();
        FakeCmd.updateAllCommands();
        ReActions.getUtil().printMSG(sender, "msg_cmdreload", Activators.size(), Locator.sizeTpLoc());
        return true;
    }

}
