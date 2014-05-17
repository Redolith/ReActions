package me.fromgate.reactions.actions;

import java.util.Map;

import me.fromgate.reactions.activators.Activator;
import me.fromgate.reactions.util.ParamUtil;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ActionDelayed extends Action {

	@Override
	public boolean execute(final Player p, Map<String, String> params) {
		Long delay= u().parseTime(ParamUtil.getParam(params, "time", "0"));
		if (delay==0) return false;
		String actionSource = ParamUtil.getParam(params, "action", "");
		if (actionSource.isEmpty()) return false;
		String actionStr;
		String paramStr = "";
		if (!actionSource.contains(" ")) actionStr = actionSource;
		else {
			actionStr = actionSource.substring(0, actionSource.indexOf(" "));
			paramStr = actionSource.substring(actionSource.indexOf(" ")+1);
		}
		final Actions action = Actions.getByName(actionStr);
		if (action==null) {
			u().logOnce(actionSource, "Failed to execute delayed action: "+actionSource);
			return false;
		}
		final boolean isAction = this.isAction();
		final Activator activator = this.getActivator();
		final Map<String,String> actionParam = ParamUtil.parseParams(paramStr);
		Bukkit.getScheduler().runTaskLater(plg(), new Runnable(){
			@Override
			public void run() {
				if (p==null) return;
				action.performAction(p, activator, isAction, actionParam);
			}
		}, u().timeToTicks(delay));
		return false;
	}

}
