package me.fromgate.reactions.util.listeners;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.event.EventManager;
import me.fromgate.reactions.util.PushBack;
import me.fromgate.reactions.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {

    public static void init() {
        Bukkit.getServer().getPluginManager().registerEvents(new MoveListener(), ReActions.getPlugin());
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        PushBack.rememberLocations(event.getPlayer(), event.getFrom(), event.getTo());
        if (Util.isSameBlock(event.getFrom(), event.getTo())) return;
        EventManager.raiseAllRegionEvents(event.getPlayer(), event.getTo(), event.getFrom());
    }
}
