package me.fromgate.reactions.util.playerselector;

import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Util;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerSelectors {
    private static List<PlayerSelector> selectors;

    public static void init() {
        selectors = new ArrayList<PlayerSelector>();
        addSelector(new Players());
        addSelector(new WorldsPlayers());
        addSelector(new LocSelector());
        addSelector(new GroupPlayers());
        addSelector(new PermPlayers());
        addSelector(new RegionsPlayers());
        addSelector(new FactionsPlayers());
    }

    public static void addSelector(PlayerSelector selector) {
        if (selector == null) return;
        if (selector.getKey() == null) return;
        selectors.add(selector);
    }

    public static Set<Player> getPlayerList(Param param) {
        Set<Player> players = new HashSet<Player>();
        for (PlayerSelector selector : selectors) {
            String selectorParam = param.getParam(selector.getKey());
            if (selector.getKey().equalsIgnoreCase("loc") && param.isParamsExists("radius"))
                selectorParam = Util.join("loc:", selectorParam, " ", "radius:", param.getParam("radius", "1"));
            players.addAll(selector.selectPlayers(selectorParam));
        }
        return players;
    }

    public static String[] getAllKeys() {
        String[] keys = new String[selectors.size()];
        int i = 0;
        for (PlayerSelector s : selectors) {
            keys[i] = s.getKey();
            i++;
        }
        return keys;
    }


}
