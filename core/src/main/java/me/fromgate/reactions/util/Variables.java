/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2017, fromgate, fromgate@gmail.com
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

package me.fromgate.reactions.util;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.event.EventManager;
import me.fromgate.reactions.util.message.M;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Variables {

    private static Map<String, String> vars = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private static Map<String, String> tempvars = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    private final static Pattern VARP = Pattern.compile("(?i).*%varp?:\\S+%.*");
    private final static Pattern FLOAT_0 = Pattern.compile("^[0-9]+\\.0$");
    private final static Pattern NUMBER = Pattern.compile("-?[0-9]+(.[0-9]+)?");

    private static String varId(Player player, String var) {
        return (player == null ? "general." + var : player.getName() + "." + var);
    }

    private static String varId(String player, String var) {
        return (player.isEmpty() ? "general." + var : player + "." + var);
    }

    public static void setVar(String player, String var, String value) {
        String prevVal = Variables.getVar(player, var, "");
        vars.put(varId(player, var), value);
        if (!Cfg.playerSelfVarFile) save();
        else save(player);
        EventManager.raiseVariableEvent(var, player, value, prevVal);
    }

    public static void setVar(Player player, String var, String value) {
        String prevVal = Variables.getVar(player, var, "");
        vars.put(varId(player, var), value);
        if (!Cfg.playerSelfVarFile) save();
        else save(player.getName());
        EventManager.raiseVariableEvent(var, player == null ? "" : player.getName(), value, prevVal);
    }

    public static void clearVar(Player player, String var) {
        String prevVal = Variables.getVar(player, var, "");
        String id = varId(player, var);
        if (vars.containsKey(id)) vars.remove(id);
        if (!Cfg.playerSelfVarFile) save();
        else save(player.getName());
        EventManager.raiseVariableEvent(var, player == null ? "" : player.getName(), "", prevVal);
    }

    public static boolean clearVar(String player, String var) {
        String prevVal = Variables.getVar(player, var, "");
        String id = varId(player, var);
        if (!vars.containsKey(id)) return false;
        vars.remove(id);
        if (!Cfg.playerSelfVarFile) save();
        else save(player);
        EventManager.raiseVariableEvent(var, player, "", prevVal);
        return true;
    }


    public static String getVar(String player, String var, String defvar) {
        String id = varId(player, var);
        if (vars.containsKey(id)) return vars.get(id);
        return defvar;
    }

    public static String getVar(Player player, String var, String defvar) {
        String id = varId(player, var);
        if (vars.containsKey(id)) return vars.get(id);
        return defvar;
    }

    public static boolean cmpVar(String playerName, String var, String cmpvalue) {
        String id = varId(playerName, var);
        if (!vars.containsKey(id)) return false;
        String value = getVar(playerName, var, "");
        if (isNumber(cmpvalue, value)) return (Double.parseDouble(cmpvalue) == Double.parseDouble(value));
        return value.equalsIgnoreCase(cmpvalue);
    }

    public static boolean cmpGreaterVar(String playerName, String var, String cmpvalue) {
        String id = varId(playerName, var);
        if (!vars.containsKey(id)) return false;
        if (!isNumber(vars.get(id), cmpvalue)) return false;
        return Double.parseDouble(vars.get(id)) > Double.parseDouble(cmpvalue);
    }

    public static boolean cmpLowerVar(String playerName, String var, String cmpvalue) {
        String id = varId(playerName, var);
        if (!vars.containsKey(id)) return false;
        if (!isNumber(vars.get(id), cmpvalue)) return false;
        return Double.parseDouble(vars.get(id)) < Double.parseDouble(cmpvalue);
    }

    public static boolean existVar(String playerName, String var) {
        return (vars.containsKey(varId(playerName, var)));
    }

    public static boolean incVar(Player player, String var) {
        return incVar(player, var, 1);
    }

    public static boolean incVar(String player, String var) {
        return incVar(player, var, 1);
    }

    public static boolean decVar(Player player, String var) {
        return incVar(player, var, -1);
    }

    public static boolean decVar(String player, String var) {
        return incVar(player, var, -1);
    }

    public static boolean incVar(Player player, String var, double addValue) {
        return incVar(player == null ? "" : player.getName(), var, addValue);
    }

    public static boolean incVar(String player, String var, double addValue) {
        String id = varId(player, var);
        if (!vars.containsKey(id)) setVar(player, var, "0");
        String valueStr = vars.get(id);
        if (!isNumber(valueStr)) return false;
        setVar(player, var, String.valueOf(Double.parseDouble(valueStr) + addValue));
        return true;
    }


    public static boolean decVar(String player, String var, double decValue) {
        return incVar(player, var, decValue * (-1));
    }

    public static boolean decVar(Player player, String var, double decValue) {
        return incVar(player, var, decValue * (-1));
    }

    public static boolean mergeVar(Player player, String var, String stringToMerge, boolean spaceDivider) {
        String space = spaceDivider ? " " : "";
        String id = varId(player, var);
        if (!vars.containsKey(id)) setVar(player, var, "");
        setVar(player, var, getVar(player, var, "") + space + stringToMerge);
        return false;
    }


    public static void save() {
        try {
            YamlConfiguration cfg = new YamlConfiguration();
            File f = new File(ReActions.instance.getDataFolder() + File.separator + "variables.yml");
            if (f.exists()) f.delete();
            f.createNewFile();
            for (String key : vars.keySet())
                cfg.set(key, vars.get(key));
            cfg.save(f);
        } catch (Exception ignored) {
        }
    }

    public static void save(String player) {
        try {
            YamlConfiguration cfg = new YamlConfiguration();
            String varDir = ReActions.instance.getDataFolder() + File.separator + "variables";
            File dir = new File(varDir);
            if (!dir.exists() && !dir.mkdirs()) return;
            saveGeneral();
            if (player == null || player.isEmpty()) return;
            UUID id = Util.getUUID(player);
            if (id == null) return;
            File f = new File(varDir + File.separator + id.toString() + ".yml");
            if (f.exists()) f.delete();
            f.createNewFile();
            for (String key : vars.keySet()) {
                if (key.contains(player)) cfg.set(key, vars.get(key));
            }
            cfg.save(f);
            removePlayerVars(player);
        } catch (Exception ignored) {
        }
    }

    private static void saveGeneral() {
        try {
            YamlConfiguration cfg = new YamlConfiguration();
            String varDir = ReActions.instance.getDataFolder() + File.separator + "variables";
            File f = new File(varDir + File.separator + "general.yml");
            if (f.exists()) f.delete();
            f.createNewFile();
            for (String key : vars.keySet()) {
                if (key.contains("general")) cfg.set(key, vars.get(key));
            }
            cfg.save(f);
        } catch (Exception ignored) {
        }
    }

    public static void load() {
        vars.clear();
        try {
            YamlConfiguration cfg = new YamlConfiguration();
            File f = new File(ReActions.instance.getDataFolder() + File.separator + "variables.yml");
            if (!f.exists()) return;
            cfg.load(f);
            for (String key : cfg.getKeys(true)) {
                if (!key.contains(".")) continue;
                vars.put(key, cfg.getString(key));
            }
            if (!Cfg.playerSelfVarFile) {
                loadVars();
                File dir = new File(ReActions.instance.getDataFolder() + File.separator + "variables");
                if (!dir.exists() || !dir.isDirectory()) return;
                String[] files = dir.list();
                for (int i = 0; i < files.length; i++) {
                    File fl = new File(dir, files[i]);
                    fl.delete();
                }
                dir.delete();
            }
        } catch (Exception ignored) {
        }
    }

    public static void loadVars() {
        if (Cfg.playerSelfVarFile) load();
        try {
            YamlConfiguration cfg = new YamlConfiguration();
            File dir = new File(ReActions.instance.getDataFolder() + File.separator + "variables");
            if (!dir.exists()) return;
            for (File f : dir.listFiles()) {
                if (!f.isDirectory()) {
                    String fstr = f.getName();
                    if (fstr.endsWith(".yml")) {
                        cfg.load(f);
                        for (String key : cfg.getKeys(true)) {
                            if (!key.contains(".")) continue;
                            vars.put(key, cfg.getString(key));
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    private static void removePlayerVars(String player) {
        try {
            Map<String, String> vars_tmp = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            YamlConfiguration cfg = new YamlConfiguration();
            String fileName = ReActions.instance.getDataFolder() + File.separator + "variables.yml";
            File f = new File(fileName);
            if (!f.exists()) return;
            cfg.load(f);
            for (String key : cfg.getKeys(true)) {
                if (!key.contains(".")) continue;
                if (key.contains(player) || key.contains("general")) continue;
                vars_tmp.put(key, cfg.getString(key));
            }

            YamlConfiguration cfg2 = new YamlConfiguration();
            if (f.delete() && f.createNewFile()) {
                for (String key : vars_tmp.keySet())
                    cfg2.set(key, vars_tmp.get(key));
                cfg2.save(f);
                vars_tmp.clear();
            }
        } catch (Exception ignored) {
        }
    }


    public static String replacePlaceholders(Player player, String str) {
        if (!VARP.matcher(str).matches()) return str;

        String newStr = str;
        for (String key : vars.keySet()) {
            String replacement = vars.get(key);
            replacement = FLOAT_0.matcher(replacement).matches() ? Integer.toString((int) Double.parseDouble(replacement)) : Matcher.quoteReplacement(replacement);
            if (key.startsWith("general.")) {
                String id = id = key.substring(8); // key.replaceFirst("general\\.", "");
                newStr = newStr.replaceAll(new StringBuilder("(?i)%var:").append(Pattern.quote(id)).append("%").toString(), replacement);
            } else {
                if (player != null && key.matches(Util.join("(?i)^", player.getName(), "\\..*"))) {
                    String id = key.replaceAll(Util.join("(?i)^", player.getName(), "\\."), "");
                    newStr = newStr.replaceAll(new StringBuilder("(?i)%varp:").append(Pattern.quote(id)).append("%").toString(), replacement);
                }
                newStr = newStr.replaceAll(new StringBuilder("(?i)%varp?:").append(Pattern.quote(key)).append("%").toString(), replacement);
            }
        }
        return newStr;
    }

	/*
     *  Temporary variables - replacement for place holders
	 */

    public static String replaceTempVars(String str) {
        if (str.isEmpty()) return str;
        String newStr = str;
        for (String key : tempvars.keySet()) {
            String replacement = tempvars.get(key);
            replacement = FLOAT_0.matcher(replacement).matches() ? Integer.toString((int) Double.parseDouble(replacement)) : Matcher.quoteReplacement(replacement);
            newStr = newStr.replaceAll("(?i)%" + key + "%", replacement);
        }
        return newStr;
    }

    public static void setTempVars(Param params) {
        if (params == null || params.isEmpty()) return;
        for (String key : params.keySet()) {
            setTempVar(key, params.getParam(key));
        }
    }

    public static void setTempVar(String varId, String value) {
        tempvars.put(varId, value);
    }

    public static void clearTempVar(String varId) {
        if (tempvars.containsKey(varId)) vars.remove(varId);
    }

    public static void clearAllTempVar() {
        tempvars.clear();
    }

    public static String getTempVar(String varId) {
        return getTempVar(varId, "");
    }

    public static String getTempVar(String varId, String defvar) {
        if (tempvars.containsKey(varId)) return tempvars.get(varId);
        return defvar;
    }

    public static void printList(CommandSender sender, int pageNum, String mask) {
        int linesPerPage = (sender instanceof Player) ? 15 : 10000;
        List<String> varList = new ArrayList<>();
        for (String key : vars.keySet()) {
            if (mask.isEmpty() || key.contains(mask)) {
                varList.add(key + " : " + vars.get(key));
            }
        }
        M.printPage(sender, varList, M.MSG_VARLIST, pageNum, linesPerPage);
    }

    public static boolean isNumber(String... str) {
        if (str.length == 0) return false;
        for (String s : str)
            if (!NUMBER.matcher(s).matches()) return false;
        return true;
    }

    public static boolean matchVar(String playerName, String var, String value) {
        String id = varId(playerName, var);
        if (!vars.containsKey(id)) return false;
        String varValue = vars.get(id);
        return varValue.matches(value);
    }

    public static Param getTempVars() {
        return new Param(tempvars);
    }
}
