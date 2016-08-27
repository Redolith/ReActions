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

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class FlagState extends Flag {

    @Override
    public boolean checkFlag(Player p, String param) {
        Posture pt = Posture.getByName(param);
        if (pt == null) return false;
        switch (pt) {
            case SNEAK:
                return p.isSneaking();
            case FLY:
                return p.isFlying();
            case SPRINT:
                return p.isSprinting();
            case VEHICLE:
                return p.isInsideVehicle();
            case STAND:
                if (p.isSleeping()) return false;
                if (p.isSneaking()) return false;
                if (p.isSprinting()) return false;
                if (p.isFlying()) return false;
                if (p.isInsideVehicle()) return false;
                return true;
            case OP:
                return p.isOp();
            case VEHICLE_BOAT:
                if (!p.isInsideVehicle()) return false;
                return p.getVehicle().getType() == EntityType.BOAT;
            case VEHICLE_HORSE:
                if (!p.isInsideVehicle()) return false;
                return p.getVehicle().getType() == EntityType.HORSE;
            case VEHICLE_MINECART:
                if (!p.isInsideVehicle()) return false;
                return p.getVehicle().getType() == EntityType.MINECART;
            case VEHICLE_PIG:
                if (!p.isInsideVehicle()) return false;
                return p.getVehicle().getType() == EntityType.PIG;
        }
        return false;
    }

    enum Posture {
        SNEAK,
        SPRINT,
        STAND,
        VEHICLE,
        VEHICLE_MINECART,
        VEHICLE_BOAT,
        VEHICLE_PIG,
        VEHICLE_HORSE,
        FLY,
        OP;

        public static Posture getByName(String name) {
            for (Posture pt : Posture.values())
                if (pt.name().equalsIgnoreCase(name)) return pt;
            return null;
        }
    }

}
