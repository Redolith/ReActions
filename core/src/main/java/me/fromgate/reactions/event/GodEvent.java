package me.fromgate.reactions.event;

import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 2017-10-27.
 */
public class GodEvent extends RAEvent {
    private boolean god;

    public GodEvent(Player player, boolean god) {
        super(player);
        this.god = god;
    }

    public boolean isGod() {
        return god;
    }

    public void setGod(boolean god) {
        this.god = god;
    }
}
