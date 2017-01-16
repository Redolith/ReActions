package me.fromgate.reactions.util.playerselector;

import me.fromgate.reactions.util.Locator;
import me.fromgate.reactions.util.Param;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;


@SelectorDefine(key = "loc")
public class LocSelector extends PlayerSelector {
    @Override
    public Set<Player> selectPlayers(String param) {
        Set<Player> players = new HashSet<Player>();
        if (param.isEmpty()) return players;
        Param params = new Param(param, "loc");
        String locStr = params.getParam("loc");
        if (locStr.isEmpty()) return players;
        Location loc = Locator.parseLocation(locStr, null);
        if (loc == null) return players;
        loc.setX(loc.getBlockX() + 0.5);
        loc.setY(loc.getBlockY() + 0.5);
        loc.setZ(loc.getBlockZ() + 0.5);
        double radius = params.getParam("radius", 1.0);
        for (Player player : loc.getWorld().getPlayers())
            if (player.getLocation().distance(loc) <= radius) players.add(player);
        return players;
    }

}
