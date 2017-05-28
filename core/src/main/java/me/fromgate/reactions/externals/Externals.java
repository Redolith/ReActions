package me.fromgate.reactions.externals;

import org.bukkit.Bukkit;

public class Externals {


    private static boolean factions = false;

    //разные переменные
    private static boolean townyConected = false;

    public static void init() {
        if (Bukkit.getPluginManager().getPlugin("Factions") != null) {
            try {
                factions = RAFactions.init();
            } catch (Throwable ignore) {

            }
        }

        if (Bukkit.getPluginManager().getPlugin("Towny") != null) {
            try {
                townyConected = RATowny.init();
            } catch (Throwable ignore) {

            }
        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            try {
                RAPlaceholderAPI.init();
            } catch (Throwable ignore) {
            }
        }


        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
            try {
                RAProtocolLib.connectProtocolLib();
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
