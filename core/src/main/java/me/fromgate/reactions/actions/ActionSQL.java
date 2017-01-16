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

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.sql.SQLManager;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.entity.Player;

public class ActionSQL extends Action {
    int sqlType;

    public ActionSQL(int sqlType) {
        this.sqlType = sqlType;
    }

    @Override
    public boolean execute(Player p, Param params) {
        String playerName = params.getParam("player", "");
        String varName = params.getParam("variable", "");
        int column = params.getParam("column", 1);
        String query = params.getParam("query", "").trim();
        switch (sqlType) {
            case 0: // SELECT to variable
                if (query.isEmpty()) return false;
                if (!query.toLowerCase().startsWith("select")) {
                    ReActions.util.logOnce("needselect" + query, "You need to use only \"SELECT\" query in SQL_SELECT action. Query: " + query);
                    return false;
                }
                if (varName.isEmpty()) return false;
                Variables.setVar(playerName, varName, SQLManager.executeSelect(query, column, params));
                break;
            case 1: // INSERT
                query = params.getParam("query", params.getParam("param-line", "")).trim();
                if (query.isEmpty()) return false;
                if (!query.toLowerCase().startsWith("insert")) {
                    ReActions.util.logOnce("needupdate" + query, "You need to use only \"INSERT\" query in SQL_INSERT action. Query: " + query);
                    return false;
                }
                SQLManager.executeUpdate(query, params);
                break;
            case 2: // UPDATE
                query = params.getParam("query", params.getParam("param-line", "")).trim();
                if (query.isEmpty()) return false;
                if (!query.toLowerCase().startsWith("update")) {
                    ReActions.util.logOnce("needupdate" + query, "You need to use only \"UPDATE\" query in SQL_UPDATE action. Query: " + query);
                    return false;
                }
                SQLManager.executeUpdate(query, params);
                break;
            case 3: // DELETE
                query = params.getParam("query", params.getParam("param-line", "")).trim();
                if (query.isEmpty()) return false;
                if (!query.toLowerCase().startsWith("delete")) {
                    ReActions.util.logOnce("needdelete" + query, "You need to use only \"DELETE\" query in SQL_DELETE action. Query: " + query);
                    return false;
                }
                SQLManager.executeUpdate(query, params);
                break;
        }
        return true;
    }


}
