package me.fromgate.reactions.util.waiter;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.util.ActVal;
import me.fromgate.reactions.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Task implements Runnable {
    String taskId;
    String playerName;
    List<ActVal> actions;
    boolean isAction;
    boolean isExecuted;
    long executionTime;
    BukkitTask task;

    public Task(String playerName, List<ActVal> actions, boolean isAction, long time) {
        this.taskId = UUID.randomUUID().toString();
        this.playerName = playerName;
        this.actions = actions;
        this.isAction = isAction;
        this.isExecuted = false;
        this.executionTime = System.currentTimeMillis() + time;
        task = Bukkit.getScheduler().runTaskLater(ReActions.getPlugin(), this, ReActions.getUtil().timeToTicks(time));
    }

    public String getId() {
        return this.taskId;
    }

    public Task(YamlConfiguration cfg, String taskId) {
        this.taskId = taskId;
        this.load(cfg, taskId);
        long time = this.executionTime - System.currentTimeMillis();
        if (time < 0) this.execute();
        else
            task = Bukkit.getScheduler().runTaskLater(ReActions.getPlugin(), this, ReActions.getUtil().timeToTicks(time));
    }

    @Override
    public void run() {
        execute();
    }

    public void execute() {
        if (this.isExecuted()) return;
        @SuppressWarnings("deprecation")
        Player p = playerName == null ? null : Bukkit.getPlayerExact(playerName);
        if (p == null && playerName != null) return;
        Actions.executeActions(p, actions, isAction);
        this.isExecuted = true;
        ActionsWaiter.remove(this);
        /*
        Bukkit.getScheduler().runTaskLater(ReActions.getPlugin(), new Runnable(){
			@Override
			public void run() {
				ActionsWaiter.remove(Task.this);
			}
		}, 1); */
    }

    public void stop() {
        this.task.cancel();
        this.task = null;
    }

    public long getExecutionTime() {
        return this.executionTime;
    }

    public boolean isTimePassed() {
        return this.executionTime < System.currentTimeMillis();
    }

    public boolean isExecuted() {
        return this.isExecuted;
    }

    public void save(YamlConfiguration cfg) {
        cfg.set(Util.join(this.taskId, ".player"), this.playerName == null ? "" : this.playerName);
        cfg.set(Util.join(this.taskId, ".execution-time"), this.executionTime);
        cfg.set(Util.join(this.taskId, ".actions.action"), this.isAction);
        List<String> actionList = new ArrayList<String>();
        for (ActVal a : this.actions) {
            actionList.add(a.toString());
        }
        cfg.set(Util.join(this.taskId, ".actions.list"), actionList);
    }

    public void load(YamlConfiguration cfg, String root) {
        this.playerName = cfg.getString(Util.join(root, ".player"));
        this.executionTime = cfg.getLong(Util.join(root, ".execution-time"), 0);
        this.isAction = cfg.getBoolean(Util.join(root, ".actions.action"), true);
        List<String> actionList = cfg.getStringList(Util.join(root, ".actions.list"));
        this.actions = new ArrayList<ActVal>();
        if (actionList != null)
            for (String a : actionList) {
                if (a.contains("=")) {
                    String av = a.substring(0, a.indexOf("="));
                    String vv = a.substring(a.indexOf("=") + 1, a.length());
                    this.actions.add(new ActVal(av, vv));
                }
            }
    }

}
