package me.fromgate.reactions.flags;

import me.fromgate.reactions.util.RAWorldGuard;

import org.bukkit.entity.Player;

public class FlagRegionOwner extends Flag{
    @Override
    public boolean checkFlag(Player p, String param) {
        return RAWorldGuard.isPlayerIsOwner(p, param);
    }

}
