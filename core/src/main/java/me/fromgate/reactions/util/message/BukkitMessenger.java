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

package me.fromgate.reactions.util.message;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BukkitMessenger implements Messenger {

    JavaPlugin plugin;


    public BukkitMessenger(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    @Override
    public boolean broadcast(String text) {
        Bukkit.getServer().broadcastMessage(text);
        return true;
    }

    @Override
    public boolean log(String text) {
        plugin.getLogger().info(text);
        return true;
    }

    @Override
    public String clean(String text) {
        return ChatColor.stripColor(text);
    }

    @Override
    public boolean tip(int seconds, Object sender, String text) {
        return tip(sender, text);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean tip(Object sender, String text) {
        Player player = toPlayer(sender);
        if (player == null) {
            return false;
        }
        player.sendTitle(null, text);
        return true;
    }

    @Override
    public boolean print(Object obj, String text) {
        CommandSender sender = toSender(obj);
        if (sender != null) {
            sender.sendMessage(text);
        } else {
            log("Failed to print message - wrong recipient: " + (obj == null ? "null" : obj.toString()));
        }
        return true;
    }

    @Override
    public boolean broadcast(String permission, String text) {
        List<Player> playerList = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (permission == null || permission.isEmpty() || player.hasPermission(permission)) {
                player.sendMessage(text);
            }
        }
        return true;
    }

    @Override
    public String toString(Object obj, boolean fullFloat) {
        if (obj == null) return "'null'";
        String s = obj.toString();
        DecimalFormat fmt = new DecimalFormat("####0.##");
        if (obj instanceof Location) {
            Location loc = (Location) obj;
            if (fullFloat)
                s = loc.getWorld() + "[" + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + "]";
            else
                s = loc.getWorld() + "[" + fmt.format(loc.getX()) + ", " + fmt.format(loc.getY()) + ", " + fmt.format(loc.getZ()) + "]";
        }
        return s;
    }

    @SuppressWarnings("deprecation")
    @Override
    public Map<String, String> load(String language) {
        Map<String, String> msg = new HashMap<>();
        YamlConfiguration lng = new YamlConfiguration();
        File f = new File(plugin.getDataFolder() + File.separator + language + ".lng");
        try {
            if (f.exists()) lng.load(f);
            else {
                InputStream is = plugin.getClass().getResourceAsStream("/lang/" + language + ".lng");
                if (is != null) lng.load(is);
            }
        } catch (Exception e) {
            M.LNG_LOAD_FAIL.log();
            return msg;
        }

        for (String key : lng.getKeys(true)) {
            if (lng.isConfigurationSection(key)) continue;
            msg.put(key, lng.getString(key));
        }
        return msg;
    }

    @Override
    public void save(String language, Map<String, String> messages) {
        YamlConfiguration lng = new YamlConfiguration();
        File f = new File(plugin.getDataFolder() + File.separator + language + ".lng");
        try {
            if (f.exists()) lng.load(f);
        } catch (Exception ignore) {
        }
        for (Map.Entry<String, String> message : messages.entrySet()) {
            lng.set(message.getKey().toLowerCase(), message.getValue());
        }

        try {
            lng.save(f);
        } catch (Exception e) {
            M.LNG_SAVE_FAIL.log();
        }
    }

    @Override
    public boolean isValidSender(Object send) {
        return (toSender(send) != null);
    }

    public CommandSender toSender(Object sender) {
        return sender instanceof CommandSender ? (CommandSender) sender : null;
    }

    public Player toPlayer(Object sender) {
        return sender instanceof Player ? (Player) sender : null;
    }
}
