/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2013, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/reactions/
 *   * 
 *  This file is part of ReActions.
 *  
 *  ReActions is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
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

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

import me.fromgate.reactions.activators.Activators;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import com.palmergames.bukkit.towny.Towny;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;



public class ReActions extends JavaPlugin {

	//String actionmsg="tp,grpadd,grprmv,msg,dmg,townset,townkick,itemrmv,itemgive,cmdplr,cmdsrv"; //отображать сообщения о выполнении действий
	String actionmsg="tp,grpadd,grprmv,townset,townkick,itemrmv,invitemrmv,itemgive,moneypay,moneygive"; //отображать сообщения о выполнении действий
	String language="english";
	boolean language_save=false;
	boolean version_check=false;
	boolean tp_center_coors = true;
	public int worlduard_recheck = 2;
	public int same_msg_delay = 10;
	public boolean horizontal_pushback = false;
	
	

	//разные переменные

	RAUtil u;
	Logger log = Logger.getLogger("Minecraft");
	private RACmd cmd;
	private RAListener l;
	protected Permission permission = null;
	boolean vault_perm = false;
	protected Economy economy = null;
	boolean vault_eco = false;
	

	public RATowny towny;
	public boolean towny_conected = false;
	public RAWorldGuard worldguard;
	public boolean worldguard_conected = false;
	
	Activators activators;
	HashMap<String,RALoc> tports = new HashMap<String,RALoc>();
	RADebug debug = new RADebug();

	@Override
	public void onEnable() {
		loadCfg();
		saveCfg();
		u = new RAUtil (this, version_check, language_save, language, "reactions", "ReActions", "react", "&3[RA]&f ");
		if (!getDataFolder().exists()) getDataFolder().mkdirs();
		l = new RAListener (this);
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(l, this);

		cmd = new RACmd (this);
		getCommand("react").setExecutor(cmd);
		activators = new Activators (this);
		
		Actions.init(this);
		Flag.init(this);
		EventManager.init(this);
		RAPushBack.init(this);
		
		loadLocs();

		vault_perm = setupPermissions();
		vault_eco = setupEconomy();

		if (checkTowny()){
			towny = new RATowny (this);
			towny_conected = towny.connected;
		}
		
		if (checkWorldGuard()){
			worldguard = new RAWorldGuard(this);
			worldguard_conected = worldguard.connected;
		}
	}


	private boolean setupPermissions()
	{
		RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) {
			permission = permissionProvider.getProvider();
		}
		return (permission != null);
	}

	private boolean setupEconomy()
	{
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}

		return (economy != null);
	}

	protected void saveLocs(){
		try {
			File f = new File (this.getDataFolder()+File.separator+"locations.yml");
			if (f.exists()) f.delete();
			if (tports.size()>0){
				f.createNewFile();
				YamlConfiguration lcs = new YamlConfiguration();
				for (String key : tports.keySet()){
					lcs.set(key+".world", tports.get(key).world);
					lcs.set(key+".x", tports.get(key).x);
					lcs.set(key+".y", tports.get(key).y);
					lcs.set(key+".z", tports.get(key).z);
					lcs.set(key+".yaw", tports.get(key).yaw);
					lcs.set(key+".pitch", tports.get(key).pitch);
				}
				lcs.save(f);
			}
		} catch (Exception e){
			e.printStackTrace();
		}	
	}


	protected void loadLocs(){
		try {
			File f = new File (this.getDataFolder()+File.separator+"locations.yml");
			tports.clear();
			if (f.exists()){
				YamlConfiguration lcs = new YamlConfiguration();
				lcs.load(f);
				for (String key : lcs.getKeys(false))
					tports.put(key,new RALoc (lcs.getString(key+".world"),
							lcs.getDouble(key+".x"),
							lcs.getDouble(key+".y"),
							lcs.getDouble(key+".z"),
							(float) lcs.getDouble(key+".yaw"),
							(float) lcs.getDouble(key+".pitch")));
			}
		} catch (Exception e){
			e.printStackTrace();
		}			

	}

	protected void saveCfg(){
		getConfig().set("general.language",language);
		getConfig().set("general.check-updates",version_check);
		getConfig().set("reactions.show-messages-for-actions",actionmsg);
		getConfig().set("reactions.center-player-teleport",tp_center_coors);
		getConfig().set("reactions.region-recheck-delay",worlduard_recheck);
		getConfig().set("reactions.horizontal-pushback-action",horizontal_pushback );
		
		
		saveConfig();
	}

	protected void loadCfg(){
		language= getConfig().getString("general.language","english");
		version_check = getConfig().getBoolean("general.check-updates",false);
		language_save = getConfig().getBoolean("general.language-save",false);

		tp_center_coors = getConfig().getBoolean("reactions.center-player-teleport",true);
		actionmsg= getConfig().getString("reactions.show-messages-for-actions","tp,grpadd,grprmv,townset,townkick,itemrmv,itemgive,moneypay,moneygive");
		worlduard_recheck = getConfig().getInt("reactions.region-recheck-delay",2);
		horizontal_pushback = getConfig().getBoolean("reactions.horizontal-pushback-action", false);
	}

	private boolean checkTowny(){
		Plugin twn = this.getServer().getPluginManager().getPlugin("Towny");
		return  ((twn != null)&&(twn instanceof Towny));
	}
	
	private boolean checkWorldGuard(){
		Plugin wg = getServer().getPluginManager().getPlugin("WorldGuard");
		return  ((wg != null)&&(wg instanceof WorldGuardPlugin));
	}

	public RAUtil getUtils(){
		return this.u;
	}

}
