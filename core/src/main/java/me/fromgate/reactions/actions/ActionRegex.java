package me.fromgate.reactions.actions;

import com.google.common.base.Joiner;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.playerselector.PlayerSelectors;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by MaxDikiy on 2017-04-29.
 */
public class ActionRegex extends Action {

    @Override
    public boolean execute(Player p, Param params) {
        String prefix = params.getParam("prefix", "");
        String regex = params.getParam("regex", "");
        String input = params.getParam("input", removeParams(params.getParam("param-line")));

        if (input.isEmpty()) return false;

        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(input);
        int count = -1;
        String group = "";

        while (m.find()) {
            count++;
            for (int i = 0; i <= m.groupCount(); i++) {
                if (m.group(i) != null) group = m.group(i);
                else group = "";
                Variables.setTempVar(prefix + "group" + count + "" + i, group);
            }
        }
        return true;
    }

    private String removeParams(String message) {
        StringBuilder sb = new StringBuilder("(?i)(");
        sb.append(Joiner.on("|").join(PlayerSelectors.getAllKeys()));
        sb.append("|hide|regex|prefix):(\\{.*\\}|\\S+)\\s{0,1}");
        return message.replaceAll(sb.toString(), "");

    }

}