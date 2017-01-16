package me.fromgate.reactions.actions;

import me.fromgate.reactions.util.ActVal;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.waiter.ActionsWaiter;
import org.bukkit.entity.Player;

import java.util.List;

public class ActionWait extends Action {

    @Override
    public boolean execute(Player p, Param params) {
        return false;
    }

    public void executeDelayed(Player player, final List<ActVal> actions, final boolean isAction, long time) {
        if (actions.isEmpty()) return;
        ActionsWaiter.executeDelayed(player, actions, isAction, time);

		/*final String playerStr = player!=null? player.getName() : null;
        Bukkit.getScheduler().runTaskLater(ReActions.getPlugin(), new Runnable(){
			@Override
			public void run() {
				@SuppressWarnings("deprecation")
				Player p = playerStr==null ? null : Bukkit.getPlayerExact(playerStr);
				if (p==null&& playerStr!=null) return;
				Actions.executeActions(p, actions, isAction);
			}
		}, time); */

    }

}
