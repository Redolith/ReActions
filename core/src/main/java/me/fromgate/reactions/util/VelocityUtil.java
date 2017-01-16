package me.fromgate.reactions.util;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class VelocityUtil {
    public static Vector calculateVelocity(Location locFrom, Location locTo, int heightGain) {
        if (!locFrom.getWorld().equals(locTo.getWorld())) return new Vector(0, 0, 0);
        // Gravity of a potion
        double gravity = 0.18; //0.115;
        Vector from = locFrom.toVector();
        Vector to = locTo.toVector();


        // Block locations
        int endGain = to.getBlockY() - from.getBlockY();
        double horizDist = Math.sqrt(distanceSquared(from, to));

        // Height gain
        int gain = heightGain;

        //double maxGain = gain > (endGain + gain) ? gain : (endGain + gain);
        double maxGain = Math.max(gain, endGain + gain);

        // Solve quadratic equation for velocity
        double a = -horizDist * horizDist / (4 * maxGain);
        double b = horizDist;
        double c = -endGain;

        double slope = -b / (2 * a) - Math.sqrt(b * b - 4 * a * c) / (2 * a);

        // Vertical velocity
        double vy = Math.sqrt(maxGain * gravity);

        // Horizontal velocity
        double vh = vy / slope;

        // Calculate horizontal direction
        int dx = to.getBlockX() - from.getBlockX();
        int dz = to.getBlockZ() - from.getBlockZ();
        double mag = Math.sqrt(dx * dx + dz * dz);
        double dirx = dx / mag;
        double dirz = dz / mag;

        // Horizontal velocity components
        double vx = vh * dirx;
        double vz = vh * dirz;

        return new Vector(vx, vy, vz);
    }

    private static double distanceSquared(Vector from, Vector to) {
        double dx = to.getBlockX() - from.getBlockX();
        double dz = to.getBlockZ() - from.getBlockZ();
        return dx * dx + dz * dz;
    }
}
