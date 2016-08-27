/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2014, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/reactions/
 *    
 *  This file is part of ReActions.
 *  
 *  ReActions is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ReActions is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with ReActions.  If not, see <http://www.gnorg/licenses/>.
 * 
 */

package me.fromgate.reactions.flags.factions;

import me.fromgate.reactions.externals.Externals;
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
        if (!Externals.isConnectedFactions()) return false;
        String[] params = param.split("\\s");
        Player player1 = Bukkit.getPlayer(params[0].trim());
        Player player2 = Bukkit.getPlayer(params[1].trim());
        String targetRel = params[2].trim();
        String playersRel = RAFactions.getRelationWith(player1, RAFactions.getPlayerFaction(player2));
        return targetRel.equalsIgnoreCase(playersRel);
    }
}
