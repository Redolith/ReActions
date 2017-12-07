package me.fromgate.reactions.flags;

import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 10/1/2017.
 */
public class FlagGreaterLower extends Flag {
    private int flagType = -1;

    public FlagGreaterLower(int flagType) {
        this.flagType = flagType;
    }

    @Override
    public boolean checkFlag(Player p, String param) {
        Param params = new Param(param, "unknown");
        float paramValue = params.getParam("param", 0);
        float value = params.getParam("value", 0);
        if (flagType == 0) {
            Variables.setTempVar("gparam", Double.toString(paramValue));
            if (paramValue > value) return true;
        } else if (flagType == 1) {
            Variables.setTempVar("lparam", Double.toString(paramValue));
            if (paramValue < value) return true;
        }
        return false;
    }
}
