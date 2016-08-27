package me.fromgate.reactions.util.playerselector;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

@SelectorDefine(key = "player")
public class Players extends PlayerSelector {

    @Override
    public Set<Player> selectPlayers(String param) {
        Set<Player> players = new HashSet<Player>();
        if (param.isEmpty()) return players;
        if (param.equalsIgnoreCase("null")) {
            players.add(null);
        } else if (param.equalsIgnoreCase("all")) {
            for (Player player : Bukkit.getOnlinePlayers())
                players.add(player);
        } else {
            String[] arrPlayers = param.split(",\\s*");
            for (String playerName : arrPlayers) {
                @SuppressWarnings("deprecation")
                Player targetPlayer = Bukkit.getPlayerExact(playerName);
                if ((targetPlayer != null) && (targetPlayer.isOnline())) players.add(targetPlayer);
            }
        }
        return players;
    }
}
