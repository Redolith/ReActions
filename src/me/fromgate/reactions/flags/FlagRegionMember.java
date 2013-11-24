package me.fromgate.reactions.flags;

import me.fromgate.reactions.util.RAWorldGuard;

import org.bukkit.entity.Player;

public class FlagRegionMember extends Flag {

    @Override
    public boolean checkFlag(Player p, String param) {
        return RAWorldGuard.isPlayerIsMember(p, param);
    }

}
