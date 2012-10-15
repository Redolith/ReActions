/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/weatherman/
 *   * 
 *  This file is part of ReActions.
 *  
 *  WeatherMan is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  WeatherMan is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with WeatherMan.  If not, see <http://www.gnorg/licenses/>.
 * 
 */

package fromgate.reactions;

import org.bukkit.Bukkit;
import org.bukkit.Location;

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
	
	

}
