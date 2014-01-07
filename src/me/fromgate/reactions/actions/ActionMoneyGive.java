package me.fromgate.reactions.actions;

import java.util.Map;

import me.fromgate.reactions.externals.RAVault;
import me.fromgate.reactions.util.Util;

import org.bukkit.entity.Player;

public class ActionMoneyGive extends Action {

    @Override
    public boolean execute(Player p, Map<String, String> params) {
        if (!RAVault.isEconomyConected()) return false;
        setMessageParam(RAVault.formatMoney(Integer.toString(moneyGive (p, params.get("param-line")))));
        return true;
    }
    
    
    private int moneyGive (Player p, String mstr){
        if (mstr.isEmpty()) return 0;
        String money="";
        String source="";
        if (mstr.contains("/")) {
            String [] m = mstr.split("/");
            if (m.length>=2){
                money = m[0];   
                source = m[1];
            }
        } else money = mstr;
        //if (!money.matches("[0-9]*")) return 0;
        int amount = Util.getMinMaxRandom(money); 
        if (amount<=0) return 0;        

        if (!source.isEmpty()){
            if (amount<RAVault.getBalance(source)) return 0;
            RAVault.withdrawPlayer(source, amount);
        } 

        RAVault.depositPlayer(p.getName(), amount);
        return amount;
    }

}
