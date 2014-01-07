package me.fromgate.reactions.actions;

import java.util.Map;

import me.fromgate.reactions.externals.RAVault;
import me.fromgate.reactions.util.ParamUtil;

import org.bukkit.entity.Player;

public class ActionGroupAdd extends Action {

    @Override
    public boolean execute(Player p, Map<String, String> params) {
        return (RAVault.playerAddGroup(p, ParamUtil.getParam(params, "param-line", "")));
    }

}
