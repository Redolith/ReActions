package me.fromgate.reactions.actions;

import java.util.Map;

import me.fromgate.reactions.util.RAEffects;
import me.fromgate.reactions.util.Util;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ActionTp extends Action{

    @Override
    public boolean execute(Player p, Map<String, String> params) {
        Location loc = teleportPlayer (p,params);
        if (loc!=null) this.setMessageParam(Util.locationToStringFormated(loc));
        return (loc!=null);
    }
    
    private Location teleportPlayer (Player p, Map<String,String> params){
        Location loc = null;
        int radius = 0;
        if (params.isEmpty()) return null;
        if (params.containsKey("param")) {
            loc = Util.locToLocation (p, Util.getParam(params, "param", ""));

        } else { 
            loc = Util.locToLocation (p, Util.getParam(params, "loc", ""));
            radius = Util.getParam(params, "radius", 0);
        }
        boolean land = Util.getParam(params, "land", true);

        if (loc != null){
            if (radius>0) loc = Util.getRandomLocationInRadius(loc, radius,land);
            if (plg().isCenterTpLocation()) {
                loc.setX(loc.getBlockX()+0.5);
                loc.setZ(loc.getBlockZ()+0.5);
            }
            while (!loc.getChunk().isLoaded()) loc.getChunk().load();
            p.teleport(loc);
            String playeffect = Util.getParam(params, "effect", "");
            if (!playeffect.isEmpty()){
                if (playeffect.equalsIgnoreCase("smoke")&&(!params.containsKey("wind"))) params.put("wind", "all");
                RAEffects.playEffect(loc, playeffect, params);
            }
        }
        return loc;
    }


}
