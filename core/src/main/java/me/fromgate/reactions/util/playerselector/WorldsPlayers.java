package me.fromgate.reactions.util.playerselector;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

@SelectorDefine(key = "world")
public class WorldsPlayers extends PlayerSelector {

    @Override
    public Set<Player> selectPlayers(String worldNames) {
        Set<Player> players = new HashSet<>();
        if (!worldNames.isEmpty()) {
            String[] arrWorlds = worldNames.split(",\\s*");
            for (String worldName : arrWorlds) {
                World world = Bukkit.getWorld(worldName);
                if (world == null) continue;
                players.addAll(world.getPlayers());
            }
        }
        return players;
    }

}
