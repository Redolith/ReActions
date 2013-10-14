package me.fromgate.reactions.actions;

import java.util.Map;

import me.fromgate.reactions.util.RAVault;
import me.fromgate.reactions.util.Util;

import org.bukkit.entity.Player;

public class ActionGroupAdd extends Action {

    @Override
    public boolean execute(Player p, Map<String, String> params) {
        return (RAVault.playerAddGroup(p, Util.getParam(params, "param-line", "")));
    }

}
