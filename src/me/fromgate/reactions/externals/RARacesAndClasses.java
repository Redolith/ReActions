package me.fromgate.reactions.externals;

import me.fromgate.reactions.ReActions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import de.tobiyas.racesandclasses.APIs.ClassAPI;
import de.tobiyas.racesandclasses.APIs.RaceAPI;

public class RARacesAndClasses {

	private static boolean enabled = false;

	public static boolean isEnabled(){
		return enabled;
	}

	public static void init(){
		try{
			enabled = isRacesAndClassesInstalled();
		} catch (Exception e){
		}
		if (enabled) ReActions.util.log("RacesAndClasses found");
	}

	private static boolean isRacesAndClassesInstalled(){
		Plugin pe = Bukkit.getServer().getPluginManager().getPlugin("RacesAndClasses");
		return (pe != null);
	}

	// Флаг RNC_RACE
	public static boolean checkRace(Player player, String race){
		if (!enabled) return false;
		return RaceAPI.getRaceOfPlayer(player.getName()).getName().equalsIgnoreCase(race);
	}

	// Действие RNC_SET_RACE
	public static boolean setRace (Player player, String race){
		if (!enabled) return false;
		if (player == null) return false;
		return RaceAPI.addPlayerToRace(player.getName(), race);
	}

	// Флаг RNC_CLASS
	public static boolean checkClass(Player player, String className){
		if (!enabled) return false;
		return ClassAPI.getClassOfPlayer(player.getName()).getName().equalsIgnoreCase(className); 
	}

	// Действие RNC_SET_CLASS
	public static boolean setClass(Player player, String className){
		if (!enabled) return false;
		if (player == null) return false;
		return ClassAPI.addPlayerToClass(player.getName(), className);
	}



}
