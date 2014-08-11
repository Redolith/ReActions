package me.fromgate.reactions.actions;

import java.util.Map;

import org.bukkit.entity.Player;

import me.fromgate.reactions.externals.Externals;
import me.fromgate.reactions.externals.RAFactions;
import me.fromgate.reactions.util.ParamUtil;

public class ActionFactionsPowerAdd extends Action {

	@Override
	public boolean execute(Player player, Map<String, String> params) {
		if (!Externals.isConnectedFactions()) return false;
		RAFactions.addPower(player, ParamUtil.getParam(params, "power", 0.0));
		return true;
	}
}
