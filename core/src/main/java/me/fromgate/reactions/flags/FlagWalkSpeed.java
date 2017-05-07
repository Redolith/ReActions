package me.fromgate.reactions.flags;

import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.Variables;
import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 5/4/2017.
 */
public class FlagWalkSpeed extends Flag {
    @Override
    public boolean checkFlag(Player p, String param) {
        if (!Util.isInteger(param)) return false;
        long walkSpeed = Math.round(p.getWalkSpeed() * 10);
        Variables.setTempVar("walkspeed", Integer.toString((int) walkSpeed));
        return walkSpeed >= Integer.parseInt(param);

    }
}
