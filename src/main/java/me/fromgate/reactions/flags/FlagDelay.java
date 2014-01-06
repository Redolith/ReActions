package me.fromgate.reactions.flags;

import me.fromgate.reactions.util.Delayer;

import org.bukkit.entity.Player;

public class FlagDelay extends Flag{

    @Override
    public boolean checkFlag(Player p, String param) {
        return Delayer.checkDelay(param);
    }

}
