package me.fromgate.reactions.flags.worldedit;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.Vector;
import me.fromgate.reactions.flags.Flag;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.Variables;
import org.bukkit.entity.Player;

import static me.fromgate.reactions.externals.RAWorldEdit.getArea;
import static me.fromgate.reactions.externals.RAWorldEdit.getMaximumPoint;
import static me.fromgate.reactions.externals.RAWorldEdit.getMinimumPoint;

/**
 * Created by MaxDikiy on 9/10/2017.
 */

public class FlagSelectionBlocks extends Flag {
    @Override
    public boolean checkFlag(Player p, String param) {
        int selectionBlocks = getArea(p);
        try {
            Vector minPoint = getMinimumPoint(p);
            Vector maxPoint = getMaximumPoint(p);
            Variables.setTempVar("minpoint", (minPoint == null) ? "" : minPoint.toString());
            Variables.setTempVar("minX", (minPoint == null) ? "" : Integer.toString(minPoint.getBlockX()));
            Variables.setTempVar("minY", (minPoint == null) ? "" : Integer.toString(minPoint.getBlockY()));
            Variables.setTempVar("minZ", (minPoint == null) ? "" : Integer.toString(minPoint.getBlockZ()));
            Variables.setTempVar("maxpoint", (maxPoint == null) ? "" : maxPoint.toString());
            Variables.setTempVar("maxX", (maxPoint == null) ? "" : Integer.toString(maxPoint.getBlockX()));
            Variables.setTempVar("maxY", (maxPoint == null) ? "" : Integer.toString(maxPoint.getBlockY()));
            Variables.setTempVar("maxZ", (maxPoint == null) ? "" : Integer.toString(maxPoint.getBlockZ()));
        } catch (IncompleteRegionException e) {
            e.printStackTrace();
        }
        Variables.setTempVar("selblocks", Integer.toString(selectionBlocks));
        return Util.isInteger(param) && selectionBlocks <= Integer.parseInt(param);
    }
}
