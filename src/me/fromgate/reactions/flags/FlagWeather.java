package me.fromgate.reactions.flags;

import org.bukkit.entity.Player;

public class FlagWeather extends Flag {
	@Override
	public boolean checkFlag(Player p, String param) {
		if (param.equalsIgnoreCase("rain")) return p.getWorld().isThundering();
		return !p.getWorld().isThundering();
	}
}
