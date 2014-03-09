package me.fromgate.reactions.event;

import org.bukkit.entity.Player;

public class PVPDeathEvent extends RAEvent {
    private Player deadplayer;

    public PVPDeathEvent (Player killer, Player killedplayer){
        super(killer);
        this.deadplayer = killedplayer;
    }

    @Override
    public Player getPlayer() {
        return this.deadplayer;
    }

    public Player getKiller() {
        return player;
    }

}
