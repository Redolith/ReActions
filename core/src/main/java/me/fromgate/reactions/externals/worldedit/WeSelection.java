package me.fromgate.reactions.externals.worldedit;

import org.bukkit.Location;
import org.bukkit.World;

public class WeSelection {

    String selType;
    World world;
    String region;
    Location min;
    Location max;
    int area;

    public WeSelection(String typeName, Location minimumPoint, Location maximumPoint, int area, World world, String region) {
        this.selType = typeName;
        this.world = world;
        this.region = region;
        this.min = minimumPoint;
        this.max = maximumPoint;
        this.area = area;
    }


    public boolean isValid() {
        return world != null && region != null && !region.isEmpty() && min != null && max != null && area != -1;
    }

    public String getSelType() {
        return selType;
    }

    public World getWorld() {
        return world;
    }

    public String getRegion() {
        return region;
    }

    public Location getMin() {
        return min;
    }

    public Location getMax() {
        return max;
    }

    public int getArea() {
        return area;
    }
}
