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

import java.util.Map;

import me.fromgate.reactions.externals.RAEffects;
import me.fromgate.reactions.util.BukkitCompatibilityFix;
import me.fromgate.reactions.util.ParamUtil;

import org.bukkit.entity.Player;

public class ActionHeal extends Action {

    @Override
    public boolean execute(Player p, Map<String, String> params) {
        int hp = ParamUtil.getParam(params, "hp", 0);
        boolean playhearts = ParamUtil.getParam(params, "hearts", true);
        if (params.containsKey("params")) hp=ParamUtil.getParam(params, "params", 0);
        double health = BukkitCompatibilityFix.getEntityHealth(p);
        double healthMax = BukkitCompatibilityFix.getEntityMaxHealth(p);
        if ((hp>0)&&(health<healthMax)) BukkitCompatibilityFix.setEntityHealth(p, Math.max(hp+health, healthMax));
        //if ((hp>0)&&(p.getHealth()<p.getMaxHealth())) p.setHealth(Math.max(hp+p.getHealth(), p.getMaxHealth()));
        if (playhearts&&RAEffects.isPlayEffectConnected()) RAEffects.playEffect(p.getEyeLocation(), "HEART", "offset:0.5 num:4 speed:0.7");
        setMessageParam(Integer.toString(hp));
        return true;
    }
}
