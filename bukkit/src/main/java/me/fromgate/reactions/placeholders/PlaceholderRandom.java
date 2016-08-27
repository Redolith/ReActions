package me.fromgate.reactions.placeholders;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.util.Util;
import org.bukkit.entity.Player;

@PlaceholderDefine(id = "Random", needPlayer = false, keys = {"RANDOM", "rnd"})
public class PlaceholderRandom extends Placeholder {

    @Override
    public String processPlaceholder(Player player, String key, String param) {
        return random(param);
    }


    private String random(String rndStr) {
        if (rndStr.matches("\\d+")) return Integer.toString(ReActions.getUtil().getRandomInt(Integer.parseInt(rndStr)));
        if (rndStr.matches("\\d+\\-\\d+")) return Integer.toString(Util.getMinMaxRandom(rndStr));
        if (rndStr.matches("[\\S,]*[\\S]")) {
            String[] ln = rndStr.split(",");
            if (ln.length == 0) return rndStr;
            return ln[ReActions.getUtil().getRandomInt(ln.length)];
        }
        return rndStr;
    }
}
