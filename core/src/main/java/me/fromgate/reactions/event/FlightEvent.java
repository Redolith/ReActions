package me.fromgate.reactions.event;

import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 5/2/2017.
 */
public class FlightEvent extends RAEvent {
    private Boolean isFlying;

    public FlightEvent(Player p, Boolean isFlying) {
        super(p);
        this.isFlying = isFlying;
    }

    public Boolean getFlight() {
        return this.isFlying;
    }

}
