package me.fromgate.reactions.flags;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class FlagGameMode  extends Flag{

    @Override
    public boolean checkFlag(Player p, String param) {
        int g=-1;
        if (u().isInteger(param)) g = Integer.parseInt(param);
        else if (param.equalsIgnoreCase("survival")) g = 0;
        else if (param.equalsIgnoreCase("creative")) g = 1;
        else if (param.equalsIgnoreCase("adventure")) g = 2;
        switch(g){
        case 0:return p.getGameMode()==GameMode.SURVIVAL;
        case 1: return p.getGameMode()==GameMode.CREATIVE;
        case 2: return p.getGameMode()==GameMode.ADVENTURE;
        }
        return false;
    }

}
