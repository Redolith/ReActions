package me.fromgate.reactions.actions;

import java.util.Map;

import me.fromgate.reactions.util.ParamUtil;

import org.bukkit.entity.Player;

public class ActionCancelEvent extends Action{

	@Override
	public boolean execute(Player p, Map<String, String> params) {
		return ParamUtil.getParam(params, "param-line", false);
	}

}
