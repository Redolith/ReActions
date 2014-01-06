package me.fromgate.reactions.util;

import java.util.HashMap;
import java.util.Map;

import me.fromgate.reactions.RAUtil;
import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.activators.Activator;
import me.fromgate.reactions.activators.Activator.FlagVal;
import me.fromgate.reactions.flags.Flags;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Placeholders {

    private static RAUtil u(){
        return ReActions.util;
    }

    public static Map<String,String> replaceAllPlaceholders (Player p, Activator a, Map<String,String> params){
        Map<String,String> newparams = new HashMap<String,String>();
        for (String key : params.keySet())
            newparams.put(key, replacePlaceholders(p,a,params.get(key)));
        return newparams;
    }

    public static String replacePlaceholders (Player p, Activator a, String param){
        String rst = param;
        String placeholders = "curtime,player,dplayer,health,deathpoint,targetplayer,"+Flags.getFtypes().toLowerCase();
        String [] phs = placeholders.split(",");
        for (String ph : phs){
            String key = "%"+ph+"%";
            rst = rst.replaceAll(key, getFlagParam(p,a,key));
            rst = rst.replaceAll(key.toUpperCase(), getFlagParam(p,a,key));
        }
        return rst;
    }

    public static String getFlagParam (Player p, Activator a, String placeholder){
        if (placeholder.startsWith("%")&&placeholder.endsWith("%")){
            String flag = placeholder.substring(1, placeholder.length()-1);
            if (placeholder.equalsIgnoreCase("%curtime%")) return Util.timeToString(p.getWorld().getTime());
            else if (placeholder.equalsIgnoreCase("%player%")) return p.getName();
            else if (placeholder.equalsIgnoreCase("%dplayer%")) return p.getDisplayName();
            else if (placeholder.equalsIgnoreCase("%targetplayer%")) return a.getTargetPlayer();
            else if (placeholder.equalsIgnoreCase("%deathpoint%")) {
                Location loc = RAPVPRespawn.getLastDeathPoint(p);
                if (loc == null) loc = p.getLocation();
                return Util.locationToString(loc);
            } else if (placeholder.equalsIgnoreCase("%health%")) {
                String hlth = "0";
                try {
                    hlth = String.valueOf(p.getHealth());
                } catch (Throwable ex){
                    ReActions.util.logOnce("plr_health", "Failed to get Player health. This feature is not compatible with CB 1.5.2 (and older)...");
                }
                return hlth;
            }
            else for (FlagVal flg : a.getFlags())
                if (flg.flag.equals(flag)) return formatFlagParam (flag, flg.value);
        }
        return placeholder;
    }

    private static String formatFlagParam(String flag, String value) {
        String rst = value;
        Flags f = Flags.getByName(flag);
        switch (f){
        case TIME:
            if (!(value.equals("day")||value.equals("night"))){
                String [] ln = value.split(",");
                String r = "";
                if (ln.length>0)
                    for (int i = 0; i<ln.length;i++){
                        if (!u().isInteger(ln[i])) continue;
                        String tmp = String.format("%02d:00", Integer.parseInt(ln[i]));
                        if (i == 0) r = tmp;
                        else r = r+", "+tmp;
                    }
                if (!r.isEmpty()) rst = r;
            }
            break;
        case CHANCE: 
            rst = value +"%";
            break;
        case MONEY:
            if (RAVault.isEconomyConected()&&u().isIntegerSigned(value))
                rst = RAVault.formatMoney(value);
            break;
        default:
            break;
        }
        return rst; 

    }



}
