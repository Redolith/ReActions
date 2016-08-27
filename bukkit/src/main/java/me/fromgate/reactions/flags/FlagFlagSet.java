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

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FlagFlagSet extends Flag {

    @Override
    public boolean checkFlag(Player p, String param) {
        if (param.isEmpty()) return false; // или true???
        List<String> flagList = parseParamsList(param);
        if (flagList.isEmpty()) return false;
        for (String flagStr : flagList) {
            boolean negative = flagStr.startsWith("!") ? true : false;
            if (negative) flagStr = flagStr.replaceFirst("!", "");
            String[] fnv = flagStr.split(":", 2);
            if (fnv.length != 2) continue;
            if (Flags.checkFlag(p, fnv[0], fnv[1].replaceAll("^\\{", "").replaceAll("\\}$", "").trim(), negative))
                return true;
        }
        return false;
    }


    public List<String> parseParamsList(String param) {
        List<String> paramList = new ArrayList<String>();
        Pattern pattern = Pattern.compile("\\S+:\\{[^\\{\\}]*\\}|\\S+");
        Matcher matcher = pattern.matcher(hideBkts(param));
        while (matcher.find()) {
            paramList.add(matcher.group().trim().replace("#BKT1#", "{").replace("#BKT2#", "}"));
        }
        return paramList;
    }

    private static String hideBkts(String s) {
        int count = 0;
        String r = "";
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            String a = String.valueOf(c);
            if (c == '{') {
                count++;
                if (count != 1) a = "#BKT1#";
            } else if (c == '}') {
                if (count != 1) a = "#BKT2#";
                count--;
            }
            r = r + a;
        }
        return r;
    }
}
