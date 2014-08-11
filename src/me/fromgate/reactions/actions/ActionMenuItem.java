package me.fromgate.reactions.actions;

import java.util.Map;
import me.fromgate.reactions.menu.InventoryMenu;
import org.bukkit.entity.Player;

public class ActionMenuItem extends Action {

	@Override
	public boolean execute(Player player, Map<String, String> params) {
		return InventoryMenu.createAndOpenInventory(player, params);
	}

}
