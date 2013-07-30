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


import me.fromgate.reactions.event.RAButtonEvent;
import me.fromgate.reactions.event.RAPlateEvent;
import me.fromgate.reactions.event.RARegionEnterEvent;
import me.fromgate.reactions.event.RARegionEvent;
import me.fromgate.reactions.event.RARegionLeaveEvent;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.FixedMetadataValue;



public class RAListener implements Listener{
    ReActions plg;

    public RAListener (ReActions plg){
        this.plg = plg;
    }

    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerDamage (EntityDamageEvent event){
        if (event.getEntityType() != EntityType.PLAYER) return;
        Player target = (Player) event.getEntity();
        if (!(event instanceof EntityDamageByEntityEvent)) return;
        EntityDamageByEntityEvent evdmg = (EntityDamageByEntityEvent)event;
        Player damager = null;
        if (evdmg.getDamager().getType() == EntityType.PLAYER){
            damager = (Player) evdmg.getDamager();
        } else if (evdmg.getDamager() instanceof Projectile){
            Projectile prj = (Projectile) evdmg.getDamager();
            if (!(prj.getShooter() instanceof Player)) return;
            damager = (Player) prj.getShooter();
        } else return;
        if (damager==null) return;
        Long time = System.currentTimeMillis();
        damager.setMetadata("reactions-pvp-time", new FixedMetadataValue (plg, time));
        target.setMetadata("reactions-pvp-time", new FixedMetadataValue (plg, time));
    }


    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = false)
    public void onPlayerJoin (PlayerJoinEvent event){
        plg.debug.offPlayerDebug(event.getPlayer());
        plg.u.UpdateMsg(event.getPlayer());
    }

    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInteract (PlayerInteractEvent event){
        EventManager.raiseButtonEvent(event);
        EventManager.raisePlateEvent(event);
    }

    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerMove (PlayerMoveEvent event){
        RAPushBack.onPlayerMove(event.getPlayer(), event.getFrom(), event.getTo());
        EventManager.raiseRegionEvent(event.getPlayer(), event.getTo());
        EventManager.raiseRgEnterEvent(event.getPlayer(), event.getFrom(), event.getTo());
        EventManager.raiseRgLeaveEvent(event.getPlayer(), event.getFrom(), event.getTo());
    }

    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event){
        EventManager.raiseRegionEvent(event.getPlayer(), event.getTo());
        EventManager.raiseRgEnterEvent(event.getPlayer(), event.getFrom(), event.getTo());
        EventManager.raiseRgLeaveEvent(event.getPlayer(), event.getFrom(), event.getTo());
    }





    /*
     * ReActions' Events 
     */
    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void onButton (RAButtonEvent event){
        plg.activators.activate(event);
    }

    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void onButton (RAPlateEvent event){
        plg.activators.activate(event);
    }

    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void onRegion (RARegionEvent event){
        plg.activators.activate(event);
    }

    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void onRegionEnter (RARegionEnterEvent event){
        plg.activators.activate(event);
    }

    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void onRegionLeave (RARegionLeaveEvent event){
        plg.activators.activate(event);
    }


}




