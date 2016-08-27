package me.fromgate.reactions.commands;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.activators.Activator;
import me.fromgate.reactions.activators.Activators;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CmdDefine(command = "react", description = "cmd_check", permission = "reactions.config",
        subCommands = {"check"}, allowConsole = false, shortDescription = "&3/react check [radius]")
public class CmdCheck extends Cmd {

    @Override
    public boolean execute(Player player, String[] args) {
        int radius = args.length > 1 && ReActions.getUtil().isIntegerGZ(args[1]) ? Integer.parseInt(args[1]) : 8;
        printActivatorsAround(player, radius);
        return true;
    }

    public void printActivatorsAround(Player p, int radius) {
        int xx = p.getLocation().getBlockX();
        int yy = p.getLocation().getBlockY();
        int zz = p.getLocation().getBlockZ();
        Set<String> lst = new HashSet<String>();
        for (int x = xx - radius; x <= xx + radius; x++)
            for (int y = yy - radius; y <= yy + radius; y++)
                for (int z = zz - radius; z <= zz + radius; z++) {
                    List<Activator> found = Activators.getActivatorInLocation(p.getWorld(), x, y, z);
                    if (found.isEmpty()) continue;
                    for (int i = 0; i < found.size(); i++)
                        lst.add(found.get(i).toString());
                }
        List<String> plst = new ArrayList<String>();
        plst.addAll(lst);
        ReActions.getUtil().printPage(p, plst, 1, "msg_check", "", true, 100);
    }
}
