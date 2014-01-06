package me.fromgate.reactions.actions;

import java.util.Map;

import me.fromgate.reactions.util.ParamUtil;
import me.fromgate.reactions.util.Util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ActionItemRemove extends Action{

    @Override
    public boolean execute(Player p, Map<String, String> params) {
        String istr = ParamUtil.getParam(params, "param-line", "");
        if (istr.isEmpty()) return false; 
        if (!u().removeItemInHand(p, istr)) return false;
        ItemStack item = u().parseItemStack(istr);
        setMessageParam(Util.itemToString(item));
        return true;
    }

}
