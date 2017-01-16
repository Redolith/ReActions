package me.fromgate.reactions.placeholders;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.flags.Flags;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Placeholders {
    private static List<Placeholder> placeholders = new ArrayList<Placeholder>();

    public static void init() {
        add(new PlaceholderPlayer());
        add(new PlaceholderMoney());
        add(new PlaceholderRandom());
        add(new PlaceholderTime());
        add(new PlaceholderCalc());
    }

    public static boolean add(Placeholder ph) {
        if (ph == null) return false;
        if (ph.getKeys().length == 0) return false;
        if (ph.getId().equalsIgnoreCase("UNKNOWN")) return false;
        placeholders.add(ph);
        return true;
    }

    public static Map<String, String> replacePlaceholders(Player p, Param param) {
        Map<String, String> resultMap = new HashMap<String, String>();
        for (String paramKey : param.getMap().keySet()) {
            resultMap.put(paramKey, replacePlaceholders(p, param.getParam(paramKey)));
        }
        return resultMap;
    }

    public static String replacePlaceholders(Player player, String string) {
        String result = string;
        result = Variables.replaceTempVars(result);
        result = Variables.replacePlaceholders(player, result);
        Pattern pattern = Pattern.compile("(%\\w+%)|(%\\w+:\\w+%)|(%\\w+:\\S+%)");
        Matcher matcher = pattern.matcher(result);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String group = new StringBuilder("%").append(replacePlaceholders(player, matcher.group().replaceAll("^%", "").replaceAll("%$", ""))).append("%").toString();
            String replacement = replacePlaceholder(player, group);
            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement == null ? group : replacement));
        }
        matcher.appendTail(sb);
        result = sb.toString();
        if (!string.equals(result)) result = replacePlaceholders(player, result);
        return result;
    }

    private static String replacePlaceholder(Player player, String field) {
        String key = field.replaceAll("^%", "").replaceAll("%$", "");
        String value = "";
        if (field.matches("(%\\w+:\\S+%)")) {
            value = field.replaceAll("^%\\w+:", "").replaceAll("%$", "");
            key = key.replaceAll(Pattern.quote(":" + value) + "$", "");
        }
        for (Placeholder ph : placeholders) {
            if (ph.checkKey(key)) return ph.processPlaceholder(player, key, value);
        }
        return field;
    }

    public static void listPlaceholders(CommandSender sender, int pageNum) {
        List<String> phList = new ArrayList<String>();
        for (Placeholder ph : placeholders) {
            for (String phKey : ph.getKeys()) {
                if (phKey.toLowerCase().equals(phKey)) continue;
                String description = ReActions.util.getMSGnc("placeholder_" + phKey);
                phList.add("&6" + phKey + "&3: &a" + description);
            }
        }
        for (Flags f : Flags.values()) {
            if (f != Flags.TIME && f != Flags.CHANCE) continue;
            String name = f.name();
            String description = ReActions.util.getMSGnc("placeholder_" + name);
            phList.add("&6" + name + "&3: &a" + description);
        }
        phList.add("&6VAR&3: &a" + ReActions.util.getMSGnc("placeholder_VAR"));
        phList.add("&6SIGN_LOC, SIGN_LINE1,.. SIGN_LINE4&3: &a" + ReActions.util.getMSGnc("placeholder_SIGNAct"));
        phList.add("&6ARG0, ARG1, ARG2...&3: &a" + ReActions.util.getMSGnc("placeholder_COMMANDAct"));
        ReActions.util.printPage(sender, phList, pageNum, "msg_placeholderlisttitle", "", false, sender instanceof Player ? 10 : 1000);
    }
}
