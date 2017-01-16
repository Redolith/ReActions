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
import me.fromgate.reactions.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ActionPlayerPotion extends Action {

    @Override
    public boolean execute(Player p, Param params) {
        String str = potionEffect(p, params);
        if (str.isEmpty()) return false;
        this.setMessageParam(str);
        return true;
    }


    private String potionEffect(Player p, Param params) {
        if (params.isEmpty()) return "";
        String peffstr = "";
        int duration = 20;
        int amplifier = 1;
        boolean ambient = false;
        if (params.isParamsExists("param")) {
            String param = params.getParam("param", "");
            if (param.isEmpty()) return "";
            if (param.contains("/")) {
                String[] prm = param.split("/");
                if (prm.length > 1) {
                    peffstr = prm[0];
                    if (u().isIntegerGZ(prm[1])) duration = Integer.parseInt(prm[1]);
                    if ((prm.length > 2) && u().isIntegerGZ(prm[2])) amplifier = Integer.parseInt(prm[2]);
                }
            } else peffstr = param;
        } else {
            peffstr = params.getParam("type", "");
            duration = u().safeLongToInt(u().timeToTicks(u().parseTime(params.getParam("time", "3s"))));
            amplifier = Math.max(params.getParam("level", 1) - 1, 0);
            ambient = params.getParam("ambient", false);
        }
        PotionEffectType pef = Util.parsePotionEffect(peffstr.toUpperCase());
        if (pef == null) return "";
        PotionEffect pe = new PotionEffect(pef, duration, amplifier, ambient);
        if (p.hasPotionEffect(pef)) p.removePotionEffect(pef);
        p.addPotionEffect(pe);
        return pe.getType().getName() + ":" + pe.getAmplifier();
    }


}
