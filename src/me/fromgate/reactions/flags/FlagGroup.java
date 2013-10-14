package me.fromgate.reactions.flags;

import me.fromgate.reactions.util.RAVault;

import org.bukkit.entity.Player;

public class FlagGroup extends Flag{

    @Override
    public boolean checkFlag(Player p, String param) {
        RAVault.playerInGroup(p, param);
        return false;
    }

}
