package me.fromgate.reactions.externals;

import org.bukkit.Bukkit;

public class Externals {
    private static boolean factions = false;

    public static void init() {
        if (Bukkit.getPluginManager().getPlugin("Factions") != null)
            try {
                factions = RAFactions.init();
            } catch (Throwable ignore) {
            }
    }

    public static boolean isConnectedFactions() {
        return factions;
    }


}
