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

import com.greatmancode.craftconomy3.Cause;
import com.greatmancode.craftconomy3.Common;
import com.greatmancode.craftconomy3.account.Account;
import com.greatmancode.craftconomy3.account.Balance;
import com.greatmancode.craftconomy3.currency.Currency;
import com.greatmancode.craftconomy3.tools.interfaces.Loader;
import me.fromgate.reactions.ReActions;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class RACraftConomy {
    private static boolean enabled = false;
    private static Common craftconomy = null;

    public static void init() {
        enabled = isCraftconomyInstalled();
        if (enabled) ReActions.util.log("CraftConomy connected");
    }

    public static boolean isEnabled() {
        return enabled;
    }

    private static boolean isCraftconomyInstalled() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Craftconomy3");
        try {
            if (plugin != null) {
                craftconomy = (Common) ((Loader) plugin).getCommon();
                return (craftconomy != null);
            }
        } catch (Throwable e) {
        }
        return false;
    }

	/*
     * Get Balance
	 * 
	 */

	/*public static double getBalance(String accountName){
        if (!enabled) return 0;
		Account account = craftconomy.getAccountManager().getAccount(accountName);
		for (Balance balanсe : account.getAllBalance()){
			if (balanсe.getCurrency().equals(craftconomy.getCurrencyManager().getDefaultCurrency())) balanсe.getBalance(); 
		}
		return 0;
	}

	public static double getBalance(String accountName, World world){
		if (!enabled) return 0;
		Account account = craftconomy.getAccountManager().getAccount(accountName);
		for (Balance balanсe : account.getAllWorldBalance(world.getName())){
			if (balanсe.getCurrency().equals(craftconomy.getCurrencyManager().getDefaultCurrency())) balanсe.getBalance(); 
		}
		return 0;
	}

	public static double getBalance(String accountName, String currencyName){
		return getBalance (accountName, currencyName,getWorldName("default"));
	}*/

    public static double getBalance(String accountName, String currencyName, String worldName) {
        if (!enabled) return 0;
        Currency currency = craftconomy.getCurrencyManager().getCurrency(currencyName);
        if (currency == null) craftconomy.getCurrencyManager().getDefaultCurrency();
        Account account = craftconomy.getAccountManager().getAccount(accountName);
        String world = getWorldName(worldName);
        return account.getBalance(world, currency.getName());
    }

    /*
     * Check amount
     */
    public static boolean hasAmount(String accountName, double amount, String currencyName, String worldName) {
        if (!enabled) return false;
        Account account = craftconomy.getAccountManager().getAccount(accountName);
        if (account == null) return false;
        if (account.hasInfiniteMoney()) return true;
        Currency currency = craftconomy.getCurrencyManager().getCurrency(currencyName);
        if (currency == null) currency = craftconomy.getCurrencyManager().getDefaultCurrency();
        return craftconomy.getAccountManager().getAccount(accountName).hasEnough(amount, getWorldName(worldName), currency.getName());
    }

	/*
    public static boolean hasAmount (String accountName, double amount, World world){
		if (!enabled) return false;
		return craftconomy.getAccountManager().getAccount(accountName).hasEnough(amount, world.getName(), craftconomy.getCurrencyManager().getDefaultCurrency().getName());
	}

	public static boolean hasAmount (String accountName, double amount, String currencyName){
		return hasAmount (accountName, amount, currencyName,"default");
	}

	public static boolean hasAmount (String accountName, double amount){
		return hasAmount (accountName, amount, "","default");
	}*/

    /*
     * creaditAccount (add money to accountTo. If accountFrom is defined - withdraw money from this account
     */
    public static boolean creditAccount(String accountTo, String accountFrom, double amount, String currencyName, String worldName) {
        if (!enabled) return false;
        if (accountTo.isEmpty()) return false;
        Account account = craftconomy.getAccountManager().getAccount(accountTo);
        if (account == null) return false;
        Currency currency = craftconomy.getCurrencyManager().getCurrency(currencyName);
        if (currency == null) currency = craftconomy.getCurrencyManager().getDefaultCurrency();
        String world = getWorldName(worldName);
        if (!accountFrom.isEmpty() && !withdrawAccount(accountFrom, amount, currency.getName(), world)) return false;
        account.deposit(amount, world, currency.getName(), Cause.PLUGIN, null);
        return true;
    }

	/*
	public static boolean creditAccount (String accountTo, String accountFrom, double amount, String currencyName){
		return creditAccount (accountTo,accountFrom,amount,currencyName, getWorldName("default"));
	}

	public static boolean creditAccount (String accountTo, String accountFrom, double amount){
		return creditAccount (accountTo, accountFrom, amount, getDefaultCurrency());
	}
	
	public static boolean creditAccount (String accountTo, double amount){
		return creditAccount (accountTo, "", amount, getDefaultCurrency());
	}*/

	/* 
	 * Debit (withdraw account)
	 */

    public static boolean debitAccount(String accountFrom, String accountTo, double amount, String currencyName, String worldName) {
        if (accountFrom.isEmpty()) return false;
        if (!withdrawAccount(accountFrom, amount, currencyName, getWorldName(worldName))) return false;
        creditAccount(accountTo, "", amount, currencyName, getWorldName(worldName));
        return true;
    }

    public static boolean debitAccount(String accountFrom, String accountTo, double amount, String currencyName) {
        return debitAccount(accountFrom, accountTo, amount, currencyName, getWorldName("default"));
    }

    public static boolean debitAccount(String accountFrom, String accountTo, double amount) {
        return debitAccount(accountFrom, accountTo, amount, getDefaultCurrency(), getWorldName("default"));
    }


	/*
	 * Tools
	 */
	
	/*
	 * 	public static boolean hasAmount (String accountName, double amount, String currencyName, String worldName){
		if (!enabled) return false;
		Account account = craftconomy.getAccountManager().getAccount(accountName);
		if (account == null) return false;
		if (account.hasInfiniteMoney()) return true;
		Currency currency  = craftconomy.getCurrencyManager().getCurrency(currencyName);
		if (currency==null) currency = craftconomy.getCurrencyManager().getDefaultCurrency();
		return craftconomy.getAccountManager().getAccount(accountName).hasEnough(amount, getWorldName(worldName), currency.getName());
	}

	 */


    public static boolean withdrawAccount(String accountStr, double amount, String currencyName, String worldName) {
        if (!enabled) return false;
        if (accountStr.isEmpty()) return false;
        Account account = craftconomy.getAccountManager().getAccount(accountStr);
        if (account == null) return false;
        if (account.hasInfiniteMoney()) return true;
        Currency currency = craftconomy.getCurrencyManager().getCurrency(currencyName);
        if (currency == null) currency = craftconomy.getCurrencyManager().getDefaultCurrency();
        String world = getWorldName(worldName);
        if (!hasAmount(accountStr, amount, currency.getName(), world)) return false;
        account.withdraw(amount, world, currency.getName(), Cause.PLUGIN, null);
        return true;
    }

    public static String getWorldName(String worldName) {
        return craftconomy.getWorldGroupManager().getWorldGroupName(worldName);
    }

    public static String getDefaultCurrency() {
        return craftconomy.getCurrencyManager().getDefaultCurrency().getName();
    }

    public static Map<String, String> getAllBalances(String accountStr) {
        String worldDef = Bukkit.getWorlds().get(0).getName();
        Map<String, String> balances = new HashMap<String, String>();
        if (accountStr.isEmpty()) return balances;
        Account account = craftconomy.getAccountManager().getAccount(accountStr);
        if (account == null) return balances;
        for (Balance balance : account.getAllBalance()) {
            String key = "money." + (balance.getWorld().equalsIgnoreCase(worldDef) ? "" : balance.getWorld() + ".") + balance.getCurrency().getName();
            String amount = craftconomy.format(balance.getWorld(), balance.getCurrency(), balance.getBalance());
            balances.put(key, amount);
            if (balance.getWorld().equals(worldDef) && balance.getCurrency().equals(craftconomy.getCurrencyManager().getDefaultCurrency()))
                balances.put("money", amount);
        }
        return balances;
    }

    public static String format(double amount, String currencyName, String worldName) {
        Currency currency = currencyName.isEmpty() ? craftconomy.getCurrencyManager().getDefaultCurrency() : craftconomy.getCurrencyManager().getCurrency(currencyName);
        if (currency == null) return Double.toString(amount);
        return craftconomy.format(getWorldName(worldName), currency, amount);
    }
}

