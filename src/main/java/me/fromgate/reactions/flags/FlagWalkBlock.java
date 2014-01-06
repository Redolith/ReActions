package me.fromgate.reactions.flags;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FlagWalkBlock extends Flag{

    @SuppressWarnings("deprecation")
    @Override
    public boolean checkFlag(Player p, String param) {
        Block walk = p.getLocation().getBlock();
        if (walk.getType() == Material.AIR) walk = walk.getRelative(BlockFace.DOWN);
        return u().compareItemStr(new ItemStack (walk.getType(),1,walk.getData()),param );
    }

}
