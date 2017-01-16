package me.fromgate.reactions.commands;

import me.fromgate.reactions.ReActions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public abstract class Cmd {
    private String command;
    private String[] subCommands;
    private String permission;
    private boolean allowConsole;
    private String description;
    private String shortDescription;


    public Cmd() {
        if (this.getClass().isAnnotationPresent(CmdDefine.class)) {
            CmdDefine cd = this.getClass().getAnnotation(CmdDefine.class);
            this.command = cd.command();
            this.subCommands = cd.subCommands();
            this.permission = cd.permission();
            this.allowConsole = cd.allowConsole();
            this.description = cd.description();
            this.shortDescription = cd.shortDescription();
        }
    }

    public boolean canExecute(CommandSender sender) {
        Player player = (sender instanceof Player) ? (Player) sender : null;
        if (player == null) return this.allowConsole;
        if (this.permission == null || this.permission.isEmpty()) return true;
        return player.hasPermission(this.permission);
    }

    public boolean isValidCommand() {
        return (this.getCommand() != null && !this.getCommand().isEmpty());
    }

    public String getCommand() {
        return this.command;
    }

    public boolean checkParams(String[] params) {
        if (this.subCommands == null) return true;
        if (this.subCommands.length == 0) return true;
        if (params.length < this.subCommands.length) return false;
        for (int i = 0; i < this.subCommands.length; i++) {
            if (!params[i].matches(this.subCommands[i])) return false;
        }
        return true;
    }

    public boolean executeCommand(CommandSender sender, String[] params) {
        if (!canExecute(sender)) return false;
        if (!this.checkParams(params)) return false;
        if (this.allowConsole) return execute(sender, params);
        else return execute((Player) sender, params);
    }

    public boolean execute(Player player, String[] params) {
        Commander.getPlugin().getLogger().info("Command \"" + this.getCommand() + "\" executed but method \"public boolean execute(Player player, String [] params)\" was not overrided");
        return false;
    }

    public boolean execute(CommandSender sender, String[] params) {
        Commander.getPlugin().getLogger().info("Command \"" + this.getCommand() + "\" executed but method \"public boolean execute(CommandSender sender, String [] params)\" was not overrided");
        return false;
    }

    public String getFullDescription() {
        return ReActions.getUtil().getMSG(this.description, this.shortDescription);

    }


}
