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

package me.fromgate.reactions;

import me.fromgate.reactions.activators.Activator;
import me.fromgate.reactions.activators.Activators;
import me.fromgate.reactions.commands.Commander;
import me.fromgate.reactions.externals.*;
import me.fromgate.reactions.menu.InventoryMenu;
import me.fromgate.reactions.placeholders.Placeholders;
import me.fromgate.reactions.sql.SQLManager;
import me.fromgate.reactions.timer.Timers;
import me.fromgate.reactions.util.*;
import me.fromgate.reactions.util.playerselector.PlayerSelectors;
import me.fromgate.reactions.util.waiter.ActionsWaiter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Logger;


public class ReActions extends JavaPlugin {
    boolean saveEmptySections = false;
    String actionMsg = "tp,grpadd,grprmv,townset,townkick,itemrmv,invitemrmv,itemgive,moneypay,moneygive"; //отображать сообщения о выполнении действий
    String language = "english";
    boolean languageSave = false;
    boolean checkUpdates = false;
    boolean centerTpCoords = true;
    public int worlduardRecheck = 2;
    public int itemHoldRecheck = 2;
    public int itemWearRecheck = 2;
    public int sameMessagesDelay = 10;
    public boolean horizontalPushback = false;
    boolean enableProfiler = true;
    int chatLength = 55;

    public static ReActions instance;
    public static RAUtil util;


    public static ReActions getPlugin() {
        return instance;
    }

    public static RAUtil getUtil() {
        return util;
    }


    //разные переменные
    RAUtil u;
    Logger log = Logger.getLogger("Minecraft");
    //private CmdOld cmd;
    private RAListener l;
    private boolean towny_conected = false;

    public boolean isTownyConnected() {
        return towny_conected;
    }

    RADebug debug = new RADebug();

    public Activator getActivator(String id) {
        return Activators.get(id);
    }

    @Override
    public void onEnable() {
        loadCfg();
        saveCfg();
        u = new RAUtil(this, languageSave, language, "react");
        UpdateChecker.init(this, "ReActions", "61726", "me/fromgate/reactions", this.checkUpdates);

        if (!getDataFolder().exists()) getDataFolder().mkdirs();
        l = new RAListener(this);
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(l, this);
        pm.registerEvents(new InventoryMenu(), this);

        instance = this;
        util = u;
        Commander.init(this);
        Timers.init();
        Activators.init();
        Bukkit.getScheduler().runTaskLater(this, new Runnable() {
            @Override
            public void run() {
                FakeCmd.init();
            }
        }, 1);

        PlayerSelectors.init();


        RAEffects.init();
        RARacesAndClasses.init();
        Externals.init();
        RAVault.init();
        RACraftConomy.init();
        RAWorldGuard.init();
        ActionsWaiter.init();
        if (Bukkit.getPluginManager().getPlugin("Towny") != null) towny_conected = RATowny.init();
        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) RAProtocolLib.connectProtocolLib();

        Delayer.load();
        Variables.load();
        Locator.loadLocs();

        SQLManager.init();
        InventoryMenu.init();
        Placeholders.init();


        Bukkit.getLogger().addHandler(new LogHandler());


        try {
            if (Class.forName("org.bukkit.event.player.PlayerInteractAtEntityEvent") != null)
                Bukkit.getPluginManager().registerEvents(new ArmorStandListener(), this);
        } catch (Throwable t) {
        }

        try {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        } catch (IOException e) {
        }


    }


    protected void saveCfg() {
        getConfig().set("general.language", language);
        getConfig().set("general.check-updates", checkUpdates);
        getConfig().set("reactions.save-empty-actions-and-flags-sections", saveEmptySections);
        getConfig().set("reactions.show-messages-for-actions", actionMsg);
        getConfig().set("reactions.center-player-teleport", centerTpCoords);
        getConfig().set("reactions.region-recheck-delay", worlduardRecheck);
        getConfig().set("reactions.item-hold-recheck-delay", itemHoldRecheck);
        getConfig().set("reactions.item-wear-recheck-delay", itemWearRecheck);
        getConfig().set("reactions.horizontal-pushback-action", horizontalPushback);
        getConfig().set("reactions.default-chat-line-length", chatLength);
        getConfig().set("actions.shoot.break-block", Shoot.actionShootBreak);
        getConfig().set("actions.shoot.penetrable", Shoot.actionShootThrough);
        saveConfig();
    }

    public void loadCfg() {
        language = getConfig().getString("general.language", "english");
        checkUpdates = getConfig().getBoolean("general.check-updates", true);
        languageSave = getConfig().getBoolean("general.language-save", false);
        chatLength = getConfig().getInt("reactions.default-chat-line-length", 55);
        saveEmptySections = getConfig().getBoolean("reactions.save-empty-actions-and-flags-sections", false);
        centerTpCoords = getConfig().getBoolean("reactions.center-player-teleport", true);
        actionMsg = getConfig().getString("reactions.show-messages-for-actions", "tp,grpadd,grprmv,townset,townkick,itemrmv,itemgive,moneypay,moneygive");
        worlduardRecheck = getConfig().getInt("reactions.region-recheck-delay", 2);
        itemHoldRecheck = getConfig().getInt("reactions.item-hold-recheck-delay", 2);
        itemWearRecheck = getConfig().getInt("reactions.item-wear-recheck-delay", 2);
        ;
        horizontalPushback = getConfig().getBoolean("reactions.horizontal-pushback-action", false);
        Shoot.actionShootBreak = getConfig().getString("actions.shoot.break-block", Shoot.actionShootBreak);
        Shoot.actionShootThrough = getConfig().getString("actions.shoot.penetrable", Shoot.actionShootThrough);
    }

    public RAUtil getUtils() {
        return this.u;
    }

    public boolean isCenterTpLocation() {
        return this.centerTpCoords;
    }

    public String getActionMsg() {
        return this.actionMsg;
    }

    public boolean saveEmpty() {
        return this.saveEmptySections;
    }

    public int getChatLineLength() {
        return this.chatLength;
    }
}
