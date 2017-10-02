package me.fromgate.reactions.commands;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.activators.Activators;
import me.fromgate.reactions.menu.InventoryMenu;
import me.fromgate.reactions.timer.Timers;
import me.fromgate.reactions.util.Cfg;
import me.fromgate.reactions.util.Delayer;
import me.fromgate.reactions.util.FakeCmd;
import me.fromgate.reactions.util.Locator;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.message.M;
import org.bukkit.command.CommandSender;

@CmdDefine(command = "react", description = M.CMD_RELOAD, permission = "reactions.config",
        subCommands = {"reload"}, allowConsole = true, shortDescription = "&3/react reload")
public class CmdReload extends Cmd {

    @Override
    public boolean execute(CommandSender sender, String[] params) {
        Activators.clear();
        Activators.loadActivators();
        Locator.loadLocs();
        ReActions.getPlugin().reloadConfig();
        Cfg.load();
        Delayer.load();
        if (!Cfg.playerSelfVarFile) Variables.load();
        else Variables.loadVars();
        Timers.init();
        InventoryMenu.load();
        FakeCmd.updateAllCommands();
        M.MSG_CMDRELOAD.print(sender, Activators.size(), Locator.sizeTpLoc());
        return true;
    }

}
