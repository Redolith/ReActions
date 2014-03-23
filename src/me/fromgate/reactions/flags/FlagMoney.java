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
