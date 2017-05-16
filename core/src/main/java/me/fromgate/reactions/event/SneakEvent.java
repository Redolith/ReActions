package me.fromgate.reactions.event;

import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 2017-05-16.
 */
public class SneakEvent extends RAEvent {
    private Boolean isSneaking;

    public SneakEvent(Player player, Boolean isSneaking) {
        super(player);
        this.isSneaking = isSneaking;
    }

    public Boolean getSneak() {
        return this.isSneaking;
    }
}