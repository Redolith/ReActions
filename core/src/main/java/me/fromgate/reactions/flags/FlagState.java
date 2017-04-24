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
    public boolean checkFlag(Player player, String param) {
        Posture pt = Posture.getByName(param);
        if (pt == null) return false;
        switch (pt) {
            case SNEAK:
                return player.isSneaking();
            case FLY:
                return player.isFlying();
            case SPRINT:
                return player.isSprinting();
            case VEHICLE:
                return player.isInsideVehicle();
            case STAND:
                if (player.isSleeping()) return false;
                if (player.isSneaking()) return false;
                if (player.isSprinting()) return false;
                if (player.isFlying()) return false;
                if (player.isInsideVehicle()) return false;
                return true;
            case OP:
                return player.isOp();
            case VEHICLE_BOAT:
                if (!player.isInsideVehicle()) return false;
                return player.getVehicle().getType() == EntityType.BOAT;
            case VEHICLE_HORSE:
                if (!player.isInsideVehicle()) return false;
                return player.getVehicle().getType() == EntityType.HORSE;
            case VEHICLE_MINECART:
                if (!player.isInsideVehicle()) return false;
                return player.getVehicle().getType() == EntityType.MINECART;
            case VEHICLE_PIG:
                if (!player.isInsideVehicle()) return false;
                return player.getVehicle().getType() == EntityType.PIG;
            case SPECTATOR_TARGET:
                if (player.getSpectatorTarget() != null) return true;
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
        OP,
        SPECTATOR_TARGET;

        public static Posture getByName(String name) {
            for (Posture pt : Posture.values())
                if (pt.name().equalsIgnoreCase(name)) return pt;
            return null;
        }
    }

}
