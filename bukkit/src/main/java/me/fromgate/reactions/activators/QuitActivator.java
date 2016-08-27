package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.QuitEvent;
import me.fromgate.reactions.util.Variables;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

public class QuitActivator extends Activator {


    QuitActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }

    public QuitActivator(String name) {
        super(name, "activators");
    }

    @Override
    public boolean activate(Event event) {
        if (event instanceof QuitEvent) {
            QuitEvent ce = (QuitEvent) event;
            Variables.setTempVar("quit-message", ce.getQuitMessage());
            boolean result = Actions.executeActivator(ce.getPlayer(), this);
            ce.setQuiteMessage(Variables.getTempVar("quit-message"));
            return result;
        }
        return false;
    }


    @Override
    public boolean isLocatedAt(Location loc) {
        return false;
    }

    @Override
    public void save(String root, YamlConfiguration cfg) {
    }

    @Override
    public void load(String root, YamlConfiguration cfg) {
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.QUIT;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name).append(" [").append(getType()).append("]");
        if (!getFlags().isEmpty()) sb.append(" F:").append(getFlags().size());
        if (!getActions().isEmpty()) sb.append(" A:").append(getActions().size());
        if (!getReactions().isEmpty()) sb.append(" R:").append(getReactions().size());
        return sb.toString();
    }

}
