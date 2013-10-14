package me.fromgate.reactions.actions;

import java.util.List;
import java.util.Map;

import me.fromgate.reactions.util.Util;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class ActionItemDrop extends Action {
    @Override
    public boolean execute(Player p, Map<String, String> params) {
        int radius = Util.getParam(params, "radius", 0);
        Location loc = Util.parseLocation(Util.getParam(params, "loc", ""));
        if (loc == null) loc = p.getLocation();
        boolean scatter = Util.getParam(params, "scatter", true);
        boolean land = Util.getParam(params, "land", true);
        List<ItemStack> items = Util.parseRandomItems(Util.getParam(params, "item", ""));
        if (items.isEmpty()) return false;
        if (radius==0) scatter = false;
        Location l = Util.getRandomLocationInRadius(loc, radius, land);
        for (ItemStack i : items){
            loc.getWorld().dropItemNaturally(l, i);
            if (scatter) l = Util.getRandomLocationInRadius(loc, radius, land);
        }
        this.setMessageParam(Util.itemsToString(items));
        return true;
    }

}
