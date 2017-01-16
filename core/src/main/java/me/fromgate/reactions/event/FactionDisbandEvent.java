package me.fromgate.reactions.event;

import org.bukkit.entity.Player;

public class FactionDisbandEvent extends RAEvent {
    String faction;

    public FactionDisbandEvent(String factionName, Player player) {
        super(player);
        this.faction = factionName;
    }

    public String getFaction() {
        return this.faction;
    }

}
