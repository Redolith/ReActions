package me.fromgate.reactions.util;

import me.fromgate.reactions.event.EventManager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class ArmorStandListener implements Listener {


    public void onPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked() instanceof LivingEntity)
            EventManager.raiseMobClickEvent(event.getPlayer(), (LivingEntity) event.getRightClicked());
    }

}
