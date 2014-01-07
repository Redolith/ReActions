package me.fromgate.reactions.actions;

import java.util.Map;

import me.fromgate.reactions.externals.RATowny;

import org.bukkit.entity.Player;

public class ActionTownKick extends Action {

    @Override
    public boolean execute(Player p, Map<String, String> params) {
        if (!plg().isTownyConnected()) return false;
        RATowny.kickFromTown (p);
        return true;
    }

}
