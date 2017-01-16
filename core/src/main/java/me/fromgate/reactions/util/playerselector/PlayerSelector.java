package me.fromgate.reactions.util.playerselector;

import org.bukkit.entity.Player;

import java.util.Set;

public abstract class PlayerSelector {
    public String getKey() {
        if (!this.getClass().isAnnotationPresent(SelectorDefine.class)) return null;
        SelectorDefine sd = this.getClass().getAnnotation(SelectorDefine.class);
        return sd.key();
    }

    public abstract Set<Player> selectPlayers(String param);

}
