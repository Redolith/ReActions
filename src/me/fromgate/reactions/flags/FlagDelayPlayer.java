package me.fromgate.reactions.flags;

import me.fromgate.reactions.util.RAFlagDelay;

import org.bukkit.entity.Player;

public class FlagDelayPlayer extends Flag {

    @Override
    public boolean checkFlag(Player p, String param) {
        return RAFlagDelay.checkPersonalDelay(p,param);
    }

}
