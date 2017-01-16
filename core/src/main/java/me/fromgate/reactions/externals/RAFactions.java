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

package me.fromgate.reactions.externals;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import me.fromgate.reactions.ReActions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class RAFactions {

    private static boolean enabled = false;
    private static FactionListener listener;

    protected static boolean init() {
        Plugin pf = Bukkit.getServer().getPluginManager().getPlugin("Factions");
        if (pf == null) return false;
        try {
            listener = new FactionListener();
            Bukkit.getPluginManager().registerEvents(listener, ReActions.instance);
            ReActions.util.log("Factions found");
            enabled = true;
        } catch (Throwable t) {
            enabled = false;
        }
        return enabled;
    }

    public static String getPlayerFaction(Player player) {
        if (!enabled) return "";
        MPlayer uplayer = MPlayer.get(player);
        return uplayer.getFaction().isDefault() ? "default" : uplayer.getFaction().getName();
    }

    public static boolean isPlayerInFaction(Player player, String faction) {
        if (!enabled) return false;
        if (player == null) return false;
        return faction.equalsIgnoreCase(getPlayerFaction(player));
    }

    public static List<Player> playersInFaction(String factionName) {
        List<Player> players = new ArrayList<Player>();
        if (!enabled) return players;
        Faction faction = getFactionByName(factionName);
        if (faction == null) return players;
        for (MPlayer uplayer : faction.getMPlayers()) {
            if (uplayer.isOffline()) continue;
            players.add(uplayer.getPlayer());
        }
        return players;
    }

    public static Faction getFactionByName(String factionName) {
        for (Faction faction : FactionColl.get().getAll()) {
            if (faction.isDefault() && factionName.equalsIgnoreCase("default")) return faction;
            if (faction.getName().equalsIgnoreCase(factionName)) return faction;
        }
        return null;
    }

    public static String getFactionAt(Location loc) {
        return BoardColl.get().getFactionAt(PS.valueOf(loc)).getName();
    }

    public static String getRelationWith(Player player, String withFactionStr) {
        MPlayer uplayer = MPlayer.get(player);
        return uplayer.getRelationTo(getFactionByName(withFactionStr)).toString();
    }

    public static void addPower(Player player, double value) {
        MPlayer uplayer = MPlayer.get(player);
        double currentPower = uplayer.getPower();
        double newPower = min(uplayer.getPowerMax(), max(currentPower + value, uplayer.getPowerMin()));
        uplayer.setPower(newPower);
    }
}
