/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2017, fromgate, fromgate@gmail.com
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

import me.fromgate.reactions.activators.Activators;
import me.fromgate.reactions.commands.Commander;
import me.fromgate.reactions.externals.Externals;
import me.fromgate.reactions.externals.LogHandler;
import me.fromgate.reactions.externals.RACraftConomy;
import me.fromgate.reactions.externals.RAEffects;
import me.fromgate.reactions.externals.RARacesAndClasses;
import me.fromgate.reactions.externals.RAVault;
import me.fromgate.reactions.externals.RAWorldEdit;
import me.fromgate.reactions.externals.RAWorldGuard;
import me.fromgate.reactions.menu.InventoryMenu;
import me.fromgate.reactions.placeholders.Placeholders;
import me.fromgate.reactions.sql.SQLManager;
import me.fromgate.reactions.timer.Timers;
import me.fromgate.reactions.util.ArmorStandListener;
import me.fromgate.reactions.util.Cfg;
import me.fromgate.reactions.util.Delayer;
import me.fromgate.reactions.util.FakeCmd;
import me.fromgate.reactions.util.Locator;
import me.fromgate.reactions.util.UpdateChecker;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.message.BukkitMessenger;
import me.fromgate.reactions.util.message.M;
import me.fromgate.reactions.util.playerselector.PlayerSelectors;
import me.fromgate.reactions.util.waiter.ActionsWaiter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;


public class ReActions extends JavaPlugin {

    public static ReActions instance;

    public static ReActions getPlugin() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        Cfg.load();
        Cfg.save();
        M.init("ReActions", new BukkitMessenger(this), Cfg.language, false, Cfg.languageSave);
        UpdateChecker.init(this, "ReActions", "61726", "reactions", Cfg.checkUpdates);

        if (!getDataFolder().exists()) getDataFolder().mkdirs();
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new RAListener(this), this);
        pm.registerEvents(new InventoryMenu(), this);

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
        RAWorldEdit.init();
        ActionsWaiter.init();

        Delayer.load();
        if (!Cfg.playerSelfVarFile) Variables.load();
        else Variables.loadVars();
        Locator.loadLocs();

        SQLManager.init();
        InventoryMenu.init();
        Placeholders.init();

        Bukkit.getLogger().addHandler(new LogHandler());


        try {
            if (Class.forName("org.bukkit.event.player.PlayerInteractAtEntityEvent") != null) {
                Bukkit.getPluginManager().registerEvents(new ArmorStandListener(), this);
            }
        } catch (Throwable ignored) {
        }

        try {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        } catch (IOException ignored) {
        }
    }

}
