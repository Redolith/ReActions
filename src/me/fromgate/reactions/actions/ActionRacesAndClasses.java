package me.fromgate.reactions.actions;

import java.util.Map;

import me.fromgate.reactions.externals.RARacesAndClasses;
import me.fromgate.reactions.util.ParamUtil;

import org.bukkit.entity.Player;

public class ActionRacesAndClasses extends Action {
	
	private boolean setRace;
	
	public ActionRacesAndClasses (boolean setRace){
		this.setRace = setRace;
	}

	@Override
	public boolean execute(Player p, Map<String, String> params) {
		if (!RARacesAndClasses.isEnabled()) return false;
		return this.setRace ? RARacesAndClasses.setRace(p, ParamUtil.getParam(params, "race", "")) : RARacesAndClasses.setClass(p, ParamUtil.getParam(params, "class", ""));
	}

}
