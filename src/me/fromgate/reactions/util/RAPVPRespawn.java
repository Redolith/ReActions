package me.fromgate.reactions.util;

import java.util.HashMap;
import java.util.Map;
import me.fromgate.reactions.event.PVPRespawnEvent;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

public class RAPVPRespawn {
    private static Map<String,String> killers = new HashMap<String,String>();
    private static Map<String,Location> deathpoints = new HashMap<String,Location>();
    
    public static void addPVPRespawn(PlayerDeathEvent event){
        Player deadplayer = event.getEntity();
        deathpoints.put(deadplayer.getName(), deadplayer.getLocation());  // это может пригодиться и в других ситуациях
        Player killer = Util.getKiller(deadplayer.getLastDamageCause());
        if (killer==null) return;
        killers.put(deadplayer.getName(), killer.getName());
    }
    
    public static Location getLastDeathPoint(Player player){
        if (deathpoints.containsKey(player.getName())) return deathpoints.get(player.getName());
        return player.getLocation();
    }
    
    public static Player getLastKiller (Player player){
        if (killers.containsKey(player.getName())) 
            return Bukkit.getPlayer(killers.get(player.getName()));
        return null;
    }
    
    public static void raisePVPRespawnEvent(Player player){
        if (!killers.containsKey(player.getName())) return;
        Player killer = getLastKiller(player);
        killers.remove(player.getName());
        if (killer == null) return;
        Bukkit.getServer().getPluginManager().callEvent(new PVPRespawnEvent(killer, player));
    }
    
}
