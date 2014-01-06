package me.fromgate.reactions.flags;

import me.fromgate.reactions.util.RAVault;

import org.bukkit.entity.Player;

public class FlagMoney extends Flag{

    @Override
    public boolean checkFlag(Player p, String amountstr) {
        return RAVault.isEconomyConected()&&u().isInteger(amountstr)&&(Integer.parseInt(amountstr)<=RAVault.getBalance(p.getName()));
    }

}
