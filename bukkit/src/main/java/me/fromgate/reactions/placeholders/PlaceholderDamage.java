package me.fromgate.reactions.placeholders;

import me.fromgate.reactions.util.BukkitCompatibilityFix;
import me.fromgate.reactions.util.Util;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

@PlaceholderDefine(id = "Damage", needPlayer = true, keys = {"DAMAGE", "DAMAGER", "DAMAGER_NAME"})
public class PlaceholderDamage extends Placeholder {

    @Override
    public String processPlaceholder(Player player, String key, String param) {
        EntityDamageEvent de = player.getLastDamageCause();
        if (de == null) return null;
        if (key.equalsIgnoreCase("DAMAGE")) return Double.toString(BukkitCompatibilityFix.getEventDamage(de));
        else if (key.equalsIgnoreCase("DAMAGER")) return getDamager(de, false);
        else if (key.equalsIgnoreCase("DAMAGER_NAME")) return getDamager(de, false);
        return null;
    }

    public String getDamager(EntityDamageEvent de, boolean damagerName) {
        if (de == null) return null;
        LivingEntity e = Util.getDamagerEntity(de);
        String type = e.getType().name();
        String name = (e instanceof Player) ? ((Player) e).getName() : (e.getCustomName() != null ? e.getCustomName() : type);
        return damagerName ? name : type;
    }
}
