package me.fromgate.reactions.actions;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import me.fromgate.reactions.externals.RAVault;
import me.fromgate.reactions.externals.RAWorldGuard;
import me.fromgate.reactions.util.ParamUtil;
import me.fromgate.reactions.util.Util;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class ActionMessage extends Action {

    @Override
    public boolean execute(Player p, Map<String, String> params) {
        sendMessage (p,params);
        return true;
    }
    
    
    private void sendMessage(Player player, Map<String, String> params){
        String message = params.get("param-line"); 
        Set<Player> players = new HashSet<Player>(); 
        if (Util.isAnyParamExist(params, "region","group","perm","world","player")){
            String region = ParamUtil.getParam(params, "region", "");
            String group = ParamUtil.getParam(params, "group", "");
            String perm = ParamUtil.getParam(params, "perm", "");
            String world = ParamUtil.getParam(params, "world", "");
            String targetPlayer = ParamUtil.getParam(params, "player", "");
            if (!region.isEmpty()) {
                players.addAll(RAWorldGuard.playersInRegion(region));
                message = message.replace("region:"+region, "");
            }
            if (!targetPlayer.isEmpty()) {
                message = message.replace("player:"+targetPlayer, "");
                Player tp = Bukkit.getPlayer(targetPlayer);
                if ((tp!=null)&&(tp.isOnline())) players.add(tp);
            }
            if (!world.isEmpty()){
                World w = Bukkit.getWorld(world);
                if (w!=null)  for (Player pl : w.getPlayers()) players.add(pl);
                message = message.replace("world:"+world, "");
            }
            if ((!group.isEmpty())||(!perm.isEmpty())){
                for (Player pl : Bukkit.getOnlinePlayers()){
                    if ((!group.isEmpty())&& RAVault.playerInGroup(pl, group)) players.add(pl);
                    if ((!perm.isEmpty())&&pl.hasPermission(perm)) players.add(pl);
                }
                if (!group.isEmpty()) message = message.replace("group:"+group, "");
                if (!perm.isEmpty()) message = message.replace("perm:"+perm, "");
            }
            message = message.replace("  ", " ");
            message = message.trim();
        } else if(player != null) players.add(player);
        for (Player p : players){
            String key = "reactions-msg-"+this.getActivatorName()+(this.isAction() ? "act" : "react");    
            boolean showmsg = false;
            if (this.getActivator().isAnnoying()){
                if (!p.hasMetadata(key)) {
                    showmsg = true;
                    p.setMetadata(key, new FixedMetadataValue(plg(),System.currentTimeMillis()));
                } else {
                    Long before = p.getMetadata(key).get(0).asLong();
                    Long now = System.currentTimeMillis();
                    if ((now-before)>(plg().sameMessagesDelay*1000)){
                        showmsg = true;
                        p.setMetadata(key, new FixedMetadataValue(plg(),now));
                    }
                }
            } else showmsg = true;
            if (showmsg) u().printMsg(p, message);
        }
    }

}
