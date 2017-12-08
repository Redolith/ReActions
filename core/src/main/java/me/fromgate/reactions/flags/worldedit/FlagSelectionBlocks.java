/*
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2017, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/reactions/
 *
 *  This file is part of ReActions.
 *
 *  ReActions is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ReActions is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with ReActions.  If not, see <http://www.gnorg/licenses/>.
 *
 */

package me.fromgate.reactions.flags.worldedit;

import me.fromgate.reactions.externals.worldedit.RaWorldEdit;
import me.fromgate.reactions.flags.Flag;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.Variables;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class FlagSelectionBlocks extends Flag {
    @Override
    public boolean checkFlag(Player player, String param) {
        int selectionBlocks = RaWorldEdit.getArea(player);
        Vector minPoint = RaWorldEdit.getMinimumPoint(player);
        Vector maxPoint = RaWorldEdit.getMaximumPoint(player);
        Variables.setTempVar("minpoint", (minPoint == null) ? "" : minPoint.toString());
        Variables.setTempVar("minX", (minPoint == null) ? "" : Integer.toString(minPoint.getBlockX()));
        Variables.setTempVar("minY", (minPoint == null) ? "" : Integer.toString(minPoint.getBlockY()));
        Variables.setTempVar("minZ", (minPoint == null) ? "" : Integer.toString(minPoint.getBlockZ()));
        Variables.setTempVar("maxpoint", (maxPoint == null) ? "" : maxPoint.toString());
        Variables.setTempVar("maxX", (maxPoint == null) ? "" : Integer.toString(maxPoint.getBlockX()));
        Variables.setTempVar("maxY", (maxPoint == null) ? "" : Integer.toString(maxPoint.getBlockY()));
        Variables.setTempVar("maxZ", (maxPoint == null) ? "" : Integer.toString(maxPoint.getBlockZ()));
        Variables.setTempVar("selblocks", Integer.toString(selectionBlocks));
        return Util.isInteger(param) && selectionBlocks <= Integer.parseInt(param);
    }
}
