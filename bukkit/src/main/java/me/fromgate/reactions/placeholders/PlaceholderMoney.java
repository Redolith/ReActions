package me.fromgate.reactions.placeholders;

import me.fromgate.reactions.externals.RAEconomics;
import org.bukkit.entity.Player;

import java.util.Map;


@PlaceholderDefine(id = "Money", needPlayer = true, keys = {"MONEY"})
public class PlaceholderMoney extends Placeholder {

    @Override
    public String processPlaceholder(Player player, String key, String param) {
        Map<String, String> params = RAEconomics.getBalances(player);
        return params.containsKey(key) ? params.get(key) : null;
    }

}
