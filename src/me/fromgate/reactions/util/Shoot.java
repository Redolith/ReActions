package me.fromgate.reactions.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.fromgate.reactions.ReActions;

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

public class Shoot {
	
	public static String actionShootBreak = "GLASS,THIN_GLASS,STAINED_GLASS,STAINED_GLASS_PANE,GLOWSTONE,REDSTONE_LAMP_OFF,REDSTONE_LAMP_ON";
	public static String actionShootThrough = "FENCE,FENCE_GATE,IRON_BARDING,IRON_FENCE,NETHER_FENCE";
	
    public static void shoot(LivingEntity shooter, Map<String,String> params){
        boolean onehit = ParamUtil.getParam(params, "singlehit", true);
        int distance = ParamUtil.getParam(params, "distance", 100);
        for (LivingEntity le : getEntityBeam (shooter,getBeam (shooter,distance), onehit)){
            double damage = (double) Util.getMinMaxRandom(ParamUtil.getParam(params, "damage", "1"));
            damageEntity (shooter,le, damage);
        }
    }
    
    private static List<Block> getBeam(LivingEntity p, int distance) {
        List<Block> beam = new ArrayList<Block>();
        BlockIterator bi = new BlockIterator (p, distance);
        while (bi.hasNext()) {
            Block b = bi.next();
            if (isEmpty(b,p)) beam.add(b);
            else break;
        }
        return beam;
    }
    
    private static Set<LivingEntity> getEntityBeam (LivingEntity shooter, List<Block> beam, boolean hitSingle){
        Set<LivingEntity> list = new HashSet<LivingEntity>();
        for (Block b : beam)
            for (Entity e : b.getChunk().getEntities()){
                if (!(e instanceof LivingEntity)) continue;
                LivingEntity le = (LivingEntity) e;
                if (le.equals(shooter)) continue;
                if (isEntityAffectByBeamBlock(b,le))  {
                    list.add(le);
                    if (hitSingle) return list;
                }
            }
        return list;
    }
    
    @SuppressWarnings("deprecation")
	private static boolean isEmpty (Block b, LivingEntity shooter){
        if (!b.getType().isSolid()) return true;
        if (ReActions.util.isItemInList(b.getType().getId(),b.getData(), actionShootThrough )) return true; 
        if ((shooter instanceof Player)&&(isShotAndBreak (b, (Player) shooter))){
            b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());
            b.breakNaturally();
            return true;
        }
        return false;
    }
    
    
    public static boolean breakBlock(Block b, Player p){
        BlockBreakEvent event = new BlockBreakEvent(b, p);
        Bukkit.getServer().getPluginManager().callEvent(event);
        return !event.isCancelled();
    }
    
    @SuppressWarnings("deprecation")
	private static boolean isShotAndBreak(Block b,Player p){
    	if (ReActions.util.isItemInList(b.getType().getId(),b.getData(), actionShootBreak ))return breakBlock(b,p);
        return false;
    }
    
    private static boolean isEntityAffectByBeamBlock(Block b, LivingEntity le){
        if (le.getLocation().getBlock().equals(b)) return true;
        if (le.getEyeLocation().getBlock().equals(b)) return true;
        return false;
    }

    public static boolean damageEntity (LivingEntity damager, LivingEntity entity, double damage){
        //EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(damager, entity, DamageCause.ENTITY_ATTACK, Math.max(damage, 0));
    	EntityEvent event= BukkitCompatibilityFix.createEntityDamageEvent(damager, entity, DamageCause.ENTITY_ATTACK, damage);

    	if (event == null) return false;
    	
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (!((Cancellable) event).isCancelled()) //e.damage(event.getDamage(), event.getDamager());
        	BukkitCompatibilityFix.damageEntity(entity, damage);
        return !((Cancellable) event).isCancelled();
    }
    
}
