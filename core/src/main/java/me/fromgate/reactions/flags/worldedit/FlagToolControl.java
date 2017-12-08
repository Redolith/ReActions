package me.fromgate.reactions.flags.worldedit;

import me.fromgate.reactions.flags.Flag;
import org.bukkit.entity.Player;

import static me.fromgate.reactions.externals.worldedit.RaWorldEdit.isToolControl;

/**
 * Created by MaxDikiy on 11/10/2017.
 */
public class FlagToolControl extends Flag {
    @Override
    public boolean checkFlag(Player player, String param) {
        return Boolean.parseBoolean(param) == isToolControl(player);
    }
}
