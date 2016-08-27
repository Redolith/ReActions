/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2014, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/reactions/
 *    
 *  This file is part of ReActions.
 *  
 *  ReActions is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ReActions is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with ReActions.  If not, see <http://www.gnorg/licenses/>.
 * 
 */

package me.fromgate.reactions.actions;

import me.fromgate.reactions.util.Param;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ActionVelocity extends Action {

    @Override
    public boolean execute(Player p, Param params) {
        Vector v = setPlayerVelocity(p, params);
        if (v == null) return false;
        this.setMessageParam("[" + v.getBlockX() + ", " + v.getBlockY() + ", " + v.getBlockZ() + "]");
        return true;
    }


    private Vector setPlayerVelocity(Player p, Param params) {
        String velstr = "";
        boolean kick = false;
        if (params.isParamsExists("param")) {
            velstr = params.getParam("param", "");
        } else {
            velstr = params.getParam("vector", "");
            if (velstr.isEmpty()) velstr = params.getParam("direction", "");
            kick = params.getParam("kick", false);
        }

        if (velstr.isEmpty()) return null;
        Vector v = p.getVelocity();
        String[] ln = velstr.split(",");
        if ((ln.length == 1) && (velstr.matches("-?(([0-9]+\\.[0-9]*)|([0-9]+))"))) {
            double power = Double.parseDouble(velstr);
            v.setY(Math.min(10, kick ? power * p.getVelocity().getY() : power));
        } else if ((ln.length == 3) &&
                ln[0].matches("-?(([0-9]+\\.[0-9]*)|([0-9]+))") &&
                ln[1].matches("-?(([0-9]+\\.[0-9]*)|([0-9]+))") &&
                ln[2].matches("-?(([0-9]+\\.[0-9]*)|([0-9]+))")) {
            double powerx = Double.parseDouble(ln[0]);
            double powery = Double.parseDouble(ln[1]);
            double powerz = Double.parseDouble(ln[2]);
            if (kick) {
                v = p.getLocation().getDirection();
                v = v.normalize();
                v = v.multiply(new Vector(powerx, powery, powerz));
                p.setFallDistance(0);
            } else v = new Vector(Math.min(10, powerx), Math.min(10, powery), Math.min(10, powerz));
        }
        p.setVelocity(v);
        return v;
    }

}
