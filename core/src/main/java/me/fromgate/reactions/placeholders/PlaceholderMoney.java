package me.fromgate.reactions.placeholders;

import me.fromgate.reactions.externals.RaEconomics;
import org.bukkit.entity.Player;

import java.util.Map;


@PlaceholderDefine(id = "Money", needPlayer = true, keys = {"MONEY"})
public class PlaceholderMoney extends Placeholder {

    @Override
    public String processPlaceholder(Player player, String key, String param) {
        Map<String, String> params = RaEconomics.getBalances(player);
        return params.getOrDefault(key, null);
    }

}
