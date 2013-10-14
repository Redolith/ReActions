package me.fromgate.reactions.actions;

import java.util.Map;

import me.fromgate.reactions.util.RAVault;
import me.fromgate.reactions.util.Util;

import org.bukkit.entity.Player;

public class ActionGroupRemove extends Action{

    @Override
    public boolean execute(Player p, Map<String, String> params) {
        String param = Util.getParam(params, "param-line", "");
        if (RAVault.playerInGroup(p, param)) {
            if (!RAVault.playerRemoveGroup(p, param)) return false;
        }
        return true;
    }

}
