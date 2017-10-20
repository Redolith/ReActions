package me.fromgate.reactions.actions;

import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Util;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by MaxDikiy on 20/10/2017.
 */
public class ActionClearRadius extends Action {
    @Override
    public boolean execute(Player p, Param params) {
        int radius = params.getParam("radius", 0);
        String type = params.getParam("type", "all");
        if (radius == 0) return false;
        List<Location> locs = Util.getMinMaxRadiusLocations(p, radius);
        if (locs.size() != 2) return false;
        List<Entity> en = Util.getEntities(locs.get(0), locs.get(1));
        int count = 0;
        for (Entity e : en) {
            if (e.getType() == EntityType.PLAYER) continue;
            if (isEntityIsTypeOf(e, type)) {
                e.remove();
                count++;
            }
        }
        setMessageParam(Integer.toString(count));
        return true;
    }

    private boolean isEntityIsTypeOf(Entity e, String type) {
        if (e == null) return false;
        if (type.isEmpty()) return true;
        if (type.equalsIgnoreCase("all")) return true;
        if (e instanceof LivingEntity) {
            if (type.equalsIgnoreCase("mob") || type.equalsIgnoreCase("mobs")) return true;
        } else {
            if (type.equalsIgnoreCase("item") || type.equalsIgnoreCase("items")) return true;
        }
        return (Util.isWordInList(e.getType().name().toLowerCase(), type.toLowerCase()));
    }

}
