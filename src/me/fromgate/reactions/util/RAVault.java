package me.fromgate.reactions.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class RAVault {
    private static boolean vault_perm = false;
    private static boolean vault_eco = false;
    private static Permission permission = null;
    private static Economy economy = null;
    
    public static String formatMoney(String value){
        if (!isEconomyConected()) return value;
        return economy.format(Double.parseDouble(value)); // Integer???
    }

    public static void init() {
        if (checkVault()){
            vault_perm = setupPermissions();
            vault_eco = setupEconomy();
        }
    }
    
    public static boolean isEconomyConected(){
        return vault_eco;
    }
    
    public static boolean isPermissionConected(){
        return vault_perm;
    }


    private static boolean setupPermissions(){
        RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    private static boolean setupEconomy(){
        RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }

    
    public static double getBalance(String account){
        if (!isEconomyConected()) return 0;
        return economy.getBalance(account);
    }
    
    public static void withdrawPlayer(String account, double amount){
        if (!isEconomyConected()) return;
        economy.withdrawPlayer(account, amount);
    }
    
    public static void depositPlayer(String account, double amount){
        if (!isEconomyConected()) return;
        economy.depositPlayer(account, amount);
    }
    
    public static boolean playerAddGroup (Player p, String group){
        if (!isPermissionConected()) return false;
        return permission.playerAddGroup(p, group);
    }

    public static boolean playerInGroup(Player p, String group) {
        if (!isPermissionConected()) return false;
        return permission.playerInGroup(p, group);
    }

    public static boolean playerRemoveGroup(Player p, String group) {
        if (!isPermissionConected()) return false;
        return permission.playerRemoveGroup(p, group);
    }
    
    private static boolean checkVault(){
        Plugin vplg = Bukkit.getServer().getPluginManager().getPlugin("Vault");
        return  ((vplg != null)&&(vplg instanceof Vault));
    }

}
