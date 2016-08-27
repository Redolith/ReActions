package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.CommandEvent;
import me.fromgate.reactions.util.FakeCmd;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

import java.util.HashSet;
import java.util.Set;

public class CommandActivator extends Activator {

    boolean checkExact;
    String command;
    Param arguments = new Param();
    boolean override;
    boolean useRegex;

    public void init() {
        Param params = new Param(command);
        if (params.isParamsExists("cmd")) {
            checkExact = true;
            arguments = params;
        }
        params.remove("param-line");
    }

    CommandActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
        init();
    }

    public CommandActivator(String name, String command, boolean override, boolean useRegex) {
        super(name, "activators");
        this.command = command;
        this.override = override;
        this.useRegex = useRegex;
        init();
    }


    private boolean checkLine(String line) {
        if (this.useRegex) return line.matches(command);
        return line.toLowerCase().startsWith(command.toLowerCase());
    }

    public boolean commandMatches(String line) {
        if (!this.checkExact) return checkLine(line);
        String[] cmdLn = line.replaceFirst("/", "").split(" ");
        if (cmdLn.length == 0) return false;
        Set<String> keys = new HashSet<String>();
        for (int i = 0; i < cmdLn.length; i++) {
            String key = (i == 0 ? "cmd" : "arg" + i);
            keys.add(key);
            if (!arguments.hasAnyParam(key)) return false;
            if (arguments.getParam(key).equalsIgnoreCase("*")) continue;
            if (!arguments.getParam(key).equalsIgnoreCase(cmdLn[i])) return false;
        }
        return arguments.keySet().equals(keys);
    }

    private void setTempVars(String command, String[] args) {
        String argStr = args.length > 1 ? command.replaceAll(new StringBuilder("^").append(args[0]).append(" ").toString(), "") : "";
        Variables.setTempVar("command", command);
        Variables.setTempVar("args", argStr);
        String argsLeft = command.replaceAll("(\\S+ )+{" + args.length + "}", "");
        Variables.setTempVar("argsleft", argsLeft);
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++)
                Variables.setTempVar(new StringBuilder("arg").append(i).toString(), args[i]);
        }

        int count = 0;
        while (argStr.indexOf(" ") > 0) {
            count++;
            argStr = argStr.substring(argStr.indexOf(" ") + 1);
            Variables.setTempVar("args" + count, argStr);
            Variables.setTempVar("argscount", Integer.toString(count + 1));
        }

    }

    @Override
    public boolean activate(Event event) {
        if (!(event instanceof CommandEvent)) return false;
        CommandEvent ce = (CommandEvent) event;
        if (ce.isParentCanceled() && !this.override) return false;
        if (!commandMatches(ce.getCommand())) return false;
        setTempVars(ce.getCommand(), ce.getArgs());
        if (!isCommandRegistered()) FakeCmd.registerNewCommand(ce.getCommand());
        return Actions.executeActivator(ce.getPlayer(), this);
    }

    public String getCommand() {
        if (this.checkExact) {
            return arguments.getParam("cmd", "");
        } else {
            String[] cmd = this.command.split(" ");
            if (cmd.length == 0) return "";
            return cmd[0];
        }
    }

    public boolean isCommandRegistered() {
        String command = getCommand();
        if (command.isEmpty()) return false;
        return isCommandRegistered(command);
    }


    public boolean isCommandRegistered(String cmd) {
        if (cmd.isEmpty()) return false;
        Command cmm = Bukkit.getServer().getPluginCommand(cmd);
        return (cmm != null);
    }

    @Override
    public boolean isLocatedAt(Location loc) {
        return false;
    }

    @Override
    public void save(String root, YamlConfiguration cfg) {
        cfg.set(root + ".override", this.override);
        cfg.set(root + ".regex", this.useRegex);
        cfg.set(root + ".command", command);
    }

    @Override
    public void load(String root, YamlConfiguration cfg) {
        this.override = cfg.getBoolean(root + ".override", true);
        this.useRegex = cfg.getBoolean(root + ".regex", false);
        this.command = cfg.getString(root + ".command");
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.COMMAND;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name).append(" [").append(getType()).append("]");
        if (!getFlags().isEmpty()) sb.append(" F:").append(getFlags().size());
        if (!getActions().isEmpty()) sb.append(" A:").append(getActions().size());
        if (!getReactions().isEmpty()) sb.append(" R:").append(getReactions().size());
        sb.append(" (override:").append(this.override);
        if (this.useRegex) sb.append(" regex:true");
        sb.append(" command:").append(this.command).append(")");
        return sb.toString();
    }

    public boolean useRegex() {
        return this.useRegex;
    }

}
