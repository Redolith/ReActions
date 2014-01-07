package me.fromgate.reactions.flags;

import me.fromgate.reactions.util.Delayer;

import org.bukkit.entity.Player;

public class FlagDelay extends Flag{
    boolean globalDelay = true;

    public FlagDelay(boolean globalDelay){
        this.globalDelay = globalDelay;
    }
    
    @Override
    public boolean checkFlag(Player p, String param) {
        if (globalDelay) return Delayer.checkDelay(param);
        else return Delayer.checkPersonalDelay(p,param);
    }

}
