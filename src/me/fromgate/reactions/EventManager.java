/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2013, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/reactions/
 *   * 
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

package me.fromgate.reactions;

import java.util.List;
import me.fromgate.reactions.event.ButtonEvent;
import me.fromgate.reactions.event.CommandEvent;
import me.fromgate.reactions.event.DoorEvent;
import me.fromgate.reactions.event.ExecEvent;
import me.fromgate.reactions.event.LeverEvent;
import me.fromgate.reactions.event.PVPKillEvent;
import me.fromgate.reactions.event.PlateEvent;
import me.fromgate.reactions.event.RegionEnterEvent;
import me.fromgate.reactions.event.RegionLeaveEvent;
import me.fromgate.reactions.event.RegionEvent;
import me.fromgate.reactions.util.RAWorldGuard;
import me.fromgate.reactions.util.Util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Button;
import org.bukkit.metadata.FixedMetadataValue;


public class EventManager {

    private static ReActions plg;

    public static void init(ReActions plugin){
        plg = plugin;
    }
    
    
    public static boolean raiseDoorEvent(PlayerInteractEvent event){
        if (!((event.getAction()==Action.RIGHT_CLICK_BLOCK)||(event.getAction()==Action.LEFT_CLICK_BLOCK))) return false;
        if (!Util.isDoorBlock(event.getClickedBlock())) return false;
        DoorEvent e = new DoorEvent (event.getPlayer(), Util.getDoorBottomBlock(event.getClickedBlock()));
        Bukkit.getServer().getPluginManager().callEvent(e);
        return true;
    }
    
    

    public static boolean raiseLeverEvent(PlayerInteractEvent event){
        if (!((event.getAction()==Action.RIGHT_CLICK_BLOCK)||(event.getAction()==Action.LEFT_CLICK_BLOCK))) return false;
        if (event.getClickedBlock().getType() != Material.LEVER) return false;
        LeverEvent e = new LeverEvent (event.getPlayer(), event.getClickedBlock());
        Bukkit.getServer().getPluginManager().callEvent(e);
        return true;
    }
    
    
    // PVP Kill Event
    public static void raisePVPKillEvent (PlayerDeathEvent event){
        Player deadplayer = event.getEntity();
        Player killer = Util.getKiller(deadplayer.getLastDamageCause());
        if (killer==null) return;
        PVPKillEvent pe = new PVPKillEvent(killer, deadplayer);
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
        return true;
        /*
         *  // Пока не удалять - возможно этот баг с кнопкой снова вылезет...
			BlockState state = event.getClickedBlock().getState();
			if (state.getData() instanceof Button){
				Button button = (Button) state.getData();
				if (button.isPowered()) {
					setButtonState(event.getClickedBlock(), false, 5);
					return;
				}
			}
         */
    }
    
    
    
    public static boolean raiseCommandEvent (Player p, String command){
        if (command.isEmpty()) return false;
        CommandEvent ce = new CommandEvent (p,command);
        Bukkit.getServer().getPluginManager().callEvent(ce);
        if (ce.isCancelled()) return true;
        return false;
    }
    
    
    public static boolean raiseExecEvent (Player player, Player targetPlayer, String activator){
        if (targetPlayer == null) return false;
        if (!targetPlayer.isOnline()) return false;
        if (activator == null) return false;
        if (activator.isEmpty()) return false;
        ExecEvent ce = new ExecEvent(player, targetPlayer, activator);
        Bukkit.getServer().getPluginManager().callEvent(ce);
        return true;
    }

    // Plate Event
    public static boolean raisePlateEvent (PlayerInteractEvent event){
        if (event.getAction() != Action.PHYSICAL) return false;
        if (!((event.getClickedBlock().getType()==Material.WOOD_PLATE)||(event.getClickedBlock().getType()==Material.STONE_PLATE))) return false;
        final Player p = event.getPlayer();
        final Location l = event.getClickedBlock().getLocation();
        Bukkit.getScheduler().runTaskLater(plg, new Runnable(){
            @Override
            public void run() {
                PlateEvent pe = new PlateEvent (p, l);
                Bukkit.getServer().getPluginManager().callEvent(pe);      
            }
        }, 1);
        return true;

    }

    /*
	public void setButtonState (final Block b, final boolean press, int delay){
		Bukkit.getScheduler().scheduleSyncDelayedTask(plg, new Runnable(){
			public void run(){
				if (!isActivator(b)) return;
				BlockState state = b.getState();
				if (state.getData() instanceof Button){
					Button button = (Button) state.getData();
					button.setPowered(press);
					state.update();
				}
			}
		}, delay);
	}

		@Deprecated
	public boolean isActivator (Block b){
		return (!activators.getActivatorInLocation(b.getLocation()).isEmpty());
	}
     */

    // WorldGuard Event (based on PlayerMoveEvent and PlayerTeleportEvent)
    public static void raiseRegionEvent (Player player, Location to){
        if (!RAWorldGuard.isConnected()) return;
        List<String> rgs = RAWorldGuard.getRegions(to);
        if (rgs.isEmpty()) return;
        for (String rg : rgs){
            if (isTimeToRaiseEvent (player,rg)){
                RegionEvent wge = new RegionEvent (player, rg);
                Bukkit.getServer().getPluginManager().callEvent(wge);	
                setFutureCheck(player,rg);
            }
        }
    }

    public static void raiseRgEnterEvent (Player player, Location from, Location to){
        if (!RAWorldGuard.isConnected()) return; 
        List<String> rgsto = RAWorldGuard.getRegions(to);
        List<String> rgsfrom = RAWorldGuard.getRegions(from);
        if (rgsto.isEmpty()) return;
        for (String rg : rgsto)
            if (!rgsfrom.contains(rg)){
                RegionEnterEvent wge = new RegionEnterEvent (player, rg);
                Bukkit.getServer().getPluginManager().callEvent(wge);				
            }
    }

    public static void raiseRgLeaveEvent (Player player, Location from, Location to){
        if (!RAWorldGuard.isConnected()) return; 
        List<String> rgsto = RAWorldGuard.getRegions(to);
        List<String> rgsfrom = RAWorldGuard.getRegions(from);
        if (rgsfrom.isEmpty()) return;
        for (String rg : rgsfrom)
            if (!rgsto.contains(rg)){
                RegionLeaveEvent wge = new RegionLeaveEvent (player, rg);
                Bukkit.getServer().getPluginManager().callEvent(wge);				
            }
    }

    // Данная функция вызывает каждую секунду 
    // эвент, на тот случай, если игрок "застыл" и PlayerMoveEvent не отрабатывает
    private static void setFutureCheck(final Player p, final String rg){
        if (isTimeToRaiseEvent(p, rg)){
            p.setMetadata("reactions-wg-"+rg, new FixedMetadataValue (plg, System.currentTimeMillis()));
            Bukkit.getScheduler().runTaskLater(plg, new Runnable(){
                @Override
                public void run() {
                    if (!p.isOnline()) return;
                    if (p.isDead()) return;
                    p.removeMetadata("reactions-wg-"+rg, plg);
                    if (RAWorldGuard.isPlayerInRegion(p, rg)){
                        RegionEvent wge = new RegionEvent (p, rg);
                        Bukkit.getServer().getPluginManager().callEvent(wge);	
                        setFutureCheck (p,rg);
                    }
                }
            }, 20*plg.worlduard_recheck);
        }
    }

    public static boolean isTimeToRaiseEvent(Player p, String rg){
        if (!p.hasMetadata("reactions-wg-"+rg)) return true;
        Long curtime = System.currentTimeMillis();
        Long prevtime = p.getMetadata("reactions-wg-"+rg).get(0).asLong();
        return ((curtime-prevtime)>(1000*plg.worlduard_recheck));
    }


}
