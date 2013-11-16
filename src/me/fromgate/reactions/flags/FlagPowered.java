package me.fromgate.reactions.flags;

import me.fromgate.playeffect.Util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.material.Lever;

public class FlagPowered extends Flag {

    @Override
    public boolean checkFlag(Player p, String param) {
        Location loc = Util.parseLocation(param);
        if (loc == null) return false;
        Block b = loc.getBlock();
        if (b.getType()==Material.LEVER){
            Lever lever = (Lever) b.getState().getData();
            return lever.isPowered();
        }
        return b.isBlockIndirectlyPowered();
    }

}
