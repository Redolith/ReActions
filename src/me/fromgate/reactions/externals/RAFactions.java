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

import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColls;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.mcore.ps.PS;
import me.fromgate.reactions.ReActions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class RAFactions {

	private static boolean enabled = false;
	private static FactionListener listener;

	public static void init(){
		enabled = isFactionsInstalled();
		if (!enabled) return;
		ReActions.util.log("Factions found");
		listener = new FactionListener();
		Bukkit.getPluginManager().registerEvents(listener,ReActions.instance);
	}

	public static boolean isFactionConnected(){
		return enabled;
	}
	
	private static boolean isFactionsInstalled() {
		Plugin pf = Bukkit.getServer().getPluginManager().getPlugin("Factions");
		if (pf == null) return false;
		try {
			return  (pf instanceof com.massivecraft.factions.Factions);	
		} catch (Throwable e){
			return false;
		}
	}

	public static String getPlayerFaction(Player player){
		if (!enabled) return "";
		UPlayer uplayer = UPlayer.get(player);
		return uplayer.getFaction().isDefault() ?  "default" : uplayer.getFaction().getName();
	}

	public static boolean isPlayerInFaction(Player player, String faction){
		if (!enabled) return false;
		if (player == null) return false;
		return faction.equalsIgnoreCase(getPlayerFaction (player));
	}

	public static List<Player> playersInFaction (String factionName){
		List<Player> players  = new ArrayList<Player>();
		if (!enabled) return players;
		Faction faction = getFactionByName (factionName);
		if (faction==null) return players;
		for (UPlayer uplayer: faction.getUPlayers()){
			if (uplayer.isOffline()) continue;
			players.add(uplayer.getPlayer());
		}
		return players;
	}

	public static Faction getFactionByName(String factionName){
		for (World world : Bukkit.getWorlds()){
			for (Faction faction : FactionColls.get().getForWorld(world.getName()).getAll()){
				if (faction.isDefault()&&factionName.equalsIgnoreCase("default")) return faction;
				if (faction.getName().equalsIgnoreCase(factionName)) return faction;
			}
		}
		return null;
	}
    
    public static String getFactionAt(Location loc) {
        return BoardColls.get().getFactionAt(PS.valueOf(loc)).getName();
    }
    
    public static String getRelationWith(Player player, String withFactionStr) {
        UPlayer uplayer = UPlayer.get(player);
        return uplayer.getRelationTo(getFactionByName(withFactionStr)).toString();
    }
}
