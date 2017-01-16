package me.fromgate.reactions.externals;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RegionCache {

    private static Set<VRegion> cache = new HashSet<VRegion>();

    public static List<VRegion> getRegion(Location loc) {
        List<VRegion> foundRegions = new ArrayList<VRegion>();
        for (VRegion vr : cache)
            if (vr.isInRegion(loc)) foundRegions.add(vr);
        return foundRegions;

    }


    public static boolean isInCache(String worldName, String regionName) {
        for (VRegion vr : cache)
            if (vr.isRegion(worldName, regionName)) return true;
        return false;
    }

    public boolean putToCache(String worldName, String regionName) {
        String region = worldName + "." + regionName;
        if (!RAWorldGuard.isRegionExists(region)) return false;
        List<Location> minMaxLocs = RAWorldGuard.getRegionMinMaxLocations(region);
        if (minMaxLocs.size() != 2) return false;
        VRegion vr = new VRegion(worldName, regionName, minMaxLocs.get(0), minMaxLocs.get(1));
        cache.add(vr);
        return true;
    }


    public class VRegion {
        String worldName;
        String regionName;
        int minX;
        int minY;
        int minZ;
        int maxX;
        int maxY;
        int maxZ;

        public VRegion(String worldName, String regionName, Location min, Location max) {
            this.worldName = worldName;
            this.regionName = regionName;
            this.minX = min.getBlockX();
            this.minY = min.getBlockY();
            this.minZ = min.getBlockZ();
            this.maxX = max.getBlockX();
            this.maxY = max.getBlockY();
            this.maxZ = max.getBlockZ();
        }


        public boolean isRegion(String worldName, String regionName) {
            if (!worldName.equalsIgnoreCase(this.worldName)) return false;
            return regionName.equalsIgnoreCase(this.regionName);
        }

        public boolean isInRegion(Location loc) {
            if (loc == null) return false;
            if (!loc.getWorld().getName().equalsIgnoreCase(worldName)) return false;
            if (loc.getX() < minX) return false;
            if (loc.getX() > maxX) return false;
            if (loc.getZ() < minZ) return false;
            if (loc.getZ() > maxZ) return false;
            if (loc.getY() < minY) return false;
            if (loc.getY() > maxY) return false;
            return true;
        }


        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result
                    + ((regionName == null) ? 0 : regionName.hashCode());
            result = prime * result
                    + ((worldName == null) ? 0 : worldName.hashCode());
            return result;
        }


        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            VRegion other = (VRegion) obj;
            if (!getOuterType().equals(other.getOuterType())) {
                return false;
            }
            if (regionName == null) {
                if (other.regionName != null) {
                    return false;
                }
            } else if (!regionName.equals(other.regionName)) {
                return false;
            }
            if (worldName == null) {
                if (other.worldName != null) {
                    return false;
                }
            } else if (!worldName.equals(other.worldName)) {
                return false;
            }
            return true;
        }


        private RegionCache getOuterType() {
            return RegionCache.this;
        }


    }
}
