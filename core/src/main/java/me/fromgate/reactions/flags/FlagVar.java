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

package me.fromgate.reactions.flags;

import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.entity.Player;

public class FlagVar extends Flag {
    private int flagType = -1;
    private boolean personalVar = false;

    public FlagVar(int flagType, boolean personalVar) {
        this.flagType = flagType;
        this.personalVar = personalVar;
    }

    @Override
    public boolean checkFlag(Player player, String param) {
        Param params = new Param(param, "param-line");
        String var;
        String value;
        String playerName = this.personalVar && (player != null) ? player.getName() : "";


        if (params.isParamsExists("id")) {
            var = params.getParam("id", "");
            if (var.isEmpty()) return false;
            value = params.getParam("value", "");
            playerName = params.getParam("player", playerName);
        } else {
            String[] ln = params.getParam("param-line", "").split("/", 2);
            if (ln.length == 0) return false;
            var = ln[0];
            value = (ln.length > 1) ? ln[1] : "";
        }
        if (playerName.isEmpty() && this.personalVar) return false;
        switch (this.flagType) {
            case 0: // VAR_EXIST
                return Variables.existVar(playerName, var);
            case 1: // VAR_COMPARE
                return Variables.cmpVar(playerName, var, value);
            case 2: // VAR_GREATER
                return Variables.cmpGreaterVar(playerName, var, value);
            case 3: // VAR_LOWER
                return Variables.cmpLowerVar(playerName, var, value);
            case 4: // VAR_MATCH
                return Variables.matchVar(playerName, var, value);
        }
        return false;
    }
}
