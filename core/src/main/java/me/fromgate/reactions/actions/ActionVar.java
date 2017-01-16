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

import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.entity.Player;

public class ActionVar extends Action {
    private int actType = -1;
    private boolean personalVar = false;

    public ActionVar(int actType, boolean personalVar) {
        this.actType = actType;
        this.personalVar = personalVar;
    }

    @Override
    public boolean execute(Player p, Param params) {

        String player = (p != null && this.personalVar) ? p.getName() : "";

        String var;
        String value;

        if (params.isParamsExists("id")) {
            var = params.getParam("id", "");
            value = params.getParam("value", "");
            player = params.getParam("player", player);
            if (var.isEmpty()) return false;
        } else {
            String[] ln = params.getParam("param-line", "").split("/", 2);
            if (ln.length == 0) return false;
            var = ln[0];
            value = (ln.length > 1) ? ln[1] : "";
        }

        if (this.personalVar && player.isEmpty()) return false;

        switch (this.actType) {
            case 0: //VAR_SET, VAR_PLAYER_SET
                Variables.setVar(player, var, value);
                return true;
            case 1: //VAR_CLEAR, VAR_PLAYER_CLEAR
                Variables.clearVar(player, var);
                return true;
            case 2: //VAR_INC, VAR_PLAYER_INC
                int incValue = value.isEmpty() || !(u().isInteger(value)) ? 1 : Integer.parseInt(value);
                return Variables.incVar(player, var, incValue);
            case 3: //VAR_DEC, VAR_PLAYER_DEC
                int decValue = value.isEmpty() || !(u().isInteger(value)) ? 1 : Integer.parseInt(value);
                return Variables.decVar(player, var, decValue);
            case 4:  //VAR_TEMP_SET
                Variables.setTempVar(var, value);
                return true;
        }
        return false;
    }

}
