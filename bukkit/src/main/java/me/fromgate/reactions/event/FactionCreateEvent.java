package me.fromgate.reactions.event;

import org.bukkit.entity.Player;

public class FactionCreateEvent extends RAEvent {
    String faction;

    public FactionCreateEvent(String factionName, Player player) {
        super(player);
        this.faction = factionName;
    }

    public String getFaction() {
        return this.faction;
    }

}
