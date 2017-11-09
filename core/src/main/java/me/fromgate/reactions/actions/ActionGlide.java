package me.fromgate.reactions.actions;

import me.fromgate.reactions.util.BukkitCompatibilityFix;
import me.fromgate.reactions.util.Param;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 5/8/2017.
 */
public class ActionGlide extends Action {
    @Override
    public boolean execute(Player p, Param params) {
        Player player;
        String playerName = params.getParam("player", p != null ? p.getName() : "");
        //noinspection deprecation
        player = playerName.isEmpty() ? null : Bukkit.getPlayerExact(playerName);
        Boolean isGlide = params.getParam("glide", true);
        return glidePlayer(player, isGlide);
    }

    public boolean glidePlayer(Player player, Boolean isGlide) {
        if (player == null || player.isDead() || !player.isOnline()) return false;
        BukkitCompatibilityFix.setGliding(player, isGlide);
        return true;
    }
}
