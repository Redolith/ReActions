/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2013, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/reactions/
 *   * 
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

package me.fromgate.reactions;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class RALoc {
    String world;
    double x;
    double y;
    double z;
    float yaw;
    float pitch;


    public RALoc (Location loc){
        this.world = loc.getWorld().getName();
        this.x = loc.getX();
        this.y = loc.getY();
        this.z = loc.getZ();
        this.yaw= loc.getYaw();
        this.pitch = loc.getPitch();

    }

    public RALoc (String world, double x, double y, double z, float yaw, float pitch){
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw= yaw;
        this.pitch = pitch;
    }

    public Location getLocation(){
        return new Location (Bukkit.getWorld(world),x,y,z,yaw,pitch);
    }

    public boolean equalToLoc(Location loc){
        return 	(loc.getWorld().getName().equalsIgnoreCase(this.world)&&
                (Math.round(loc.getX())==Math.round(x))&&
                (Math.round(loc.getY())==Math.round(y))&&
                (Math.round(loc.getZ())==Math.round(z)));
    }

    @Override
    public String toString(){
        DecimalFormat fmt = new DecimalFormat("####0.##");
        return "["+this.world+"] "+fmt.format(x)+", "+fmt.format(y)+", "+fmt.format(z);
    }

    // world,x,y,z,[yaw,pitch]
    public static Location parseLocation (String strloc){
        Location loc = null;
        if (strloc.isEmpty()) return null;
        String [] ln = strloc.split(",");
        if (!((ln.length==4)||(ln.length==6))) return null;
        World w = Bukkit.getWorld(ln[0]);
        if (w==null) return null;
        for (int i = 1; i<ln.length; i++)
            if (!(ln[i].matches("-?[0-9]+[0-9]*\\.[0-9]+")||ln[i].matches("-?[1-9]+[0-9]*"))) return null;
        loc = new Location (w, Double.parseDouble(ln[1]),Double.parseDouble(ln[2]),Double.parseDouble(ln[3]));
        if (ln.length==6){
            loc.setYaw(Float.parseFloat(ln[4]));
            loc.setPitch(Float.parseFloat(ln[5]));
        }
        return loc;
    }

    public static String locactionToStringFormated(Location loc){
        DecimalFormat fmt = new DecimalFormat("####0.##");
        return "["+loc.getWorld().getName()+"] "+fmt.format(loc.getX())+", "+fmt.format(loc.getY())+", "+fmt.format(loc.getZ());
    }
    public static String locationToString(Location loc){
        if (loc == null) return "";
        return loc.getWorld().getName()+","+loc.getX()+","+loc.getY()+","+loc.getZ()+","+loc.getY()+","+loc.getPitch();

    }

}
