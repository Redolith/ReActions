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

package me.fromgate.reactions.util.mob;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.externals.RAEffects;
import me.fromgate.reactions.externals.RAWorldGuard;
import me.fromgate.reactions.util.BukkitCompatibilityFix;
import me.fromgate.reactions.util.Locator;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.item.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobSpawn {

    private static Map<LivingEntity, List<ItemStack>> drops = new HashMap<LivingEntity, List<ItemStack>>();


    public static void mobSpawn(Player p, Param params) {
        String mob = params.getParam("type", "").toUpperCase();
        if (mob.isEmpty()) {
            ReActions.getUtil().log("Failed to spawn mob: " + params.getParam("param-line"));
            return;
        }
        String locstr = params.getParam("loc", "");
        Location loc = Locator.parseLocation(locstr, p == null ? null : p.getLocation());
        String region = params.getParam("region", "");
        int radius = params.getParam("radius", 0);
        int num = Util.getMinMaxRandom(params.getParam("num", "1"));
        String hparam = params.getParam("health", "0");
        double health = Util.getMinMaxRandom(hparam);
        String playeffect = params.getParam("effect", "");
        String dtheffect = params.getParam("dtheffect", "");
        String chest = params.getParam("chest", "");
        String leg = params.getParam("leg", "");
        String helm = params.getParam("helm", "");
        String boot = params.getParam("boot", "");
        String weapon = params.getParam("weapon", "");
        boolean land = params.getParam("land", true);
        String poteff = params.getParam("potion", "");
        String name = params.getParam("name", "");
        String drop = params.getParam("drop", "");
        String xp = params.getParam("xp", "");
        String money = params.getParam("money", "");
        String growl = params.getParam("growl", "");
        String cry = params.getParam("cry", "");
        String equip = params.getParam("equip", "");
        double dmg = params.getParam("dmg", 1.0D);
        String exec = params.getParam("run", "");
        String exec_delay = params.getParam("rundelay", "1t");

        if (RAWorldGuard.isRegionExists(region)) loc = Locator.getRegionLocation(region, land);
        else if (radius > 0) loc = Locator.getRadiusLocation(loc, radius, land);
        if (loc == null) return;

        for (int i = 0; i < num; i++) {
            List<LivingEntity> mobs = spawnMob(loc, mob);
            for (LivingEntity le : mobs) {
                setMobHealth(le, health);
                setMobName(le, name);
                potionEffect(le, poteff);
                if (equip.isEmpty()) setMobEquipment(le, helm, chest, leg, boot, weapon);
                else setMobEquipment(le, equip);
                setMobDrop(le, drop);
                setMobXP(le, xp);
                setMobMoney(le, money);
                setMobExec(le, exec, exec_delay);
                setMobDmgMultiplier(le, dmg);
                setMobGrowl(le, growl);
                setMobCry(le, cry);
                setDeathEffect(le, dtheffect);
            }
            playMobEffect(loc, playeffect);
        }
    }


    @SuppressWarnings("deprecation")
    private static List<LivingEntity> spawnMob(Location loc, String mobstr) {
        List<LivingEntity> mobs = new ArrayList<LivingEntity>();
        String[] ln = mobstr.split(":");
        if (ln.length < 1) return mobs;

        for (int i = 0; i < Math.min(2, ln.length); i++) {
            String mbs = ln[i];
            String name = "";
            if (mbs.contains("$")) {
                name = mbs.substring(0, mbs.indexOf("$"));
                mbs = mbs.substring(name.length() + 1);
            }


            EntityType et = EntityType.fromName(mbs);
            if (mbs.equalsIgnoreCase("horse")) et = MobHorse.getHorseType();
            if (mbs.equalsIgnoreCase("wither")) et = MobWither.getWitherType();

            if (et == null) {
                ReActions.util.logOnce("mobspawnunknowntype_" + mobstr, "Unknown mob type " + mbs + " (" + mobstr + ")");
                continue;
            }
            Entity e = loc.getWorld().spawnEntity(loc, et);
            if (e == null) {
                ReActions.util.logOnce("mobspawnfail_" + mobstr, "Cannot spawn mob " + mbs + " (" + mobstr + ")");
                continue;
            }

            if (!(e instanceof LivingEntity)) {
                e.remove();
                ReActions.util.logOnce("mobspawnnotmob_" + mobstr, "Cannot spawn mob " + mbs + " (" + mobstr + ")");
                continue;
            }


            LivingEntity mob = (LivingEntity) e;
            setMobName(mob, name);
            mobs.add(mob);
        }
        if (mobs.size() == 2) mobs.get(1).setPassenger(mobs.get(0));
        return mobs;
    }


    public static void playMobEffect(Location loc, String playeffect) {
        if (playeffect.isEmpty()) return;
        int data = 0;
        if (playeffect.equalsIgnoreCase("smoke")) data = 9;
        RAEffects.playEffect(loc, playeffect, data);
    }

    public static void setMobName(LivingEntity e, String name) {
        if (name.isEmpty()) return;
        if ((e.getCustomName() != null) && (!e.getCustomName().isEmpty())) return;
        e.setCustomName(ChatColor.translateAlternateColorCodes('&', name.replace("_", " ")));
        e.setCustomNameVisible(true);
    }

    public static void setMobXP(LivingEntity e, String xp) {
        if (xp.isEmpty()) return;
        e.setMetadata("ReActions-xp", new FixedMetadataValue(ReActions.instance, xp));
    }

    public static void setMobMoney(LivingEntity e, String money) {
        if (money.isEmpty()) return;
        e.setMetadata("ReActions-money", new FixedMetadataValue(ReActions.instance, money));
    }

    public static void setMobExec(LivingEntity e, String exec_activator, String exec_delay) {
        if (exec_activator.isEmpty()) return;
        e.setMetadata("ReActions-activator", new FixedMetadataValue(ReActions.instance, "activator:" + exec_activator + (exec_delay.isEmpty() ? "" : " delay:" + exec_delay)));
    }

    public static void setMobDrop(LivingEntity e, String drop) {
        //id:data*amount,id:dat*amount%chance;id:data*amount;id:dat*amount%chance;id:data*amount;id:dat*amount%chance
        if (drop.isEmpty()) return;
        List<ItemStack> stack = ItemUtil.parseRandomItemsStr(drop);
        if (stack.isEmpty()) return;
        setMobDropStack(e, stack);
    }

    private static void setMobDmgMultiplier(LivingEntity e, double dmg) {
        if (dmg < 0) return;
        e.setMetadata("ReActions-dmg", new FixedMetadataValue(ReActions.instance, dmg));
    }

    private static void setMobCry(LivingEntity e, String cry) {
        if (cry.isEmpty()) return;
        e.setMetadata("ReActions-cry", new FixedMetadataValue(ReActions.instance, cry));
    }

    private static void setMobGrowl(LivingEntity e, String growl) {
        if (growl.isEmpty()) return;
        e.setMetadata("ReActions-growl", new FixedMetadataValue(ReActions.instance, growl));
    }

    public static void setMobDropStack(LivingEntity e, List<ItemStack> stack) {
        if (stack.isEmpty()) return;
        drops.put(e, stack);
        //e.setMetadata("ReActions-drop", new FixedMetadataValue(ReActions.instance, stack));
    }

    private static void setDeathEffect(LivingEntity e, String dtheffect) {
        if (dtheffect.isEmpty()) return;
        e.setMetadata("ReActions-deatheffect", new FixedMetadataValue(ReActions.instance, dtheffect));
    }


    public static void setMobHealth(LivingEntity e, double health) {
        if (health > 0) {
            if (health > BukkitCompatibilityFix.getEntityHealth(e))
                BukkitCompatibilityFix.setEntityMaxHealth(e, health);
            BukkitCompatibilityFix.setEntityHealth(e, health);
        }
    }

    public static void setMobEquipment(LivingEntity e, String equip) {
        if (equip.isEmpty()) return;
        if (!ReActions.util.isWordInList(e.getType().name(), "zombie,skeleton")) return;
        String[] ln = equip.split(";");
        if (ln.length == 0) return;
        String[] eq = {"", "", "", "", ""};
        for (int i = 0; i < Math.min(ln.length, 5); i++) eq[i] = ln[i];
        setMobEquipment(e, eq[0], eq[1], eq[2], eq[3], eq[4]);
    }

    public static void setMobEquipment(LivingEntity e, String helm, String chest, String leg, String boot, String weapon) {
        // if (!ReActions.util.isWordInList(e.getType().name(), "zombie,skeleton,villager")) return;
        if (!helm.isEmpty()) {
            ItemStack item = ItemUtil.getRndItem(helm);
            if (item != null) e.getEquipment().setHelmet(item);
        }
        if (!chest.isEmpty()) {
            ItemStack item = ItemUtil.getRndItem(chest);
            if (item != null) e.getEquipment().setChestplate(item);
        }
        if (!leg.isEmpty()) {
            ItemStack item = ItemUtil.getRndItem(leg);
            if (item != null) e.getEquipment().setLeggings(item);
        }
        if (!boot.isEmpty()) {
            ItemStack item = ItemUtil.getRndItem(boot);
            if (item != null) e.getEquipment().setBoots(item);
        }
        if (!weapon.isEmpty()) {
            ItemStack item = ItemUtil.getRndItem(weapon);
            if (item != null) e.getEquipment().setItemInHand(item);
        }
    }

    public static void potionEffect(LivingEntity e, String potion) {
        if (potion.isEmpty()) return;
        String[] pts = potion.split(",");
        for (String pot : pts) {
            String pef = "";
            int level = 1;
            String[] ln = pot.split(":");
            pef = ln[0];
            PotionEffectType pet = Util.parsePotionEffect(pef);
            if (pet == null) continue;
            if ((ln.length == 2) && ReActions.util.isInteger(ln[1])) level = Integer.parseInt(ln[1]);
            PotionEffect pe = new PotionEffect(pet, Integer.MAX_VALUE, level, true);
            e.addPotionEffect(pe);
        }
    }


    public static List<ItemStack> getMobDrop(LivingEntity le) {
        if (drops.containsKey(le)) {
            List<ItemStack> drop = drops.get(le);
            drops.remove(le);
            return drop;
        } else {
            List<LivingEntity> deadMobs = new ArrayList<LivingEntity>();
            for (LivingEntity l : drops.keySet())
                if (l.isDead()) deadMobs.add(l);
            for (LivingEntity l : deadMobs)
                drops.remove(l);
        }
        return null;
    }


}
