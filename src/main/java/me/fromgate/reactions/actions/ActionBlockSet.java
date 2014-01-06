package me.fromgate.reactions.actions;

import java.util.Map;
import me.fromgate.reactions.util.ParamUtil;
import me.fromgate.reactions.util.Util;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ActionBlockSet extends Action {

    @SuppressWarnings("deprecation")
    @Override
    public boolean execute(Player p, Map<String, String> params) {
        String istr = ParamUtil.getParam(params, "block", "");
        if (istr.isEmpty()) return false;
        ItemStack item = u().parseItemStack(istr);
        if ((item==null)||((!item.getType().isBlock()))){
            u().logOnce("wrongblock"+istr, "Failed to execute action BLOCK_SET. Wrong block "+istr.toUpperCase());
            return false;
        }
        Location loc = Util.parseLocation(ParamUtil.getParam(params, "loc", ""));
        if (loc == null) return false;
        loc.getBlock().setType(item.getType());
        loc.getBlock().setData(item.getData().getData());
        setMessageParam(Util.itemToString(item));
        return true;
    }

}
