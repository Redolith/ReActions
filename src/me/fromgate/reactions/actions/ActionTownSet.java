package me.fromgate.reactions.actions;

import java.util.Map;

import me.fromgate.reactions.externals.RATowny;
import me.fromgate.reactions.util.ParamUtil;

import org.bukkit.entity.Player;

public class ActionTownSet extends Action {

    @Override
    public boolean execute(Player p, Map<String, String> params) {
        if (!plg().isTownyConnected()) return false;
        String town = ParamUtil.getParam(params, "param-line", "");
        if (town.isEmpty()) return false;
        RATowny.addToTown(p, town);
        return true;
    }

}
