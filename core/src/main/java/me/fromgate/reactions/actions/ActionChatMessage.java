package me.fromgate.reactions.actions;

import me.fromgate.reactions.util.Param;
import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 2017-10-04.
 */
public class ActionChatMessage extends Action {
    @Override
    public boolean execute(Player p, Param params) {
        if (p != null) {
            String msg = params.getParam("param-line");
            msg = msg.replaceFirst("^[\\s\\/]+", "");
            p.chat(msg);
        }
        return true;
    }
}
