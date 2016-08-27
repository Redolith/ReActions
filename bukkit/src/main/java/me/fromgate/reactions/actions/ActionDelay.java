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

import me.fromgate.reactions.util.Delayer;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.entity.Player;

public class ActionDelay extends Action {

    boolean globalDelay;

    public ActionDelay(boolean globalDelay) {
        this.globalDelay = globalDelay;
    }

    @Override
    public boolean execute(Player p, Param params) {
        String timeStr = "";
        String playerName = this.globalDelay ? "" : (p != null ? p.getName() : "");
        String variableId = "";
        boolean add = false;
        if (params.isParamsExists("id", "delay") || params.isParamsExists("id", "time")) {
            variableId = params.getParam("id", "");
            playerName = params.getParam("player", playerName);
            timeStr = params.getParam("delay", params.getParam("time", ""));
            add = params.getParam("add", false);
        } else {
            String oldFormat = params.getParam("param-line", "");
            if (oldFormat.contains("/")) {
                String[] m = oldFormat.split("/");
                if (m.length >= 2) {
                    timeStr = m[0];
                    variableId = m[1];
                }
            } else timeStr = oldFormat;
        }

        if (timeStr.isEmpty()) return false;
        if (variableId.isEmpty()) return false;
        setDelay(playerName, variableId, u().parseTime(timeStr), add);
        Delayer.setTempPlaceholders(playerName, variableId);
        setMessageParam(Variables.getTempVar("delay-left-hms", timeStr));
        return true;
    }

    private void setDelay(String playerName, String variableId, long delayTime, boolean add) {
        if (playerName.isEmpty()) Delayer.setDelay(variableId, delayTime, add);
        else Delayer.setPersonalDelay(playerName, variableId, delayTime, add);
    }

}
