/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2014, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/reactions/
 *    
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

package me.fromgate.reactions.actions;

import java.util.Map;
import me.fromgate.reactions.util.ParamUtil;
import me.fromgate.reactions.util.Delayer;
import org.bukkit.entity.Player;

public class ActionDelay extends Action {
	
	boolean personal;
	
	public ActionDelay (boolean personalDelay){
		this.personal = personalDelay;
	}

    @Override
    public boolean execute(Player p, Map<String, String> params) {
    	String timeStr = "";
    	String variableId = p.getName();
    	
    	if (ParamUtil.isParamExists(params, "id","delay")||ParamUtil.isParamExists(params, "id","time")){
    		variableId = ParamUtil.getParam(params, "id", "");
    		timeStr = ParamUtil.getParam(params, "delay", ParamUtil.getParam(params, "time", ""));
    	} else {
    		String oldFormat = ParamUtil.getParam(params, "param-line", "");
            if (oldFormat.contains("/")){
                String[] m = oldFormat.split("/");
                if (m.length>=2){
                    timeStr = m[0];
                    variableId = m[1];
                }
            } else timeStr = oldFormat;
    	}
    	if (timeStr.isEmpty()) return false;
    	if (variableId.isEmpty()) return false;
    	setDelay(p,variableId,u().parseTime(timeStr));
        setMessageParam(timeStr);
        return true;
    }
    
    private void setDelay(Player p, String variableId, long delayTime){
        if (!this.personal) Delayer.setDelay(variableId, delayTime);
        else if (p!=null) Delayer.setPersonalDelay(p, variableId, delayTime);
    }

}
