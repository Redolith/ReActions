package me.fromgate.reactions.actions;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ActionCommandOp extends Action{
    @Override
    public boolean execute(Player p, Map<String, String> params) {
        if (p == null) return false;
        boolean isop = p.isOp();
        p.setOp(true);
        CommandSender sender = p;
        plg().getServer().dispatchCommand(sender, ChatColor.translateAlternateColorCodes('&', params.get("param-line")));
        p.setOp(isop);
        return true;
    }


}
