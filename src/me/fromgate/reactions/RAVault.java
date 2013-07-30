package me.fromgate.reactions;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class RAVault {

    ReActions plg;



    private boolean vault_perm = false;
    private boolean vault_eco = false;

    private Permission permission = null;
    private Economy economy = null;
    
    
    public String formatMoney(String value){
        if (!isEconomyConected()) return value;
        return economy.format(Double.parseDouble(value)); // Integer???
    }
    

    public RAVault(ReActions reActions) {
        plg = reActions;
        if (checkVault()){
            vault_perm = setupPermissions();
            vault_eco = setupEconomy();
        }
    }

    public boolean isEconomyConected(){
        return vault_eco;
    }
    
    public boolean isPermissionConected(){
        return vault_perm;
    }


    private boolean setupPermissions(){
        RegisteredServiceProvider<Permission> permissionProvider = plg.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    private boolean setupEconomy(){
        RegisteredServiceProvider<Economy> economyProvider = plg.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }

    
    public double getBalance(String account){
        if (!isEconomyConected()) return 0;
        return economy.getBalance(account);
    }
    
    public void withdrawPlayer(String account, double amount){
        if (!isEconomyConected()) return;
        economy.withdrawPlayer(account, amount);
    }
    
    public void depositPlayer(String account, double amount){
        if (!isEconomyConected()) return;
        economy.depositPlayer(account, amount);
    }
    
    public boolean playerAddGroup (Player p, String group){
        if (!isPermissionConected()) return false;
        return permission.playerAddGroup(p, group);
    }


    public boolean playerInGroup(Player p, String group) {
        if (!isPermissionConected()) return false;
        return permission.playerInGroup(p, group);
    }

    public boolean playerRemoveGroup(Player p, String group) {
        if (!isPermissionConected()) return false;
        return permission.playerRemoveGroup(p, group);
    }
    
    
    private boolean checkVault(){
        Plugin vplg = plg.getServer().getPluginManager().getPlugin("Vault");
        return  ((vplg != null)&&(vplg instanceof Vault));
    }

}
