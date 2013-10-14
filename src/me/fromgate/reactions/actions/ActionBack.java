package me.fromgate.reactions.actions;

import java.util.Map;

import me.fromgate.reactions.util.RAPushBack;
import me.fromgate.reactions.util.Util;

import org.bukkit.entity.Player;

public class ActionBack extends Action {

    @Override
    public boolean execute(Player p, Map<String, String> params) {
        int prev = Util.getParam(params, "param-line", 1);
        return RAPushBack.teleportToPrev(p, prev);
    }

}
