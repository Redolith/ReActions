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

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class FlagGameMode extends Flag {

    @Override
    public boolean checkFlag(Player p, String param) {
        int g = -1;
        if (u().isInteger(param)) g = Integer.parseInt(param);
        else if (param.equalsIgnoreCase("survival")) g = 0;
        else if (param.equalsIgnoreCase("creative")) g = 1;
        else if (param.equalsIgnoreCase("adventure")) g = 2;
        switch (g) {
            case 0:
                return p.getGameMode() == GameMode.SURVIVAL;
            case 1:
                return p.getGameMode() == GameMode.CREATIVE;
            case 2:
                return p.getGameMode() == GameMode.ADVENTURE;
        }
        return false;
    }

}
