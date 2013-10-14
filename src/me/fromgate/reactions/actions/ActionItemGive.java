package me.fromgate.reactions.actions;

import java.util.List;
import java.util.Map;

import me.fromgate.reactions.util.Util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ActionItemGive extends Action {

    @Override
    public boolean execute(Player p, Map<String, String> params) {
        List<ItemStack> items = giveItemPlayer(p,Util.getParam(params, "param-line", ""));
        if (items.isEmpty()) return false;
        this.setMessageParam(Util.itemsToString(items));
        return true;
    }
    
    
    private List<ItemStack> giveItemPlayer(final Player p, final String param) {
        final List<ItemStack> items = Util.parseRandomItems(param);
        if (!items.isEmpty())
            Bukkit.getScheduler().scheduleSyncDelayedTask(plg(), new Runnable(){
                public void run(){
                    for (ItemStack i : items)
                        u().giveItemOrDrop(p, i);
                }
            }, 1);
        return items;
    }

}
