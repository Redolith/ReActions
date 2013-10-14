package me.fromgate.reactions.actions;

import java.util.Map;

import me.fromgate.reactions.util.RAMobSpawn;

import org.bukkit.entity.Player;

public class ActionMobSpawn extends Action {

    @Override
    public boolean execute(Player p, Map<String, String> params) {
        RAMobSpawn.mobSpawn(p, params);
        return true;
    }

}
