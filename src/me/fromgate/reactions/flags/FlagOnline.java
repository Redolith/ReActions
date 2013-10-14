package me.fromgate.reactions.flags;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class FlagOnline extends Flag{

    @Override
    public boolean checkFlag(Player p, String param) {
        if (!u().isIntegerGZ(param)) return false;
        int reqplayer = Integer.parseInt(param);
        return (reqplayer<=Bukkit.getOnlinePlayers().length);
    }

}
