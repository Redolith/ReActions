package me.fromgate.reactions.actions;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ActionCommand extends Action {

    @Override
    public boolean execute(Player p, Map<String, String> params) {
        if (p == null) return false;
        CommandSender sender = p;
        plg().getServer().dispatchCommand(sender, ChatColor.translateAlternateColorCodes('&', params.get("param-line")));
        return true;
    }

}
