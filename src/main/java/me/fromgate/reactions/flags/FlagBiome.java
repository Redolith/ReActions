package me.fromgate.reactions.flags;

import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

public class FlagBiome extends Flag {

    @Override
    public boolean checkFlag(Player p, String param) {
        Biome b = null;
        for (Biome bb : Biome.values()){
            if (bb.name().equalsIgnoreCase(param)) b = bb;
        }
        if (b==null) return false;
        return p.getLocation().getBlock().getBiome().equals(b);
    }

}
