package me.fromgate.reactions.actions;

import java.util.Map;

import me.fromgate.reactions.util.Util;

import org.bukkit.entity.Player;

public class ActionSound extends Action {
    @Override
    public boolean execute(Player p, Map<String, String> params) {
        String str = Util.soundPlay(p.getLocation(), params);
        if (str.isEmpty()) return false;
        this.setMessageParam(str);
        return true;
    }
}
