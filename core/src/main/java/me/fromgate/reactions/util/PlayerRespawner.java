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

package me.fromgate.reactions.util;

import me.fromgate.reactions.activators.PlayerDeathActivator;
import me.fromgate.reactions.event.PlayerRespawnedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerRespawner {
    private static Map<String, LivingEntity> players = new HashMap<String, LivingEntity>();
    private static Map<String, Location> deathpoints = new HashMap<String, Location>();

    public static void addPlayerRespawn(PlayerDeathEvent event) {
        Player deadplayer = event.getEntity();
        deathpoints.put(deadplayer.getName(), deadplayer.getLocation());  // это может пригодиться и в других ситуациях
        LivingEntity killer = Util.getAnyKiller(deadplayer.getLastDamageCause());
        players.put(deadplayer.getName(), killer);
    }

    public static Location getLastDeathPoint(Player player) {
        if (deathpoints.containsKey(player.getName())) return deathpoints.get(player.getName());
        return player.getLocation();
    }

    public static LivingEntity getLastKiller(Player player) {
        if (players.containsKey(player.getName()))
            return players.get(player.getName());
        return null;
    }

    public static void raisePlayerRespawnEvent(Player player) {
        if (!players.containsKey(player.getName())) return;
        LivingEntity killer = getLastKiller(player);
        players.remove(player.getName());
        PlayerDeathActivator.DeathCause d = PlayerDeathActivator.DeathCause.OTHER;
        if (killer != null && killer.getType() == EntityType.PLAYER) d = PlayerDeathActivator.DeathCause.PVP;
        else if (killer != null && killer instanceof LivingEntity) d = PlayerDeathActivator.DeathCause.PVE;
        Bukkit.getServer().getPluginManager().callEvent(new PlayerRespawnedEvent(player, killer, d));
    }

}
