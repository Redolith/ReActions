/*
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2017, fromgate, fromgate@gmail.com
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


package me.fromgate.reactions.util.listeners;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.event.EventManager;
import me.fromgate.reactions.util.Cfg;
import me.fromgate.reactions.util.PushBack;
import me.fromgate.reactions.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;

public class MoveListener implements Listener {

    private static Map<String, Location> prevLocations = new HashMap<>();

    public static void init() {
        if (Cfg.playerMoveTaskUse) {
            Bukkit.getScheduler().runTaskTimer(ReActions.getPlugin(), () -> {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    Location from = prevLocations.getOrDefault(player.getName(), null);
                    Location to = player.getLocation();
                    if (!to.getWorld().equals(from.getWorld())) from = null;
                    proccesMove(player, from, to);
                    prevLocations.put(player.getName(), to);
                });
            }, 30, Cfg.playerMoveTaskTick);
        } else {
            Bukkit.getServer().getPluginManager().registerEvents(new MoveListener(), ReActions.getPlugin());
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        proccesMove(event.getPlayer(), event.getFrom(), event.getTo());
    }

    private static void proccesMove(Player player, Location from, Location to) {
        PushBack.rememberLocations(player, from, to);
        if (!Util.isSameBlock(from, to)) {
            EventManager.raiseAllRegionEvents(player, to, from);
        }
    }

    public static void initLocation(Player player) {
        if (Cfg.playerMoveTaskUse) {
            prevLocations.put(player.getName(), player.getLocation());
        }
    }

    public static void removeLocation(Player player) {
        if (prevLocations.containsKey(player.getName())) {
            prevLocations.remove(player.getName());
        }
    }

}