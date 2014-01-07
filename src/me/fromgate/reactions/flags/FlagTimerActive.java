package me.fromgate.reactions.flags;

import me.fromgate.reactions.timer.Timers;

import org.bukkit.entity.Player;

public class FlagTimerActive extends Flag {

	@Override
	public boolean checkFlag(Player p, String param) {
		return Timers.isTimerWorking(param);
	}

}
