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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.event.TownAddResidentEvent;
import com.palmergames.bukkit.towny.event.TownRemoveResidentEvent;
import com.palmergames.bukkit.towny.exceptions.EmptyTownException;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import static com.palmergames.bukkit.towny.object.TownyObservableType.TOWN_ADD_RESIDENT;
import static com.palmergames.bukkit.towny.object.TownyObservableType.TOWN_REMOVE_RESIDENT;

public class ReActions extends JavaPlugin {

	//конфигурация
	//String actionmsg="tp,grpadd,grprmv,msg,dmg,townset,townkick,itemrmv,itemgive,cmdplr,cmdsrv"; //отображать сообщения о выполнении действий
	String actionmsg="tp,grpadd,grprmv,townset,townkick,itemrmv,itemgive,moneypay,moneygive"; //отображать сообщения о выполнении действий
	String language="english";
	boolean language_save=false;
	boolean version_check=false;



	//разные переменные
	String ftypes = "group,perm,time,item,town,money";
	String atypes = "tp,grpadd,grprmv,msg,dmg,townset,townkick,itemrmv,itemgive,cmdplr,cmdsrv,moneypay,moneygive";
	RAUtil u;
	Logger log = Logger.getLogger("Minecraft");
	private RACmd cmd;
	private RAListener l;
	protected Permission permission = null;
	protected Economy economy = null;
	boolean vault_perm = false;
	boolean vault_eco = false;
	protected Towny towny = null;
	boolean towny_conected = false;
	HashMap<String,Activator> clickers = new HashMap<String,Activator>();
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

		loadActivators();
		loadLocs();

		vault_perm = setupPermissions();
		vault_eco = setupEconomy();
		towny_conected = connectToTowny();

		//vault_found = connectToVault(); 

	}


	//	String ftypes = "group,perm,time,item,town,money";
	private boolean checkFlag (Player p, String flag, String param, boolean not){
		boolean chr = false;
		if (flag.equalsIgnoreCase("group")) chr= (vault_perm&&permission.playerInGroup(p, param));
		else if (flag.equalsIgnoreCase("perm")) chr=p.hasPermission(param);
		else if (flag.equalsIgnoreCase("time")) chr=checkTime(p, param);
		else if (flag.equalsIgnoreCase("item")) chr=checkItem (p, param);
		else if (flag.equalsIgnoreCase("town")) chr=playerInTown (p, param);
		else if (flag.equalsIgnoreCase("money")) chr=playerHasMoney (p, param);
		if (not) chr= !chr;
		return chr;
	}

	private boolean playerHasMoney (Player p, String amountstr){
		//if (!vault_eco) return false;
		return vault_eco&&(amountstr.matches("[0-9]*")&&(Integer.parseInt(amountstr)<=economy.getBalance(p.getName()))); 
	}
	
	private boolean playerInTown(Player p, String townname){
		if (!towny_conected) return false;
		Resident rsd = towny.getTownyUniverse().getResidentMap().get(p.getName());
		if (!rsd.hasTown()||townname.isEmpty()) return false;
		try {
			return (townname.equalsIgnoreCase(rsd.getTown().getName()));
		} catch (NotRegisteredException e) {
			return false;
		}
	}
	
	public boolean checkFlags (Player p, Activator c){
		return debug.checkFlagAndDebug(p, checkAllFlags (p, c));
	}
	
	
	public boolean checkAllFlags (Player p, Activator c){
		if (c.flags.size()>0)
			for (int i = 0; i<c.flags.size();i++)
				if (!checkFlag (p, c.flags.get(i).flag, c.flags.get(i).value, c.flags.get(i).not)) return false;
		return true;
	}


	public void performActions (Player p, boolean action, Activator c){
		if (action&&(c.actions.size()>0))
			for (int i = 0;i<c.actions.size();i++)
				performAction (p, c.actions.get(i).flag, c.actions.get(i).value);

		else if ((!action)&&(c.reactions.size()>0))
			for (int i = 0;i<c.reactions.size();i++)
				performAction (p, c.reactions.get(i).flag, c.reactions.get(i).value);
	}

	public boolean checkLoc(String locstr){
		return (locstr.equalsIgnoreCase("player")||
				locstr.equalsIgnoreCase("viewpoint")||
				tports.containsKey(locstr));
	}

	public Location locToLocation(Player p, String locstr){
		Location loc = null;
		if (locstr.equalsIgnoreCase("player")) loc = p.getLocation();
		else if (locstr.equalsIgnoreCase("viewpoint")) loc = p.getTargetBlock(null, 100).getLocation(); 
		else loc = tports.get(locstr).getLocation();
		return loc;
	}
	
	public String locToString(Player p, String locstr){
		String loc = u.MSGnc("loc_unknown");
		Location tl = locToLocation (p, locstr);
		if (tl!=null) loc = "["+tl.getWorld().getName()+"] ("+tl.getBlockX()+", "+tl.getBlockY()+", "+tl.getBlockZ()+")";
		return loc;
	}



	//String atypes = "tp,grpadd,grprmv,msg,dmg,townset,townkick,itemrmv,itemgive,cmdplr,cmdsrv,moneypay,moneygive";
	public void performAction(Player p, String act, String param){
		String msgparam =param;
		
		if (act.equalsIgnoreCase("tp")&&checkLoc(param)){
			p.teleport(locToLocation (p,param));
		} else if (act.equalsIgnoreCase("grpadd")&&vault_perm&&(!param.isEmpty())){
			
			if (!permission.playerAddGroup(p, param))
				msgparam=msgparam+"fail";
			
		} else if (act.equalsIgnoreCase("grprmv")&&vault_perm&&(!param.isEmpty())){
			if (permission.playerInGroup(p, param)) {
				if (!permission.playerRemoveGroup(p, param)) msgparam=msgparam+"fail";;
			}
		} else if (act.equalsIgnoreCase("msg")&&(!param.isEmpty())){
			u.PrintMsg(p, param);
		} else if (act.equalsIgnoreCase("dmg")&&(!param.isEmpty())&&(param.matches("[1-9]+[0-9]*"))){
			p.damage(Integer.parseInt(param));
		} else if (act.equalsIgnoreCase("dmg")){
			p.playEffect(EntityEffect.HURT);
			msgparam=msgparam+"hit";
		} else if (act.equalsIgnoreCase("msgall")&&(!param.isEmpty())){
			sendBroadCastMsg (p,param);
		} else if (act.equalsIgnoreCase("townset")&&(!param.isEmpty())){
			addToTown(p, param);
		} else if (act.equalsIgnoreCase("townkick")){
			kickFromTown (p);
		} else if (act.equalsIgnoreCase("itemrmv")&&(!param.isEmpty())){
			removeItemInHand(p, param);
		} else if (act.equalsIgnoreCase("itemgive")&&(!param.isEmpty())){
			giveItemPlayer(p,param);
		} else if (act.equalsIgnoreCase("cmdplr")&&(!param.isEmpty())){
			getServer().dispatchCommand(p, param.replaceAll("%player%",p.getName()));
		} else if (act.equalsIgnoreCase("cmdsrv")&&(!param.isEmpty())){
			getServer().dispatchCommand(getServer().getConsoleSender(), param.replaceAll("%player%",p.getName()));
		} else if (act.equalsIgnoreCase("moneypay")&&(vault_eco)&&(!param.isEmpty())){
			msgparam = Integer.toString(moneyPay (p, param))+";"+this.economy.currencyNamePlural();
		} else if (act.equalsIgnoreCase("moneygive")&&(vault_eco)&&(!param.isEmpty())){
			msgparam = Integer.toString(moneyGive (p, param))+";"+this.economy.currencyNamePlural();
		}
		
		//выводим сообщение о выполнении действия
		if (u.isWordInList(act, actionmsg)) u.PrintMSG(p, "act_"+act,msgparam);
	}
	
	


	private int moneyPay (Player p, String mstr){
		if (mstr.isEmpty()) return 0;
		String money="";
		String target="";
		if (mstr.contains("/")) {
			String [] m = mstr.split("/");
			if (m.length>=2){
				money = m[0];	
				target = m[1];
			}
		} else money = mstr;
		if (!money.matches("[0-9]*")) return 0;
		int amount = Integer.parseInt(money);
		if ((amount<=0)||(amount>economy.getBalance(p.getName()))) return 0;
		economy.withdrawPlayer(p.getName(), amount);
		if (!target.isEmpty()) economy.depositPlayer(target, amount);
		return amount;
	}
	
	private int moneyGive (Player p, String mstr){
		if (mstr.isEmpty()) return 0;
		String money="";
		String source="";
		if (mstr.contains("/")) {
			String [] m = mstr.split("/");
			if (m.length>=2){
				money = m[0];	
				source = m[1];
			}
		} else money = mstr;
		if (!money.matches("[0-9]*")) return 0;
		int amount = Integer.parseInt(money);
		if (amount<=0) return 0;		
		
		if (!source.isEmpty()){
			if (amount<economy.getBalance(source)) return 0;
			economy.withdrawPlayer(source, amount);
		} 
		
		economy.depositPlayer(p.getName(), amount);
		return amount;
	}

	private void giveItemPlayer(Player p, String param) {
		ItemStack item = u.parseItem(param);
		if (item!=null)	p.getInventory().addItem(item);
	}


	public void sendBroadCastMsg(Player sender, String msg){
		for (Player p : getServer().getOnlinePlayers())
			u.PrintMsg(p, msg);
	}

	public void executeClicker(Player p, Activator c){
		performActions(p, (checkFlags(p,c)), c); 
	}

	protected boolean connectToTowny(){
		Plugin twn = getServer().getPluginManager().getPlugin("Towny");
		if ((twn != null)&&(twn instanceof Towny)){
			this.towny = (Towny) twn;
			return true;
		}
		return false;
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

	protected boolean checkTime(Player p, String time){
		Long ctime = p.getWorld().getTime();
		if (time.equalsIgnoreCase("day")) {
			return ((ctime>=0)&&(ctime<12000));
		} else if (time.equalsIgnoreCase("night")) {
			return ((ctime>=12000)&&(ctime<23999));

		} else {
			String [] tln = time.split(","); 
			if (tln.length>0){
				for (int i = 0; i<tln.length; i++)
					if (tln[i].matches("[0-9]+")){
						int ct = (int) ((ctime / 1000 + 8) % 24);
						if (ct == Integer.parseInt(tln[i])) return true;
					}
			}
		}
		return false;
	}

	protected boolean removeItemInHand (Player p, String item){
		return u.removeItemInHand(p, item);
	}

	// item <id>:<count>:<data>
	protected boolean checkItem(Player p, String item){
		return u.compareItemStr(p.getItemInHand(), item);
	}


	protected void kickFromTown (Player p){
		Resident rsd = towny.getTownyUniverse().getResidentMap().get(p.getName());
		
		if (rsd.hasTown()){
			Town town;
			try {
				town = rsd.getTown();
				if (!rsd.isMayor())	townRemoveResident (town,rsd);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	private void townRemoveResident (Town town, Resident resident) throws NotRegisteredException, EmptyTownException {
			town.removeResident(resident);
			towny.deleteCache(resident.getName());
			TownyUniverse.getDataSource().saveResident(resident);
			TownyUniverse.getDataSource().saveTown(town);
			towny.getTownyUniverse().setChangedNotify(TOWN_REMOVE_RESIDENT);
			Bukkit.getPluginManager().callEvent(new TownRemoveResidentEvent (resident, town));
	}


	protected void addToTown (Player p, String town){
		if (towny_conected){
			Town newtown = towny.getTownyUniverse().getTownsMap().get(town.toLowerCase());
			if (newtown != null){
				Resident rsd = towny.getTownyUniverse().getResidentMap().get(p.getName()); 
				if (rsd.hasTown()){
					Town twn = null;
					try {
						twn = rsd.getTown();
						townRemoveResident  (twn, rsd);
					} catch (Exception e){
						e.printStackTrace();
					}
				}
				if (!rsd.hasTown()){
					try {
						newtown.addResident(rsd);
						towny.deleteCache(rsd.getName());
						TownyUniverse.getDataSource().saveResident(rsd);
						TownyUniverse.getDataSource().saveTown(newtown);
						towny.getTownyUniverse().setChangedNotify(TOWN_ADD_RESIDENT);
						Bukkit.getPluginManager().callEvent(new TownAddResidentEvent (rsd, newtown));
					} catch (Exception e) {
						e.printStackTrace();
					}					
				}
			}
		}
	}


	protected void saveClickers(){
		try {
			File f = new File (this.getDataFolder()+File.separator+"buttons.yml");

			if (f.exists()) f.delete();
			if (clickers.size()>0){
				f.createNewFile();
				YamlConfiguration btn = new YamlConfiguration();
				for (String key : clickers.keySet()){
					Activator c = clickers.get(key);
					btn.set(key+".world", c.world);
					btn.set(key+".x",c.x);
					btn.set(key+".y",c.y);
					btn.set(key+".z",c.z);
					if (c.flags.size()>0){
						List<String> flg = new ArrayList<String>();

						for (int i = 0; i<c.flags.size();i++)	flg.add(c.flags.get(i).toString());
						
/*						for (String fkey : c.flags.keySet()){
							String s = fkey+"="+c.flags.get(fkey);
							flg.add(s);
						}*/
						
						if (flg.size()>0)
							btn.set(key+".flags",flg);
					}

					if (c.actions.size()>0){
						List<String> act = new ArrayList<String>();
						for (int i = 0; i<c.actions.size();i++){
							String s = c.actions.get(i).flag+"="+c.actions.get(i).value;
							act.add(s);
						}

						if (act.size()>0)
							btn.set(key+".actions",act);
					}

					if (c.reactions.size()>0){
						List<String> react = new ArrayList<String>();
						for (int i = 0; i<c.reactions.size();i++){
							String s =c.reactions.get(i).flag+"="+c.reactions.get(i).value;
							react.add(s);
						}
						if (react.size()>0)
							btn.set(key+".reactions",react);
					}
				}
				btn.save(f);
			}
		} catch (Exception e){
			e.printStackTrace();
		}		
	}


	protected void loadActivators(){
		try {
			File f = new File (this.getDataFolder()+File.separator+"buttons.yml");
			if (f.exists()){
				YamlConfiguration lcs = new YamlConfiguration();
				lcs.load(f);
				for (String key : lcs.getKeys(false)){
					Activator clk = new Activator (lcs.getString(key+".world"),
							lcs.getInt(key+".x"),
							lcs.getInt(key+".y"),
							lcs.getInt(key+".z"));

					// Вытаскиваем флаги
					List<String> flg = lcs.getStringList(key+".flags");
					if (flg.size()>0){
						for (int i = 0; i< flg.size();i++){
							clk.addFlag(flg.get(i));
							
							/*String []ln = flg.get(i).split("=");
							if (ln.length>0){
								if (ln.length==2) clk.flags.put(ln[0], ln[1]);
								else clk.flags.put(ln[0], "");*/
							//}
						}
					}

					// Вытаскиваем действия					
					List<String> act = lcs.getStringList(key+".actions");
					if (act.size()>0){
						for (int i = 0; i< act.size();i++){
							String []ln = act.get(i).split("=");
							if (ln.length>0){
								if (ln.length==2) clk.addAction(ln[0], ln[1]); 
								else clk.addAction(ln[0], "");
							}
						}
					}

					// Вытаскиваем реакции
					List<String> react = lcs.getStringList(key+".reactions");
					if (react.size()>0){
						for (int i = 0; i< react.size();i++){
							String []ln = react.get(i).split("=");
							if (ln.length>0){
								if (ln.length==2) clk.addReAction(ln[0], ln[1]); 
								else clk.addReAction(ln[0], "");
							}
						}
					}					

					clickers.put(key, clk);

				}
			}

		} catch (Exception e){
			e.printStackTrace();
		}		

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
							lcs.getInt(key+".x"),
							lcs.getInt(key+".y"),
							lcs.getInt(key+".z"),
							(float) lcs.getDouble(key+".yaw"),
							(float) lcs.getDouble(key+".pitch")));
			}
		} catch (Exception e){
			e.printStackTrace();
		}			

	}
	
	protected void saveCfg(){
		getConfig().set("general.language-save",language);
		getConfig().set("general.check-updates",version_check);
		getConfig().set("reactions.show-messages-for-actions",actionmsg);
		saveConfig();
	}
	
	protected void loadCfg(){
		language= getConfig().getString("general.language","english");
		version_check = getConfig().getBoolean("general.check-updates",false);
		language_save = getConfig().getBoolean("general.language-save",false);
		actionmsg= getConfig().getString("reactions.show-messages-for-actions","tp,grpadd,grprmv,townset,townkick,itemrmv,itemgive,moneypay,moneygive");
	}




}
