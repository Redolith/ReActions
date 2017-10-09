package me.fromgate.reactions.flags;

import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.Variables;
import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 9/10/2017.
 */
import static me.fromgate.reactions.externals.RAWorldEdit.getArea;

public class FlagSelectionBlocks extends Flag {
    @Override
    public boolean checkFlag(Player p, String param) {
        int selectionBlocks = getArea(p);
        Variables.setTempVar("selblocks", Integer.toString(selectionBlocks));
        return Util.isInteger(param) && selectionBlocks <= Integer.parseInt(param);
    }
}
