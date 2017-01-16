package me.fromgate.reactions.util.playerselector;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

@SelectorDefine(key = "group")
public class PermPlayers extends PlayerSelector {

    @Override
    public Set<Player> selectPlayers(String param) {
        Set<Player> players = new HashSet<Player>();
        if (param.isEmpty()) return players;
        String[] perms = param.split(",\\s*");
        for (Player player : Bukkit.getOnlinePlayers())
            for (String p : perms)
                if (player.hasPermission(p)) players.add(player);
        return players;
    }

}
