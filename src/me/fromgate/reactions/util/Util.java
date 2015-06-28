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

package me.fromgate.reactions.util;

import me.fromgate.reactions.RAUtil;
import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.externals.Externals;
import me.fromgate.reactions.externals.RAFactions;
import me.fromgate.reactions.externals.RAVault;
import me.fromgate.reactions.externals.RAWorldGuard;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Util {

	private static RAUtil u(){
		return ReActions.util;
	}

	public static int getMinMaxRandom(String minmaxstr){
		int min = 0;
		int max = 0;
		String strmin = minmaxstr;
		String strmax = minmaxstr;

		if (minmaxstr.contains("-")){
			strmin = minmaxstr.substring(0,minmaxstr.indexOf("-"));
			strmax = minmaxstr.substring(minmaxstr.indexOf("-")+1);
		}
		if (strmin.matches("[1-9]+[0-9]*")) min = Integer.parseInt(strmin);
		max = min;
		if (strmax.matches("[1-9]+[0-9]*")) max = Integer.parseInt(strmax);
		if (max>min) return min + u().tryChance(1+max-min);
		else return min;
	}

	public static String soundPlay (Location loc, Param params){
		if (params.isEmpty()) return "";
		Location soundLoc = loc;
		String sndstr = "";
		String strvolume ="1";
		String strpitch = "1";
		float pitch = 1;
		float volume = 1;
		if (params.hasAnyParam("param")){
			String param = params.getParam("param", "");
			if (param.isEmpty()) return "";
			if (param.contains("/")){
				String[] prm = param.split("/");
				if (prm.length>1){
					sndstr = prm[0];
					strvolume = prm[1];
					if (prm.length>2) strpitch = prm[2];
				}
			} else sndstr = param;
			if (strvolume.matches("[0-9]+-?\\.[0-9]*")) volume = Float.parseFloat(strvolume);
			if (strpitch.matches("[0-9]+-?\\.[0-9]*")) pitch = Float.parseFloat(strpitch);            
		} else {
			String locationStr = params.getParam("loc");
			soundLoc = locationStr.isEmpty() ? loc : Locator.parseLocation(locationStr, null);
			sndstr = params.getParam("type", "");
			pitch = params.getParam("pitch", 1.0f);
			volume = params.getParam("volume", 1.0f);
		}
		Sound sound = getSoundStr (sndstr);
		if (soundLoc!=null) soundLoc.getWorld().playSound(soundLoc, sound, volume, pitch);
		return sound.name();
	}

	public static void soundPlay (Location loc, String param){
		if (param.isEmpty()) return;
		/*Map<String,String> params = new HashMap<String,String>();
		params.put("param", param); */
		Param params = new Param (param,"param");
		soundPlay (loc, params);
	}


	private static Sound getSoundStr(String param) {
		Sound snd = null;
		try{
			snd= Sound.valueOf(param.toUpperCase());
		} catch(Exception e){
		}
		if (snd == null) snd = Sound.CLICK; 
		return snd;
	}


	public static List<Entity> getEntities (Location l1, Location l2){
		List<Entity> entities = new ArrayList<Entity>();
		if (!l1.getWorld().equals(l2.getWorld())) return entities;
		int x1 = Math.min(l1.getBlockX(), l2.getBlockX());
		int x2 = Math.max(l1.getBlockX(), l2.getBlockX());
		int y1 = Math.min(l1.getBlockY(), l2.getBlockY());
		int y2 = Math.max(l1.getBlockY(), l2.getBlockY());
		int z1 = Math.min(l1.getBlockZ(), l2.getBlockZ());
		int z2 = Math.max(l1.getBlockZ(), l2.getBlockZ());
		int chX1 = x1>>4;
		int chX2 = x2>>4;
		int chZ1 = z1>>4;
		int chZ2 = z2>>4;
		for (int x = chX1; x<=chX2; x++)
			for (int z = chZ1; z<=chZ2; z++){
				for (Entity e : l1.getWorld().getChunkAt(x, z).getEntities()){
					double ex = e.getLocation().getX();
					double ey = e.getLocation().getY();
					double ez = e.getLocation().getZ();
					if ((x1<=ex)&&(ex<=x2)&&(y1<=ey)&&(ey<=y2)&&(z1<=ez)&&(ez<=z2))
						entities.add(e);
				}
			}
		return entities;
	}


	@SuppressWarnings("deprecation")
	public static String replaceStandartLocations (Player p, String param){
		if (p==null) return param;
		Location targetBlock = null;
		try{
			targetBlock = p.getTargetBlock(null, 100).getLocation();
		} catch(Exception e){
		}
		Map<String,Location> locs = new HashMap<String,Location>();
		locs.put("%here%", p.getLocation());
		locs.put("%eye%", p.getEyeLocation());
		locs.put("%head%", p.getEyeLocation());
		locs.put("%viewpoint%", targetBlock);
		locs.put("%view%", targetBlock);
		locs.put("%selection%", Selector.getSelectedLocation(p));
		locs.put("%select%", Selector.getSelectedLocation(p));
		locs.put("%sel%", Selector.getSelectedLocation(p));
		String newparam = param;
		for (String key : locs.keySet()){
			Location l = locs.get(key);
			if (l==null) continue;
			newparam = newparam.replace(key, Locator.locationToString(l));
		}
		return newparam;
	} 
 
	public static PotionEffectType parsePotionEffect (String name) {
		PotionEffectType pef = null;
		try{
			pef = PotionEffectType.getByName(name);
		} catch(Exception e){
		}
		return pef;
	}

	public static Player getKiller(EntityDamageEvent event){
		if (event instanceof EntityDamageByEntityEvent){
			EntityDamageByEntityEvent evdmg = (EntityDamageByEntityEvent)event;
			if (evdmg.getDamager().getType() == EntityType.PLAYER) return (Player) evdmg.getDamager();
			if (evdmg.getCause() == DamageCause.PROJECTILE){
				Projectile prj = (Projectile) evdmg.getDamager();
				LivingEntity shooterEntity = BukkitCompatibilityFix.getShooter(prj);
				if (shooterEntity == null) return null;
				if (shooterEntity instanceof Player) return (Player) shooterEntity;
			}
		}
		return null;
	}


	public static LivingEntity getDamagerEntity(EntityDamageEvent event){
		if (event instanceof EntityDamageByEntityEvent){
			EntityDamageByEntityEvent evdmg = (EntityDamageByEntityEvent)event;
			if (evdmg.getCause() == DamageCause.PROJECTILE){
				Projectile prj = (Projectile) evdmg.getDamager();
				LivingEntity shooterEntity = BukkitCompatibilityFix.getShooter(prj);
				if (shooterEntity == null) return null;
				return shooterEntity;
			} else if (evdmg.getDamager() instanceof LivingEntity) return (LivingEntity) evdmg.getDamager();
		}
		return null;
	}

	public static boolean isAnyParamExist (Map<String,String> params, String... param){
		for (String key : params.keySet())
			for (String prm : param){
				if (key.equalsIgnoreCase(prm)) return true;
			}
		return false;
	}

	public static boolean setOpen (Block b, boolean open){
		BlockState state = b.getState();
		if (!(state.getData() instanceof Openable)) return false;
		Openable om = (Openable) state.getData();
		om.setOpen(open);
		state.setData((MaterialData) om);
		state.update();
		return true;
	}

	public static boolean isOpen(Block b){
		if (b.getState().getData() instanceof Openable){
			Openable om = (Openable) b.getState().getData();
			return om.isOpen();
		}
		return false; 
	}

	public static Block getDoorBottomBlock(Block b){
		if (b.getType()!=Material.WOODEN_DOOR) return b;
		if (b.getRelative(BlockFace.DOWN).getType()==Material.WOODEN_DOOR)
			return b.getRelative(BlockFace.DOWN);
		return b;
	}

	public static boolean isDoorBlock (Block b){
		if (b.getType() == Material.WOODEN_DOOR) return true;
		if (b.getType() == Material.TRAP_DOOR) return true;
		if (b.getType() == Material.FENCE_GATE) return true;
		return false;
	}

	//region,rgplayer,player,world,faction,group,perm
	public static Set<Player> getPlayerList(Param param, Player singlePlayer){
		Set<Player> players = new HashSet<Player>();
		if (param.hasAnyParam("region","rgplayer","player","world","faction","group","perm")){
			// Players in regions
			if (RAWorldGuard.isConnected()){
				String regionNames = param.getParam("region", "");
				if (regionNames.isEmpty()) regionNames = param.getParam("rgplayer", "");
				String[] arrRegion = regionNames.split(",");
				for (String regionName: arrRegion)
					players.addAll(RAWorldGuard.playersInRegion(regionName));
			}

			// Players in faction
			if (Externals.isConnectedFactions()){
				String factionNames = param.getParam("faction", "");
				String [] arrFaction = factionNames.split(",");
				for (String factionName : arrFaction)
					players.addAll(RAFactions.playersInFaction(factionName));
			}

			// Players in worlds
			String worldNames = param.getParam("world", "");
			String[] arrWorlds = worldNames.split(",");
			for (String worldName: arrWorlds){
				World world = Bukkit.getWorld(worldName);
				if (world ==null) continue;
				for (Player p : world.getPlayers()) players.add(p);
			}

			// Player by permission & group
			String group = param.getParam("group", "");
			String perm = param.getParam("perm", "");
			if ((!group.isEmpty())||(!perm.isEmpty())){
				for (Player pl : Bukkit.getOnlinePlayers()){
					if ((!group.isEmpty())&& RAVault.playerInGroup(pl, group)) players.add(pl);
					if ((!perm.isEmpty())&&pl.hasPermission(perm)) players.add(pl);
				}
			}

			// Players by name (all = all players, null - empty player)
			String playerNames = param.getParam("player", "");
			if (playerNames.equalsIgnoreCase("all")){
				players.clear();
				for (Player player : Bukkit.getOnlinePlayers())
					players.add(player);				
			} else if (playerNames.equalsIgnoreCase("null")){
				players.clear();
				players.add(null);
			} else {
				String[] arrPlayers = playerNames.split(",");
				for (String playerName: arrPlayers){
					@SuppressWarnings("deprecation")
					Player targetPlayer = Bukkit.getPlayerExact(playerName);
					if ((targetPlayer !=null)&&(targetPlayer.isOnline()))players.add(targetPlayer);	
				}
			}
		}
		if (players.isEmpty()&& singlePlayer != null) players.add(singlePlayer);
		return players;
	}


}
