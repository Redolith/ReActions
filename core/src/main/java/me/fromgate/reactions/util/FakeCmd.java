package me.fromgate.reactions.util;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.activators.Activator;
import me.fromgate.reactions.activators.ActivatorType;
import me.fromgate.reactions.activators.Activators;
import me.fromgate.reactions.activators.CommandActivator;
import me.fromgate.reactions.util.message.M;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class FakeCmd implements CommandExecutor {

    private static FakeCmd fakeCmd;

    public static void init() {
        fakeCmd = new FakeCmd();
        updateAllCommands();
    }

    public static void updateAllCommands() {
        StringBuilder commands = new StringBuilder();
        for (Activator a : Activators.getActivators(ActivatorType.COMMAND)) {
            CommandActivator c = (CommandActivator) a;
            if (c.useRegex()) continue; // Пропускаем, эти команды будут регистрироваться в реальном времени
            if (!c.isCommandRegistered() && registerNewCommand(c.getCommand())) {
                if (commands.length() > 0) commands.append(", ");
                commands.append(c.getCommand());
            }
        }
        M.CMD_REGISTERED.log(commands.toString());
    }

    public static boolean registerNewCommand(String commandStr) {
        if (ReActions.getPlugin().getCommand(commandStr) != null) return false;
        PluginCommand cmd = findCommand(commandStr);
        if (cmd == null) return false;
        CommandMap commandMap = getCommandMap();
        if (commandMap == null) return false;
        commandMap.register(commandStr.toLowerCase(), cmd);
        try {
            ReActions.getPlugin().getCommand(commandStr).setExecutor(fakeCmd);
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    @Override
    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
        return true;
    }


    private static PluginCommand findCommand(String commandStr) {
        try {
            Constructor<PluginCommand> commandConstructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            commandConstructor.setAccessible(true);
            return commandConstructor.newInstance(commandStr, ReActions.getPlugin());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static CommandMap getCommandMap() {
        try {
            Field commandField = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
            commandField.setAccessible(true);
            return (CommandMap) commandField.get(Bukkit.getPluginManager());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
