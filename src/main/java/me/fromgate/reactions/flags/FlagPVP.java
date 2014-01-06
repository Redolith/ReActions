package me.fromgate.reactions.flags;

import org.bukkit.entity.Player;

public class FlagPVP extends Flag{

    @Override
    public boolean checkFlag(Player p, String param) {
        if (!p.hasMetadata("reactions-pvp-time")) return false;
        if (!param.matches("[1-9]+[0-9]*")) return false;
        Long delay = Long.parseLong(param)*1000;
        Long curtime = System.currentTimeMillis();
        Long pvptime = p.getMetadata("reactions-pvp-time").get(0).asLong();
        return ((curtime-pvptime)<delay);
    }

}
