package me.fromgate.reactions.flags;

import me.fromgate.reactions.externals.RAFactions;

import org.bukkit.entity.Player;

public class FlagFaction extends Flag {
	

	@Override
	public boolean checkFlag(Player player, String param) {
		return RAFactions.isPlayerInFaction(player, param);
	}

}
