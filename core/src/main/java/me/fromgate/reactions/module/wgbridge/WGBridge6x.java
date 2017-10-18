package me.fromgate.reactions.module.wgbridge;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionQuery;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.Association;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WGBridge6x extends WGBridge {

    private static WorldGuardPlugin worldguard = null;
    private static RegionQuery query = null;

    @Override
    public void init() {
        if (!isConnected()) return;
        setVersion("WGBridge 0.0.2/WG6x");
        if (this.wgPlugin instanceof WorldGuardPlugin) {
            worldguard = (WorldGuardPlugin) wgPlugin;
            query = worldguard.getRegionContainer().createQuery();
        } else this.connected = false;
    }

    @Override
    public List<String> getRegions(Location loc) {
        List<String> rgs = new ArrayList<>();
        if (loc == null) return rgs;
        if (!connected) return rgs;
        ApplicableRegionSet rset = query.getApplicableRegions(loc);
        if ((rset == null) || (rset.size() == 0)) return rgs;
        for (ProtectedRegion rg : rset) rgs.add((loc.getWorld().getName() + "." + rg.getId()).toLowerCase());
        return rgs;
    }

    @Override
    public List<String> getRegions(Player p) {
        return getRegions(p.getLocation());
    }

    @Override
    public int countPlayersInRegion(String rg) {
        if (!connected) return 0;
        int count = 0;
        for (Player p : Bukkit.getOnlinePlayers())
            if (isPlayerInRegion(p, rg)) count++;
        return count;
    }

    @Override
    public List<Player> playersInRegion(String rg) {
        List<Player> plrs = new ArrayList<>();
        if (!connected) return plrs;
        for (Player p : Bukkit.getOnlinePlayers())
            if (isPlayerInRegion(p, rg)) plrs.add(p);
        return plrs;
    }

    @Override
    public boolean isPlayerInRegion(Player p, String rg) {
        if (!connected) return false;
        List<String> rgs = getRegions(p);
        if (rgs.isEmpty() && !rg.contains("__global__")) return false;
        World world = getRegionWorld(rg);
        String regionName = getRegionName(rg);
        return rgs.isEmpty() && rg.contains("__global__") && p.getWorld() == world || rgs.contains((world.getName() + "." + regionName).toLowerCase());
    }

    @Override
    public boolean isRegionExists(String rg) {
        if (!connected) return false;
        if (rg.isEmpty()) return false;
        World world = getRegionWorld(rg);
        String regionName = getRegionName(rg);
        return (worldguard.getRegionManager(world).getRegions().containsKey(regionName));
    }

    @Override
    public List<Location> getRegionMinMaxLocations(String rg) {
        List<Location> locs = new ArrayList<>();
        if (!connected) return locs;
        World world = getRegionWorld(rg);
        String regionName = getRegionName(rg);
        ProtectedRegion prg = worldguard.getRegionManager(world).getRegion(regionName);
        if (prg == null) return locs;
        locs.add(new Location(world, prg.getMinimumPoint().getX(), prg.getMinimumPoint().getY(), prg.getMinimumPoint().getZ()));
        locs.add(new Location(world, prg.getMaximumPoint().getX(), prg.getMaximumPoint().getY(), prg.getMaximumPoint().getZ()));
        return locs;
    }

    @Override
    public List<Location> getRegionLocations(String rg, boolean land) {
        List<Location> locs = new ArrayList<>();
        if (!connected) return locs;
        World world = getRegionWorld(rg);
        String regionName = getRegionName(rg);
        ProtectedRegion prg = worldguard.getRegionManager(world).getRegion(regionName);

        if (prg != null) {
            for (int x = prg.getMinimumPoint().getBlockX(); x <= prg.getMaximumPoint().getBlockX(); x++)
                for (int y = prg.getMinimumPoint().getBlockY(); y <= prg.getMaximumPoint().getBlockY(); y++)
                    for (int z = prg.getMinimumPoint().getBlockZ(); z <= prg.getMaximumPoint().getBlockZ(); z++) {
                        Location t = new Location(world, x, y, z);
                        if (t.getBlock().isEmpty() && t.getBlock().getRelative(BlockFace.UP).isEmpty()) {
                            if (land && t.getBlock().getRelative(BlockFace.DOWN).isEmpty()) continue;
                            t.add(0.5, 0, 0.5);
                            locs.add(t);
                        }
                    }
        }
        return locs;
    }

    @Override
    public boolean isPlayerIsMemberOrOwner(Player p, String region) {
        if (!connected) return false;
        LocalPlayer localPlayer = p != null ? worldguard.wrapPlayer(p) : null;
        if (localPlayer == null) return false;
        if (region.isEmpty()) return false;
        World world = getRegionWorld(region);
        String regionName = getRegionName(region);
        ProtectedRegion rg = worldguard.getRegionManager(world).getRegion(regionName);
        if (rg == null) return false;
        return localPlayer.getAssociation(Arrays.asList(rg)) != Association.NON_MEMBER;
    }


    @Override
    public boolean isPlayerIsOwner(Player p, String region) {
        if (!connected) return false;
        LocalPlayer localPlayer = p != null ? worldguard.wrapPlayer(p) : null;
        if (localPlayer == null) return false;
        if (region.isEmpty()) return false;
        World world = getRegionWorld(region);
        String regionName = getRegionName(region);
        ProtectedRegion rg = worldguard.getRegionManager(world).getRegion(regionName);
        if (rg == null) return false;
        return localPlayer.getAssociation(Arrays.asList(rg)) == Association.OWNER;
    }

    @Override
    public boolean isPlayerIsMember(Player p, String region) {
        if (!connected) return false;
        LocalPlayer localPlayer = p != null ? worldguard.wrapPlayer(p) : null;
        if (localPlayer == null) return false;
        if (region.isEmpty()) return false;
        World world = getRegionWorld(region);
        String regionName = getRegionName(region);
        ProtectedRegion rg = worldguard.getRegionManager(world).getRegion(regionName);
        if (rg == null) return false;
        return localPlayer.getAssociation(Arrays.asList(rg)) == Association.MEMBER;
    }

    @Override
    public boolean isLocationInRegion(Location loc, String region) {
        if (loc == null) return false;
        if (!connected) return false;
        World world = getRegionWorld(region);
        if (!loc.getWorld().equals(world)) return false;
        String regionName = getRegionName(region);
        ProtectedRegion rg = worldguard.getRegionManager(world).getRegion(regionName);
        if (rg == null) return false;
        return (rg.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFlagInRegion(Player p, String region) {
        if (!connected) return false;
        String[] parts;
        String[] group_parts = region.split("\\/");
        if (group_parts.length > 1) parts = group_parts[0].split("\\.");
        else parts = region.split("\\.");
        if (parts.length < 3 || parts.length > 4) return false;
        World world;
        String regionName;
        String flagName;
        String valueName;
        if (parts.length == 3) {
            world = Bukkit.getWorlds().get(0);
            regionName = parts[0];
            flagName = parts[1];
            valueName = parts[2];
        } else {
            world = Bukkit.getWorld(parts[0]);
            regionName = parts[1];
            flagName = parts[2];
            valueName = parts[3];
        }
        if (world == null) return false;
        if (flagName == null) return false;
        ProtectedRegion rg = worldguard.getRegionManager(world).getRegion(regionName);
        if (rg == null) return false;
        ApplicableRegionSet set = worldguard.getRegionManager(world).getApplicableRegions(rg);
        Flag<?> f = DefaultFlag.fuzzyMatchFlag(flagName);
        if (f == null) return false;
        LocalPlayer localPlayer = p != null ? worldguard.wrapPlayer(p) : null;
        if (set.getFlag(f, localPlayer) == null) return false;
        Boolean result = false;
        String flagStr = set.getFlag(f, localPlayer).toString();
        if (flagStr.equalsIgnoreCase(valueName)) result = true;

        if (result && group_parts.length > 1) {
            RegionGroup group;
            group = set.getFlag(f.getRegionGroupFlag());
            if ((group.toString()).replace("_", "").equalsIgnoreCase(group_parts[1].replace("_", ""))) return true;
        } else if (result) return true;
        return false;
    }

    public LocalPlayer getWrapPlayer(Player player) {
        return worldguard.wrapPlayer(player);
    }

    public RegionManager getRegionManager(World world) {
        return worldguard.getRegionContainer().get(world);
    }
}
