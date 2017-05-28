package me.fromgate.reactions.externals;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RAPlaceholderAPI {

    private static boolean enabled;

    public static boolean isEnabled() {
        return enabled;
    }

    public static void init() {
        enabled = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    public static String processPlaceholder(Player player, String text) {
        if (!enabled) return text;
        return PlaceholderAPI.setPlaceholders(player, text);
    }
}
