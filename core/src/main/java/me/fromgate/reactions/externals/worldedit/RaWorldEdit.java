package me.fromgate.reactions.externals.worldedit;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Polygonal2DSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.fromgate.reactions.externals.RaWorldGuard;
import me.fromgate.reactions.util.message.M;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


/**
 * Created by MaxDikiy on 9/10/2017.
 */

public class RaWorldEdit {
    private static boolean connected = false;
    private static WorldEditPlugin worldedit = null;

    public static boolean isConnected() {
        return connected;
    }

    public static void init() {
        Plugin plugin = null;
        try {
            plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
            worldedit = (WorldEditPlugin) plugin;
            worldedit.getWorldEdit().getEventBus().register(new WeListener());
            connected = true;
        } catch (Throwable e) {
            M.logMessage("Worledit not found...");
            connected = false;
        }
    }

    public LocalConfiguration getLocalConfiguration() {
        return worldedit.getLocalConfiguration();
    }

    public static LocalSession getSession(Player player) {
        return worldedit.getSession(player);
    }

    public static org.bukkit.util.Vector getMinimumPoint(Player player) {
        if (isConnected()) return null;
        Region r = null;
        try {
            r = getRegion(player);
        } catch (Exception ignored) {
        }
        if (r == null) return null;
        Vector v = r.getMinimumPoint();
        return new org.bukkit.util.Vector(v.getX(), v.getY(), v.getZ());
    }

    public static org.bukkit.util.Vector getMaximumPoint(Player player) {
        if (isConnected()) return null;
        Region r = null;
        try {
            r = getRegion(player);
        } catch (Exception ignored) {
        }
        if (r == null) return null;
        Vector v = r.getMaximumPoint();
        return new org.bukkit.util.Vector(v.getX(), v.getY(), v.getZ());
    }

    public static Region getRegion(Player player) throws IncompleteRegionException {
        RegionSelector rs = getRegionSelector(player);
        if (rs == null) return null;
        return rs.getRegion();
    }

    public static RegionSelector getRegionSelector(Player player) {
        Selection sel = worldedit.getSelection(player);
        if (sel == null) return null;
        return sel.getRegionSelector();
    }

    public static Selection getSelection(Player player) {
        return worldedit.getSelection(player);
    }

    public static boolean hasSuperPickAxe(Player player) {
        return isConnected() && getSession(player).hasSuperPickAxe();
    }

    public static boolean isToolControl(Player player) {
        return isConnected() && getSession(player).isToolControlEnabled();
    }

    public static int getArea(Player player) {
        Selection selection = getSelection(player);
        if (selection == null) return 0;
        return selection.getArea();
    }

    public static BukkitPlayer getBukkitPlayer(Player player) {
        return worldedit.wrapPlayer(player);
    }

    public static ProtectedRegion checkRegionFromSelection(Player player, String id) throws CommandException {
        Selection selection = getSelection(player);
        // Detect the type of region from WorldEdit
        if (selection instanceof Polygonal2DSelection) {
            Polygonal2DSelection polySel = (Polygonal2DSelection) selection;
            int minY = polySel.getNativeMinimumPoint().getBlockY();
            int maxY = polySel.getNativeMaximumPoint().getBlockY();
            return new ProtectedPolygonalRegion(id, polySel.getNativePoints(), minY, maxY);
        } else if (selection instanceof CuboidSelection) {
            BlockVector min = selection.getNativeMinimumPoint().toBlockVector();
            BlockVector max = selection.getNativeMaximumPoint().toBlockVector();
            return new ProtectedCuboidRegion(id, min, max);
        } else {
            //	Bukkit.broadcastMessage("§c"+"Sorry, you can only use cuboids and polygons for WorldGuard regions.");
            return null;
        }
    }

    public static boolean checkRegionInRadius(Player p, int radius) {
        if (!isConnected()) return false;
        World world = p.getWorld();
        LocalPlayer player = RaWorldGuard.getWrapPlayer(p);
        String id = "__canbuild__";
        Vector loc = player.getPosition();
        BlockVector min = new Vector(loc.getBlockX() + radius, 0, loc.getBlockZ() + radius).toBlockVector();
        BlockVector max = new Vector(loc.getBlockX() - radius, world.getMaxHeight(), loc.getBlockZ() - radius).toBlockVector();
        ProtectedRegion region = new ProtectedCuboidRegion(id, min, max);

        ApplicableRegionSet set = RaWorldGuard.getRegionManager(world).getApplicableRegions(region);
        if (RaWorldGuard.getRegionManager(world).overlapsUnownedRegion(region, player)) {
            for (ProtectedRegion each : set) {
                if (each != null && !each.getOwners().contains(player) && !each.getMembers().contains(player))
                    return true;
            }
        }
        return false;
    }

    public static int canBuildSelection(Player p) throws CommandException {
        boolean canBuild = false;
        World world = p.getWorld();
        LocalPlayer player = RaWorldGuard.getWrapPlayer(p);
        String id = "__canbuild__";
        ProtectedRegion region = checkRegionFromSelection(p, id);
        if (region == null) return 3;
        ApplicableRegionSet set = RaWorldGuard.getRegionManager(world).getApplicableRegions(region);
        if (RaWorldGuard.getRegionManager(world).overlapsUnownedRegion(region, player)) {
            for (ProtectedRegion each : set) {
                if (each != null) {
                    if (!each.getOwners().contains(player) && !each.getMembers().contains(player)) {
                        return 1;
                    }
                    if (each.getOwners().contains(player) || each.getMembers().contains(player)) {
                        canBuild = true;
                    }
                }
            }
            if (!canBuild) return 1;
        } else {
            for (ProtectedRegion each : set) {
                if (each != null) {
                    if (each.getOwners().contains(player) || each.getMembers().contains(player)) {
                        BlockVector rgBlockMin = region.getMinimumPoint();
                        BlockVector rgBlockMax = region.getMaximumPoint();
                        if (each.contains(rgBlockMin.getBlockX(), rgBlockMin.getBlockY(), rgBlockMin.getBlockZ())
                                && each.contains(rgBlockMax.getBlockX(), rgBlockMax.getBlockY(), rgBlockMax.getBlockZ())) {
                            canBuild = true;
                        }
                    }
                }
            }
            if (!canBuild) return 2;
        }
        return 0;
    }
}