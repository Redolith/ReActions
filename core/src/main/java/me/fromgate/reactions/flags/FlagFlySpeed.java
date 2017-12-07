package me.fromgate.reactions.flags;

import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.Variables;
import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 5/4/2017.
 */
public class FlagFlySpeed extends Flag {
    @Override
    public boolean checkFlag(Player player, String param) {
        if (!Util.isInteger(param)) return false;
        long flySpeed = Math.round(player.getFlySpeed() * 10);
        Variables.setTempVar("flyspeed", Integer.toString((int) flySpeed));
        return flySpeed >= Integer.parseInt(param);
    }
}
