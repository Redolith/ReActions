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

import me.fromgate.reactions.util.Locator;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.VelocityUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ActionVelocityJump extends Action {

    @Override
    public boolean execute(Player p, Param params) {
        u().SC("&cWarning! VELOCITY_JUMP action is under construction. In next version of plugin it could be changed, renamed or removed!");
        String locStr = params.getParam("loc", "");
        if (locStr.isEmpty()) return false;
        Location loc = Locator.parseCoordinates(locStr);
        if (loc == null) return false;
        int jumpHeight = params.getParam("jump", 5);
        Vector velocity = VelocityUtil.calculateVelocity(p.getLocation(), loc, jumpHeight);
        p.setVelocity(velocity);
        return false;
    }

}
