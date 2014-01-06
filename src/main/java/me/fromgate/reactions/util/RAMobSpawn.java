package me.fromgate.reactions.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.fromgate.reactions.ReActions;

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

public class RAMobSpawn {

    public static void mobSpawn(Player p, Map<String,String> params) {
        String mob = ParamUtil.getParam(params, "type", "PIG");
        String locstr = ParamUtil.getParam(params, "loc", "");
        Location loc = Util.locToLocation(p,locstr);
        String region = ParamUtil.getParam(params, "region", "");
        int radius = ParamUtil.getParam(params, "radius", 0);
        int num=Util.getMinMaxRandom(ParamUtil.getParam(params, "num", "1"));
        String hparam = ParamUtil.getParam(params, "health", "0");
        double health = Util.getMinMaxRandom(hparam);
        String playeffect = ParamUtil.getParam(params, "effect", "");
        String dtheffect = ParamUtil.getParam(params, "dtheffect", "");
        String chest = ParamUtil.getParam(params, "chest", "");
        String leg = ParamUtil.getParam(params, "leg", "");
        String helm = ParamUtil.getParam(params, "helm", "");
        String boot = ParamUtil.getParam(params, "boot", "");
        String weapon = ParamUtil.getParam(params, "weapon", "");
        boolean land = ParamUtil.getParam(params, "land", true);
        String poteff = ParamUtil.getParam(params, "potion", "");
        String name = ParamUtil.getParam(params, "name", "");
        String drop = ParamUtil.getParam(params, "drop", "");
        String xp= ParamUtil.getParam(params, "xp", "");
        String money = ParamUtil.getParam(params, "money", "");
        String growl = ParamUtil.getParam(params, "growl", "");
        String cry = ParamUtil.getParam(params, "cry", "");
        String equip = ParamUtil.getParam(params, "equip", "");
        double dmg = ParamUtil.getParam(params, "dmg", 1.0D);
        String exec = ParamUtil.getParam(params, "run", "");
        String exec_delay = ParamUtil.getParam(params, "rundelay", "1t");

        if (loc == null) loc = p.getLocation();
        List<Location> locs = null;
        
        if (RAWorldGuard.isRegionExists(region)) locs = Util.getLocationsRegion(region, land);
        else if (radius>0) locs = Util.getLocationsRadius(loc, radius, land);

        for (int i = 0; i<num; i++){

            if ((locs!=null)&&(!locs.isEmpty()))
                loc = Util.getRandomLocationList(locs);


            List<LivingEntity> mobs = spawnMob(loc,mob);


            for (LivingEntity le : mobs){

                setMobHealth (le,health);
                setMobName (le,name);
                potionEffect (le,poteff);
                if (equip.isEmpty()) setMobEquipment(le,helm, chest, leg, boot,weapon);
                else setMobEquipment(le,equip);
                setMobDrop (le,drop);
                setMobXP (le,xp);
                setMobMoney (le,money);
                setMobExec (le,exec,exec_delay );
                setMobDmgMultiplier (le,dmg);
                setMobGrowl(le,growl);
                setMobCry(le,cry);
                setDeathEffect (le,dtheffect);

            }

            playMobEffect (loc,playeffect);

        }
    }


    @SuppressWarnings("deprecation")
    private static List<LivingEntity> spawnMob(Location loc, String mobstr) {
        List<LivingEntity> mobs = new ArrayList<LivingEntity>();
        String [] ln = mobstr.split(":");
        if (ln.length<1) return mobs;

        for (int i = 0;i<Math.min(2, ln.length); i++){
            String mbs =  ln[i];
            String name = "";
            if (mbs.contains("$")){
                name = mbs.substring(0,mbs.indexOf("$"));
                mbs = mbs.substring(name.length()+1);
            }

            
            EntityType et = EntityType.fromName(mbs);
            if (mbs.equalsIgnoreCase("horse")) et = EntityType.HORSE;
            
            if (et == null){
                ReActions.util.logOnce("mobspawnunknowntype_"+mobstr, "Unknown mob type "+mbs+" ("+mobstr+")");
                continue;
            }
            Entity e = loc.getWorld().spawnEntity(loc, et);
            if (e == null) {
                ReActions.util.logOnce("mobspawnfail_"+mobstr, "Cannot spawn mob "+mbs+" ("+mobstr+")");
                continue;
            }
        
            if (!(e instanceof LivingEntity)) {
                e.remove();
                ReActions.util.logOnce("mobspawnnotmob_"+mobstr, "Cannot spawn mob "+mbs+" ("+mobstr+")");
                continue;
            }
            
            
            
            LivingEntity mob = (LivingEntity)e;
            setMobName (mob, name);
            mobs.add(mob);
        }
        if (mobs.size()==2) mobs.get(1).setPassenger(mobs.get(0));
        return mobs;
    }


    public static void playMobEffect (Location loc, String playeffect){
        if (playeffect.isEmpty()) return;
        int data = 0;
        if (playeffect.equalsIgnoreCase("smoke")) data = 9;
        RAEffects.playEffect(loc, playeffect, data);
    }

    public static void setMobName (LivingEntity e, String name){
        if (name.isEmpty()) return;
        if ((e.getCustomName()!=null)&&(!e.getCustomName().isEmpty())) return;
        e.setCustomName(ChatColor.translateAlternateColorCodes('&', name.replace("_", " ")));    
        e.setCustomNameVisible(true);
    }

    public static void setMobXP(LivingEntity e, String xp){
        if (xp.isEmpty()) return;  
        e.setMetadata("ReActions-xp", new FixedMetadataValue(ReActions.instance, xp));
    }

    public static void setMobMoney (LivingEntity e, String money){
        if (money.isEmpty()) return;  
        e.setMetadata("ReActions-money", new FixedMetadataValue(ReActions.instance, money));
    }
    
    public static void setMobExec (LivingEntity e, String exec_activator, String exec_delay){
        if (exec_activator.isEmpty()) return;  
        e.setMetadata("ReActions-activator", new FixedMetadataValue(ReActions.instance, "activator:"+exec_activator+(exec_delay.isEmpty() ? "" : " delay:"+exec_delay)));
    }

    public static void setMobDrop(LivingEntity e, String drop){
        //id:data*amount,id:dat*amount%chance;id:data*amount;id:dat*amount%chance;id:data*amount;id:dat*amount%chance
        if (drop.isEmpty())  return;
        String stack = Util.parseRandomItemsStr(drop);
        if (stack.isEmpty()) return;
        setMobDropStack (e,stack);
    }

    private static void setMobDmgMultiplier(LivingEntity e, double dmg) {
        if (dmg<0) return;
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

    public static void setMobDropStack(LivingEntity e, String stack) {
        if (stack.isEmpty()) return;
        e.setMetadata("ReActions-drop", new FixedMetadataValue(ReActions.instance, stack));
    }

    private static void setDeathEffect(LivingEntity e, String dtheffect) {
        if (dtheffect.isEmpty()) return;
        e.setMetadata("ReActions-deatheffect", new FixedMetadataValue(ReActions.instance, dtheffect));
    }


    public static void setMobHealth (LivingEntity e, double health){
        if (health>0){
            try {
                e.setMaxHealth(health);
                e.setHealth(health);
            } catch (Throwable ex){
                ReActions.util.logOnce("mob_health", "Failed to set mob health. This feature is not compatible with CB 1.5.2 (and older)...");
            }
        }
    }

    public static void setMobEquipment(LivingEntity e, String equip){
        if (equip.isEmpty()) return;
        if (!ReActions.util.isWordInList(e.getType().name(), "zombie,skeleton")) return;
        String[] ln = equip.split(";");
        if (ln.length == 0) return;
        String [] eq = {"","","","",""};
        for (int i = 0; i< Math.min(ln.length, 5); i++) eq [i] = ln[i];
        setMobEquipment (e,eq[0],eq[1],eq[2],eq[3],eq[4]);
    }

    public static void setMobEquipment(LivingEntity e, String helm, String chest, String leg, String boot, String weapon){
        if (!ReActions.util.isWordInList(e.getType().name(), "zombie,skeleton")) return;
        if (!helm.isEmpty()){
            ItemStack item = Util.getRndItem(helm);
            if (item != null) e.getEquipment().setHelmet(item);
        }
        if (!chest.isEmpty()){
            ItemStack item = Util.getRndItem(chest);
            if (item != null) e.getEquipment().setChestplate(item);
        }
        if (!leg.isEmpty()){
            ItemStack item = Util.getRndItem(leg);
            if (item != null) e.getEquipment().setLeggings(item);
        }
        if (!boot.isEmpty()){
            ItemStack item = Util.getRndItem(boot);
            if (item != null) e.getEquipment().setBoots(item);
        }
        if (!weapon.isEmpty()){
            ItemStack item = Util.getRndItem(weapon);
            if (item != null) e.getEquipment().setItemInHand(item);
        }        
    }

    public static void potionEffect (LivingEntity e, String potion){
        if (potion.isEmpty()) return;
        String [] pts = potion.split(",");
        for (String pot : pts){
            String pef = "";
            int level = 1;
            String [] ln = pot.split(":");
            pef = ln[0];
            PotionEffectType pet = Util.parsePotionEffect(pef);
            if (pet==null) continue;
            if ((ln.length == 2)&&ReActions.util.isInteger(ln[1])) level = Integer.parseInt(ln[1]);
            PotionEffect pe = new PotionEffect (pet, Integer.MAX_VALUE,level,true);
            e.addPotionEffect(pe);
        }
    }




}
