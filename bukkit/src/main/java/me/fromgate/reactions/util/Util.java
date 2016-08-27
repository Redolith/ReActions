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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Util {

    private static RAUtil u() {
        return ReActions.util;
    }

    public static int getMinMaxRandom(String minmaxstr) {
        int min = 0;
        int max = 0;
        String strmin = minmaxstr;
        String strmax = minmaxstr;

        if (minmaxstr.contains("-")) {
            strmin = minmaxstr.substring(0, minmaxstr.indexOf("-"));
            strmax = minmaxstr.substring(minmaxstr.indexOf("-") + 1);
        }
        if (strmin.matches("[1-9]+[0-9]*")) min = Integer.parseInt(strmin);
        max = min;
        if (strmax.matches("[1-9]+[0-9]*")) max = Integer.parseInt(strmax);
        if (max > min) return min + u().tryChance(1 + max - min);
        else return min;
    }

    public static String soundPlay(Location loc, Param params) {
        if (params.isEmpty()) return "";
        Location soundLoc = loc;
        String sndstr = "";
        String strvolume = "1";
        String strpitch = "1";
        float pitch = 1;
        float volume = 1;
        if (params.hasAnyParam("param")) {
            String param = params.getParam("param", "");
            if (param.isEmpty()) return "";
            if (param.contains("/")) {
                String[] prm = param.split("/");
                if (prm.length > 1) {
                    sndstr = prm[0];
                    strvolume = prm[1];
                    if (prm.length > 2) strpitch = prm[2];
                }
            } else sndstr = param;
            if (strvolume.matches("([0-9]+\\.[0-9]+)|([0-9]+)")) volume = Float.parseFloat(strvolume);
            if (strpitch.matches("([0-9]+\\.[0-9]+)|([0-9]+)")) pitch = Float.parseFloat(strpitch);
        } else {
            String locationStr = params.getParam("loc");
            soundLoc = locationStr.isEmpty() ? loc : Locator.parseLocation(locationStr, null);
            sndstr = params.getParam("type", "");
            pitch = params.getParam("pitch", 1.0f);
            volume = params.getParam("volume", 1.0f);
        }
        Sound sound = getSoundStr(sndstr);
        if (soundLoc != null) soundLoc.getWorld().playSound(soundLoc, sound, volume, pitch);
        return sound.name();
    }

    public static void soundPlay(Location loc, String param) {
        if (param.isEmpty()) return;
        /*Map<String,String> params = new HashMap<String,String>();
        params.put("param", param); */
        Param params = new Param(param, "param");
        soundPlay(loc, params);
    }


    private static Sound getSoundStr(String param) {
        Sound snd = null;
        try {
            snd = Sound.valueOf(param.toUpperCase());
        } catch (Exception e) {
        }
        if (snd == null) snd = Sound.UI_BUTTON_CLICK;
        return snd;
    }


    public static List<Entity> getEntities(Location l1, Location l2) {
        List<Entity> entities = new ArrayList<Entity>();
        if (!l1.getWorld().equals(l2.getWorld())) return entities;
        int x1 = Math.min(l1.getBlockX(), l2.getBlockX());
        int x2 = Math.max(l1.getBlockX(), l2.getBlockX());
        int y1 = Math.min(l1.getBlockY(), l2.getBlockY());
        int y2 = Math.max(l1.getBlockY(), l2.getBlockY());
        int z1 = Math.min(l1.getBlockZ(), l2.getBlockZ());
        int z2 = Math.max(l1.getBlockZ(), l2.getBlockZ());
        int chX1 = x1 >> 4;
        int chX2 = x2 >> 4;
        int chZ1 = z1 >> 4;
        int chZ2 = z2 >> 4;
        for (int x = chX1; x <= chX2; x++)
            for (int z = chZ1; z <= chZ2; z++) {
                for (Entity e : l1.getWorld().getChunkAt(x, z).getEntities()) {
                    double ex = e.getLocation().getX();
                    double ey = e.getLocation().getY();
                    double ez = e.getLocation().getZ();
                    if ((x1 <= ex) && (ex <= x2) && (y1 <= ey) && (ey <= y2) && (z1 <= ez) && (ez <= z2))
                        entities.add(e);
                }
            }
        return entities;
    }


    @SuppressWarnings("deprecation")
    public static String replaceStandartLocations(Player p, String param) {
        if (p == null) return param;
        Location targetBlock = null;
        try {
            targetBlock = p.getTargetBlock((Set<Material>) null, 100).getLocation();
        } catch (Exception e) {
        }
        Map<String, Location> locs = new HashMap<String, Location>();
        locs.put("%here%", p.getLocation());
        locs.put("%eye%", p.getEyeLocation());
        locs.put("%head%", p.getEyeLocation());
        locs.put("%viewpoint%", targetBlock);
        locs.put("%view%", targetBlock);
        locs.put("%selection%", Selector.getSelectedLocation(p));
        locs.put("%select%", Selector.getSelectedLocation(p));
        locs.put("%sel%", Selector.getSelectedLocation(p));
        String newparam = param;
        for (String key : locs.keySet()) {
            Location l = locs.get(key);
            if (l == null) continue;
            newparam = newparam.replace(key, Locator.locationToString(l));
        }
        return newparam;
    }

    public static PotionEffectType parsePotionEffect(String name) {
        PotionEffectType pef = null;
        try {
            pef = PotionEffectType.getByName(name);
        } catch (Exception e) {
        }
        return pef;
    }

    public static LivingEntity getAnyKiller(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent evdmg = (EntityDamageByEntityEvent) event;
            if (evdmg.getDamager() instanceof LivingEntity) return (LivingEntity) evdmg.getDamager();
            if (evdmg.getCause() == DamageCause.PROJECTILE) {
                Projectile prj = (Projectile) evdmg.getDamager();
                LivingEntity shooterEntity = BukkitCompatibilityFix.getShooter(prj);
                if (shooterEntity == null) return null;
                if (shooterEntity instanceof LivingEntity) return (LivingEntity) shooterEntity;
            }
        }
        return null;
    }


    public static Player getKiller(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent evdmg = (EntityDamageByEntityEvent) event;
            if (evdmg.getDamager().getType() == EntityType.PLAYER) return (Player) evdmg.getDamager();
            if (evdmg.getCause() == DamageCause.PROJECTILE) {
                Projectile prj = (Projectile) evdmg.getDamager();
                LivingEntity shooterEntity = BukkitCompatibilityFix.getShooter(prj);
                if (shooterEntity == null) return null;
                if (shooterEntity instanceof Player) return (Player) shooterEntity;
            }
        }
        return null;
    }


    public static LivingEntity getDamagerEntity(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent evdmg = (EntityDamageByEntityEvent) event;
            if (evdmg.getCause() == DamageCause.PROJECTILE) {
                Projectile prj = (Projectile) evdmg.getDamager();
                LivingEntity shooterEntity = BukkitCompatibilityFix.getShooter(prj);
                if (shooterEntity == null) return null;
                return shooterEntity;
            } else if (evdmg.getDamager() instanceof LivingEntity) return (LivingEntity) evdmg.getDamager();
        }
        return null;
    }

    public static boolean isAnyParamExist(Map<String, String> params, String... param) {
        for (String key : params.keySet())
            for (String prm : param) {
                if (key.equalsIgnoreCase(prm)) return true;
            }
        return false;
    }

    public static boolean setOpen(Block b, boolean open) {
        BlockState state = b.getState();
        if (!(state.getData() instanceof Openable)) return false;
        Openable om = (Openable) state.getData();
        om.setOpen(open);
        state.setData((MaterialData) om);
        state.update();
        return true;
    }

    public static boolean isOpen(Block b) {
        if (b.getState().getData() instanceof Openable) {
            Openable om = (Openable) b.getState().getData();
            return om.isOpen();
        }
        return false;
    }

    public static Block getDoorBottomBlock(Block b) {
        if (b.getType() != Material.WOODEN_DOOR) return b;
        if (b.getRelative(BlockFace.DOWN).getType() == Material.WOODEN_DOOR)
            return b.getRelative(BlockFace.DOWN);
        return b;
    }

    public static boolean isDoorBlock(Block b) {
        if (b.getType() == Material.WOODEN_DOOR) return true;
        if (b.getType() == Material.TRAP_DOOR) return true;
        if (b.getType() == Material.FENCE_GATE) return true;
        return false;
    }

    public static String join(String... s) {
        StringBuilder sb = new StringBuilder();
        for (String str : s)
            sb.append(str);
        return sb.toString();
    }

}
