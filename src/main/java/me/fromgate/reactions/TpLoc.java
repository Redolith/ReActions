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

public class TpLoc {
    String world;
    double x;
    double y;
    double z;
    float yaw;
    float pitch;
    //int radius;

    public TpLoc (Location loc){
        this.world = loc.getWorld().getName();
        this.x = loc.getX();
        this.y = loc.getY();
        this.z = loc.getZ();
        this.yaw= loc.getYaw();
        this.pitch = loc.getPitch();
        //this.radius = 0;
    }

    /*public RALoc (Location loc,int radius){
        this.world = loc.getWorld().getName();
        this.x = loc.getX();
        this.y = loc.getY();
        this.z = loc.getZ();
        this.yaw= loc.getYaw();
        this.pitch = loc.getPitch();
        this.radius = radius;
    }*/

    public TpLoc (String world, double x, double y, double z, float yaw, float pitch){
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw= yaw;
        this.pitch = pitch;
       // this.radius = 0;
    }

    /*public RALoc (String world, double x, double y, double z, float yaw, float pitch, int radius){
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw= yaw;
        this.pitch = pitch;
        this.radius = radius;
    }*/

    public Location getLocation(){
        return new Location (Bukkit.getWorld(world),x,y,z,yaw,pitch);
    }
    
    /*
    public Location getCentralLocation(){
        return new Location (Bukkit.getWorld(world),x,y,z,yaw,pitch);
    }

    public Location getLocation(){
        return Util.getRandomLocationInRadius(new Location (Bukkit.getWorld(world),x,y,z,yaw,pitch), this.radius);
    }*/

    public boolean equalToLoc(Location loc){
        return 	(loc.getWorld().getName().equalsIgnoreCase(this.world)&&
                (Math.round(loc.getX())==Math.round(x))&&
                (Math.round(loc.getY())==Math.round(y))&&
                (Math.round(loc.getZ())==Math.round(z)));
    }

    @Override
    public String toString(){
        DecimalFormat fmt = new DecimalFormat("####0.##");
        //return  "["+this.world+"] "+fmt.format(x)+", "+fmt.format(y)+", "+fmt.format(z)+((this.radius>0)?" @R"+this.radius : "");
        return  "["+this.world+"] "+fmt.format(x)+", "+fmt.format(y)+", "+fmt.format(z);
    }


}
