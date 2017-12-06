package me.fromgate.reactions.util;

import me.fromgate.reactions.ReActions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashSet;
import java.util.Set;

public class GodMode {

    private static Set<EntityDamageEvent> godCheckEvents = new HashSet<>();

    public static boolean isGod(Player player) {
        return player.hasMetadata("reactions-god") && player.getMetadata("reactions-god").get(0).asBoolean();
    }

    public static boolean setGod(Player player) {
        if (!isGod(player)) {
            player.setMetadata("reactions-god", new FixedMetadataValue(ReActions.instance, true));
            return true;
        }
        return false;
    }

    public static boolean removeGod(Player player) {
        if (isGod(player)) {
            player.removeMetadata("reactions-god", ReActions.instance);
            return true;
        }
        return false;
    }

    public static void setCheckGod(Player player) {
        player.setMetadata("reactions-god-check", new FixedMetadataValue(ReActions.instance, true));
        setEventGod(player);
    }

    public static boolean checkGod(Player player) {
        boolean result = false;
        if (player.hasMetadata("reactions-god-check")) {
            result = player.getMetadata("reactions-god-check").get(0).asBoolean();
            player.removeMetadata("reactions-god-check", ReActions.instance);
        }
        return result;
    }

    public static void setEventGod(Player player) {
        //noinspection deprecation
        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(player, player, EntityDamageEvent.DamageCause.CUSTOM, 0);
        godCheckEvents.add(event);
        Bukkit.getPluginManager().callEvent(event);
    }

    public static void cancelGodEvent(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent && godCheckEvents.contains(event)) {
            godCheckEvents.remove(event);
            event.setCancelled(true);
        }
    }
}
