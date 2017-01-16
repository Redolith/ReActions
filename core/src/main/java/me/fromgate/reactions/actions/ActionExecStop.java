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

package me.fromgate.reactions.actions;

import me.fromgate.reactions.activators.Activators;
import me.fromgate.reactions.util.Param;
import org.bukkit.entity.Player;

public class ActionExecStop extends Action {

    @Override
    public boolean execute(Player p, Param params) {
        String player = params.getParam("player", (p == null ? "" : p.getName()));
        if (player.isEmpty()) return false;
        String activator = params.getParam("activator", "");
        if (activator.isEmpty()) return false;
        Activators.stopExec(player, activator);
        setMessageParam(activator);
        return true;
    }

}
