package me.fromgate.reactions.commands;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.message.M;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Commander implements CommandExecutor {
    private static List<Cmd> commands = new ArrayList<Cmd>();
    private static JavaPlugin plugin;
    private static Commander commander;

    public static void init(JavaPlugin plg) {
        plugin = plg;
        commander = new Commander();
        addNewCommand(new CmdHelp());
        addNewCommand(new CmdRun());
        addNewCommand(new CmdAdd());
        addNewCommand(new CmdSet());
        addNewCommand(new CmdCopy());
        addNewCommand(new CmdList());
        addNewCommand(new CmdInfo());
        addNewCommand(new CmdGroup());
        addNewCommand(new CmdRemove());
        addNewCommand(new CmdClear());
        addNewCommand(new CmdSelect());
        addNewCommand(new CmdDebug());
        addNewCommand(new CmdCheck());
        addNewCommand(new CmdReload());
        addNewCommand(new CmdExec());
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static boolean addNewCommand(Cmd cmd) {
        if (cmd.getCommand() == null) return false;
        if (cmd.getCommand().isEmpty()) return false;
        plugin.getCommand(cmd.getCommand()).setExecutor(commander);
        commands.add(cmd);
        return true;
    }

    public static boolean isPluginYml(String cmdStr) {
        return plugin.getDescription().getCommands().containsKey(cmdStr);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String cmdLabel, String[] args) {
        for (Cmd cmd : commands) {
            if (!cmd.getCommand().equalsIgnoreCase(command.getLabel())) continue;
            if (cmd.executeCommand(sender, args)) return true;
        }
        return false;
    }

    public static void printHelp(CommandSender sender, int page) {
        List<String> helpList = new ArrayList<>();
        for (Cmd cmd : commands) {
            helpList.add(cmd.getFullDescription());
        }
        M.printMessage(sender, "&6&lReActions v" + ReActions.getPlugin().getDescription().getVersion() + " &r&6| " + M.HLP_HELP.getText("NO_COLOR"));
        Util.printPage(sender, helpList, null, page);
    }


}
