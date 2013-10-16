package me.fromgate.reactions.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.fromgate.reactions.EventManager;
import me.fromgate.reactions.activators.Activator;
import me.fromgate.reactions.util.RAWorldGuard;
import me.fromgate.reactions.util.Util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ActionExecute extends Action {

    @Override
    public boolean execute(Player p, Map<String, String> params) {
        return execActivator (p, params);
    }

    public boolean execActivator (Player p, Map<String,String> params){
        String id = "";
        String tps= "";
        List<Player> targetPlayers = new ArrayList<Player>();
        Player targetPlayer = p;
        long delay = 1;
        if (params.containsKey("param")) id = Util.getParam(params, "param", "");
        else {
            tps = Util.getParam(params, "player", "");
            targetPlayer = (tps.isEmpty() ? p : Bukkit.getPlayer(tps));
            delay = Util.timeToTicks(Util.parseTime(Util.getParam(params, "delay", "1000")));
            id = Util.getParam(params, "exec", "");
            String region = Util.getParam(params, "rgplayer", "");
            if (!region.isEmpty()) targetPlayers = RAWorldGuard.playersInRegion(region);
            else targetPlayers.add(targetPlayer);
        }
        if (id.isEmpty()) return false;
        if (targetPlayers.isEmpty()) return false;
        for (Player player : targetPlayers) execActivator (p,player,id, delay);
        setMessageParam(id);
        return true;
    }
    
    public void execActivator(final Player p, final Player targetPlayer, final String id, long delay_ticks){
        Activator act = plg().getActivator(id);
        if (act == null) {
            u().logOnce("wrongact_"+id, "Failed to run exec activator "+id+". Activator not found.");
            return;
        }

        if (!act.getType().equalsIgnoreCase("exec")){
            u().logOnce("wrongactype_"+id, "Failed to run exec activator "+id+". Wrong activator type.");
            return;
        }


        Bukkit.getScheduler().runTaskLater(plg(), new Runnable(){
            @Override
            public void run() {
                EventManager.raiseExecEvent(p, targetPlayer, id);
            }
        }, Math.max(1, delay_ticks));
    }
    
}
