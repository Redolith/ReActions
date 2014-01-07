package me.fromgate.reactions.actions;

import java.util.List;
import java.util.Map;

import me.fromgate.reactions.externals.RAWorldGuard;
import me.fromgate.reactions.util.ParamUtil;
import me.fromgate.reactions.util.Util;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class ActionClearRegion extends Action {

    @Override
    public boolean execute(Player p, Map<String, String> params) {
        String region = ParamUtil.getParam(params, "region", "");
        String type = ParamUtil.getParam(params, "type", "all");
        if (region.isEmpty()) return false;
        if (!RAWorldGuard.isConnected()) return false;
        List<Location> locs = RAWorldGuard.getRegionMinMaxLocations(region);
        if (locs.size()!=2) return false;
        List<Entity> en = Util.getEntities(locs.get(0), locs.get(1));
        int count = 0;
        for (Entity e : en){
            if (e.getType()==EntityType.PLAYER) continue;
            if (isEntityIsTypeOf (e, type)) {
                e.remove();
                count++;
            }
        }
        setMessageParam(Integer.toString(count));
        return true;
    }

    
    private boolean isEntityIsTypeOf(Entity e, String type){
        if (e == null) return false;
        if (type.isEmpty()) return true;
        if (type.equalsIgnoreCase("all")) return true;
        if (e instanceof LivingEntity){
            if (type.equalsIgnoreCase("mob")||type.equalsIgnoreCase("mobs")) return true;
        } else {
            if (type.equalsIgnoreCase("item")||type.equalsIgnoreCase("items")) return true;
        }
        return (u().isWordInList(e.getType().name(), type));
    }
}
