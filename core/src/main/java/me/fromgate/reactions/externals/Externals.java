package me.fromgate.reactions.externals;

import me.fromgate.reactions.externals.worldedit.RaWorldEdit;
import org.bukkit.Bukkit;

public class Externals {


    private static boolean factions = false;

    //разные переменные
    private static boolean townyConected = false;

    public static void init() {
        if (Bukkit.getPluginManager().getPlugin("Factions") != null) {
            try {
                factions = RaFactions.init();
            } catch (Throwable ignore) {

            }
        }

        if (Bukkit.getPluginManager().getPlugin("Towny") != null) {
            try {
                townyConected = RaTowny.init();
            } catch (Throwable ignore) {

            }
        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            try {
                RaPlaceholderAPI.init();
            } catch (Throwable ignore) {
            }
        }


        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
            try {
                RaProtocolLib.connectProtocolLib();
            } catch (Throwable ignore) {
            }
        }

        if (Bukkit.getPluginManager().getPlugin("WorldEdit") != null) {
            try {
                RaWorldEdit.init();
            } catch (Throwable ignore) {
            }
        }

        if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
            try {
                RaWorldGuard.init();
            } catch (Throwable ignore) {
            }
        }
    }

    public static boolean isConnectedFactions() {
        return factions;
    }

    public static boolean isTownyConnected() {
        return townyConected;
    }

}
