package me.fromgate.reactions.actions;

import java.util.Map;

import me.fromgate.reactions.util.ParamUtil;
import me.fromgate.reactions.util.Util;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ActionPlayerPotion extends Action {

    @Override
    public boolean execute(Player p, Map<String, String> params) {
        String str = potionEffect (p,params);
        if (str.isEmpty()) return false;
        this.setMessageParam(str);
        return true;
    }
    
    
    private String potionEffect(Player p, Map<String,String> params) {
        if (params.isEmpty()) return "";
        String peffstr = "";
        int duration=20;
        int amplifier = 1;
        boolean ambient = false;
        if (params.containsKey("param")){
            String param = ParamUtil.getParam(params, "param", "");
            if (param.isEmpty()) return "";
            if (param.contains("/")){
                String[] prm = param.split("/");
                if (prm.length>1){
                    peffstr = prm[0];
                    if (u().isIntegerGZ(prm[1])) duration = Integer.parseInt(prm[1]);
                    if ((prm.length>2)&&u().isIntegerGZ(prm[2])) amplifier= Integer.parseInt(prm[2]);
                }
            } else peffstr = param;            
        } else {
            peffstr = ParamUtil.getParam(params, "type", "");
            duration = u().safeLongToInt(u().timeToTicks(u().parseTime(ParamUtil.getParam(params, "time", "3s")))); 
            amplifier = Math.max(ParamUtil.getParam(params, "level", 1)-1, 0);
            ambient = ParamUtil.getParam(params, "ambient", false);
        }
        PotionEffectType pef = Util.parsePotionEffect (peffstr.toUpperCase());
        if (pef == null) return "";
        PotionEffect pe = new PotionEffect (pef, duration, amplifier,ambient);
        if (p.hasPotionEffect(pef)) p.removePotionEffect(pef);
        p.addPotionEffect(pe);
        return pe.getType().getName()+":"+pe.getAmplifier();
    }
    
    
    
}
