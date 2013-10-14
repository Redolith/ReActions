package me.fromgate.reactions.actions;

import java.util.Map;

import me.fromgate.reactions.util.RATowny;
import me.fromgate.reactions.util.Util;

import org.bukkit.entity.Player;

public class ActionTownSet extends Action {

    @Override
    public boolean execute(Player p, Map<String, String> params) {
        if (!plg().isTownyConnected()) return false;
        String town = Util.getParam(params, "param-line", "");
        if (town.isEmpty()) return false;
        RATowny.addToTown(p, town);
        return true;
    }

}
