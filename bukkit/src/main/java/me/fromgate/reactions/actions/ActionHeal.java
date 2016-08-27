/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2015, fromgate, fromgate@gmail.com
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

import me.fromgate.reactions.externals.RAEffects;
import me.fromgate.reactions.util.BukkitCompatibilityFix;
import me.fromgate.reactions.util.Param;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ActionHeal extends Action {

    @SuppressWarnings("deprecation")
    @Override
    public boolean execute(Player p, Param params) {
        Player player = p;
        double hp = params.getParam("hp", 0);
        boolean playhearts = params.getParam("hearts", true);
        if (params.isParamsExists("params")) hp = params.getParam("params", 0);
        String playerName = params.getParam("player", p != null ? p.getName() : "");
        player = playerName.isEmpty() ? null : Bukkit.getPlayerExact(playerName);
        if (player == null) return false;
        double health = BukkitCompatibilityFix.getEntityHealth(player);
        double healthMax = BukkitCompatibilityFix.getEntityMaxHealth(player);
        if ((hp > 0) && (health < healthMax))
            BukkitCompatibilityFix.setEntityHealth(player, Math.max(hp + health, healthMax));
        if (playhearts && RAEffects.isPlayEffectConnected())
            RAEffects.playEffect(player.getEyeLocation(), "HEART", "offset:0.5 num:4 speed:0.7");
        setMessageParam(Double.toString(hp));
        return true;
    }
}
