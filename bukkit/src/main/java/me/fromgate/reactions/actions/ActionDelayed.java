package me.fromgate.reactions.actions;

import me.fromgate.reactions.util.ActVal;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.waiter.ActionsWaiter;
import org.bukkit.entity.Player;

public class ActionDelayed extends Action {

    @Override
    public boolean execute(Player p, Param params) {
        long delay = u().parseTime(params.getParam("time", "0"));
        if (delay == 0) return false;

        String actionSource = params.getParam("action", "");
        if (actionSource.isEmpty()) return false;
        String actionStr;
        String paramStr = "";
        if (!actionSource.contains(" ")) actionStr = actionSource;
        else {
            actionStr = actionSource.substring(0, actionSource.indexOf(" "));
            paramStr = actionSource.substring(actionSource.indexOf(" ") + 1);
        }

        if (!Actions.isValid(actionStr)) {
            u().logOnce(actionSource, "Failed to execute delayed action: " + actionSource);
            return false;
        }

        ActVal av = new ActVal(actionStr, paramStr);
        ActionsWaiter.executeDelayed(p, av, this.isAction(), delay);

		
		
		
		/*
        final Actions action = Actions.getByName(actionStr);
		if (action==null) {
			u().logOnce(actionSource, "Failed to execute delayed action: "+actionSource);
			return false;
		}
		
		final boolean isAction = this.isAction();
		//final Activator activator = this.getActivator();
		final Param actionParam = new Param (paramStr);
		Bukkit.getScheduler().runTaskLater(plg(), new Runnable(){
			@Override
			public void run() {
				if (p==null) return;
				action.performAction(p,  isAction, actionParam);
			}
		}, u().timeToTicks(delay));
		
		*/

        return false;
    }

}
