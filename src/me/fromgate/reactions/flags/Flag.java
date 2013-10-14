package me.fromgate.reactions.flags;

import me.fromgate.reactions.RAUtil;
import me.fromgate.reactions.ReActions;

import org.bukkit.entity.Player;

public abstract class Flag {
    Flags ftype;
    ReActions plg(){
        return ReActions.instance;
    }
    
    RAUtil u(){
        return ReActions.util;
    }
    
    public abstract boolean checkFlag(Player p, String param);
}
