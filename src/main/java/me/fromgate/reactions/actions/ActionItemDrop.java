package me.fromgate.reactions.actions;

import java.util.List;
import java.util.Map;

import me.fromgate.reactions.util.ParamUtil;
import me.fromgate.reactions.util.Util;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class ActionItemDrop extends Action {
    @Override
    public boolean execute(Player p, Map<String, String> params) {
        int radius = ParamUtil.getParam(params, "radius", 0);
        Location loc = Util.parseLocation(ParamUtil.getParam(params, "loc", ""));
        if (loc == null) loc = p.getLocation();
        boolean scatter = ParamUtil.getParam(params, "scatter", true);
        boolean land = ParamUtil.getParam(params, "land", true);
        List<ItemStack> items = Util.parseRandomItems(ParamUtil.getParam(params, "item", ""));
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
