package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.PlayerInventoryClickEvent;
import me.fromgate.reactions.util.Locator;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

public class InventoryClickActivator extends Activator {
    private ClickType click;


    public InventoryClickActivator(String name, String param) {
        super(name, "activators");
        Param params = new Param(param);
        this.click = ClickType.getByName(params.getParam("click", "ANY"));
    }

    public InventoryClickActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }


    @Override
    public boolean activate(Event event) {
        if (!(event instanceof PlayerInventoryClickEvent )) return false;
        PlayerInventoryClickEvent pice = (PlayerInventoryClickEvent ) event;
        if (pice.getClickType() == null) return false;
        if (!clickCheck(pice.getClickType())) return false;
        Variables.setTempVar("click", pice.getClickType().toString());
        return Actions.executeActivator(pice.getPlayer(), this);
    }

    @Override
    public boolean isLocatedAt(Location l) {
        return false;
    }


    @Override
    public void save(String root, YamlConfiguration cfg) {
        cfg.set(root + ".click-type", click.name());
    }

    @Override
    public void load(String root, YamlConfiguration cfg) {
        click = ClickType.getByName(cfg.getString(root + ".click-type", "ANY"));
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.INVENTORY_CLICK;
    }

    enum ClickType {
        ANY,
        CONTROL_DROP,
        CREATIVE,
        DROP,
        DOUBLE_CLICK,
        LEFT,
        MIDDLE,
        NUMBER_KEY,
        RIGHT,
        SHIFT_LEFT,
        SHIFT_RIGHT,
        UNKNOWN,
        WINDOW_BORDER_LEFT,
        WINDOW_BORDER_RIGHT;

        public static ClickType getByName(String clickStr) {
            if (clickStr.equalsIgnoreCase("CONTROL_DROP")) return ClickType.CONTROL_DROP;
            if (clickStr.equalsIgnoreCase("CREATIVE")) return ClickType.CREATIVE;
            if (clickStr.equalsIgnoreCase("DROP")) return ClickType.DROP;
            if (clickStr.equalsIgnoreCase("DOUBLE_CLICK")) return ClickType.DOUBLE_CLICK;
            if (clickStr.equalsIgnoreCase("LEFT")) return ClickType.LEFT;
            if (clickStr.equalsIgnoreCase("MIDDLE")) return ClickType.MIDDLE;
            if (clickStr.equalsIgnoreCase("NUMBER_KEY")) return ClickType.NUMBER_KEY;
            if (clickStr.equalsIgnoreCase("RIGHT")) return ClickType.RIGHT;
            if (clickStr.equalsIgnoreCase("SHIFT_LEFT")) return ClickType.SHIFT_LEFT;
            if (clickStr.equalsIgnoreCase("SHIFT_RIGHT")) return ClickType.SHIFT_RIGHT;
            if (clickStr.equalsIgnoreCase("WINDOW_BORDER_LEFT")) return ClickType.WINDOW_BORDER_LEFT;
            if (clickStr.equalsIgnoreCase("WINDOW_BORDER_RIGHT")) return ClickType.WINDOW_BORDER_RIGHT;
            if (clickStr.equalsIgnoreCase("UNKNOWN")) return ClickType.UNKNOWN;
            return ClickType.ANY;
        }
    }

    
    private boolean clickCheck(org.bukkit.event.inventory.ClickType ct) {
        if (click.name().equals("ANY")) return true;
        return ct.name().equals(click.name());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name).append(" [").append(getType()).append("]");
        if (!getFlags().isEmpty()) sb.append(" F:").append(getFlags().size());
        if (!getActions().isEmpty()) sb.append(" A:").append(getActions().size());
        if (!getReactions().isEmpty()) sb.append(" R:").append(getReactions().size());
        sb.append(" (");
        sb.append(" click:").append(this.click.name());
        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
