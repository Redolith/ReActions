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

package me.fromgate.reactions.event;

import me.fromgate.reactions.RAUtil;
import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.activators.Activator;
import me.fromgate.reactions.activators.ActivatorType;
import me.fromgate.reactions.activators.Activators;
import me.fromgate.reactions.activators.ItemHoldActivator;
import me.fromgate.reactions.activators.ItemWearActivator;
import me.fromgate.reactions.activators.MessageActivator;
import me.fromgate.reactions.activators.MessageActivator.Source;
import me.fromgate.reactions.activators.SignActivator;
import me.fromgate.reactions.externals.RAWorldGuard;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.item.ItemUtil;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Button;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;

public class EventManager {
	private static ReActions plg(){
		return ReActions.instance;
	}

	private static RAUtil u(){
		return ReActions.util;
	}

	public static boolean raiseFactionEvent(Player p, String oldFaction, String newFaction) {
		FactionEvent e = new FactionEvent (p,oldFaction, newFaction);
		Bukkit.getServer().getPluginManager().callEvent(e); 
		return true;
	}


	public static boolean raiseFactionCreateEvent(String factionName, Player player) {
		FactionCreateEvent e = new FactionCreateEvent (factionName, player);
		Bukkit.getServer().getPluginManager().callEvent(e); 
		return true;		
	}

	public static boolean raiseFactionDisbandEvent(String factionName, Player player) {
		FactionDisbandEvent e = new FactionDisbandEvent (factionName, player);
		Bukkit.getServer().getPluginManager().callEvent(e); 
		return true;		
	}



	public static boolean raiseFactionRelationEvent(String faction, String factionOther, String oldRelation, String newRelation) {
		FactionRelationEvent e = new FactionRelationEvent(faction, factionOther, oldRelation, newRelation);
		Bukkit.getServer().getPluginManager().callEvent(e);
		return true;
	}

	public static boolean raiseMobClickEvent (Player p, LivingEntity mob){
		if (mob == null) return false;
		MobClickEvent e = new MobClickEvent(p,mob);
		Bukkit.getServer().getPluginManager().callEvent(e);       
		return true;
	}
	
	public static boolean raiseMobKillEvent (Player p, LivingEntity mob){
		if (mob == null) return false;
		MobKillEvent e = new MobKillEvent(p,mob);
		Bukkit.getServer().getPluginManager().callEvent(e);       
		return true;
	}


	public static boolean raiseJoinEvent(Player player, boolean joinfirst){
		JoinEvent e = new JoinEvent (player,joinfirst);
		Bukkit.getServer().getPluginManager().callEvent(e);
		return true;
	}

	public static boolean raiseDoorEvent(PlayerInteractEvent event){
		if (!((event.getAction()==Action.RIGHT_CLICK_BLOCK)||(event.getAction()==Action.LEFT_CLICK_BLOCK))) return false;
		if (!Util.isDoorBlock(event.getClickedBlock())) return false;
		DoorEvent e = new DoorEvent (event.getPlayer(), Util.getDoorBottomBlock(event.getClickedBlock()));
		Bukkit.getServer().getPluginManager().callEvent(e);
		return e.isCancelled();
	}

	public static boolean raiseItemClickEvent(PlayerInteractEntityEvent event){
		if (event.getPlayer().getItemInHand()==null) return false;
		if (event.getPlayer().getItemInHand().getType() == Material.AIR) return false;
		ItemClickEvent ice = new ItemClickEvent (event.getPlayer());
		Bukkit.getServer().getPluginManager().callEvent(ice);
		return true;
	}

	public static boolean raiseItemClickEvent(PlayerInteractEvent event){
		if ((event.getAction() != Action.RIGHT_CLICK_AIR)&&(event.getAction() != Action.RIGHT_CLICK_BLOCK)) return false;
		if (event.getPlayer().getItemInHand()==null) return false;
		if (event.getPlayer().getItemInHand().getType() == Material.AIR) return false;
		ItemClickEvent ice = new ItemClickEvent (event.getPlayer());
		Bukkit.getServer().getPluginManager().callEvent(ice);
		return true;
	}


	public static boolean raiseLeverEvent(PlayerInteractEvent event){
		if (!((event.getAction()==Action.RIGHT_CLICK_BLOCK)||(event.getAction()==Action.LEFT_CLICK_BLOCK))) return false;
		if (event.getClickedBlock().getType() != Material.LEVER) return false;
		LeverEvent e = new LeverEvent (event.getPlayer(), event.getClickedBlock());
		Bukkit.getServer().getPluginManager().callEvent(e);
		return e.isCancelled();
	}


	// PVP Kill Event
	public static void raisePVPKillEvent (PlayerDeathEvent event){
		Player deadplayer = event.getEntity();
		Player killer = Util.getKiller(deadplayer.getLastDamageCause());
		if (killer==null) return;
		PVPKillEvent pe = new PVPKillEvent(killer, deadplayer);
		Bukkit.getServer().getPluginManager().callEvent(pe);
	}

	// PVP Death Event
	public static void raisePVPDeathEvent (PlayerDeathEvent event){
		Player deadplayer = event.getEntity();
		Player killer = Util.getKiller(deadplayer.getLastDamageCause());
		if (killer==null) return;
		PVPDeathEvent pe = new PVPDeathEvent(killer, deadplayer);
		Bukkit.getServer().getPluginManager().callEvent(pe);
	}

	// Button Event
	public static boolean raiseButtonEvent (PlayerInteractEvent event){
		if (!((event.getAction()==Action.RIGHT_CLICK_BLOCK)||(event.getAction()==Action.LEFT_CLICK_BLOCK))) return false;
		if (!((event.getClickedBlock().getType()==Material.STONE_BUTTON)||(event.getClickedBlock().getType()==Material.WOOD_BUTTON))) return false;
		BlockState state = event.getClickedBlock().getState();
		if (state.getData() instanceof Button){
			Button button = (Button) state.getData();
			if (button.isPowered()) return false;
		}
		ButtonEvent be = new ButtonEvent (event.getPlayer(), event.getClickedBlock().getLocation());
		Bukkit.getServer().getPluginManager().callEvent(be);
		return be.isCancelled();
	}

	public static boolean raiseSignEvent(Player player, String[] lines, Location loc, boolean leftClick) {
		for (Activator act : Activators.getActivators(ActivatorType.SIGN)){
			SignActivator sign = (SignActivator) act;
			if (sign.checkMask(lines)){
				SignEvent se = new SignEvent (player, lines, loc, leftClick);
				Bukkit.getServer().getPluginManager().callEvent(se);
				return true;
			}
		}
		return false;
	}

	public static boolean raiseCommandEvent (Player p, String command, boolean canceled){
		if (command.isEmpty()) return false;
		String [] args = command.split(" ");
		CommandEvent ce = new CommandEvent (p,command,args, canceled);
		Bukkit.getServer().getPluginManager().callEvent(ce);
		if (ce.isCancelled()) return true;
		return false;
	}

	public static boolean raiseExecEvent (CommandSender sender, String param){
		if (param.isEmpty()) return false;
		return raiseExecEvent (sender, new Param (param,"player")); 
	}

	public static boolean raiseExecEvent (CommandSender sender, Param param){
		if (param.isEmpty()) return false;
		final Player senderPlayer = (sender instanceof Player) ? (Player) sender : null;
		final String id = param.getParam("activator",param.getParam("exec"));
		if (id.isEmpty()) return false;
		Activator act = plg().getActivator(id);
		if (act == null) {
			u().logOnce("wrongact_"+id, "Failed to run exec activator "+id+". Activator not found.");
			return false;
		}
		if (act.getType()!=ActivatorType.EXEC){
			u().logOnce("wrongactype_"+id, "Failed to run exec activator "+id+". Wrong activator type.");
			return false;
		}
		int repeat = Math.min(param.getParam("repeat", 1), 1);
		long delay = u().timeToTicks(u().parseTime(param.getParam("delay", "1t")));
		final List<Player> target = new ArrayList<Player>();
		target.addAll(Util.getPlayerList(param,null)); 
		if (target.isEmpty() &&(senderPlayer !=null)) target.add(senderPlayer); 
		if (target.isEmpty()) target.add(null); 
		for (int i = 0; i<repeat; i++){
			Bukkit.getScheduler().runTaskLater(plg(), new Runnable(){
				@Override
				public void run() {
					for (Player p : target){
						if (Activators.isStopped(p, id, true)) continue;
						ExecEvent ce = new ExecEvent(senderPlayer, p, id);
						Bukkit.getServer().getPluginManager().callEvent(ce);
					}
				}
			}, delay*repeat);
		}
		return true;
	}

	// Plate Event
	public static boolean raisePlateEvent (PlayerInteractEvent event){
		if (event.getAction() != Action.PHYSICAL) return false;
		if (!((event.getClickedBlock().getType()==Material.WOOD_PLATE)||(event.getClickedBlock().getType()==Material.STONE_PLATE))) return false;
		final Player p = event.getPlayer();
		final Location l = event.getClickedBlock().getLocation();
		Bukkit.getScheduler().runTaskLater(plg(), new Runnable(){
			@Override
			public void run() {
				PlateEvent pe = new PlateEvent (p, l);
				Bukkit.getServer().getPluginManager().callEvent(pe);      
			}
		}, 1);
		return false;
	}

	public static void raiseItemWearEvent (final Player player){
		Bukkit.getScheduler().runTaskLater(plg(), new Runnable(){
			@Override
			public void run() {
				if (!player.isOnline()) return;
				if (player.isDead()) return;
				boolean hasArmour = false;
				for (ItemStack is : player.getInventory().getArmorContents()){
					if (is!=null&&is.getType() != Material.AIR) hasArmour = true;
				}
				if (!hasArmour) return;
				for (ItemWearActivator iw : Activators.getItemWearActivatos())
					raiseItemWearEvent (player, iw.getItemStr());
			}
		}, 1);
	}

	public static boolean raiseItemWearEvent (Player player, String itemStr){
		if (!player.isOnline()) return false;
		if (player.isDead()) return false;
		//String id = "reactions-rchk-iw-"+itemStr;
		if (!isTimeToRaiseEvent (player,"iw-"+itemStr, plg().itemWearRecheck)) return false;
		player.removeMetadata("reactions-rchk-iw-"+itemStr, plg());
		ItemWearEvent iwe = new ItemWearEvent (player);
		if (!iwe.isItemWeared(itemStr)) return false;
		Bukkit.getServer().getPluginManager().callEvent(iwe);   
		setFutureItemWearCheck (player,itemStr);
		return true;
	}

	private static void setFutureItemWearCheck(final Player p, final String itemStr){
		final String id = "iw-"+itemStr;
		if (isTimeToRaiseEvent(p, id,plg().itemHoldRecheck)){
			p.setMetadata("reactions-rchk-"+id, new FixedMetadataValue (plg(), System.currentTimeMillis()));
			Bukkit.getScheduler().runTaskLater(plg(), new Runnable(){
				@Override
				public void run() {
					if (!p.isOnline()) return;
					if (p.isDead()) return;
					p.removeMetadata("reactions-rchk-"+id, plg());
					ItemWearEvent iwe = new ItemWearEvent (p);
					if (iwe.isItemWeared(itemStr)){
						Bukkit.getServer().getPluginManager().callEvent(iwe);   
						setFutureItemWearCheck (p,itemStr);
					}
				}
			}, 20*plg().itemWearRecheck);
		}
	}



	public static void raiseItemHoldEvent (final Player player){
		Bukkit.getScheduler().runTaskLater(plg(), new Runnable(){
			@Override
			public void run() {
				if (!player.isOnline()) return;
				if (player.isDead()) return;
				if (player.getItemInHand()==null) return;
				if (player.getItemInHand().getType() == Material.AIR) return;
				for (ItemHoldActivator ih : Activators.getItemHoldActivatos())
					if (raiseItemHoldEvent (player, ih.getItemStr())) return;
			}
		}, 1);

	}


	public static boolean raiseItemHoldEvent (Player player, String itemstr){
		if (!player.isOnline()) return false;
		if (player.isDead()) return false;
		if (player.getItemInHand()==null) return false;
		if (player.getItemInHand().getType() == Material.AIR) return false;
		String id = "reactions-rchk-ih-"+itemstr;
		if (!isTimeToRaiseEvent (player,id, plg().itemHoldRecheck)) return false;
		player.removeMetadata(id, plg());
		if (!ItemUtil.compareItemStr(player.getItemInHand(), itemstr)) return false;
		ItemHoldEvent ihe = new ItemHoldEvent (player);
		Bukkit.getServer().getPluginManager().callEvent(ihe);   
		setFutureItemHoldCheck (player,itemstr);
		return true;
	}

	public static void raiseAllRegionEvents (final Player player, final Location to, final Location from){
		if (!RAWorldGuard.isConnected()) return;
		Bukkit.getScheduler().runTaskAsynchronously(ReActions.instance, new Runnable(){
			@Override
			public void run() {

				final List<String> regionsTo = RAWorldGuard.getRegions(to);
				final List<String> regionsFrom = RAWorldGuard.getRegions(from);

				Bukkit.getScheduler().runTask(ReActions.instance, new Runnable(){
					@Override
					public void run() {
						raiseRegionEvent  (player,regionsTo);
						raiseRgEnterEvent (player, regionsTo, regionsFrom);
						raiseRgLeaveEvent (player, regionsTo, regionsFrom);
					}
				});
			}
		}); 
	}



	private static void raiseRegionEvent (Player player, List<String> to){
		if (to.isEmpty()) return;
		for (String rg : to){
			if (isTimeToRaiseEvent (player,rg, plg().worlduardRecheck)){
				RegionEvent wge = new RegionEvent (player, rg);
				Bukkit.getServer().getPluginManager().callEvent(wge);	
				setFutureRegionCheck(player,rg);
			}
		}
	}


	private static void raiseRgEnterEvent (Player player, List<String> regionTo, List<String> regionFrom){
		if (regionTo.isEmpty()) return;
		for (String rg : regionTo)
			if (!regionFrom.contains(rg)){
				RegionEnterEvent wge = new RegionEnterEvent (player, rg);
				Bukkit.getServer().getPluginManager().callEvent(wge);				
			}
	}

	private static void raiseRgLeaveEvent (Player player, List<String> regionTo, List<String> regionFrom){
		if (regionFrom.isEmpty()) return;
		for (String rg : regionFrom)
			if (!regionTo.contains(rg)){
				RegionLeaveEvent wge = new RegionLeaveEvent (player, rg);
				Bukkit.getServer().getPluginManager().callEvent(wge);				
			}
	}

	// Данная функция вызывает каждую секунду 
	// эвент, на тот случай, если игрок "застыл" и PlayerMoveEvent не отрабатывает
	private static void setFutureRegionCheck(final Player p, final String region){
		final String rg = "rg-"+region;
		if (isTimeToRaiseEvent(p, rg,plg().worlduardRecheck)){
			p.setMetadata("reactions-rchk-"+rg, new FixedMetadataValue (plg(), System.currentTimeMillis()));
			Bukkit.getScheduler().runTaskLater(plg(), new Runnable(){
				@Override
				public void run() {
					if (!p.isOnline()) return;
					if (p.isDead()) return;
					p.removeMetadata("reactions-rchk-"+rg, plg());
					if (RAWorldGuard.isPlayerInRegion(p, region)){
						RegionEvent wge = new RegionEvent (p, region);
						Bukkit.getServer().getPluginManager().callEvent(wge);	
						setFutureRegionCheck (p,region);
					}
				}
			}, 20*plg().worlduardRecheck);
		}
	}

	private static void setFutureItemHoldCheck(final Player p, final String itemstr){
		final String id = "ih-"+itemstr;
		if (isTimeToRaiseEvent(p, id,plg().itemHoldRecheck)){
			p.setMetadata("reactions-rchk-"+id, new FixedMetadataValue (plg(), System.currentTimeMillis()));
			Bukkit.getScheduler().runTaskLater(plg(), new Runnable(){
				@Override
				public void run() {
					if (!p.isOnline()) return;
					if (p.isDead()) return;
					if (p.getItemInHand()==null) return;
					if (p.getItemInHand().getType() == Material.AIR) return;
					p.removeMetadata("reactions-rchk-"+id, plg());
					if (ItemUtil.compareItemStr(p.getItemInHand(), itemstr)){
						ItemHoldEvent ihe = new ItemHoldEvent (p);
						Bukkit.getServer().getPluginManager().callEvent(ihe);   
						setFutureItemHoldCheck (p,itemstr);
					}
				}
			}, 20*plg().itemHoldRecheck);
		}
	}

	public static boolean isTimeToRaiseEvent(Player p, String id, int seconds){
		if (!p.hasMetadata("reactions-rchk-"+id)) return true;
		Long curtime = System.currentTimeMillis();
		Long prevtime = p.getMetadata("reactions-rchk-"+id).get(0).asLong();
		return ((curtime-prevtime)>=(1000*seconds)); 
	}

	public static boolean raiseMessageEvent(CommandSender sender, Source source, String message) {
		Player player = sender!=null&&(sender instanceof Player) ? (Player) sender : null;
		for (MessageActivator a : Activators.getMessageActivators()){
			if (a.filterMessage(source, message)) {
				MessageEvent me = new MessageEvent (player, a, message);
				Bukkit.getServer().getPluginManager().callEvent(me);
				return me.isCancelled();
			}
		}
		return false;
	}

	public static void raiseVariableEvent(String var, String playerName,String newValue, String prevValue) {
		if (newValue.equalsIgnoreCase(prevValue)) return;
		@SuppressWarnings("deprecation")
		Player player = Bukkit.getPlayerExact(playerName);
		if (!playerName.isEmpty()&&player==null) return;
		VariableEvent ve = new VariableEvent (player, var, newValue, prevValue);
		Bukkit.getServer().getPluginManager().callEvent(ve);
	}




}
