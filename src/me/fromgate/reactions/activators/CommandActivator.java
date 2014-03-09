package me.fromgate.reactions.activators;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.CommandEvent;
import me.fromgate.reactions.util.ParamUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

public class CommandActivator extends Activator {

	boolean checkExact=false;
	String command;
	Map<String,String> arguments = new HashMap<String,String>();


	public void init(){
		Map<String,String> params = ParamUtil.parseParams(command);
		if (ParamUtil.isParamExists(params, "cmd")){
			checkExact = true;
			arguments = params;
		}
		params.remove("param-line");
	}

	CommandActivator(String name, String group, YamlConfiguration cfg) {
		super(name, group, cfg);
		init();
	}

	public CommandActivator(String name, String command){
		super (name,"activators");
		this.command = command;
		init();
	}

	
	public boolean commandMaches(String line){
		if (!this.checkExact) return (line.toLowerCase().startsWith(command.toLowerCase()));
		String [] cmdLn = line.replaceFirst("/", "").split(" ");
		if (cmdLn.length==0) return false;
		Set<String> keys = new HashSet<String>();
		for (int i =0; i<cmdLn.length;i++) {
			String key = (i==0 ? "cmd" : "arg"+i);
			keys.add(key);
			if (!arguments.containsKey(key)) return false;
			if (arguments.get(key).equalsIgnoreCase("*")) continue;
			if (!arguments.get(key).equalsIgnoreCase(cmdLn[i])) return false;
		}
		return arguments.keySet().equals(keys);
	}
	
	
	
	@Override
	public boolean activate(Event event) {
		if (!(event instanceof CommandEvent)) return false;
		CommandEvent ce = (CommandEvent) event;
		if (!commandMaches(ce.getCommand())) return false;
		return Actions.executeActivator(ce.getPlayer(), this);
	}

	public String getCommand(){
		if (this.checkExact){
			return ParamUtil.getParam(arguments, "cmd", "");
		} else {
			String[] cmd = this.command.split(" ");
			if (cmd.length==0) return "";
			return cmd[0];
		}
	}
	
	public boolean isCommandRegistered(){
		String command = getCommand();
		if (command.isEmpty()) return false;
		return isCommandRegistered (command);
	}


	public boolean isCommandRegistered(String cmd){
		if (cmd.isEmpty()) return false;
		Command cmm  = Bukkit.getServer().getPluginCommand(cmd);
		return (cmm != null);
	}

	@Override
	public boolean isLocatedAt(Location loc) {
		return false;
	}

	@Override
	public void save(String root, YamlConfiguration cfg) {
		cfg.set(root+".command",command);
	}

	@Override
	public void load(String root, YamlConfiguration cfg) {
		command = cfg.getString(root+".command"); 
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.COMMAND;
	}


}
