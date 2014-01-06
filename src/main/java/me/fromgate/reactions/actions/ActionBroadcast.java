package me.fromgate.reactions.actions;

import java.util.Map;

import org.bukkit.entity.Player;

public class ActionBroadcast extends Action {

    @Override
    public boolean execute(Player p, Map<String, String> params) {
        for (Player pl: plg().getServer().getOnlinePlayers())
            u().printMsg(pl, params.get("param-line"));
        return true;
    }

}
