package me.fromgate.reactions.util.waiter;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.util.ActVal;
import me.fromgate.reactions.util.message.M;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ActionsWaiter {

    private static Set<Task> tasks;

    private static BukkitTask saveTask;

    public static void init() {
        tasks = Collections.newSetFromMap(new ConcurrentHashMap<Task, Boolean>()); //new HashSet<>();
        saveTask = null;
        load();
    }

    public static void executeDelayed(Player player, ActVal action, boolean isAction, long time) {
        if (action == null) return;
        List<ActVal> actions = new ArrayList<>();
        actions.add(action);
        executeDelayed(player, actions, isAction, time);
    }

    public static void executeDelayed(Player player, List<ActVal> actions, boolean isAction, long time) {
        if (actions.isEmpty()) return;
        String playerStr = player != null ? player.getName() : null;
        Task task = new Task(playerStr, actions, isAction, time);
        tasks.add(task);
        save();
    }

    public static void remove(Task task) {
        if (tasks.contains(task)) tasks.remove(task);
        save();
    }

    public static void load() {
        if (!tasks.isEmpty()) {
            for (Task t : tasks) {
                t.stop();
            }
        }
        tasks.clear();
        YamlConfiguration cfg = new YamlConfiguration();
        File f = new File(ReActions.getPlugin().getDataFolder() + File.separator + "delayed-actions.yml");
        try {
            cfg.load(f);
        } catch (Exception e) {
            M.logMessage("Failed to load delayed actions");
            return;
        }
        for (String key : cfg.getKeys(false)) {
            Task t = new Task(cfg, key);
            tasks.add(t);
        }
    }

    public static void refresh() {
        Set<Task> toRemove = new HashSet<>();
        if (tasks.isEmpty()) return;
        for (Task t : tasks) {
            if (t.isTimePassed()) t.execute();
            if (t.isExecuted()) toRemove.add(t);
        }
        if (toRemove.isEmpty()) return;
        for (Task t : toRemove) {
            if (tasks.contains(t)) tasks.remove(t);
        }
        save();
    }

    public static void save() {
        if (saveTask == null) {
            saveTask = Bukkit.getScheduler().runTaskLater(ReActions.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    YamlConfiguration cfg = new YamlConfiguration();
                    File f = new File(ReActions.getPlugin().getDataFolder() + File.separator + "delayed-actions.yml");
                    if (f.exists()) f.delete();
                    for (Task t : tasks) {
                        if (!t.isExecuted()) t.save(cfg);
                    }
                    try {
                        cfg.save(f);
                    } catch (Throwable e) {
                        M.logMessage("Failed to save delayed actions");
                    }
                    saveTask = null;
                }
            }, 1);
        }
    }
}