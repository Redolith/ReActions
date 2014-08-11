package me.fromgate.reactions.flags.factions;

import me.fromgate.reactions.externals.RAFactions;
import me.fromgate.reactions.flags.Flag;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Если два указанных игрока находятся в определенном отношении
 * Параметры: <игрок1> <игрок2> <отношение>
 * Список отношений: LEADER, OFFICER, MEMBER, RECRUIT, ALLY, TRUCE, NEUTRAL, ENEMY
 */
public class FlagPlayersInRel extends Flag {
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean checkFlag(Player p, String param) {
        if (!RAFactions.isFactionConnected()) return false;

        String[] params = param.split("\\s");
        Player player1 = Bukkit.getPlayer(params[0].trim());
        Player player2 = Bukkit.getPlayer(params[1].trim());
        String targetRel = params[2].trim();

        String playersRel = RAFactions.getRelationWith(player1, RAFactions.getPlayerFaction(player2));
        return targetRel.equalsIgnoreCase(playersRel);
    }
}
