package me.fromgate.reactions.event;

import org.bukkit.entity.Player;

public class FactionEvent extends RAEvent {
	
	private String newFaction;
	private String oldFaction;

	public FactionEvent(Player player, String oldFaction, String newFaction) {
		super(player);
		this.newFaction = newFaction;
		this.oldFaction = oldFaction;
	}
	
	public String getOldFaction(){
		return oldFaction;
	}

	public String getNewFaction(){
		return newFaction;
	}

	
}
