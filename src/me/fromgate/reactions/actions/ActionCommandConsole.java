package me.fromgate.reactions.actions;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ActionCommandConsole extends Action {

    @Override
    public boolean execute(Player p, Map<String, String> params) {
        plg().getServer().dispatchCommand(Bukkit.getConsoleSender(), ChatColor.translateAlternateColorCodes('&', params.get("param-line")));
        return true;
    }

}
