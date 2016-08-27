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


package me.fromgate.reactions.util;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.Map;


public class Teleporter {
    private static Map<Player, PlayerTeleportEvent> events = new HashMap<Player, PlayerTeleportEvent>();

    public static void startTeleport(PlayerTeleportEvent event) {
        events.put(event.getPlayer(), event);
    }

    public static void stopTeleport(Player player) {
        if (events.containsKey(player)) events.remove(player);
    }

    public static void teleport(Player player, Location location) {
        if (events.containsKey(player)) {
            events.get(player).setTo(location);
        } else player.teleport(location);
    }


}
