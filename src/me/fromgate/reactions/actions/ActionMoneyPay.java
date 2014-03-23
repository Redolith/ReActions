package me.fromgate.reactions.actions;

import java.util.HashMap;
import java.util.Map;
import me.fromgate.reactions.externals.RAEconomics;
import me.fromgate.reactions.util.ParamUtil;
import me.fromgate.reactions.util.Util;
import org.bukkit.entity.Player;

public class ActionMoneyPay extends Action {

    @Override
    public boolean execute(Player p, Map<String, String> params) {
    	if (!RAEconomics.isEconomyFound()) return false;
    	if (params.size() == 0) return false;
    	if (params.size() <=2) params = parseOldFormat (p,params.get("param-line"));
    	String amountStr = ParamUtil.getParam(params, "amount", "");
    	if (amountStr.isEmpty()) return false;
    	String currencyName = ParamUtil.getParam(params, "currency", "");
    	String worldName = ParamUtil.getParam(params, "world", "");
    	String target = ParamUtil.getParam(params, "target", "");
    	String source = ParamUtil.getParam(params, "source", ParamUtil.getParam(params, "player", (p !=null ? p.getName() : "")));
    	if (source.isEmpty()) return false;
    	String message = RAEconomics.debitAccount (source,target,amountStr,currencyName,worldName);
    	if (message.isEmpty()) return false;
    	setMessageParam(message);
    	return true;
    }
    
    private Map<String,String> parseOldFormat(Player p, String mstr){
    	Map<String,String> newParams = new HashMap<String,String>();
    	if (p != null) newParams.put("source", p.getName());
    	if (mstr.contains("/")) {
    		String [] m = mstr.split("/");
            if (m.length>=2){
            	newParams.put("amount", m[0].contains("-") ? Integer.toString(Util.getMinMaxRandom(m[0])) : m[0]);
            	newParams.put("target", m[1]);
            }    		
    	} else newParams.put("amount", mstr.contains("-") ? Integer.toString(Util.getMinMaxRandom(mstr)) : mstr);
    	return newParams;
    }
}
