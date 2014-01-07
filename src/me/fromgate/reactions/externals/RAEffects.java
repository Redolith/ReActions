package me.fromgate.reactions.externals;

import java.util.HashMap;
import java.util.Map;

import me.fromgate.playeffect.PlayEffect;
import me.fromgate.reactions.RAUtil;
import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.util.ParamUtil;
import me.fromgate.reactions.util.Util;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;



public class RAEffects {
    private static RAUtil u(){
        return ReActions.util;
    }
    private static String efftypes = "smoke,flame,ender,potion";
    
    private static boolean use_play_effects = false;
    
    //ENDER_SIGNAL  POTION_BREAK MOBSPAWNER_FLAMES  SMOKE 
    public static void init(){
        use_play_effects= isPlayEffectInstalled();
        if (use_play_effects) u().log("PlayEffect plugin is found!");  
        else {
            u().log("PlayEffect plugin is not found at your system");
            u().log("If you need more effects please download PlayEffect from:");
            u().log("http://dev.bukkit.org/bukkit-plugins/playeffect/");
        }
        
    }
    
    public static boolean isPlayEffectConnected(){
        return use_play_effects;
    }
    
    private static boolean isPlayEffectInstalled(){
        Plugin pe = Bukkit.getServer().getPluginManager().getPlugin("PlayEffect");
        return ((pe != null)&&(pe instanceof PlayEffect));
    }
    
    private static Effect getEffectByName(String name){
        if (name.equalsIgnoreCase("smoke")) return Effect.SMOKE;
        else if (name.equalsIgnoreCase("flame")) return Effect.MOBSPAWNER_FLAMES;
        else if (name.equalsIgnoreCase("ender")) return Effect.ENDER_SIGNAL;
        else if (name.equalsIgnoreCase("potion")) return Effect.POTION_BREAK;
        else return Effect.SMOKE;
    }
    
    public static void playEffect (Location loc, String eff, String param){
        Map<String,String> params = ParamUtil.parseParams(param);
        params.remove("param-line");
        playEffect(loc,eff,params);
    }
    
    public static void playEffect (Location loc, String eff, Map<String,String> params){
        if (use_play_effects){
            params.put("loc", Util.locationToString(loc));
            playPlayEffect(eff,params);
        } else {
            int data = 9;
            if (params.containsKey("wind")) data = parseSmokeDirection (params.get("wind"));
            playStandartEffect (loc,eff,data);
        }
    }
    
    public static void playEffect (Location loc, String eff, int data){
        if (use_play_effects){
            Map<String,String> params = new HashMap<String,String>();
            params.put("loc", Util.locationToString(loc));
            playPlayEffect(eff,params);
        } else {
            playStandartEffect (loc,eff,data);
        }
        
    }
    
    public static void playStandartEffect (Location loc, String eff, int data){
        int mod = data;
        World w = loc.getWorld();
        Effect effect = getEffectByName(eff);
        if (effect == null) return;
        if (eff.equalsIgnoreCase("smoke")){
            if (mod<0) mod = 0;
            if (mod>8) mod = 8;
            if (data == 10) mod = u().tryChance(9);
            if (data == 9){
                for (int i = 0; i<9;i++)
                    w.playEffect(loc, Effect.SMOKE, i); 
                
            } else w.playEffect(loc, Effect.SMOKE, mod); 
        } else w.playEffect(loc, effect, mod);
    }
    
    private static void playPlayEffect (String eff, Map<String,String> params){
        PlayEffect.play(eff, params);
    }
    
    public static void playEffect (Player p, Map<String,String> params){
        String eff = ParamUtil.getParam(params, "eff", "");
        if (eff.isEmpty()) eff =  ParamUtil.getParam(params, "type", "SMOKE"); // для совместимости со старыми версиями
        if (use_play_effects) {
            playPlayEffect(eff, params);    
        } else {
            Location loc = null;
            int modifier = 0;
            int radius = 0;
            
            boolean land = ParamUtil.getParam(params, "land", "true").equalsIgnoreCase("false");
            if (!u().isWordInList(eff, efftypes)) return;
            
            if (eff.equalsIgnoreCase("SMOKE")) modifier = parseSmokeDirection (ParamUtil.getParam(params, "dir", "random"));
            else modifier = Util.getMinMaxRandom(ParamUtil.getParam(params, "data", "0"));
            loc=Util.parseLocation(ParamUtil.getParam(params, "loc", Util.locationToString(p.getLocation())));
            radius = ParamUtil.getParam(params, "radius", 0);
            if (radius>0) loc = Util.getRandomLocationInRadius(loc, radius,land);
            playStandartEffect (loc,eff,modifier);            
        }
    }
    
    private static int parseSmokeDirection (String dir_str) {
        int d = 10;  
        if (dir_str.equalsIgnoreCase("n")) {d = 7;};
        if (dir_str.equalsIgnoreCase("nw")) {d = 8;};
        if (dir_str.equalsIgnoreCase("ne")) {d = 6;};
        if (dir_str.equalsIgnoreCase("s")) {d = 1;};
        if (dir_str.equalsIgnoreCase("sw")) {d = 2;};
        if (dir_str.equalsIgnoreCase("se")) {d = 0;};
        if (dir_str.equalsIgnoreCase("w")) {d = 5;};
        if (dir_str.equalsIgnoreCase("e")) {d = 3;};
        if (dir_str.equalsIgnoreCase("calm")) {d = 4;};
        if (dir_str.equalsIgnoreCase("up")) {d = 4;};
        if (dir_str.equalsIgnoreCase("all")) {d = 9;};
        if (dir_str.equalsIgnoreCase("rnd")) {d = 10;};
        if (dir_str.equalsIgnoreCase("random")) {d = 10;};
        return d;
    }

}
