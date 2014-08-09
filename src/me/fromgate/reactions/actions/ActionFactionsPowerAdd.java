package me.fromgate.reactions.actions;

import com.massivecraft.factions.entity.UPlayer;
import me.fromgate.reactions.externals.RAFactions;
import me.fromgate.reactions.util.ParamUtil;
import org.bukkit.entity.Player;

import java.util.Map;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class ActionFactionsPowerAdd extends Action {
    
    @Override
    public boolean execute(Player p, Map<String, String> params) {
        if (!RAFactions.isFactionConnected()) return false;
        
        double value = ParamUtil.getParam(params, "value", 0.0);
        UPlayer player = UPlayer.get(p);
        
        double currentPower = player.getPower();
        double newPower = min(player.getPowerMax(), max(currentPower + value, player.getPowerMin()));
        player.setPower(newPower);
        
        return true;
    }
}
