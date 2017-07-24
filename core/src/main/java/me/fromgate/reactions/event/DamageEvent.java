package me.fromgate.reactions.event;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 * Created by MaxDikiy on 2017-07-23.
 */
public class DamageEvent extends RAEvent {
    private double damage;
    private DamageCause cause;
    public String source;

    public DamageEvent(Player player, double damage, DamageCause cause, String source) {
        super(player);
        this.damage = damage;
        this.cause = cause;
        this.source = source;
    }

    public double getDamage() {
        return this.damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public DamageCause getCause() {
        return this.cause;
    }

    public String getSource() {
        return this.source;
    }

}
