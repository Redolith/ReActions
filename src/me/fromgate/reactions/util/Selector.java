package me.fromgate.reactions.util;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Selector {
    private static Map<String, Location> locs = new HashMap<String,Location>(); 
    
    @SuppressWarnings("deprecation")
    public static void selectLocation (Player p, Location loc){
        if (p == null) return;
        if (loc == null) loc = p.getTargetBlock(null, 100).getLocation();
        locs.put(p.getName(), loc);
    }
    
    public static Location getSelectedLocation (Player p){
        if (locs.containsKey(p.getName())) return locs.get(p.getName());
        return null;
    }
    
    public static String getSelectedStrLoc(Player p){
        Location loc = getSelectedLocation(p);
        if (loc == null) return "";
        return Util.locationToString(loc);
    }

}
