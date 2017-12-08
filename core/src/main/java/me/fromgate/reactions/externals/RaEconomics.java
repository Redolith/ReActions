/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2017, fromgate, fromgate@gmail.com
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
import java.util.regex.Pattern;


public class RaEconomics {
    private final static Pattern FLOAT = Pattern.compile("[0-9]+(\\.?[0-9]*)?");

    public static boolean isEconomyFound() {
        if (RaCraftConomy.isEnabled()) return true;
        return RaVault.isEconomyConected();
    }

    public static boolean hasMoney(String account, double amount, String currencyName, String worldName) {
        if (RaCraftConomy.isEnabled()) return RaCraftConomy.hasAmount(account, amount, currencyName, worldName);
        if (RaVault.isEconomyConected()) return RaVault.hasMoney(account, worldName, amount);
        return false;
    }


    public static String creditAccount(String target, String source, String amountStr, String currencyName, String worldName) {
        if (target.isEmpty()) return "";
        if (!isFloat(amountStr)) return "";
        double amount = Double.parseDouble(amountStr);
        if (RaCraftConomy.isEnabled()) {
            if (RaCraftConomy.creditAccount(target, source, amount, currencyName, worldName))
                return RaCraftConomy.format(amount, currencyName, worldName);
        } else if (RaVault.isEconomyConected()) {
            if (RaVault.creditAccount(target, source, amount, worldName))
                return RaVault.format(amount, worldName);
        }
        return "";
    }

    public static String debitAccount(String accountFrom, String accountTo, String amountStr, String currencyName, String worldName) {
        if (accountFrom.isEmpty()) return "";
        if (!isFloat(amountStr)) return "";
        double amount = Double.parseDouble(amountStr);
        if (RaCraftConomy.isEnabled()) {
            if (RaCraftConomy.debitAccount(accountFrom, accountTo, amount, currencyName, worldName))
                return RaCraftConomy.format(amount, currencyName, worldName);
        } else if (RaVault.isEconomyConected()) {
            if (RaVault.debitAccount(accountFrom, accountTo, amount, worldName))
                return RaVault.format(amount, worldName);
        }
        return "";
    }

    public static boolean isFloat(String numStr) {
        return FLOAT.matcher(numStr).matches();
    }

    public static Map<String, String> getBalances(Player p) {
        if (RaCraftConomy.isEnabled()) return RaCraftConomy.getAllBalances(p.getName());
        else if (RaVault.isEconomyConected()) return RaVault.getAllBalances(p.getName());
        return new HashMap<>();
    }

    public static String format(double amount, String currencyName, String worldName) {
        if (RaCraftConomy.isEnabled()) return RaCraftConomy.format(amount, currencyName, worldName);
        if (RaVault.isEconomyConected())
            return RaVault.format(amount, worldName.isEmpty() ? Bukkit.getWorlds().get(0).getName() : worldName);
        return Double.toString(amount);
    }

}
