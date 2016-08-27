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
import org.bukkit.entity.Player;

public class FlagCompare extends Flag {

    @Override
    public boolean checkFlag(Player p, String param) {
        Param params = new Param(param, "unknown");
        String paramValue = params.getParam("param", "");
        if (paramValue.isEmpty()) return false;
        if (!params.isParamsExists("value1")) return false;
        for (String valueKey : params.keySet()) {
            if (!((valueKey.toLowerCase()).startsWith("value"))) continue;
            String value = params.getParam(valueKey);
            if (u().isIntegerSigned(value, paramValue) && (Integer.parseInt(value) == Integer.parseInt(paramValue)))
                return true;
            else if (paramValue.equalsIgnoreCase(value)) return true;
        }
        return false;
    }
}
