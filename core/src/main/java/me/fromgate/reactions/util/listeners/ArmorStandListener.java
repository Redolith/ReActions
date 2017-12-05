package me.fromgate.reactions.util.listeners;

import me.fromgate.reactions.event.EventManager;
import me.fromgate.reactions.util.BukkitCompatibilityFix;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class ArmorStandListener implements Listener {


    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)
    public void onPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent event) {
        if (!BukkitCompatibilityFix.isHandSlot(event)) return;
        if (event.getRightClicked() instanceof LivingEntity) {
            EventManager.raiseMobClickEvent(event.getPlayer(), (LivingEntity) event.getRightClicked());
        }
    }

}
