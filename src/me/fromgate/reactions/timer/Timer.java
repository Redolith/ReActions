/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2014, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/reactions/
 *   * 
 *  This file is part of ReActions.
 *  
 *  ReActions is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ReActions is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with ReActions.  If not, see <http://www.gnorg/licenses/>.
 * 
 */

package me.fromgate.reactions.timer;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.quartz.CronExpression;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.util.ParamUtil;

public class Timer {
	
	private boolean timerType;
	private Set<String> timesIngame;
	private CronExpression timeServer;
	private Map<String,String> params;
	private boolean pause;
	
	public Timer (Map<String,String> params){
		this.timesIngame = new HashSet<String>();
		this.params=params;
		this.timerType = !ParamUtil.getParam(params, "timer-type", "ingame").equalsIgnoreCase("server");
		this.pause = ParamUtil.getParam(params, "paused", false);
		params.put("paused", String.valueOf(this.pause));
		this.parseTime();
		
	}
	
	/**
	 * @return true = ingame, false - server
	 */
	public boolean getTimerType(){
		return this.timerType;
	}
	
	public Map<String,String> getParams(){
		return this.params;
	}
	
	public void parseTime(){
		if (this.timerType){
			this.timesIngame = new HashSet<String>();
			for (String time : ParamUtil.getParam(params, "time", "").split(",")){
				this.timesIngame.add(time);
			}
		} else {
			String time = ParamUtil.getParam(params, "time", "").replace("_", " ");
			try {
				this.timeServer = new CronExpression(time);
			} catch (ParseException e) {
				ReActions.util.logOnce(time, "Failed to parse cron format: "+time);
				this.timeServer = null;
				e.printStackTrace();
			}
		}
	}
	
	
	public boolean isTimeToRun(){
		if (isPaused()) return false;
		return this.timerType ? isIngameTimeToRun() : isServerTimeToRun();
	}

	private boolean isServerTimeToRun() {
		if (this.timerType) return false;
		if (this.timeServer == null) return false;
		return (this.timeServer.isSatisfiedBy(new Date()));
	}

	private boolean isIngameTimeToRun() {
		return timerType&&timesIngame.contains(Time.currentIngameTime());
	}
	
	public boolean isIngameTimer(){
		return this.timerType;
	}

	public Set<String> getIngameTimes() {
		return this.timesIngame;
	}

	public String toString(){
		return ParamUtil.getParam(this.getParams(), "activator", "Undefined") + " : "+ParamUtil.getParam(this.getParams(), "time", "Undefined") +(this.isIngameTimer() ? " (ingame)" : " (server)");
	}

	public boolean isPaused (){
		return this.pause;
	}
	
	public void setPause(boolean pause) {
		this.pause = pause;
	}

}
