package me.fromgate.reactions.actions;

import me.fromgate.reactions.util.Param;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ActionFly extends Action {
    @Override
    public boolean execute(Player p, Param params) {
        Player player;
        String playerName = params.getParam("player", p != null ? p.getName() : "");
        //noinspection deprecation
        player = playerName.isEmpty() ? null : Bukkit.getPlayerExact(playerName);
        AllowFly allow = AllowFly.NONE;
        AllowFly fly = AllowFly.NONE;
        if (params.hasAnyParam("allow")) {
            if (params.getParam("allow", true)) allow = AllowFly.TRUE;
            else allow = AllowFly.FALSE;
        }
        if (params.hasAnyParam("fly")) {
            if (params.getParam("fly", true)) fly = AllowFly.TRUE;
            else fly = AllowFly.FALSE;
        }

        return flyPlayer(player, allow, fly);
    }

    enum AllowFly {
        TRUE,
        FALSE,
        NONE
    }

    public boolean flyPlayer(Player player, AllowFly allow, AllowFly fly) {
        if (player == null || player.isDead() || !player.isOnline()) return false;

        if (allow == AllowFly.TRUE && fly == AllowFly.TRUE || allow == AllowFly.FALSE && fly == AllowFly.FALSE) {
            player.setAllowFlight(allow == AllowFly.TRUE);
            player.setFlying(fly == AllowFly.TRUE);
        }
        if (allow == AllowFly.TRUE && fly == AllowFly.FALSE) {
            if (!player.getAllowFlight()) player.setAllowFlight(true);
            if (player.isFlying()) player.setFlying(false);
        }
        if (allow == AllowFly.FALSE && fly == AllowFly.TRUE) {
            if (!player.isFlying()) player.setAllowFlight(false);
        }
        if (allow == AllowFly.TRUE && fly == AllowFly.NONE) {
            if (!player.getAllowFlight()) player.setAllowFlight(true);
        }
        if (allow == AllowFly.FALSE && fly == AllowFly.NONE) {
            if (player.getAllowFlight()) player.setAllowFlight(false);
        }
        if (allow == AllowFly.NONE && fly == AllowFly.TRUE) {
            if (!player.getAllowFlight()) player.setAllowFlight(true);
            player.setFlying(true);
        }
        if (allow == AllowFly.NONE && fly == AllowFly.FALSE) {
            player.setFlying(false);
        }

        return allow != AllowFly.NONE && fly != AllowFly.NONE;
    }
}
