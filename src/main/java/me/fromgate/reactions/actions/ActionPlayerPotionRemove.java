package me.fromgate.reactions.actions;

import java.util.Map;

import me.fromgate.reactions.util.ParamUtil;
import me.fromgate.reactions.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ActionPlayerPotionRemove extends Action{

    @Override
    public boolean execute(Player p, Map<String, String> params) {
        String str = removePotionEffect(p, ParamUtil.getParam(params, "param-line", ""));
        if (str.isEmpty()) return false;
        this.setMessageParam(str);
        return true;
    }

    private String removePotionEffect(Player p, String param) {
        String str = "";
        if (param.equalsIgnoreCase("all")||param.equalsIgnoreCase("*"))
            for (PotionEffect pe :p.getActivePotionEffects()) p.removePotionEffect(pe.getType());
        else {
            String [] pefs = param.split(",");
            if (pefs.length>0){
                for (int i = 0; i<pefs.length; i++){
                    PotionEffectType pef = Util.parsePotionEffect (pefs[i]);
                    if (pef == null) continue;
                    if (p.hasPotionEffect(pef)) {
                        p.removePotionEffect(pef);
                        str = str.isEmpty() ? pef.getName() : str+", "+pef.getName(); 
                    }
                }
            }
        }
        return str;

    }

}
