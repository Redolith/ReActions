package me.fromgate.reactions.flags;

import org.bukkit.entity.Player;

public class FlagDirection extends Flag {

    @Override
    public boolean checkFlag(Player p, String param) {
        return isPlayerDirected (p,param);
    }

    enum Direction{
        SOUTH,
        SOUTHWEST,
        WEST,
        NORTHWEST,
        NORTH,
        NORTHEAST,
        EAST,
        SOUTHEAST;
    }


    private boolean isPlayerDirected (Player p, String dirstr){
        Direction d1 = getDirectionByName(dirstr);
        if (d1==null) return false;
        Direction d2 = getPlayerDirection (p);
        if (d2==null) return false;
        return (d1 == d2);
    }
    
    
    private Direction getDirectionByName(String dirstr){
        for (Direction d : Direction.values())
        if (d.name().equalsIgnoreCase(dirstr)) return d;
        return null;

    }

    private Direction getPlayerDirection(Player p){
        double angle = (p.getLocation().getYaw() < 0) ? (360 + p.getLocation().getYaw()) : p.getLocation().getYaw();
        int sector = (int) (angle - ((angle + 22.5) % 45.0) + 22.5);
        switch (sector){
        case 1: return Direction.SOUTH;
        case 360: return Direction.SOUTH;
        case 45: return Direction.SOUTHWEST;
        case 90: return Direction.WEST;
        case 135: return Direction.NORTHWEST;
        case 180: return Direction.NORTH;
        case 225: return Direction.NORTHEAST;
        case 270: return Direction.EAST;
        case 315: return Direction.SOUTHEAST;
        }
        return null;
    }



}
