package me.fromgate.reactions.flags;

import me.fromgate.reactions.externals.RARacesAndClasses;

import org.bukkit.entity.Player;

public class FlagRacesAndClasses extends Flag {
	
	private boolean checkRace;
	
	public FlagRacesAndClasses (boolean checkRace){
		this.checkRace = checkRace;
	}

	@Override
	public boolean checkFlag(Player p, String param) {
		if (!RARacesAndClasses.isEnabled()) return false;
		return this.checkRace ? RARacesAndClasses.checkRace(p, param) : RARacesAndClasses.checkClass(p, param);
	}

}
