/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2014, fromgate, fromgate@gmail.com
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

package me.fromgate.reactions.flags;

import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

public class FlagBiome extends Flag {

    @Override
    public boolean checkFlag(Player p, String param) {
        Biome b = null;
        for (Biome bb : Biome.values()) {
            if (bb.name().equalsIgnoreCase(param)) b = bb;
        }
        if (b == null) return false;
        return p.getLocation().getBlock().getBiome().equals(b);
    }

}
