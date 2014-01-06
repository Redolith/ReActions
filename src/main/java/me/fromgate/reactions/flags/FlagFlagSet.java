package me.fromgate.reactions.flags;

import org.bukkit.entity.Player;

public class FlagFlagSet extends Flag {

    @Override
    public boolean checkFlag(Player p, String param) {
        if (param.isEmpty()) return false; // или true???
        String [] flagsStr = param.split(" ");
        for (String flagStr : flagsStr ){
            boolean negative = flagStr.startsWith("!") ? true : false;
            if (negative) flagStr = flagStr.replaceFirst("!", "");
            String [] fnv = flagStr.split(":",2);
            if (fnv.length!=2) continue;
            if (Flags.checkFlag(p, fnv[0], fnv[1], negative)) return true;
        }
        return false;
    }



}
