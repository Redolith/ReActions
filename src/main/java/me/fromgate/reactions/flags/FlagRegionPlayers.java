package me.fromgate.reactions.flags;

import me.fromgate.reactions.util.RAWorldGuard;

import org.bukkit.entity.Player;

public class FlagRegionPlayers extends Flag {

    @Override
    public boolean checkFlag(Player p, String param) {
        if (!RAWorldGuard.isConnected()) return false;
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
