package me.fromgate.reactions.actions;

import java.util.Map;

import me.fromgate.reactions.timer.Timers;
import me.fromgate.reactions.util.ParamUtil;

import org.bukkit.entity.Player;

public class ActionTimer extends Action {
	private boolean pauseTimer;
	/*    TIMER_STOP("timerstop",false,new ActionTimer(false)),
    TIMER_RESUME("timerresume",false,new ActionTimer(true));
    */
	
	public ActionTimer (boolean pauseTimer){
		this.pauseTimer= pauseTimer;
	}
	
	@Override
	public boolean execute(Player p, Map<String, String> params) {
		String timer = ParamUtil.getParam(params, "timer", "");
		if (timer.isEmpty()) return false;
		return Timers.setPause (timer,pauseTimer);
	}

}
