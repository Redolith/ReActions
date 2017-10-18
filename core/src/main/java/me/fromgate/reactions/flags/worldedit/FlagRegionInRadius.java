package me.fromgate.reactions.flags.worldedit;

import me.fromgate.reactions.externals.RAWorldEdit;
import me.fromgate.reactions.flags.Flag;
import org.bukkit.entity.Player;

public class FlagRegionInRadius extends Flag {
    @Override
    public boolean checkFlag(Player p, String param) {
        int radius = 0;
        if (!param.isEmpty()) radius = Integer.parseInt(param);
        return RAWorldEdit.checkRegionInRadius(p, radius);
    }
}
