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

import com.google.common.base.Joiner;
import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.message.M;
import me.fromgate.reactions.util.playerselector.PlayerSelectors;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class ActionLog extends Action {

    private Logger log = Logger.getLogger("Minecraft");

    @Override
    public boolean execute(Player p, Param params) {
        if (params.hasAnyParam("prefix", "color")) {
            String plg_name = ReActions.getPlugin().getDescription().getName();
            Boolean prefix = params.getParam("prefix", true);
            Boolean color = params.getParam("color", false);
            String message = params.getParam("text", removeParams(params.getParam("param-line")));
            if (message.isEmpty()) return false;
            if (prefix) {
                this.log(message, plg_name, color);
            } else this.log(message, "", color);
        } else M.logMessage(params.getParam("param-line"));

        return true;
    }

    private String removeParams(String message) {
        StringBuilder sb = new StringBuilder("(?i)(");
        sb.append(Joiner.on("|").join(PlayerSelectors.getAllKeys()));
        sb.append("|hide|prefix|color):(\\{.*\\}|\\S+)\\s{0,1}");
        return message.replaceAll(sb.toString(), "");

    }

    public void log(String msg, String prefix, Boolean color) {
        String px = "";
        if (prefix != "") px = "[" + prefix + "] ";
        if (color) log.info(ChatColor.translateAlternateColorCodes('&', px + msg));
        else log.info(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', px + msg)));
    }
}
