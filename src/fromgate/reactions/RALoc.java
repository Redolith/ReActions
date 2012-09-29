package fromgate.reactions;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class RALoc {
	String world;
	int x;
	int y;
	int z;
	float yaw;
	float pitch;
	
	
	public RALoc (Location loc){
		this.world = loc.getWorld().getName();
		this.x = loc.getBlockX();
		this.y = loc.getBlockY();
		this.z = loc.getBlockZ();
		this.yaw= loc.getYaw();
		this.pitch = loc.getPitch();
	
	}
	
	public RALoc (String world, int x, int y, int z, float yaw, float pitch){
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
				(loc.getBlockX()==this.x)&&
				(loc.getBlockY()==this.y)&&
				(loc.getBlockZ()==this.z));
	}
	
	

}
