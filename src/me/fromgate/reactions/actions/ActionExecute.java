package me.fromgate.reactions.actions;

import java.util.Map;
import me.fromgate.reactions.EventManager;
import me.fromgate.reactions.util.ParamUtil;
import org.bukkit.entity.Player;

public class ActionExecute extends Action {

    @Override
    public boolean execute(Player p, Map<String, String> params) {
        return execActivator (p, params);
    }

    public boolean execActivator (Player p, Map<String,String> params){
        String id = ParamUtil.getParam(params, "activator", "");
        if (id.isEmpty()) return false;
        setMessageParam(id);
        return EventManager.raiseExecEvent(p, params);
    }
    
}
