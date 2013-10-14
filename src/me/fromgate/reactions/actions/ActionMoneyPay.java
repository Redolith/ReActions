package me.fromgate.reactions.actions;

import java.util.Map;

import me.fromgate.reactions.util.RAVault;
import me.fromgate.reactions.util.Util;

import org.bukkit.entity.Player;

public class ActionMoneyPay extends Action {

    @Override
    public boolean execute(Player p, Map<String, String> params) {
        if (!RAVault.isEconomyConected()) return false;
        setMessageParam(RAVault.formatMoney(Integer.toString(moneyPay (p,Util.getParam(params, "param-line", "")))));
        return true;
    }
    
    
    private int moneyPay (Player p, String mstr){
        if (mstr.isEmpty()) return 0;
        String money="";
        String target="";
        if (mstr.contains("/")) {
            String [] m = mstr.split("/");
            if (m.length>=2){
                money = m[0];   
                target = m[1];
            }
        } else money = mstr;
        int amount = Util.getMinMaxRandom(money); 
        if ((amount<=0)||(amount>RAVault.getBalance(p.getName()))) return 0;
        RAVault.withdrawPlayer(p.getName(), amount);
        if (!target.isEmpty()) RAVault.depositPlayer(target, amount);
        return amount;
    }


}
