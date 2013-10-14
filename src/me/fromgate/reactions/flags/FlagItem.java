package me.fromgate.reactions.flags;

import org.bukkit.entity.Player;

public class FlagItem extends Flag{

    @Override
    public boolean checkFlag(Player p, String item) {
        return u().compareItemStr(p.getItemInHand(), item);
    }
    
        

}
