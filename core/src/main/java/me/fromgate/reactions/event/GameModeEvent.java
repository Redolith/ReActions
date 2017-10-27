package me.fromgate.reactions.event;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

/**
 * Created by MaxDikiy on 2017-10-27.
 */
public class GameModeEvent extends RAEvent {
    private GameMode gameMode;
    public GameModeEvent(Player player, GameMode gameMode) {
        super(player);
        this.gameMode = gameMode;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }
}
