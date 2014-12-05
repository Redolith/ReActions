/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2014, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/reactions/
 *    
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

package me.fromgate.reactions.externals.wgbridge;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import me.fromgate.reactions.ReActions;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class RAWorldGuard {

	private static WGRegion bridge = null;
	
    public static boolean init(){
    	bridge = getWGBridge();
    	return bridge.isConnected();
    }

    public static List<String> getRegions(Location loc){
    	return bridge.getRegions(loc);
    }

    public static List<String> getRegions (Player p){
    	return bridge.getRegions(p);
    }

    public static int countPlayersInRegion (String rg){
    	return bridge.countPlayersInRegion(rg);
    }

    public static List<Player> playersInRegion (String rg){
    	return bridge.playersInRegion(rg);
    }


    public static boolean isPlayerInRegion (Player p, String rg){
    	return bridge.isPlayerInRegion(p, rg);
    }

    public static boolean isRegionExists(String rg){
    	return bridge.isRegionExists(rg);
    }

    public static List<Location> getRegionMinMaxLocations(String rg){
    	return bridge.getRegionMinMaxLocations(rg);
    }
    
    public static List<Location> getRegionLocations(String rg, boolean land){
    	return bridge.getRegionLocations(rg, land);
    }
    
    public static boolean isPlayerIsMemberOrOwner (Player p, String region){
    	return bridge.isPlayerIsMemberOrOwner(p, region);
    }
    
    public static boolean isPlayerIsOwner (Player p, String region){
        return bridge.isPlayerIsOwner(p, region);
    }
    
    public static boolean isPlayerIsMember (Player p, String region){
    	return bridge.isPlayerIsMember(p, region);
    }
    
    public static boolean isConnected(){
    	return bridge.isConnected();
    }
    
    
    public static WGRegion getWGBridge(){
		File dir = new File ( ReActions.instance.getDataFolder()+File.separator+"lib");
		if (dir.exists()&&dir.isDirectory()) {
			File[] fl = dir.listFiles();
			for (File f : fl){
				if (!f.getName().toLowerCase().matches("wgbridge.*\\.jar")) continue;
				JarFile jarFile;
				try {
					jarFile = new JarFile(f.getPath());
				} catch (Exception e1) {
					continue;
				}
				Enumeration<JarEntry> e = jarFile.entries();
				ClassLoader cl;					
				try {
					URL[] urls = { new URL("jar:file:" +f.getPath()+"!/") };
					cl = URLClassLoader.newInstance(urls, ReActions.instance.getClass().getClassLoader());
				} catch (Exception e2) {
					continue;
				}
				
				while (e.hasMoreElements()) {
					JarEntry je = (JarEntry) e.nextElement();
					if(je.isDirectory() || !je.getName().endsWith(".class")){
						continue;
					}
					String className = je.getName().substring(0,je.getName().length()-6);
					className = className.replace('/', '.');
					if (className.contains("$")) continue; //пропускаем лишнее
					try {
						Class<?> clazz = Class.forName(className, true, cl);
						Class<? extends WGRegion> c = clazz.asSubclass(WGRegion.class);
						WGRegion wgregon = (WGRegion) c.newInstance();
						if (wgregon.isConnected()) return wgregon;
					} catch (Exception e3){
						continue;
					}
				}
			}
		}
    	return new WGBridge();
    }
    
}
