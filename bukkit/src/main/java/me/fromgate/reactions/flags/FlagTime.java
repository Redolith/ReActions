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

import me.fromgate.reactions.util.Variables;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class FlagTime extends Flag {

    @Override
    public boolean checkFlag(Player p, String time) {
        saveTempVar(time);
        Long ctime = Bukkit.getWorlds().get(0).getTime();
        if (p != null) ctime = p.getWorld().getTime();

        if (time.equalsIgnoreCase("day")) {
            return ((ctime >= 0) && (ctime < 12000));
        } else if (time.equalsIgnoreCase("night")) {
            return ((ctime >= 12000) && (ctime < 23999));

        } else {
            String[] tln = time.split(",");
            if (tln.length > 0) {
                for (int i = 0; i < tln.length; i++)
                    if (tln[i].matches("[0-9]+")) {
                        int ct = (int) ((ctime / 1000 + 8) % 24);
                        if (ct == Integer.parseInt(tln[i])) return true;
                    }
            }
        }
        return false;
    }

    private void saveTempVar(String time) {
        String result = time;
        if (!(time.equals("day") || time.equals("night"))) {
            String[] ln = time.split(",");
            if (ln.length > 0)
                for (int i = 0; i < ln.length; i++) {
                    if (!u().isInteger(ln[i])) continue;
                    String tmp = String.format("%02d:00", Integer.parseInt(ln[i]));
                    if (i == 0) result = tmp;
                    else result = result + ", " + tmp;
                }
        }
        Variables.setTempVar("TIME", result);
    }
}

