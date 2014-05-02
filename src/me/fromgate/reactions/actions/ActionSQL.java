package me.fromgate.reactions.actions;

import java.util.Map;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.sql.SQLManager;
import me.fromgate.reactions.util.ParamUtil;
import me.fromgate.reactions.util.Variables;

import org.bukkit.entity.Player;

public class ActionSQL extends Action {
	int sqlType;

	public ActionSQL(int sqlType) {
		this.sqlType=sqlType;
	}

	@Override
	public boolean execute(Player p, Map<String, String> params) {
		String playerName = ParamUtil.getParam(params, "player", "");
		String varName = ParamUtil.getParam(params, "variable", "");
		int column = ParamUtil.getParam(params, "column", 1);
		String query = ParamUtil.getParam(params, "query", "").trim();
		switch (sqlType){
		case 0: // SELECT to variable
			if (query.isEmpty()) return false;
			if (!query.toLowerCase().startsWith("select")) {
				ReActions.util.logOnce("needselect"+query, "You need to use only \"SELECT\" query in SQL_SELECT action. Query: "+query);
				return false;
			}
			if (varName.isEmpty()) return false;
			Variables.setVar(playerName, varName, SQLManager.executeSelect(query, column));
			break;
		case 1: // INSERT
			query = ParamUtil.getParam(params, "query", ParamUtil.getParam(params,"param-line","")).trim();
			if (query.isEmpty()) return false;
			if (!query.toLowerCase().startsWith("insert")) {
				ReActions.util.logOnce("needupdate"+query, "You need to use only \"INSERT\" query in SQL_INSERT action. Query: "+query);
				return false;
			}
			SQLManager.executeUpdate(query);
			break;
		case 2: // UPDATE
			query = ParamUtil.getParam(params, "query", ParamUtil.getParam(params,"param-line","")).trim();
			if (query.isEmpty()) return false;
			if (!query.toLowerCase().startsWith("update")) {
				ReActions.util.logOnce("needupdate"+query, "You need to use only \"UPDATE\" query in SQL_UPDATE action. Query: "+query);
				return false;
			}
			SQLManager.executeUpdate(query);
			break;
		}
		// TODO Auto-generated method stub
		return true;
	}

	
	
	
	
	
	
}
