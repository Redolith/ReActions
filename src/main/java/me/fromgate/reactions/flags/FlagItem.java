package me.fromgate.reactions.flags;

import me.fromgate.reactions.util.Util;

import org.bukkit.entity.Player;

public class FlagItem extends Flag{

    @Override
    public boolean checkFlag(Player p, String item) {
        return Util.compareItemStr(p.getItemInHand(), item);
    }
    
        

}
