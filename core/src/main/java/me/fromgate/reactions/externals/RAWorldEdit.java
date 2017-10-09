package me.fromgate.reactions.externals;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.fromgate.reactions.util.message.M;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Created by MaxDikiy on 9/10/2017.
 */
public class RAWorldEdit {
    private static Plugin wePlugin = null;
    private static boolean connected = false;
    private static WorldEditPlugin worldedit = null;

    private static void connectToWorldEdit() {
        Plugin twn = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        if (twn == null) return;
        wePlugin = twn;
        connected = true;
    }

    public static boolean isConnected() {
        return connected;
    }

    public static boolean init() {
        connectToWorldEdit();
        if (!isConnected()) {
            try {
                worldedit = WorldGuardPlugin.inst().getWorldEdit();
                connected = true;
                return true;
            } catch (CommandException e) {
                M.logMessage("Worledit not found...");
            }
            return false;
        }
        if (wePlugin instanceof WorldEditPlugin) {
            worldedit = (WorldEditPlugin) wePlugin;
        } else connected = false;
        if (connected) {
            M.logMessage("WorldEdit " + wePlugin.getDescription().getVersion() + " found and loaded.");
        } else M.logMessage("Worledit not found...");
        return connected;
    }

    public LocalConfiguration getLocalConfiguration() {
        return worldedit.getLocalConfiguration();
    }

    public LocalSession getLocalSession(Player player) {
        return worldedit.getSession(player);
    }

    private static Selection getSelection(Player player) {
        return worldedit.getSelection(player);
    }

    public static int getArea(Player player) {
        Selection selection = getSelection(player);
        if (selection == null) return 0;
        return selection.getArea();
    }

    public BukkitPlayer getBukkitPlayer(Player player) {
        return worldedit.wrapPlayer(player);
    }

}