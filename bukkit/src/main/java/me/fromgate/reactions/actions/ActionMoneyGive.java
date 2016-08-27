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

import me.fromgate.reactions.externals.RAEconomics;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Util;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ActionMoneyGive extends Action {

    @Override
    public boolean execute(Player p, Param params) {
        if (!RAEconomics.isEconomyFound()) return false;
        if (params.isEmpty()) return false;
        if (params.size() <= 2) params = parseOldFormat(p, params.getParam("param-line"));
        String amountStr = params.getParam("amount", "");
        if (amountStr.isEmpty()) return false;
        String currencyName = params.getParam("currency", "");
        String worldName = params.getParam("world", "");
        String target = params.getParam("target", params.getParam("player", (p == null ? "" : p.getName())));
        if (target.isEmpty()) return false;
        String source = params.getParam("source", "");
        String message = RAEconomics.creditAccount(target, source, amountStr, currencyName, worldName);
        if (message.isEmpty()) return false;
        setMessageParam(message);
        return true;
    }

    private Param parseOldFormat(Player p, String mstr) {
        Map<String, String> newParams = new HashMap<String, String>();
        if (p != null) newParams.put("target", p.getName());
        if (mstr.contains("/")) {
            String[] m = mstr.split("/");
            if (m.length >= 2) {
                newParams.put("amount", m[0].contains("-") ? Integer.toString(Util.getMinMaxRandom(m[0])) : m[0]);
                newParams.put("sourse", m[1]);
            }
        } else newParams.put("amount", mstr.contains("-") ? Integer.toString(Util.getMinMaxRandom(mstr)) : mstr);
        return new Param(newParams);
    }
}
