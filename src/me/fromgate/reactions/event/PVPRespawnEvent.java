package me.fromgate.reactions.event;

import org.bukkit.entity.Player;

public class PVPRespawnEvent extends RAEvent{
    private Player deadplayer;


    public PVPRespawnEvent (Player player, Player killedplayer){
        super (player);
        this.deadplayer = killedplayer;
    }


    @Override
    public Player getPlayer() {
        return this.deadplayer;
    }

    public Player getKiller() {
        return this.player;
    }

}
