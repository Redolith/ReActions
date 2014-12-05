package me.fromgate.reactions.flags;

import java.util.Map;

import me.fromgate.reactions.util.Locator;
import me.fromgate.reactions.util.ParamUtil;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FlagBlock extends Flag {

	@Override
	public boolean checkFlag(Player p, String param) {
		if (param.isEmpty()) return false;
		Map<String,String> params = ParamUtil.parseParams(param, "loc");
		Location loc = Locator.parseLocation(ParamUtil.getParam(params, "loc", ""), null);
		if (loc == null) return false;
		String istr = ParamUtil.getParam(params, "block", "");
		if (istr.isEmpty()) return loc.getBlock().getType() != Material.AIR;
		ItemStack item = u().parseItemStack(istr);
		if ((item==null)||((!item.getType().isBlock()))){
			u().logOnce("wrongblockflag"+istr, "Failed to check flag BLOCK. Wrong block "+istr.toUpperCase()+" Parameters: "+param);
			return false;
		}
		return loc.getBlock().getType() == item.getType();
	}

}
