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

import me.fromgate.reactions.sql.SQLManager;
import me.fromgate.reactions.util.ParamUtil;

import org.bukkit.entity.Player;

public class FlagSQL extends Flag {
	boolean check;
	public FlagSQL (boolean check){
		this.check = check;
	}

	@Override
	public boolean checkFlag(Player p, String param) {
		if (!SQLManager.isEnabled()) return false;
		Map<String,String> params = ParamUtil.parseParams(param);
		if (!ParamUtil.isParamExists(params, "value","select","from")&&
				!(ParamUtil.isParamExists(params, "query"))) return false;
		String value = ParamUtil.getParam(params, "value", "");
		String select = ParamUtil.getParam(params, "select", "");
		String query = ParamUtil.getParam(params, "query", "");
		if (query.isEmpty()){
			if (select.isEmpty()) return false;
			String from = ParamUtil.getParam(params, "from", "");
			if (from.isEmpty()) return false;
			String where = ParamUtil.getParam(params, "where", "");
			query = "SELECT "+select+" FROM "+from+(where.isEmpty() ? "" : " WHERE "+where);
		}
		int column = ParamUtil.getParam(params, "column", 1);
		if (check) return SQLManager.compareSelect(value, query, column);
		else return SQLManager.isSelectResultEmpty(query);
	}

}
