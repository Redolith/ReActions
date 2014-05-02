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

import me.fromgate.reactions.externals.RAEconomics;
import me.fromgate.reactions.util.ParamUtil;

import org.bukkit.entity.Player;

public class FlagMoney extends Flag{

    @Override
    public boolean checkFlag(Player p, String param) {
    	if (!RAEconomics.isEconomyFound()) return false;
    	Map<String,String> params = ParamUtil.parseParams(param,"amount");
    	String amountStr = ParamUtil.getParam(params, "amount", "a");
    	if (!RAEconomics.isFloat(amountStr)) return false;
    	double amount = Double.parseDouble(amountStr);
    	String account = ParamUtil.getParam(params, "account", ParamUtil.getParam(params, "player", p==null ? "" : p.getName()));
    	if (account.isEmpty()) return false;
    	String currency = ParamUtil.getParam(params, "currency", "");
    	String world = ParamUtil.getParam(params, "world", "");
    	return RAEconomics.hasMoney(account, amount, currency, world);
        //return RAVault.isEconomyConected()&&u().isInteger(param)&&(Integer.parseInt(param)<=RAVault.getBalance(p.getName()));
    }
    
    
    
    

}
