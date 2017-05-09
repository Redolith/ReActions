/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2017, fromgate, fromgate@gmail.com
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

import me.fromgate.reactions.event.EventManager;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Shoot {

    public static String actionShootBreak = "GLASS,THIN_GLASS,STAINED_GLASS,STAINED_GLASS_PANE,GLOWSTONE,REDSTONE_LAMP_OFF,REDSTONE_LAMP_ON";
    public static String actionShootThrough = "FENCE,FENCE_GATE,IRON_BARDING,IRON_FENCE,NETHER_FENCE";

    public static void shoot(LivingEntity shooter, Param params) {
        boolean onehit = params.getParam("singlehit", true);
        int distance = params.getParam("distance", 100);
        for (LivingEntity le : getEntityBeam(shooter, getBeam(shooter, distance), onehit)) {
            double damage = (double) Util.getMinMaxRandom(params.getParam("damage", "1"));
            if (damage > 0) {
                damageEntity(shooter, le, damage);
            }
            if (params.hasAnyParam("run")) {
                executeActivator(shooter instanceof Player ? (Player) shooter : null, le, params.getParam("run"));
            }
        }
    }

    private static String getMobName(LivingEntity mob) {
        if (mob.getCustomName() == null) return mob.getType().name();
        return mob.getCustomName();
    }

    private static void executeActivator(Player shooter, LivingEntity target, String paramStr) {
        Param param = Param.parseParams(paramStr);
        if (param.isEmpty() || !param.hasAnyParam("activator", "exec")) return;
        Player player = target instanceof Player ? (Player) target : null;
        if (player == null && param.getParam("playeronly", true)) return;
        param.set("player", player == null ? "null" : player.getName());
        Param tempVars = new Param();
        tempVars.set("targettype", target.getType().name());
        tempVars.set("targetname", (player == null) ? getMobName(target) : player.getName());
        tempVars.set("targetloc", Locator.locationToString(target.getLocation()));
        if (shooter != null) {
            tempVars.set("shooter", shooter.getName());
            tempVars.set("shooterloc", Locator.locationToString(shooter.getLocation()));
        }
        EventManager.raiseExecEvent(shooter, param, tempVars);
    }

    private static List<Block> getBeam(LivingEntity p, int distance) {
        List<Block> beam = new ArrayList<>();
        BlockIterator bi = new BlockIterator(p, distance);
        while (bi.hasNext()) {
            Block b = bi.next();
            if (isEmpty(b, p)) beam.add(b);
            else break;
        }
        return beam;
    }

    private static Set<LivingEntity> getEntityBeam(LivingEntity shooter, List<Block> beam, boolean hitSingle) {
        Set<LivingEntity> list = new HashSet<>();
        for (Block b : beam)
            for (Entity e : b.getChunk().getEntities()) {
                if (!(e instanceof LivingEntity)) continue;
                LivingEntity le = (LivingEntity) e;
                if (le.equals(shooter)) continue;
                if (isEntityAffectByBeamBlock(b, le)) {
                    list.add(le);
                    if (hitSingle) return list;
                }
            }
        return list;
    }

    @SuppressWarnings("deprecation")
    private static boolean isEmpty(Block b, LivingEntity shooter) {
        if (!b.getType().isSolid()) return true;
        if (Util.isItemInList(b.getType().getId(), b.getData(), actionShootThrough)) return true;
        if ((shooter instanceof Player) && (isShotAndBreak(b, (Player) shooter))) {
            b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());
            b.breakNaturally();
            return true;
        }
        return false;
    }


    public static boolean breakBlock(Block b, Player p) {
        BlockBreakEvent event = new BlockBreakEvent(b, p);
        Bukkit.getServer().getPluginManager().callEvent(event);
        return !event.isCancelled();
    }

    @SuppressWarnings("deprecation")
    private static boolean isShotAndBreak(Block b, Player p) {
        if (Util.isItemInList(b.getType().getId(), b.getData(), actionShootBreak)) return breakBlock(b, p);
        return false;
    }

    private static boolean isEntityAffectByBeamBlock(Block b, LivingEntity le) {
        if (le.getLocation().getBlock().equals(b)) return true;
        return le.getEyeLocation().getBlock().equals(b);
    }

    public static boolean damageEntity(LivingEntity damager, LivingEntity entity, double damage) {
        EntityEvent event = BukkitCompatibilityFix.createEntityDamageByEntityEvent(damager, entity, DamageCause.ENTITY_ATTACK, damage);

        if (event == null) return false;

        Bukkit.getServer().getPluginManager().callEvent(event);
        if (!((Cancellable) event).isCancelled()) {
            BukkitCompatibilityFix.damageEntity(entity, damage);
        }
        return !((Cancellable) event).isCancelled();
    }

}
