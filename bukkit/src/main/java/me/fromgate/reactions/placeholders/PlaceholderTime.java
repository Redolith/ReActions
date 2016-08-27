package me.fromgate.reactions.placeholders;

import me.fromgate.reactions.timer.Time;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@PlaceholderDefine(id = "Time", needPlayer = false, keys = {"TIME_INGAME", "curtime", "TIME_SERVER", "servertime"})
public class PlaceholderTime extends Placeholder {
    @Override
    public String processPlaceholder(Player player, String key, String param) {
        if (equalsIgnoreCase(key, "TIME_INGAME", "curtime"))
            return Time.ingameTimeToString((player == null ? Bukkit.getWorlds().get(0).getTime() : player.getWorld().getTime()), false);
        else if (equalsIgnoreCase(key, "TIME_SERVER", "servertime"))
            return Time.fullTimeToString(System.currentTimeMillis(), param.isEmpty() ? "dd-MM-YYYY HH:mm:ss" : param);
        return null;
    }
}
