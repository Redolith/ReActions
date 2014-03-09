package me.fromgate.reactions.event;

import org.bukkit.entity.Player;

public class ExecEvent extends RAEvent {
    private String activator; 
    private Player targetPlayer;

    public ExecEvent (Player p, Player targetPlayer, String activator) {
        super(p);
        this.targetPlayer = targetPlayer;
        this.activator = activator;
    }

    public String getActivatorId() {
        return this.activator;
    }

    public Player getTargetPlayer(){
        return this.targetPlayer;
    }
}