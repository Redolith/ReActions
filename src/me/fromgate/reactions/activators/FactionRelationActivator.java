package me.fromgate.reactions.activators;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import me.fromgate.reactions.event.FactionRelationEvent;
import me.fromgate.reactions.util.ParamUtil;
import me.fromgate.reactions.util.Variables;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;
import me.fromgate.reactions.actions.Actions;


public class FactionRelationActivator extends Activator {

	private Set<String> factions;
	private String oldRelation;
	private String newRelation;

	public FactionRelationActivator(String name, String group, YamlConfiguration cfg){
        super (name, group, cfg);
    }
	
	public FactionRelationActivator(String name, String param) {
		super(name, "activators");
		this.factions = new HashSet<String>();
		Map<String,String> params = ParamUtil.parseParams(param, "newrelation");
		this.factions.add(ParamUtil.getParam(params, "faction", ParamUtil.getParam(params, "faction1", "ANY")).toUpperCase());
		this.factions.add(ParamUtil.getParam(params, "faction2", ParamUtil.getParam(params, "otherfaction", "ANY")).toUpperCase());
		this.newRelation = ParamUtil.getParam(params, "faction2","ANY");
		this.newRelation= ParamUtil.getParam(params, "newrelation", "ANY");
		this.oldRelation= ParamUtil.getParam(params, "oldrelation", "ANY");
	}
	

	
	public boolean mustExecute(String faction1, String faction2, String oldRelation, String newRelation){
		if (!isFactionRelated (faction1,faction2)) return false;
		return isRelation (this.oldRelation,oldRelation)&&isRelation (this.newRelation,newRelation);
	}

	public boolean isRelation(String relation1, String relation2){
		if (relation1.isEmpty()) return true;
		if (relation1.equalsIgnoreCase("any")) return true;
		return relation1.equalsIgnoreCase(relation2);
	}
	
	public boolean isFactionRelated(String faction1, String faction2){
		if (factions.isEmpty()) return true;
		if ((factions.size()==1)&&factions.contains("ANY")) return true;
		if (!factions.contains("ANY")) 	return factions.contains(faction1.toUpperCase())&&factions.contains(faction2.toUpperCase());
		return factions.contains(faction1.toUpperCase())||factions.contains(faction2.toUpperCase());
	}

	@Override
	public boolean activate(Event event) {
		if (!(event instanceof FactionRelationEvent)) return false;
		FactionRelationEvent fe = (FactionRelationEvent) event;
		Variables.setTempVar("faction1", fe.getFaction());
		Variables.setTempVar("faction2", fe.getOtherFaction());
		Variables.setTempVar("oldrelation", fe.getOldRelation());
		Variables.setTempVar("newrelation", fe.getNewRelation());
		if (this.mustExecute(fe.getFaction(), fe.getOtherFaction(), fe.getOldRelation(), fe.getNewRelation())){
			return Actions.executeActivator(null, this);
		}
		return false;
	}

	@Override
	public boolean isLocatedAt(Location loc) {
		return false;
	}

	@Override
	public void save(String root, YamlConfiguration cfg) {
		List<String> factionList= new ArrayList<String>();
		for (String faction : this.factions) factionList.add(faction);
		cfg.set(root+".factions", factionList);
		cfg.set(root+".old-relation", this.oldRelation);
		cfg.set(root+".new-relation", this.newRelation);
	}

	@Override
	public void load(String root, YamlConfiguration cfg) {
		this.factions = new HashSet<String>();
		this.factions.addAll(cfg.getStringList(root+".factions"));
		this.oldRelation= cfg.getString(root+".old-relation","ANY");
		this.newRelation= cfg.getString(root+".new-relation","ANY");
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.FCT_RELATION;
	}

}
