package me.fromgate.reactions.flags;

import org.bukkit.entity.Player;

public class FlagChance extends Flag {

    @Override
    public boolean checkFlag(Player p, String param) {
        int d = 50;
        if (u().isInteger(param)) d = Integer.parseInt(param);
        d = Math.max(Math.min(d, 100), 0);
        return u().rollDiceChance(d);
    }

}
