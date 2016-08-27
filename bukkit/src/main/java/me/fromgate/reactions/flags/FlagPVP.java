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

import me.fromgate.reactions.util.Param;
import org.bukkit.entity.Player;

public class FlagPVP extends Flag {

    @Override
    public boolean checkFlag(Player p, String param) {
        if (!p.hasMetadata("reactions-pvp-time")) return false;
        Param params = new Param(param, "time");
        String timeStr = params.getParam("time");
        Long delay = u().parseTime(timeStr);
        if (delay == 0) return false;
        Long curtime = System.currentTimeMillis();
        Long pvptime = p.getMetadata("reactions-pvp-time").get(0).asLong();
        return ((curtime - pvptime) < delay);
    }

}
