package me.fromgate.reactions.flags;

import org.bukkit.entity.Player;

public class FlagItemInventory extends Flag{

    @Override
    public boolean checkFlag(Player p, String item) {
        return (u().parseItemStack(item).getAmount()<=u().countItemInInventory(p, item));
    }

}
