package me.fromgate.reactions.actions;

import java.util.Map;

import me.fromgate.playeffect.Util;
import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.util.ParamUtil;
import me.fromgate.reactions.util.VelocityUtil;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ActionVelocityJump extends Action{

	@Override
	public boolean execute(Player p, Map<String, String> params) {
		u().SC("&cWarning! VELOCITY_JUMP action is under construction. In next version of plugin it could be changed, renamed or removed!");
		String locStr = ParamUtil.getParam(params, "loc", "");
		ReActions.util.BC("locStr: "+locStr);
		if (locStr.isEmpty()) return false;
		Location loc = Util.parseLocation(locStr);
		if (loc == null) return false;
		
		ReActions.util.BC("loc: "+loc.toString());
		ReActions.util.BC("loc.vector: "+loc.toVector().toString());
		
		int jumpHeight = ParamUtil.getParam(params, "jump", 5);
		Vector velocity = VelocityUtil.calculateVelocity(p.getLocation(), loc, jumpHeight);
		ReActions.util.BC("Vector: "+velocity.toString());
		p.setVelocity(velocity);
		return false;
	}

}
