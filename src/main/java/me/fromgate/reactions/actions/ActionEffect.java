package me.fromgate.reactions.actions;

import java.util.Map;

import me.fromgate.reactions.util.RAEffects;

import org.bukkit.entity.Player;

public class ActionEffect extends Action {

    @Override
    public boolean execute(Player p, Map<String, String> params) {
        RAEffects.playEffect(p, params);
        return true;
    }

}
