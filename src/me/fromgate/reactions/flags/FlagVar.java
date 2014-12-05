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

import java.util.Map;

import me.fromgate.reactions.util.ParamUtil;
import me.fromgate.reactions.util.Variables;

import org.bukkit.entity.Player;

public class FlagVar extends Flag{
    private int flagType = -1;
    private boolean personalVar = false;

    public FlagVar(int flagType, boolean personalVar) {
        this.flagType = flagType;
        this.personalVar = personalVar;
    }

    @Override
    public boolean checkFlag(Player player, String param) {
        Player p = player;
        Map<String,String> params = ParamUtil.parseParams(param, "param-line");
        String var;
        String value;
        
        if (ParamUtil.isParamExists(params, "id")){
            var = ParamUtil.getParam(params, "id", "");
            if (var.isEmpty()) return false;
            value = ParamUtil.getParam(params, "value", "");
        } else {
            String [] ln = ParamUtil.getParam(params, "param-line", "").split("/",2);
            if (ln.length == 0) return false;
            var = ln[0];
            value = (ln.length>1) ? ln[1] : "";
        }
        
        if (!this.personalVar) p = null;
        else if (p == null) return false;
        switch (this.flagType){
        case 0: // VAR_EXIST
            return Variables.existVar(p, var);
        case 1: 
            return Variables.cmpVar(p, var, value);
        case 2: 
            return Variables.cmpGreaterVar(p, var, value);
        case 3: 
            return Variables.cmpLowerVar(p, var, value);
        case 4: 
        	return Variables.matchVar(p,var, value);
        }
        return false;
    }
    
    
    

}
