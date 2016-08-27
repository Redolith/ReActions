package me.fromgate.reactions.util.waiter;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.util.ActVal;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class ActionsWaiter {
    //private static Map<String,List<ActVal>> actions = new HashMap<String,List<ActVal>>();

    private static Set<Task> tasks;

    public static void init() {
        tasks = new HashSet<Task>();
        load();
        /*
        Bukkit.getScheduler().runTaskTimer(ReActions.getPlugin(), new Runnable(){
			@Override
			public void run() {
				refresh();
			}
		}, 10, 20); */
    }

    public static void executeDelayed(Player player, ActVal action, boolean isAction, long time) {
        if (action == null) return;
        List<ActVal> actions = new ArrayList<ActVal>();
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
            for (Task t : tasks)
                t.stop();
        }
        tasks.clear();
        YamlConfiguration cfg = new YamlConfiguration();
        File f = new File(ReActions.getPlugin().getDataFolder() + File.separator + "delayed-actions.yml");
        try {
            cfg.load(f);
        } catch (Exception e) {
            ReActions.getUtil().log("Failed to load delayed actions");
            return;
        }
        for (String key : cfg.getKeys(false)) {
            Task t = new Task(cfg, key);
            tasks.add(t);
        }
    }

    public static void refresh() {
        Set<Task> toRemove = new HashSet<Task>();
        if (tasks.isEmpty()) return;
        for (Iterator<Task> it = tasks.iterator(); it.hasNext(); ) {
            Task t = it.next();
            if (t.isTimePassed()) t.execute();
            if (t.isExecuted()) toRemove.add(t);
        }
        if (toRemove.isEmpty()) return;
        for (Task t : toRemove)
            if (tasks.contains(t)) tasks.remove(t);
        save();
    }

    public static void save() {
        YamlConfiguration cfg = new YamlConfiguration();
        File f = new File(ReActions.getPlugin().getDataFolder() + File.separator + "delayed-actions.yml");
        if (f.exists()) f.delete();
        for (Task t : tasks) {
            if (!t.isExecuted()) t.save(cfg);
        }
        try {
            cfg.save(f);
        } catch (Throwable e) {
            ReActions.getUtil().log("Failed to save delayed actions");
        }
    }


}
