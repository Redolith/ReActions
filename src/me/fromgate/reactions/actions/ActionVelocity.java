package me.fromgate.reactions.actions;

import java.util.Map;

import me.fromgate.reactions.util.Util;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ActionVelocity extends Action{

    @Override
    public boolean execute(Player p, Map<String, String> params) {
        Vector v = setPlayerVelocity (p, params);
        if (v == null) return false;
        this.setMessageParam("["+v.getBlockX()+", "+v.getBlockY()+", "+v.getBlockZ()+"]");
        return true;
    }
    
    
    private Vector setPlayerVelocity(Player p, Map<String,String> params) {
        String velstr = "";
        boolean multiply = false;
        if (params.containsKey("param")){
            velstr = Util.getParam(params, "param", "");
        } else {
            velstr = Util.getParam(params, "direction","");
            multiply = Util.getParam(params, "multiply", false);
        }

        if (velstr.isEmpty()) return null;
        Vector v = p.getVelocity();
        String [] ln = velstr.split(",");
        if ((ln.length == 1)&&(velstr.matches("-?(([0-9]+\\.[0-9]*)|([0-9]+))"))) {
            double power = Double.parseDouble(velstr);
            v.setY(Math.min(10, multiply ? power*p.getVelocity().getY() : power));
        } else if ((ln.length == 3)&&
                ln[0].matches("-?(([0-9]+\\.[0-9]*)|([0-9]+))")&&
                ln[1].matches("-?(([0-9]+\\.[0-9]*)|([0-9]+))")&&
                ln[2].matches("-?(([0-9]+\\.[0-9]*)|([0-9]+))")) {
            double powerx = Double.parseDouble(ln[0]);
            double powery = Double.parseDouble(ln[1]);
            double powerz = Double.parseDouble(ln[2]);
            if (multiply){
                powerx = powerx*p.getVelocity().getX();
                powery = powery*p.getVelocity().getY();
                powerz = powerz*p.getVelocity().getZ();
            }

            v = new Vector (Math.min(10,powerx),Math.min(10,powery),Math.min(10,powerz));
        }
        p.setVelocity(v);
        return v;
    }

}
