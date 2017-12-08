package me.fromgate.reactions.placeholders;

import me.fromgate.reactions.externals.RaPlaceholderAPI;
import me.fromgate.reactions.flags.Flags;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.message.M;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Placeholders {
    private final static Pattern PATTERN_RAW = Pattern.compile("%raw:((%\\w+%)|(%\\w+:\\w+%)|(%\\w+:\\S+%))%");
    private final static Pattern PATTERN_ANY = Pattern.compile("(%\\w+%)|(%\\w+:\\w+%)|(%\\w+:\\S+%)");

    private static List<Placeholder> placeholders = new ArrayList<>();

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

    public static String replacePlaceholderButRaw(Player player, String string) {
        String result = string;
        List<String> raws = new ArrayList<>();
        Matcher matcher = PATTERN_RAW.matcher(result);
        StringBuffer sb = new StringBuffer();
        int count = 0;
        while (matcher.find()) {
            raws.add(matcher.group().replaceAll("(^%raw:)|(%$)", ""));
            matcher.appendReplacement(sb, "~~~[[[RAW" + count + "]]]~~~");
            count++;
        }
        matcher.appendTail(sb);
        result = replacePlaceholders(player, sb.toString());
        if (!raws.isEmpty()) {
            for (int i = 0; i < raws.size(); i++) {
                result = result.replace("~~~[[[RAW" + i + "]]]~~~", raws.get(i));
            }
        }
        return result;
    }

    private static String replacePlaceholders(Player player, String string) {
        String result = string;
        result = Variables.replaceTempVars(result);
        result = Variables.replacePlaceholders(player, result);
        Matcher matcher = PATTERN_ANY.matcher(result);
        StringBuffer sb = new StringBuffer();
        String group;
        String replacement;
        while (matcher.find()) {
            group = "%" +
                    replacePlaceholders(player,
                            matcher.group().replaceAll("(^%)|(%$)", "")) +
                    "%";
            replacement = replacePlaceholder(player, group);
            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement == null ? group : replacement));
        }
        matcher.appendTail(sb);
        result = sb.toString();
        if (!string.equals(result)) result = replacePlaceholders(player, result);
        result = RaPlaceholderAPI.processPlaceholder(player, result);
        return result;
    }

    private final static Pattern PH_W_S = Pattern.compile("(%\\w+:\\S+%)");

    private static String replacePlaceholder(Player player, String field) {
        String key = field.replaceAll("^%", "").replaceAll("%$", "");
        String value = "";
        if (PH_W_S.matcher(field).matches()) {
            value = field.replaceAll("^%\\w+:", "").replaceAll("%$", "");
            key = key.replaceAll(Pattern.quote(":" + value) + "$", "");
        }
        for (Placeholder ph : placeholders) {
            if (ph.checkKey(key)) return ph.processPlaceholder(player, key, value);
        }
        return field;
    }

    public static void listPlaceholders(CommandSender sender, int pageNum) {
        List<String> phList = new ArrayList<>();
        for (Placeholder ph : placeholders) {
            for (String phKey : ph.getKeys()) {
                if (phKey.toLowerCase().equals(phKey)) continue;
                M desc = M.getByName("placeholder_" + phKey);
                if (desc == null) {
                    M.LNG_FAIL_PLACEHOLDER_DESC.log(phKey);
                } else {
                    phList.add("&6" + phKey + "&3: &a" + desc.getText("NOCOLOR"));
                }
            }
        }
        for (Flags f : Flags.values()) {
            if (f != Flags.TIME && f != Flags.CHANCE) continue;
            String name = f.name();
            M desc = M.getByName("placeholder_" + name);
            if (desc == null) {
                M.LNG_FAIL_PLACEHOLDER_DESC.log(name);
            } else {
                phList.add("&6" + name + "&3: &a" + desc.getText("NOCOLOR"));
            }
        }
        phList.add("&6VAR&3: &a" + M.PLACEHOLDER_VAR.getText("NOCOLOR"));
        phList.add("&6SIGN_LOC, SIGN_LINE1,.. SIGN_LINE4&3: &a" + M.PLACEHOLDER_SIGNACT.getText("NOCOLOR"));
        phList.add("&6ARG0, ARG1, ARG2...&3: &a" + M.PLACEHOLDER_COMMANDACT.getText("NOCOLOR"));
        M.printPage(sender, phList, M.MSG_PLACEHOLDERLISTTITLE, pageNum, sender instanceof Player ? 10 : 1000);
    }
}
