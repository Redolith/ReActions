package me.fromgate.reactions.flags;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class FlagTime extends Flag{

    @Override
    public boolean checkFlag(Player p, String time) {
        Long ctime = Bukkit.getWorlds().get(0).getTime();
        if (p!=null) ctime = p.getWorld().getTime();
        
        if (time.equalsIgnoreCase("day")) {
            return ((ctime>=0)&&(ctime<12000));
        } else if (time.equalsIgnoreCase("night")) {
            return ((ctime>=12000)&&(ctime<23999));

        } else {
            String [] tln = time.split(","); 
            if (tln.length>0){
                for (int i = 0; i<tln.length; i++)
                    if (tln[i].matches("[0-9]+")){
                        int ct = (int) ((ctime / 1000 + 8) % 24);
                        if (ct == Integer.parseInt(tln[i])) return true;
                    }
            }
        }
        return false;

    }

}
