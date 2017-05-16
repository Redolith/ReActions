package me.fromgate.reactions.actions;

import me.fromgate.reactions.util.Param;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 2017-05-16.
 */
public class ActionFlySpeed extends Action {
    @Override
    public boolean execute(Player p, Param params) {
        Player player = p;
        double speed;
        if (params.hasAnyParam("speed", "player")) {
            String playerName = params.getParam("player", p != null ? p.getName() : "");
            //noinspection deprecation
            player = playerName.isEmpty() ? null : Bukkit.getPlayerExact(playerName);
            speed = params.getParam("speed", 0);
        } else speed = params.getParam("param-line", 0);
        return flySpeedPlayer(player, speed / 10);
    }

    public boolean flySpeedPlayer(Player player, double speed) {
        if (player == null || player.isDead() || !player.isOnline()) return false;
        if (speed > 1) speed = 1;
        if (speed < 0) speed = 0;
        player.setFlySpeed((float) speed);
        return true;
    }
}