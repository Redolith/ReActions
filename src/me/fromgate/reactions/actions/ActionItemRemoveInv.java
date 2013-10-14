package me.fromgate.reactions.actions;

import java.util.Map;

import me.fromgate.reactions.util.Util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ActionItemRemoveInv extends Action {

    @Override
    public boolean execute(Player p, Map<String, String> params) {
        String istr = Util.getParam(params, "param-line", "");
        if (istr.isEmpty()) return false; 
        u().removeItemInInventory(p, istr);
        ItemStack item = u().parseItemStack(istr);
        setMessageParam(Util.itemToString(item));
        return true;
    }


}
