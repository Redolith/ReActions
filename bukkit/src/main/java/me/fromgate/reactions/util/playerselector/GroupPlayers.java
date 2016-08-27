package me.fromgate.reactions.util.playerselector;

import me.fromgate.reactions.externals.RAVault;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

@SelectorDefine(key = "group")
public class GroupPlayers extends PlayerSelector {

    @Override
    public Set<Player> selectPlayers(String param) {
        Set<Player> players = new HashSet<Player>();
        if (!RAVault.isPermissionConected()) return players;
        if (param.isEmpty()) return players;
        String[] group = param.split(",\\s*");
        for (Player player : Bukkit.getOnlinePlayers())
            for (String g : group)
                if (RAVault.playerInGroup(player, g)) players.add(player);
        return players;
    }
}
