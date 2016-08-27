package me.fromgate.reactions.actions;

import me.fromgate.reactions.externals.Externals;
import me.fromgate.reactions.externals.RAFactions;
import me.fromgate.reactions.util.Param;
import org.bukkit.entity.Player;

public class ActionFactionsPowerAdd extends Action {

    @Override
    public boolean execute(Player player, Param params) {
        if (!Externals.isConnectedFactions()) return false;
        RAFactions.addPower(player, params.getParam("power", 0.0));
        return true;
    }
}
