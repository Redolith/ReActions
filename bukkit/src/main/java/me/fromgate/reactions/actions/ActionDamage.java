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

import me.fromgate.reactions.util.BukkitCompatibilityFix;
import me.fromgate.reactions.util.Param;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.entity.Player;

public class ActionDamage extends Action {

    @SuppressWarnings("deprecation")
    @Override
    public boolean execute(Player p, Param params) {
        Player player = p;
        double damage = 0;
        if (params.hasAnyParam("damage", "player")) {
            String playerName = params.getParam("player", p != null ? p.getName() : "");
            player = playerName.isEmpty() ? null : Bukkit.getPlayerExact(playerName);
            damage = params.getParam("damage", 0);
        } else params.getParam("param-line", 0);
        return damagePlayer(player, damage);
    }


    public boolean damagePlayer(Player player, double damage) {
        if (player == null || player.isDead() || !player.isOnline()) return false;
        if (damage > 0) BukkitCompatibilityFix.damageEntity(player, damage);
        else player.playEffect(EntityEffect.HURT);
        setMessageParam(Double.toString(damage));
        return true;
    }

}
