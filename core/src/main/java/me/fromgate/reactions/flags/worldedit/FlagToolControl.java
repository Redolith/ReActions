package me.fromgate.reactions.flags.worldedit;

import me.fromgate.reactions.flags.Flag;
import org.bukkit.entity.Player;

import static me.fromgate.reactions.externals.RAWorldEdit.isToolControl;

/**
 * Created by MaxDikiy on 11/10/2017.
 */
public class FlagToolControl extends Flag {
    @Override
    public boolean checkFlag(Player p, String param) {
        boolean isTool = isToolControl(p);

        return Boolean.parseBoolean(param) == isTool;
    }
}
