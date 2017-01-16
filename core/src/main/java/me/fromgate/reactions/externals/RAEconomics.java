/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2014, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/reactions/
 *    
 *  This file is part of ReActions.
 *  
 *  ReActions is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ReActions is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with ReActions.  If not, see <http://www.gnorg/licenses/>.
 * 
 */

package me.fromgate.reactions.externals;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;


public class RAEconomics {

    public static boolean isEconomyFound() {
        if (RACraftConomy.isEnabled()) return true;
        if (RAVault.isEconomyConected()) return true;
        return false;
    }

    public static boolean hasMoney(String account, double amount, String currencyName, String worldName) {
        if (RACraftConomy.isEnabled()) return RACraftConomy.hasAmount(account, amount, currencyName, worldName);
        if (RAVault.isEconomyConected()) return RAVault.hasMoney(account, worldName, amount);
        return false;
    }


    public static String creditAccount(String target, String source, String amountStr, String currencyName, String worldName) {
        if (target.isEmpty()) return "";
        if (!isFloat(amountStr)) return "";
        double amount = Double.parseDouble(amountStr);
        if (RACraftConomy.isEnabled()) {
            if (RACraftConomy.creditAccount(target, source, amount, currencyName, worldName))
                return RACraftConomy.format(amount, currencyName, worldName);
        } else if (RAVault.isEconomyConected()) {
            if (RAVault.creditAccount(target, source, amount, worldName))
                return RAVault.format(amount, worldName);
        }
        return "";
    }

    public static String debitAccount(String accountFrom, String accountTo, String amountStr, String currencyName, String worldName) {
        if (accountFrom.isEmpty()) return "";
        if (!isFloat(amountStr)) return "";
        double amount = Double.parseDouble(amountStr);
        if (RACraftConomy.isEnabled()) {
            if (RACraftConomy.debitAccount(accountFrom, accountTo, amount, currencyName, worldName))
                return RACraftConomy.format(amount, currencyName, worldName);
        } else if (RAVault.isEconomyConected()) {
            if (RAVault.debitAccount(accountFrom, accountTo, amount, worldName))
                return RAVault.format(amount, worldName);
        }
        return "";
    }


    public static boolean isFloat(String numStr) {
        return numStr.matches("[0-9]+\\.?[0-9]*");
    }

    public static Map<String, String> getBalances(Player p) {
        if (RACraftConomy.isEnabled()) return RACraftConomy.getAllBalances(p.getName());
        else if (RAVault.isEconomyConected()) return RAVault.getAllBalances(p.getName());
        return new HashMap<String, String>();
    }

    public static String format(double amount, String currencyName, String worldName) {
        if (RACraftConomy.isEnabled()) return RACraftConomy.format(amount, currencyName, worldName);
        if (RAVault.isEconomyConected())
            return RAVault.format(amount, worldName.isEmpty() ? Bukkit.getWorlds().get(0).getName() : worldName);
        return Double.toString(amount);
    }

}
