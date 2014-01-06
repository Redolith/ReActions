package me.fromgate.reactions.flags;

import me.fromgate.reactions.util.RATowny;

import org.bukkit.entity.Player;

public class FlagTown extends Flag{

    @Override
    public boolean checkFlag(Player p, String param) {
        if (!plg().isTownyConnected()) return false; 
        return RATowny.playerInTown(p, param);
    }

}
