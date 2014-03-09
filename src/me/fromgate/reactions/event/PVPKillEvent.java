package me.fromgate.reactions.event;

import org.bukkit.entity.Player;

public class PVPKillEvent extends RAEvent{
    private Player deadplayer;

    public PVPKillEvent (Player player, Player killedplayer){
        super (player);
        this.deadplayer = killedplayer;
    }

    public Player getKilledPlayer() {
        return this.deadplayer;
    }

}
