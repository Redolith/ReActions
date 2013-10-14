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

import me.fromgate.reactions.event.RAButtonEvent;
import me.fromgate.reactions.event.RACommandEvent;
import me.fromgate.reactions.event.RAExecEvent;
import me.fromgate.reactions.event.RAPVPDeathEvent;
import me.fromgate.reactions.event.RAPVPKillEvent;
import me.fromgate.reactions.event.RAPlateEvent;
import me.fromgate.reactions.event.RARegionEnterEvent;
import me.fromgate.reactions.event.RARegionEvent;
import me.fromgate.reactions.event.RARegionLeaveEvent;
import me.fromgate.reactions.util.RADebug;
import me.fromgate.reactions.util.RAMobSpawn;
import me.fromgate.reactions.util.RAPVPDeath;
import me.fromgate.reactions.util.RAPushBack;
import me.fromgate.reactions.util.RAVault;
import me.fromgate.reactions.util.Util;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class RAListener implements Listener{
    ReActions plg;

    public RAListener (ReActions plg){
        this.plg = plg;
    }
    
    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event){
        RAPVPDeath.addPVPDeath(event);
        EventManager.raisePVPKillEvent(event);
    }

    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerRespawn(PlayerRespawnEvent event){
        RAPVPDeath.raisePVPDeathEvent(event.getPlayer());
    }

        
    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void onDropLoot(EntityDeathEvent event){
        if (event.getEntity().hasMetadata("ReActions-drop")) {
            //List<ItemStack> stacks = RAMobSpawn.parseItemStacks(event.getEntity().getMetadata("ReActions-drop").get(0).asString());
            List<ItemStack> stacks = Util.parseItemStacks (event.getEntity().getMetadata("ReActions-drop").get(0).asString());
                    
            
            if (stacks != null) {
                event.getDrops().clear();
                event.getDrops().addAll(stacks);
            }
        }
        if (event.getEntity().hasMetadata("ReActions-xp")) {
            int xp = Util.getMinMaxRandom(event.getEntity().getMetadata("ReActions-xp").get(0).asString());
            event.setDroppedExp(xp);
        }
        
        if (event.getEntity().hasMetadata("ReActions-money")) {
            if (!RAVault.isEconomyConected()) return;
            Player killer = Util.getKiller (event.getEntity().getLastDamageCause());
            if (killer != null){
                int money = Util.getMinMaxRandom(event.getEntity().getMetadata("ReActions-money").get(0).asString());    
                RAVault.depositPlayer(killer.getName(), money);
                plg.u.printMSG(killer, "msg_mobbounty",'e','6',RAVault.formatMoney(Integer.toString(money)),event.getEntity().getType().name());
            }
        }
        
        if (event.getEntity().hasMetadata("ReActions-deatheffect")) {
            RAMobSpawn.playMobEffect(event.getEntity().getLocation(), event.getEntity().getMetadata("ReActions-deatheffect").get(0).asString());
        }
    }

    
    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void onMobGrowl (EntityDamageEvent event){
        if ((event.getCause()!=DamageCause.ENTITY_ATTACK)&&(event.getCause()!=DamageCause.PROJECTILE)) return;
        if (event.getEntityType() != EntityType.PLAYER) return;
        if (!(event instanceof EntityDamageByEntityEvent)) return;
        EntityDamageByEntityEvent evdmg = (EntityDamageByEntityEvent)event;
        LivingEntity damager = Util.getDamagerEntity(evdmg);
        if (damager == null) return;
        if (damager.getType() == EntityType.PLAYER) return;
        if (!damager.hasMetadata("ReActions-growl")) return;
        String growl = damager.getMetadata("ReActions-growl").get(0).asString();
        if (growl == null) return;
        if (growl.isEmpty()) return;
        Util.soundPlay(damager.getLocation(), growl);
    }
    
    @EventHandler(priority=EventPriority.HIGH, ignoreCancelled = true)
    public void onMobDmg (EntityDamageEvent event){
        if ((event.getCause()!=DamageCause.ENTITY_ATTACK)&&(event.getCause()!=DamageCause.PROJECTILE)) return;
        if (!(event instanceof EntityDamageByEntityEvent)) return;
        EntityDamageByEntityEvent evdmg = (EntityDamageByEntityEvent)event;
        LivingEntity damager = Util.getDamagerEntity(evdmg);
        if (damager == null) return;
        if (damager.getType() == EntityType.PLAYER) return;
        if (!damager.hasMetadata("ReActions-dmg")) return;
        double dmg = damager.getMetadata("ReActions-dmg").get(0).asDouble();
        if (dmg<0) return;
        try {
            dmg = event.getDamage()*dmg;
            event.setDamage(dmg);    
        } catch (Throwable tw){
            plg.u.logOnce("sethealth", "Can't modify mob's health. This feature is supported only at craftbukkit 1.6.2 (and newer)!");
        }
    }
    
    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void onMobCry (EntityDamageEvent event){
        if ((event.getCause()!=DamageCause.ENTITY_ATTACK)&&(event.getCause()!=DamageCause.PROJECTILE)) return;
        if (event.getEntityType() == EntityType.PLAYER) return;
        if (!(event.getEntity() instanceof LivingEntity)) return;
        LivingEntity le = (LivingEntity) event.getEntity();
        if (!le.hasMetadata("ReActions-cry")) return;
        String cry = le.getMetadata("ReActions-cry").get(0).asString();
        if (cry == null) return;
        if (cry.isEmpty()) return;
        if (!(event instanceof EntityDamageByEntityEvent)) return;
        EntityDamageByEntityEvent evdmg = (EntityDamageByEntityEvent)event;
        if (evdmg.getDamager() instanceof Projectile){
            Projectile prj = (Projectile) evdmg.getDamager();
            if (!(prj.getShooter() instanceof Player)) return;
        } else if (evdmg.getDamager().getType() != EntityType.PLAYER) return;
        Util.soundPlay(le.getLocation(), cry);        
    }
    

    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void onPvPDamage(EntityDamageEvent event){
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
    public void onPlayerCommand (PlayerCommandPreprocessEvent event){
        if (EventManager.raiseCommandEvent(event.getPlayer(), event.getMessage().replaceFirst("/", "")))
            event.setCancelled(true);
    }

    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = false)
    public void onPlayerJoin (PlayerJoinEvent event){
        RADebug.offPlayerDebug(event.getPlayer());
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
    
    
    @EventHandler(priority=EventPriority.HIGH, ignoreCancelled = false)
    public void onPlayerJoinInRegion (PlayerJoinEvent event){
        EventManager.raiseRegionEvent(event.getPlayer(), event.getPlayer().getLocation());
        EventManager.raiseRgEnterEvent(event.getPlayer(), null, event.getPlayer().getLocation());
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

    
    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void onRegionLeave (RAExecEvent event){
        plg.activators.activate(event);
    }

    
    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void onCommandActivator (RACommandEvent event){
        plg.activators.activate(event);
    }
    
    
    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void onPVPKillActivator (RAPVPKillEvent event){
        plg.activators.activate(event);
    }
    
    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void onPVPDeathActivator (RAPVPDeathEvent event){
        plg.activators.activate(event);
    }


    
        


}




