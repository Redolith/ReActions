/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2015, fromgate, fromgate@gmail.com
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

package me.fromgate.reactions;

import me.fromgate.reactions.util.message.M;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RAUtil {
    Random random = new Random();

    /* Функция проверяет входит ли число (int)
     * в список чисел представленных в виде строки вида n1,n2,n3,...nN
     */
    public boolean isIdInList(int id, String str) {
        if (!str.isEmpty()) {
            String[] ln = str.split(",");
            if (ln.length > 0)
                for (int i = 0; i < ln.length; i++)
                    if ((!ln[i].isEmpty()) && ln[i].matches("[0-9]*") && (Integer.parseInt(ln[i]) == id)) return true;
        }
        return false;
    }

    /*
     * Функция проверяет входит ли слово (String) в список слов
     * представленных в виде строки вида n1,n2,n3,...nN
     */
    public boolean isWordInList(String word, String str) {
        String[] ln = str.split(",");
        if (ln.length > 0)
            for (int i = 0; i < ln.length; i++)
                if (ln[i].equalsIgnoreCase(word)) return true;
        return false;
    }

    /*
     * Функция проверяет входит есть ли item (блок) с заданным id и data в списке,
     * представленным в виде строки вида id1:data1,id2:data2,MATERIAL_NAME:data
     * При этом если data может быть опущена
     */
    public boolean isItemInList(int id, int data, String str) {
        String[] ln = str.split(",");
        if (ln.length > 0)
            for (int i = 0; i < ln.length; i++)
                if (compareItemStr(id, data, ln[i])) return true;
        return false;
    }

    @Deprecated
    public boolean compareItemStr(int item_id, int item_data, String itemstr) {
        return compareItemStrIgnoreName(item_id, item_data, 1, itemstr);
    }


    public boolean compareItemStrIgnoreName(int item_id, int item_data, int item_amount, String itemstr) {
        if (!itemstr.isEmpty()) {
            int id = -1;
            int amount = 1;
            int data = -1;
            String[] si = itemstr.split("\\*");
            if (si.length > 0) {
                if ((si.length == 2) && si[1].matches("[1-9]+[0-9]*")) amount = Integer.parseInt(si[1]);
                String ti[] = si[0].split(":");
                if (ti.length > 0) {
                    if (ti[0].matches("[0-9]*")) id = Integer.parseInt(ti[0]);
                    else {
                        try {
                            id = Material.getMaterial(ti[0]).getId();
                        } catch (Exception e) {
                            M.logOnce("unknownitem" + ti[0], "Unknown item: " + ti[0]);
                        }
                    }
                    if ((ti.length == 2) && (ti[1]).matches("[0-9]*")) data = Integer.parseInt(ti[1]);
                    return ((item_id == id) && ((data < 0) || (item_data == data)) && (item_amount >= amount));
                }
            }
        }
        return false;
    }

    public boolean rollDiceChance(int chance) {
        return (random.nextInt(100) < chance);
    }

    public int tryChance(int chance) {
        return random.nextInt(chance);
    }


    public int getRandomInt(int maxvalue) {
        return random.nextInt(maxvalue);
    }

    public boolean isIntegerSigned(String... str) {
        if (str.length == 0) return false;
        for (String s : str)
            if (!s.matches("-?[0-9]+[0-9]*")) return false;
        return true;
    }

    public boolean isInteger(String str) {
        return (str.matches("[0-9]+[0-9]*"));
    }

    public boolean isInteger(String... str) {
        if (str.length == 0) return false;
        for (String s : str)
            if (!s.matches("[0-9]+[0-9]*")) return false;
        return true;
    }


    public boolean isIntegerGZ(String str) {
        return (str.matches("[1-9]+[0-9]*"));
    }

    public boolean isIntegerGZ(String... str) {
        if (str.length == 0) return false;
        for (String s : str)
            if (!s.matches("[1-9]+[0-9]*")) return false;
        return true;
    }

    public int getMinMaxRandom(String minmaxstr) {
        int min = 0;
        int max = 0;
        String strmin = minmaxstr;
        String strmax = minmaxstr;
        if (minmaxstr.contains("-")) {
            strmin = minmaxstr.substring(0, minmaxstr.indexOf("-"));
            strmax = minmaxstr.substring(minmaxstr.indexOf("-") + 1);
        }
        if (strmin.matches("[1-9]+[0-9]*")) min = Integer.parseInt(strmin);
        max = min;
        if (strmax.matches("[1-9]+[0-9]*")) max = Integer.parseInt(strmax);
        if (max > min) return min + tryChance(1 + max - min);
        else return min;
    }

    public Long timeToTicks(Long time) {
        //1000 ms = 20 ticks
        return Math.max(1, (time / 50));
    }

    public Long parseTime(String time) {
        int dd = 0; // дни
        int hh = 0; // часы
        int mm = 0; // минуты
        int ss = 0; // секунды
        int tt = 0; // тики
        int ms = 0; // миллисекунды
        if (isInteger(time)) {
            ss = Integer.parseInt(time);
        } else if (time.matches("^[0-5][0-9]:[0-5][0-9]$")) {
            String[] ln = time.split(":");
            if (isInteger(ln[0])) mm = Integer.parseInt(ln[0]);
            if (isInteger(ln[1])) ss = Integer.parseInt(ln[1]);
        } else if (time.matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$")) {
            String[] ln = time.split(":");
            if (isInteger(ln[0])) hh = Integer.parseInt(ln[0]);
            if (isInteger(ln[1])) mm = Integer.parseInt(ln[1]);
            if (isInteger(ln[2])) ss = Integer.parseInt(ln[2]);
        } else {
            Pattern pattern = Pattern.compile("\\d+(ms|d|h|m|s)");
            Matcher matcher = pattern.matcher(time);
            while (matcher.find()) {
                String foundTime = matcher.group();
                if (foundTime.matches("^\\d+ms")) {
                    ms = Integer.parseInt(time.replace("ms", ""));
                } else if (foundTime.matches("^\\d+d")) {
                    dd = Integer.parseInt(time.replace("d", ""));
                } else if (foundTime.matches("^\\d+h")) {
                    hh = Integer.parseInt(time.replace("h", ""));
                } else if (foundTime.matches("^\\d+m$")) {
                    mm = Integer.parseInt(time.replace("m", ""));
                } else if (foundTime.matches("^\\d+s$")) {
                    ss = Integer.parseInt(time.replace("s", ""));
                } else if (foundTime.matches("^\\d+t$")) {
                    tt = Integer.parseInt(time.replace("t", ""));
                }
            }
        }
        return (dd * 86400000L) + (hh * 3600000L) + (mm * 60000L) + (ss * 1000L) + (tt * 50L) + ms;
    }

    public String itemToString(ItemStack item) {
        String str = "";
        String n = item.getItemMeta().hasDisplayName() ? ChatColor.stripColor(item.getItemMeta().getDisplayName()) : item.getType().name();
        String a = item.getAmount() > 1 ? "*" + item.getAmount() : "";
        str = n + a;
        return str;
    }

    public int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE) return Integer.MIN_VALUE;
        if (l > Integer.MAX_VALUE) return Integer.MAX_VALUE;
        return (int) l;
    }


}
