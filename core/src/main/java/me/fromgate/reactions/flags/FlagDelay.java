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

package me.fromgate.reactions.flags;

import me.fromgate.reactions.util.Delayer;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Util;
import org.bukkit.entity.Player;

public class FlagDelay extends Flag {
    boolean globalDelay = true;

    public FlagDelay(boolean globalDelay) {
        this.globalDelay = globalDelay;
    }

    @Override
    public boolean checkFlag(Player player, String param) {
        String playerName = this.globalDelay ? "" : (player != null ? player.getName() : "");
        long updateTime = 0;
        String id = param;

        Param params = new Param(param);
        if (params.isParamsExists("id")) {
            id = params.getParam("id");
            updateTime = Util.parseTime(params.getParam("set-delay", params.getParam("set-time", "0")));
            playerName = params.getParam("player", playerName);
        }
        boolean result = playerName.isEmpty() ? Delayer.checkDelay(id, updateTime) : Delayer.checkPersonalDelay(playerName, id, updateTime);
        Delayer.setTempPlaceholders(playerName, id);
        return result;
    }

}
