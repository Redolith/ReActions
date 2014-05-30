package me.fromgate.reactions.activators;

import java.util.Map;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.FactionEvent;
import me.fromgate.reactions.util.ParamUtil;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

public class FactionActivator extends Activator {
	
	private String newFaction;
	private String oldFaction;
	
	public FactionActivator(String name, String group, YamlConfiguration cfg){
        super (name, group, cfg);
    }
	public FactionActivator(String name, String param) {
		 super (name,"activators");
		 Map<String,String> params = ParamUtil.parseParams(param, "newfaction");
		 this.newFaction = ParamUtil.getParam(params, "newfaction", ParamUtil.getParam(params, "faction", "ANY"));
		 this.oldFaction = ParamUtil.getParam(params, "oldfaction", "ANY");
	}

	@Override
	public boolean activate(Event event) {
		if (!(event instanceof FactionEvent)) return false;
		FactionEvent fe = (FactionEvent) event;
		if (!(newFaction.isEmpty()||newFaction.equalsIgnoreCase("any")||fe.getNewFaction().equalsIgnoreCase(newFaction))) return false;
		if (!(oldFaction.isEmpty()||oldFaction.equalsIgnoreCase("any")||fe.getOldFaction().equalsIgnoreCase(oldFaction))) return false;
		return Actions.executeActivator(fe.getPlayer(), this);
	}

	@Override
	public boolean isLocatedAt(Location loc) {
		return false;
	}

	@Override
	public void save(String root, YamlConfiguration cfg) {
		cfg.set(root+".new-faction", newFaction.isEmpty() ? "ANY" : newFaction);
		cfg.set(root+".old-faction", oldFaction.isEmpty() ? "ANY" : oldFaction);
	}

	@Override
	public void load(String root, YamlConfiguration cfg) {
		this.newFaction = cfg.getString(root+".new-faction", "ANY");
		this.oldFaction = cfg.getString(root+".old-faction", "ANY");
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.FCT_CHANGE;
	}

}
