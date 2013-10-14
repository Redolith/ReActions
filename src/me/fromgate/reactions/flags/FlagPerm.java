package me.fromgate.reactions.flags;

import org.bukkit.entity.Player;

public class FlagPerm extends Flag {

    @Override
    public boolean checkFlag(Player p, String param) {
        return p.hasPermission(param);
    }

}
