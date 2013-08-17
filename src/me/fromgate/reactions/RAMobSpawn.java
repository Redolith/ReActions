package me.fromgate.reactions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String mob = Util.getParam(params, "type", "PIG");
        String locstr = Util.getParam(params, "loc", "");
        Location loc = Actions.locToLocation(p,locstr);
        String region = Util.getParam(params, "region", "");
        int radius = Util.getParam(params, "radius", 0);
        int num=Util.getMinMaxRandom(Util.getParam(params, "num", "1"));

        String hparam = Util.getParam(params, "health", "0");
        double health = Util.getMinMaxRandom(hparam);

        String playeffect = Util.getParam(params, "effect", "");
        String dtheffect = Util.getParam(params, "dtheffect", "");

        String chest = Util.getParam(params, "chest", "");
        String leg = Util.getParam(params, "leg", "");
        String helm = Util.getParam(params, "helm", "");
        String boot = Util.getParam(params, "boot", "");
        String weapon = Util.getParam(params, "weapon", "");
        boolean land = Util.getParam(params, "land", true);

        String poteff = Util.getParam(params, "potion", "");
        String name = Util.getParam(params, "name", "");

        String drop = Util.getParam(params, "drop", "");
        String xp= Util.getParam(params, "xp", "");
        String money = Util.getParam(params, "money", "");

        String growl = Util.getParam(params, "growl", "");
        String cry = Util.getParam(params, "cry", "");

        String equip = Util.getParam(params, "equip", "");
        
        double dmg = Util.getParam(params, "dmg", 1.0D);

        List<Location> locs = null;
        if (ReActions.instance.worldguard.isRegionExists(region)) locs = Util.getLocationsRegion(region, land);
        else if (radius>0) locs = Util.getLocationsRadius(loc, radius, land);

        for (int i = 0; i<num; i++){
            if (loc == null) loc = p.getLocation();
            if ((locs!=null)&&(!locs.isEmpty()))
                loc = Util.getRandomLocationList(locs);
            Entity e = loc.getWorld().spawnEntity(loc, EntityType.fromName(mob));
            if (e == null) break;
            if (!(e instanceof LivingEntity)) {
                e.remove();
                break;
            }

            playMobEffect (loc,playeffect);
            LivingEntity le = (LivingEntity)e;
            setMobHealth (le,health);
            setMobName (le,name);
            potionEffect (le,poteff);
            if (equip.isEmpty()) setMobEquipment(le,helm, chest, leg, boot,weapon);
            else setMobEquipment(le,equip);
            setMobDrop (le,drop);
            setMobXP (le,xp);
            setMobMoney (le,money);
            setMobDmgMultiplier (le,dmg);
            setMobGrowl(le,growl);
            setMobCry(le,cry);
            //String deatheffect = Util.getParam(params, "deatheffect", "");
            setDeathEffect (le,dtheffect);
        }
    }


    public static void playMobEffect (Location loc, String playeffect){
        if (playeffect.isEmpty()) return;
        int data = 0;
        if (playeffect.equalsIgnoreCase("smoke")) data = 9;
        RAEffects.playEffect(loc, playeffect, data);
    }

    public static void setMobName (LivingEntity e, String name){
        if (name.isEmpty()) return;
        e.setCustomName(name);    
        e.setCustomNameVisible(true);
    }

    //String xp= Util.getParam(params, "xp", "");

    public static void setMobXP(LivingEntity e, String xp){
        if (xp.isEmpty()) return;  
        e.setMetadata("ReActions-xp", new FixedMetadataValue(ReActions.instance, xp));
    }

    public static void setMobMoney (LivingEntity e, String money){
        if (money.isEmpty()) return;  
        e.setMetadata("ReActions-money", new FixedMetadataValue(ReActions.instance, money));
    }


    public static void setMobDrop(LivingEntity e, String drop){
        //id:data*amount,id:dat*amount@chance;id:data*amount;id:dat*amount@chance;id:data*amount;id:dat*amount@chance
        if (drop.isEmpty()) return;
        String [] loots = drop.split(";");
        Map<String,Integer> drops = new HashMap<String,Integer>();
        int maxchance = 0;
        int nochcount = 0;
        for (String loot: loots){
            String [] ln = loot.split("@");
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

        if (drops.isEmpty()) return;
        int eqperc = (nochcount*100)/drops.size();
        maxchance = maxchance+eqperc*nochcount;
        int rnd = ReActions.util.random.nextInt(maxchance);
        int curchance = 0;
        for (String stack : drops.keySet()){
            curchance = curchance+ (drops.get(stack)<0 ? eqperc : drops.get(stack));
            if (rnd<=curchance) {
                setMobDropStack (e,stack);
                return;
            }
        }
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
            PotionEffectType pet = Actions.parsePotionEffect(pef);
            if (pet==null) continue;
            if ((ln.length == 2)&&ReActions.util.isInteger(ln[1])) level = Integer.parseInt(ln[1]);
            PotionEffect pe = new PotionEffect (pet, Integer.MAX_VALUE,level,true);
            e.addPotionEffect(pe);
        }
    }

    public static List<ItemStack> parseItemStacks (String items){
        List<ItemStack> stacks = new ArrayList<ItemStack>();
        String[] ln = items.split(",");
        for (String item : ln){
            ItemStack stack = ReActions.util.parseItemStack(item);
            if (stack != null) stacks.add(stack);
        }
        return stacks;
    }


}
