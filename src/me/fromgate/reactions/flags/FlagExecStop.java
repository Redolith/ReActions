package me.fromgate.reactions.flags;

import me.fromgate.reactions.activators.Activators;

import org.bukkit.entity.Player;

public class FlagExecStop extends Flag{

    @Override
    public boolean checkFlag(Player p, String param) {
        if (param.isEmpty()) return false;
        return Activators.isStopped(p, param, false);
    }

}
