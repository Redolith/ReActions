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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import me.fromgate.reactions.RAUtil;
import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.externals.RAFactions;
import me.fromgate.reactions.externals.RAVault;
import me.fromgate.reactions.externals.RAWorldGuard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;
import org.bukkit.potion.PotionEffectType;

public class Util {

	private static ReActions plg(){
		return ReActions.instance;
	}


	private static RAUtil u(){
		return ReActions.util;
	}

	// world,x,y,z,[yaw,pitch]
	// world,x,y,z,[yaw,pitch]@radius
	public static Location parseLocation (String strloc){
		// Радиус пока игнорируем....
		Location loc = null;
		if (strloc.isEmpty()) return null;
		String [] lr = strloc.split("@");
		String sloc = lr[0];
		int radius = 0;
		if ((lr.length==2)&&(lr[1].matches("[1-9]+[0-9]*"))) radius = Integer.parseInt(lr[1]);
		String [] ln = sloc.split(",");
		if (!((ln.length==4)||(ln.length==6))) return null;
		World w = Bukkit.getWorld(ln[0]);
		if (w==null) return null;
		for (int i = 1; i<ln.length; i++){
			if (!(u().isInteger(ln[i])||ln[i].matches("-?[0-9]+[0-9]*\\.[0-9]+")||ln[i].matches("-?[1-9]+[0-9]*"))) return null;
		}

		loc = new Location (w, Double.parseDouble(ln[1]),Double.parseDouble(ln[2]),Double.parseDouble(ln[3]));
		if (ln.length==6){
			loc.setYaw(Float.parseFloat(ln[4]));
			loc.setPitch(Float.parseFloat(ln[5]));
		}
		if (radius >0) loc = getRandomLocationInRadius (loc, radius,true);
		return loc;
	}


	public static List<Location> getLocationsRadius (Location center, int radius, boolean land){
		List<Location> locs = new ArrayList<Location>();
		if (radius>0){
			for (int x = center.getBlockX()-radius; x<=center.getBlockX()+radius; x++)
				for (int y = center.getBlockY()-radius; y<=center.getBlockY()+radius; y++)
					for (int z = center.getBlockZ()-radius; z<=center.getBlockZ()+radius; z++){
						Location t = new Location (center.getWorld(),x,y,z,center.getYaw(),center.getPitch());
						if (t.getBlock().isEmpty()&&t.getBlock().getRelative(BlockFace.UP).isEmpty()){
							if (land&&t.getBlock().getRelative(BlockFace.DOWN).isEmpty()) continue;
							t.add(center.getX()-center.getBlockX(),center.getY()-center.getBlockY(),center.getZ()-center.getBlockZ());
							t.setY(center.getY());
							t.setPitch(center.getPitch());
							locs.add(t);
						}
					}
		} 
		return locs;
	}

	public static List<Location> getLocationsRegion (String region, boolean land){
		return RAWorldGuard.getRegionLocations(region, land);
	}


	public static Location getRandomLocationList(List<Location> locs){
		if ((locs == null)||locs.isEmpty()) return null;
		return locs.get(u().tryChance(locs.size()));
	}

	public static Location getRandomLocationInRadius(Location l, int radius, boolean land){
		Location loc = l;
		if (radius>0) {
			List<Location> emptyloc = getLocationsRadius(l,radius,land);
			if (!emptyloc.isEmpty()) {
				loc = emptyloc.get(u().tryChance(emptyloc.size()));
				loc.add(l.getX()-l.getBlockX(), l.getY()-l.getBlockY(), l.getZ()-l.getBlockZ());
				loc.setYaw(l.getYaw());
				loc.setPitch(l.getPitch());
			}
		}
		return loc;
	}

	public static String locationToStringFormated(Location loc){
		if (loc == null) return "";
		DecimalFormat fmt = new DecimalFormat("####0.##");
		String lstr = loc.toString();
		try {
			lstr = "["+loc.getWorld().getName()+"] "+fmt.format(loc.getX())+", "+fmt.format(loc.getY())+", "+fmt.format(loc.getZ());
		} catch (Exception e){
		}
		return lstr;
	}

	public static String locationToString(Location loc){
		if (loc == null) return "";
		return loc.getWorld().getName()+","+
		trimDouble(loc.getX())+","+
		trimDouble(loc.getY())+","+
		trimDouble(loc.getZ())+","+
		(float)trimDouble(loc.getYaw())+","+
		(float)trimDouble(loc.getPitch());
	}

	public static double trimDouble(double d){
		int i = (int) (d*1000);
		return ((double)i)/1000;
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



	public static ItemStack getRndItem (String str){
		if (str.isEmpty()) return new ItemStack (Material.AIR);
		String [] ln = str.split(",");
		if (ln.length==0) return new ItemStack (Material.AIR);
		ItemStack item = ItemUtil.parseItemStack(ln[u().tryChance(ln.length)]);
		if (item == null) return new ItemStack (Material.AIR);
		item.setAmount(1);
		return item;
	}

	public static String soundPlay (Location loc, Map<String,String> params){
		if (params.isEmpty()) return "";
		String sndstr = "";
		String strvolume ="1";
		String strpitch = "1";
		float pitch = 1;
		float volume = 1;
		if (params.containsKey("param")){
			String param = ParamUtil.getParam(params, "param", "");
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
			sndstr = ParamUtil.getParam(params, "type", "");
			pitch = ParamUtil.getParam(params, "pitch", 1.0f);
			volume = ParamUtil.getParam(params, "volume", 1.0f);
		}
		Sound sound = getSoundStr (sndstr);
		loc.getWorld().playSound(loc, sound, volume, pitch);
		return sound.name();
	}

	public static void soundPlay (Location loc, String param){
		if (param.isEmpty()) return;
		Map<String,String> params = new HashMap<String,String>();
		params.put("param", param);
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

	public static List<ItemStack> parseRandomItems (String stacks){
		return parseItemStacks (Util.parseRandomItemsStr(stacks));
	}


	public static List<ItemStack> parseItemStacks (String items){
		List<ItemStack> stacks = new ArrayList<ItemStack>();
		String[] ln = items.split(";");
		for (String item : ln){
			ItemStack stack = ItemUtil.parseItemStack(item);
			if (stack != null) stacks.add(stack);
		}
		return stacks;
	}
	public static String itemToString (ItemStack item){
		String itemStr ="";
		if (item == null) return itemStr;
		String itemName = item.getType().name();
		try {
			if (item.getItemMeta().hasDisplayName()) itemName =ChatColor.stripColor(item.getItemMeta().getDisplayName());
		} catch (Exception e){
		}
		String amount = item.getAmount()>1 ? "*"+item.getAmount() : "";
		itemStr = itemName+amount;
		return itemStr;
	}

	public static String itemsToString (List<ItemStack> items){
		String str="";
		for (ItemStack i : items){
			String itemStr = itemToString(i);
			if (!itemStr.isEmpty()) str = str.isEmpty() ? itemStr : ", " +itemStr;
		}
		return str;        
	}

	//id:data*amount@enchant:level,color;id:data*amount@chance/id:data*amount@enchant:level,color;id:data*amount@chance
	public static String parseRandomItemsStr (String items){
		if (items.isEmpty()) return "";
		String [] loots = items.split("/");
		Map<String,Integer> drops = new HashMap<String,Integer>();
		int maxchance = 0;
		int nochcount = 0;
		for (String loot: loots){
			String [] ln = loot.split("%");
			if (ln.length>0){
				String stacks = ln[0];
				if (stacks.isEmpty()) continue;
				int chance =-1;
				if ((ln.length==2)&&ReActions.util.isInteger(ln[1])) {
					chance = Integer.parseInt(ln[1]);
					maxchance += chance; 
				} else nochcount++;
				drops.put(stacks, chance);
			}
		}

		if (drops.isEmpty()) return "";
		int eqperc = (nochcount*100)/drops.size();
		maxchance = maxchance+eqperc*nochcount;
		int rnd = ReActions.util.tryChance(maxchance);
		int curchance = 0;
		for (String stack : drops.keySet()){
			curchance = curchance+ (drops.get(stack)<0 ? eqperc : drops.get(stack));
			if (rnd<=curchance) return stack;
		}
		return "";
	}


	@SuppressWarnings("deprecation")
	public static Location locToLocation(Player p, String locstr){
		Location loc = null;
		if (locstr.equalsIgnoreCase("player")||locstr.equalsIgnoreCase("here")) loc = p.getLocation();
		else if (locstr.equalsIgnoreCase("eye")||locstr.equalsIgnoreCase("head")) loc = p.getEyeLocation();
		else if (locstr.equalsIgnoreCase("sel")||locstr.equalsIgnoreCase("selection")) loc = Selector.getSelectedLocation(p);
		else if (locstr.equalsIgnoreCase("view")||locstr.equalsIgnoreCase("viewpoint")) loc = p.getTargetBlock(null, 100).getLocation(); 
		else if (plg().containsTpLoc(locstr)) loc = plg().getTpLoc(locstr);
		else loc = Util.parseLocation(locstr);
		return loc;
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
		locs.put("%sel%", Selector.getSelectedLocation(p));
		String newparam = param;
		for (String key : locs.keySet()){
			Location l = locs.get(key);
			if (l==null) continue;
			newparam = newparam.replace(key, Util.locationToString(l));
		}
		return newparam;
	}

	public static String locToString(Player p, String locstr){
		String loc = u().getMSGnc("loc_unknown");
		Location tl = locToLocation (p, locstr);
		if (tl!=null) loc = "["+tl.getWorld().getName()+"] ("+tl.getBlockX()+", "+tl.getBlockY()+", "+tl.getBlockZ()+")";
		return loc;
	}

	public static PotionEffectType parsePotionEffect (String name) {
		PotionEffectType pef = null;
		try{
			pef = PotionEffectType.getByName(name);
		} catch(Exception e){
		}
		return pef;
	}

	public static String timeToString(long time, boolean showms) {
		String timeStr = "";
		int hours = (int) ((time / 1000 + 8) % 24);
		int minutes = (int) (60 * (time % 1000) / 1000);
		timeStr = String.format("%02d:%02d", hours, minutes);
		if (showms&&(time<1000))  timeStr = Long.toString(time)+"ms";
		return timeStr;
	}


	public static Player getKiller(EntityDamageEvent event){
		if (event instanceof EntityDamageByEntityEvent){
			EntityDamageByEntityEvent evdmg = (EntityDamageByEntityEvent)event;
			if (evdmg.getDamager().getType() == EntityType.PLAYER) return (Player) evdmg.getDamager();
			if (evdmg.getCause() == DamageCause.PROJECTILE){
				Projectile prj = (Projectile) evdmg.getDamager();
				LivingEntity shooterEntity = BukkitCompatibilityFix.getShooter(prj);
				if (shooterEntity == null) return null;
				//if (prj.getShooter() == null) return null;
				//if (prj.getShooter() instanceof Player) return (Player) prj.getShooter();
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
				//if (prj.getShooter() == null) return null;
				return shooterEntity;
				/*try {
					if (prj.getShooter() instanceof LivingEntity) return (LivingEntity) prj.getShooter();
				} catch (Exception e){
					u().log("Cause: "+evdmg.getCause().name());
					u().log("Damager: "+evdmg.getDamage()==null? "null" : evdmg.getDamager().toString());
					u().log("Projectile: "+ prj == null ? "null" : prj.toString());
					u().log("Shooter: "+ ((prj!=null&&prj.getShooter()!=null) ? prj.getShooter().toString() : "null"));
					e.printStackTrace();
				}*/
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
	public static Set<Player> getPlayerList(Map<String,String> params, Player singlePlayer){
		Set<Player> players = new HashSet<Player>();
		if (params.containsKey("region")||params.containsKey("rgplayer")||params.containsKey("player")||
				params.containsKey("world")||
				params.containsKey("faction")||
				params.containsKey("group")||
				params.containsKey("perm")){

			// Players in regions
			if (RAWorldGuard.isConnected()){
				String regionNames = ParamUtil.getParam(params, "region", "");
				if (regionNames.isEmpty()) regionNames = ParamUtil.getParam(params, "rgplayer", "");
				String[] arrRegion = regionNames.split(",");
				for (String regionName: arrRegion)
					players.addAll(RAWorldGuard.playersInRegion(regionName));
			}

			// Players in faction
			if (RAFactions.isFactionConnected()){
				String factionNames = ParamUtil.getParam(params, "faction", "");
				String [] arrFaction = factionNames.split(",");
				for (String factionName : arrFaction)
					players.addAll(RAFactions.playersInFaction(factionName));
			}

			// Players in worlds
			String worldNames = ParamUtil.getParam(params, "world", "");
			String[] arrWorlds = worldNames.split(",");
			for (String worldName: arrWorlds){
				World world = Bukkit.getWorld(worldName);
				if (world ==null) continue;
				for (Player p : world.getPlayers()) players.add(p);
			}

			// Player by permission & group
			String group = ParamUtil.getParam(params, "group", "");
			String perm = ParamUtil.getParam(params, "perm", "");
			if ((!group.isEmpty())||(!perm.isEmpty())){
				for (Player pl : Bukkit.getOnlinePlayers()){
					if ((!group.isEmpty())&& RAVault.playerInGroup(pl, group)) players.add(pl);
					if ((!perm.isEmpty())&&pl.hasPermission(perm)) players.add(pl);
				}
			}

			// Players by name (all = all players, null - empty player)
			String playerNames = ParamUtil.getParam(params, "player", "");
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
		} else if (singlePlayer != null) players.add(singlePlayer);
		return players;
	}


}
