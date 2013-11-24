package me.fromgate.reactions.actions;

import java.util.Map;
import me.fromgate.reactions.activators.Activators;
import me.fromgate.reactions.util.ParamUtil;
import org.bukkit.entity.Player;

public class ActionExecStop extends Action{

    @Override
    public boolean execute(Player p, Map<String, String> params) {
        String player = ParamUtil.getParam(params, "player", (p == null ? "" : p.getName()));
        if (player.isEmpty()) return false;
        String activator = ParamUtil.getParam(params, "activator", "");
        if (activator.isEmpty()) return false;
        Activators.stopExec(player, activator);
        setMessageParam(activator);
        return true;
    }

}
