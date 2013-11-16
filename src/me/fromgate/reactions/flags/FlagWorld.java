package me.fromgate.reactions.flags;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class FlagWorld extends Flag {

    @Override
    public boolean checkFlag(Player p, String param) {
        World w = Bukkit.getWorld(param);
        if (w == null) return false;
        return p.getWorld().equals(w);
    }

}
