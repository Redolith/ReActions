package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.GameModeEvent;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

/**
 * Created by MaxDikiy on 2017-10-27.
 */
public class GameModeActivator extends Activator {
    private GameMode gameMode;

    public GameModeActivator(String name, String param) {
        super(name, "activators");
        Param params = new Param(param);
        this.gameMode = GameMode.getByName(params.getParam("gamemode", "ANY"));
    }

    public GameModeActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }

    @Override
    public boolean activate(Event event) {
        if (!(event instanceof GameModeEvent)) return false;
        GameModeEvent e = (GameModeEvent) event;
        if (!gameModeCheck(e.getGameMode())) return false;
        Variables.setTempVar("gamemode", e.getGameMode().toString());
        return Actions.executeActivator(e.getPlayer(), this);
    }

    enum GameMode {
        SURVIVAL,
        CREATIVE,
        ADVENTURE,
        SPECTATOR,
        ANY;

        public static GameMode getByName(String gmStr) {
            if (gmStr != null) {
                for (GameMode gmType : values()) {
                    if (gmStr.equalsIgnoreCase(gmType.name())) {
                        return gmType;
                    }
                }
            }
            return GameMode.ANY;
        }
    }

    private boolean gameModeCheck(org.bukkit.GameMode gm) {
        if (gameMode.name().equals("ANY")) return true;
        return gm.name().equals(gameMode.name());
    }

    @Override
    public boolean isLocatedAt(Location loc) {
        return false;
    }

    @Override
    public void save(String root, YamlConfiguration cfg) {
        cfg.set(root + ".gamemode", gameMode.name());
    }

    @Override
    public void load(String root, YamlConfiguration cfg) {
        gameMode = GameMode.getByName(cfg.getString(root + ".gamemode", "ANY"));
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.GAME_MODE;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name).append(" [").append(getType()).append("]");
        if (!getFlags().isEmpty()) sb.append(" F:").append(getFlags().size());
        if (!getActions().isEmpty()) sb.append(" A:").append(getActions().size());
        if (!getReactions().isEmpty()) sb.append(" R:").append(getReactions().size());
        sb.append(" (");
        sb.append("gamemode:").append(this.gameMode.name());
        sb.append(")");
        return sb.toString();
    }

}
