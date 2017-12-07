package me.fromgate.reactions.actions;

import me.fromgate.reactions.externals.RAWorldEdit;
import me.fromgate.reactions.util.Param;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 18/10/2017.
 */
public class ActionWESuperPickaxe extends Action {
    @Override
    public boolean execute(Player p, Param params) {
        Player player = p;
        boolean isSP;
        if (params.hasAnyParam("value", "player")) {
            String playerName = params.getParam("player", p != null ? p.getName() : "");
            isSP = params.getParam("value", false);
            //noinspection deprecation
            player = playerName.isEmpty() ? null : Bukkit.getPlayerExact(playerName);
        } else isSP = params.getParam("param-line", false);

        if (isSP) RAWorldEdit.getSession(player).enableSuperPickAxe();
        else RAWorldEdit.getSession(player).disableSuperPickAxe();
        return true;

    }
}
