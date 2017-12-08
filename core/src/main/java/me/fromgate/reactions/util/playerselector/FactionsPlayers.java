package me.fromgate.reactions.util.playerselector;

import me.fromgate.reactions.externals.Externals;
import me.fromgate.reactions.externals.RaFactions;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

@SelectorDefine(key = "faction")
public class FactionsPlayers extends PlayerSelector {

    @Override
    public Set<Player> selectPlayers(String factionNames) {
        Set<Player> players = new HashSet<>();
        if (!Externals.isConnectedFactions()) return players;
        if (factionNames.isEmpty()) return players;
        String[] arrFaction = factionNames.split(",\\s*");
        for (String factionName : arrFaction)
            players.addAll(RaFactions.playersInFaction(factionName));
        return players;
    }


}
