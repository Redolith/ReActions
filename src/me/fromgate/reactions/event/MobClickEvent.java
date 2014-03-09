package me.fromgate.reactions.event;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class MobClickEvent extends RAEvent {
    private LivingEntity mob;
    
    public MobClickEvent (Player p,LivingEntity mob) {
    	super (p);
        this.mob = mob;
    }

    public LivingEntity getMob(){
        return this.mob;
    }    

}
