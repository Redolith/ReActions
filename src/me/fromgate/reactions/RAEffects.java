package me.fromgate.reactions;

import java.util.Map;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;




public class RAEffects {
    private static ReActions plg;
    private static String efftypes = "smoke,flame,ender,potion";
    
    //ENDER_SIGNAL  POTION_BREAK MOBSPAWNER_FLAMES  SMOKE 
    public static void init(ReActions plugin){
        plg = plugin;
    }
    
    
    private static Effect getEffectByName(String name){
        if (name.equalsIgnoreCase("smoke")) return Effect.SMOKE;
        else if (name.equalsIgnoreCase("flame")) return Effect.MOBSPAWNER_FLAMES;
        else if (name.equalsIgnoreCase("ender")) return Effect.ENDER_SIGNAL;
        else if (name.equalsIgnoreCase("potion")) return Effect.POTION_BREAK;
        else return null;
    }
    
    public static void playEffect (Location loc, String eff, int data){
        int mod = data;
        World w = loc.getWorld();
        Effect effect = getEffectByName(eff);
        if (effect == null) return;
        if (eff.equalsIgnoreCase("smoke")){
            if (mod<0) mod = 0;
            if (mod>8) mod = 8;
            if (data == 10) mod = plg.u.random.nextInt(9);
            if (data == 9){
                for (int i = 0; i<9;i++)
                    w.playEffect(loc, Effect.SMOKE, i); 
                
            } else w.playEffect(loc, Effect.SMOKE, mod); 
        } else w.playEffect(loc, effect, mod);
    }
    
    public static void playEffect (Player p, Map<String,String> params){
        Location loc = null;
        String eff = "";
        int modifier = 0;
        int radius = 0;
        //Map<String,String> params = Util.parseActionParam(param);
        eff = Util.getParam(params, "type", "SMOKE");
        boolean land = Util.getParam(params, "land", "true").equalsIgnoreCase("false");
        if (!plg.u.isWordInList(eff, efftypes)) return;
        
        if (eff.equalsIgnoreCase("SMOKE")) modifier = parseSmokeDirection (Util.getParam(params, "dir", "random"));
        else modifier = Util.getMinMaxRandom(Util.getParam(params, "data", "0"));
        loc=Util.parseLocation(Util.getParam(params, "loc", Util.locationToString(p.getLocation())));
        radius = Util.getParam(params, "radius", 0);
        if (radius>0) loc = Util.getRandomLocationInRadius(loc, radius,land);
        playEffect (loc,eff,modifier);
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
