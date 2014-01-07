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
import me.fromgate.reactions.externals.RAEffects;
import me.fromgate.reactions.externals.RARacesAndClasses;
import me.fromgate.reactions.externals.RATowny;
import me.fromgate.reactions.externals.RAVault;
import me.fromgate.reactions.externals.RAWorldGuard;
import me.fromgate.reactions.timer.Timers;
import me.fromgate.reactions.util.Delayer;
import me.fromgate.reactions.util.RADebug;
import me.fromgate.reactions.util.Shoot;
import me.fromgate.reactions.util.Variables;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.palmergames.bukkit.towny.Towny;


public class ReActions extends JavaPlugin {
    String actionMsg="tp,grpadd,grprmv,townset,townkick,itemrmv,invitemrmv,itemgive,moneypay,moneygive"; //отображать сообщения о выполнении действий
    
    
    String language="english";
    boolean languageSave=false;
    boolean checkUpdates=false;
    boolean centerTpCoords = true;
    public int worlduardRecheck = 2;
    public int itemHoldRecheck = 2;
    public int itemWearRecheck = 2;
    public int sameMessagesDelay = 10;
    public boolean horizontalPushback = false;
    boolean enableProfiler = true;
    private boolean needUpdate;
    public static ReActions instance;
    public static RAUtil util;
    

    //разные переменные
    RAUtil u;
    Logger log = Logger.getLogger("Minecraft");
    private Cmd cmd;
    private RAListener l;
    private boolean towny_conected = false;

    public boolean isTownyConnected(){
        return towny_conected;
    }

    //Activators activators;
    HashMap<String,TpLoc> tports = new HashMap<String,TpLoc>();
    RADebug debug = new RADebug();

    public Activator getActivator(String id){
        return Activators.get(id);
    }

    @Override
    public void onEnable() {
        loadCfg();
        saveCfg();
        u = new RAUtil (this, languageSave, language, "react");
        u.initUpdateChecker("ReActions", "61726", "reactions", this.checkUpdates);
        if (!getDataFolder().exists()) getDataFolder().mkdirs();
        l = new RAListener (this);
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(l, this);
        cmd = new Cmd (this);
        getCommand("react").setExecutor(cmd);
        instance = this;
        util = u;
        Timers.init();
        Activators.init();
        RAEffects.init();
        RARacesAndClasses.init();
        Delayer.load();
        Variables.load();
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
        getConfig().set("general.check-updates",checkUpdates);
        getConfig().set("reactions.show-messages-for-actions",actionMsg);
        getConfig().set("reactions.center-player-teleport",centerTpCoords);
        getConfig().set("reactions.region-recheck-delay",worlduardRecheck);
        getConfig().set("reactions.item-hold-recheck-delay",itemHoldRecheck);
        getConfig().getInt("reactions.item-wear-recheck-delay",itemWearRecheck);
        getConfig().set("reactions.horizontal-pushback-action",horizontalPushback );
        getConfig().set("reactions.need-file-update", needUpdate);
        getConfig().set("actions.shoot.break-block",Shoot.actionShootBreak);
        getConfig().set("actions.shoot.penetrable",Shoot.actionShootThrough);
        saveConfig();
    }

    protected void loadCfg(){
        language= getConfig().getString("general.language","english");
        checkUpdates = getConfig().getBoolean("general.check-updates",true);
        languageSave = getConfig().getBoolean("general.language-save",false);
        centerTpCoords = getConfig().getBoolean("reactions.center-player-teleport",true);
        actionMsg= getConfig().getString("reactions.show-messages-for-actions","tp,grpadd,grprmv,townset,townkick,itemrmv,itemgive,moneypay,moneygive");
        worlduardRecheck = getConfig().getInt("reactions.region-recheck-delay",2);
        itemHoldRecheck = getConfig().getInt("reactions.item-hold-recheck-delay",2);
        itemWearRecheck = getConfig().getInt("reactions.item-wear-recheck-delay",2);;
        horizontalPushback = getConfig().getBoolean("reactions.horizontal-pushback-action", false);
        needUpdate= getConfig().getBoolean("reactions.need-file-update", true);
        Shoot.actionShootBreak = getConfig().getString("actions.shoot.break-block",Shoot.actionShootBreak);
        Shoot.actionShootThrough = getConfig().getString("actions.shoot.penetrable",Shoot.actionShootThrough);
    }

    private boolean checkTowny(){
        Plugin twn = this.getServer().getPluginManager().getPlugin("Towny");
        return  ((twn != null)&&(twn instanceof Towny));
    }

    public RAUtil getUtils(){
        return this.u;
    }

    public boolean isCenterTpLocation(){
        return this.centerTpCoords;
    }

    public String getActionMsg(){
        return this.actionMsg;
    }

    public boolean containsTpLoc(String locstr){
        return tports.containsKey(locstr);
    }

    public Location getTpLoc(String locstr){
        if (tports.containsKey(locstr)) return tports.get(locstr).getLocation();
        return null;
    }

    public boolean needUpdateFiles() {
        return needUpdate;
    }

    public void setUpdateFiles (boolean update){
        if (update!= needUpdate){
            needUpdate = update;
            saveCfg();
        }
    }
}
