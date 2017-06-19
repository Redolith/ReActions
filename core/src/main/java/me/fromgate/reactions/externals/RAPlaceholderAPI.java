package me.fromgate.reactions.externals;

import me.clip.placeholderapi.PlaceholderAPI;
import me.fromgate.reactions.util.message.M;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RAPlaceholderAPI {

    private static boolean enabled;

    public static boolean isEnabled() {
        return enabled;
    }

    public static void init() {
        enabled = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
        if (enabled) M.logMessage("Connected to PlaceholderAPI");
    }

    public static String processPlaceholder(Player player, String text) {
        if (!enabled) return text;
        return PlaceholderAPI.setPlaceholders(player, text);
    }
}
