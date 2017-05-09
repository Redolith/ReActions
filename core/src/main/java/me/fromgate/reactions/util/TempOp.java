package me.fromgate.reactions.util;


import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;


public class TempOp {

    private static Set<String> tempOps = new HashSet<>();

    public static void setTempOp(CommandSender sender) {
        if (sender instanceof Player) {
            if (sender.isOp()) return;
            tempOps.add(sender.getName());
            sender.setOp(true);
        }
    }

    public static void removeTempOp(CommandSender sender) {
        if (sender instanceof Player) {
            if (!tempOps.contains(sender.getName())) return;
            tempOps.remove(sender.getName());
            sender.setOp(false);
        }
    }

}
