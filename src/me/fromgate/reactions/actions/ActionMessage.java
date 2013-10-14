package me.fromgate.reactions.actions;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import me.fromgate.reactions.util.RAVault;
import me.fromgate.reactions.util.RAWorldGuard;
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
        if (Util.isAnyParamExist(params, "region","group","perm","world")){
            String region = Util.getParam(params, "region", "");
            String group = Util.getParam(params, "group", "");
            String perm = Util.getParam(params, "perm", "");
            String world = Util.getParam(params, "world", "");
            if (!region.isEmpty()) players.addAll(RAWorldGuard.playersInRegion(region));
            if (!world.isEmpty()){
                World w = Bukkit.getWorld(world);
                if (w!=null)  for (Player pl : w.getPlayers()) players.add(pl);
            }
            if ((!group.isEmpty())||(!perm.isEmpty()))
                for (Player pl : Bukkit.getOnlinePlayers()){
                    if ((!group.isEmpty())&& RAVault.playerInGroup(pl, group)) players.add(pl);
                    if ((!perm.isEmpty())&&pl.hasPermission(perm)) players.add(pl);
                }        
            message = message.replace("region:"+region, "").replace("group:"+group, "").replace("perm:"+perm, "").replace("world:"+world, "");
            message = message.replace("  ", " ");
            message = message.trim();
        } else if(player != null) players.add(player);
        //if (players.isEmpty()&&(player != null)) players.add(player);
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
                    if ((now-before)>(plg().same_msg_delay*1000)){
                        showmsg = true;
                        p.setMetadata(key, new FixedMetadataValue(plg(),now));
                    }
                }
            } else showmsg = true;
            if (showmsg) u().printMsg(p, message);
        }
    }

    
    /*
     *  public static String replacePlaceholders (Player p, Activator a, String param){
        String rst = param;
        String placeholders = "curtime,player,dplayer,health,"+Flag.getFtypes();
        String [] phs = placeholders.split(",");
        for (String ph : phs){
            String key = "%"+ph+"%";
            rst = rst.replaceAll(key, getFlagParam(p,a,key));
        }
        return rst;
    }
     */
    
}
