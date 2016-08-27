package me.fromgate.reactions.commands;

import me.fromgate.reactions.ReActions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.ChatPaginator;
import org.bukkit.util.ChatPaginator.ChatPage;
import org.bukkit.util.Java15Compat;

import java.util.ArrayList;
import java.util.Arrays;
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
        List<String> helpList = new ArrayList<String>();
        for (Cmd cmd : commands) {
            helpList.add(cmd.getFullDescription());
        }
        int pageHeight = (sender instanceof Player) ? 9 : 1000;

        ReActions.getUtil().printMsg(sender, "&6&lReActions v" + ReActions.getPlugin().getDescription().getVersion() + " &r&6| " + ReActions.getUtil().getMSG("hlp_help", '6'));
        ChatPage chatPage = paginate(helpList, page, ReActions.getPlugin().getChatLineLength(), pageHeight);

        for (String str : chatPage.getLines())
            sender.sendMessage(str);

        if (pageHeight == 9)
            ReActions.getUtil().printMSG(sender, "lst_footer", 'e', '6', chatPage.getPageNumber(), chatPage.getTotalPages());
    }

    public static ChatPage paginate(List<String> unpaginatedStrings, int pageNumber, int lineLength, int pageHeight) {
        List<String> lines = new ArrayList<String>();
        for (String str : unpaginatedStrings) {
            lines.addAll(Arrays.asList(ChatPaginator.wordWrap(str, lineLength)));
        }
        int totalPages = lines.size() / pageHeight + (lines.size() % pageHeight == 0 ? 0 : 1);
        int actualPageNumber = pageNumber <= totalPages ? pageNumber : totalPages;
        int from = (actualPageNumber - 1) * pageHeight;
        int to = from + pageHeight <= lines.size() ? from + pageHeight : lines.size();
        String[] selectedLines = (String[]) Java15Compat.Arrays_copyOfRange((String[]) lines.toArray(new String[lines.size()]), from, to);
        return new ChatPage(selectedLines, actualPageNumber, totalPages);
    }

}
