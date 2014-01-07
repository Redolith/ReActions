package me.fromgate.reactions.flags;

import me.fromgate.reactions.externals.RAWorldGuard;

import org.bukkit.entity.Player;

public class FlagRegion extends Flag {

    private int flagType = 0;

    public FlagRegion (int flagType){
        this.flagType = flagType;
    }

    @Override
    public boolean checkFlag(Player p, String param) {
        if (!RAWorldGuard.isConnected()) return false;
        switch (flagType){
        case 0: return RAWorldGuard.isPlayerInRegion(p, param);
        case 1: return playersInRegion(param);
        case 2: return RAWorldGuard.isPlayerIsMember(p, param);
        case 3:  return RAWorldGuard.isPlayerIsOwner(p, param);
        }
        return false;
    }

    private boolean playersInRegion (String param){
        String rg = param;
        int minp = 1;
        if (param.contains("/")){
            rg = param.substring(0,param.indexOf("/"));
            String s = param.substring(param.indexOf("/")+1);
            if ((!s.isEmpty())&&u().isInteger(s)) minp = Integer.parseInt(s);
        }
        return (minp<=RAWorldGuard.countPlayersInRegion(rg));
    }
}
