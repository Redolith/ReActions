package me.fromgate.reactions.event;

import org.bukkit.entity.Player;

public class JoinEvent extends RAEvent {
    private boolean firstjoin = false;
    
    public JoinEvent (Player p, boolean firstjoin){
        super (p);
        this.firstjoin = firstjoin;
    }

    public boolean isFirstJoin(){
        return this.firstjoin;
    }
}
