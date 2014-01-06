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
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;
import me.fromgate.reactions.activators.Activator;
import me.fromgate.reactions.activators.Activators;
import me.fromgate.reactions.util.RADebug;
import me.fromgate.reactions.util.RAEffects;
import me.fromgate.reactions.util.RATowny;
import me.fromgate.reactions.util.RAVault;
import me.fromgate.reactions.util.RAWorldGuard;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.palmergames.bukkit.towny.Towny;


public class ReActions extends JavaPlugin {

    //String actionmsg="tp,grpadd,grprmv,msg,dmg,townset,townkick,itemrmv,itemgive,cmdplr,cmdsrv"; //отображать сообщения о выполнении действий
    String actionmsg="tp,grpadd,grprmv,townset,townkick,itemrmv,invitemrmv,itemgive,moneypay,moneygive"; //отображать сообщения о выполнении действий
    String language="english";
    boolean language_save=false;
    boolean version_check=false;
    boolean tp_center_coors = true;
    public int worlduard_recheck = 2;
    public int itemhold_recheck = 2;
    public int same_msg_delay = 10;
    public boolean horizontal_pushback = false;
    boolean enable_profiler = true;
    private boolean need_update;
    //public boolean play_hearts_heal = true;




    public static ReActions instance;
    public static RAUtil util;


    //разные переменные

    RAUtil u;
    Logger log = Logger.getLogger("Minecraft");
    private Cmd cmd;
    private RAListener l;



    //public RATowny towny;
    private boolean towny_conected = false;
    public boolean isTownyConnected(){
        return towny_conected;
    }
    //public RAWorldGuard worldguard;
    //public boolean worldguard_conected = false;

    //public RAVault vault;


    //Activators activators;
    HashMap<String,TpLoc> tports = new HashMap<String,TpLoc>();
    RADebug debug = new RADebug();


    public Activator getActivator(String id){
        return Activators.get(id);
        //return  activators.get(id);
    }

    @Override
    public void onEnable() {
        loadCfg();
        saveCfg();
        u = new RAUtil (this, language_save, language, "react");
        u.initUpdateChecker("ReActions", "61726", "a2a7b26dd4dc9bc496c80de4b49e87cb42e34ae3", "reactions", this.version_check);
        if (!getDataFolder().exists()) getDataFolder().mkdirs();
        l = new RAListener (this);
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(l, this);

        cmd = new Cmd (this);
        getCommand("react").setExecutor(cmd);
        instance = this;
        util = u;
        Activators.init();
        //EventManager.init(this);
        RAEffects.init();
        loadLocs();
        RAVault.init();
        RAWorldGuard.init();
        if (checkTowny()) towny_conected = RATowny.init();

        try {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        } catch (IOException e) {
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
                    tports.put(key,new TpLoc (lcs.getString(key+".world"),
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
        getConfig().set("reactions.need-file-update", need_update);
        saveConfig();
    }

    protected void loadCfg(){
        language= getConfig().getString("general.language","english");
        version_check = getConfig().getBoolean("general.check-updates",true);
        language_save = getConfig().getBoolean("general.language-save",false);
        tp_center_coors = getConfig().getBoolean("reactions.center-player-teleport",true);
        actionmsg= getConfig().getString("reactions.show-messages-for-actions","tp,grpadd,grprmv,townset,townkick,itemrmv,itemgive,moneypay,moneygive");
        worlduard_recheck = getConfig().getInt("reactions.region-recheck-delay",2);
        horizontal_pushback = getConfig().getBoolean("reactions.horizontal-pushback-action", false);
        need_update= getConfig().getBoolean("reactions.need-file-update", true);
    }

    private boolean checkTowny(){
        Plugin twn = this.getServer().getPluginManager().getPlugin("Towny");
        return  ((twn != null)&&(twn instanceof Towny));
    }

    public RAUtil getUtils(){
        return this.u;
    }

    public boolean isCenterTpLocation(){
        return this.tp_center_coors;
    }

    public String getActionMsg(){
        return this.actionmsg;
    }

    public boolean containsTpLoc(String locstr){
        return tports.containsKey(locstr);
    }

    public Location getTpLoc(String locstr){
        if (tports.containsKey(locstr)) return tports.get(locstr).getLocation();
        return null;
    }

    public boolean needUpdateFiles() {
        return need_update;
    }

    public void setUpdateFiles (boolean update){
        if (update!= need_update){
            need_update = update;
            saveCfg();
        }
    }
}
