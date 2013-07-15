/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2013, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/reactions/
 *   * 
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
package me.fromgate.reactions;

import me.fromgate.reactions.activators.Activator;
import me.fromgate.reactions.activators.Activator.FlagVal;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Flag {

    static ReActions plg;
    static RAUtil u;

    public static void init(ReActions plugin){
        plg = plugin;
        u = plg.u;
    }


    static String ftypes = "group,perm,time,item,invitem,town,money,chance,pvp,online,delay,pdelay";

    public static boolean checkFlag (Player p, String flag, String param, boolean not){
        boolean chr = false;
        if (flag.equalsIgnoreCase("group")) chr= (plg.vault_perm&&plg.permission.playerInGroup(p, param));
        else if (flag.equalsIgnoreCase("perm")) chr=p.hasPermission(param);
        else if (flag.equalsIgnoreCase("time")) chr=checkTime(p, param);
        else if (flag.equalsIgnoreCase("item")) chr=checkItem (p, param);
        else if (flag.equalsIgnoreCase("invitem")) chr=checkInventoryItem (p, param);
        else if (flag.equalsIgnoreCase("town")) chr=playerInTown(p, param);
        else if (flag.equalsIgnoreCase("money")) chr=playerHasMoney (p, param);
        else if (flag.equalsIgnoreCase("chance")) chr=rollDice (p, param);
        else if (flag.equalsIgnoreCase("pvp")) chr=checkPlayerInPVP(p,param);
        else if (flag.equalsIgnoreCase("online")) chr=checkServerOnline(p,param);
        else if (flag.equalsIgnoreCase("delay")) chr=checkDelay(p,param);
        else if (flag.equalsIgnoreCase("pdelay")) chr=checkPersonalDelay(p,param);
        if (not) chr= !chr;
        return chr;
    }



    private static boolean checkPersonalDelay(Player p, String param) {
        return RAFlagDelay.checkPersonalDelay(p,param);
    }


    private static boolean checkDelay(Player p, String param) {
        return RAFlagDelay.checkDelay(param);
    }


    // item <id>:<count>*<data>
    public static boolean checkItem(Player p, String item){
        return u.compareItemStr(p.getItemInHand(), item);
    }

    private static boolean checkInventoryItem(Player p, String item) {
        return (u.parseItemStack(item).getAmount()<=u.countItemInInventory(p, item));
    }


    public static boolean checkTime(Player p, String time){
        Long ctime = p.getWorld().getTime();
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

    private static boolean rollDice (Player p, String param){
        int d = 50;
        if ((!param.isEmpty())&&(param.matches("[1-9]+[0-9]*"))) d = Integer.parseInt(param);
        d = Math.max(Math.min(d, 100), 0);
        return u.rollDiceChance(d);
    }
    private static boolean playerInTown (Player p, String param){
        if (!plg.towny_conected) return false; 
        return plg.towny.playerInTown(p, param);
    }

    private static boolean playerHasMoney (Player p, String amountstr){
        return plg.vault_eco&&(amountstr.matches("[0-9]*")&&(Integer.parseInt(amountstr)<=plg.economy.getBalance(p.getName()))); 
    }

    public static boolean checkFlags (Player p, Activator c){
        return plg.debug.checkFlagAndDebug(p, checkAllFlags (p, c));
    }

    public static boolean checkAllFlags (Player p, Activator c){
        if (c.getFlags().size()>0)
            for (int i = 0; i<c.getFlags().size();i++){
                FlagVal f = c.getFlags().get(i);
                if (!checkFlag (p, f.flag, f.value, f.not)) return false;
            }
        return true;
    }

    private static boolean checkServerOnline(Player p, String param) {
        if (!param.matches("[1-9]+[0-9]*")) return false;
        int reqplayer = Integer.parseInt(param);
        return (reqplayer<=Bukkit.getOnlinePlayers().length);
    }


    private static boolean checkPlayerInPVP(Player p, String param) {
        if (!p.hasMetadata("reactions-pvp-time")) return false;
        if (!param.matches("[1-9]+[0-9]*")) return false;
        Long delay = Long.parseLong(param)*1000;
        Long curtime = System.currentTimeMillis();
        Long pvptime = p.getMetadata("reactions-pvp-time").get(0).asLong();
        return ((curtime-pvptime)<delay);
    }

}
