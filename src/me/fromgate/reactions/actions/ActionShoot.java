package me.fromgate.reactions.actions;

import java.util.Map;

import me.fromgate.reactions.util.Shoot;

import org.bukkit.entity.Player;

public class ActionShoot extends Action {
	
    @Override
    public boolean execute(Player p, Map<String, String> params) {
        Shoot.shoot(p, params);
        return true;
    }

}
