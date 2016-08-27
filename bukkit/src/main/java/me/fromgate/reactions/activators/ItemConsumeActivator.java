package me.fromgate.reactions.activators;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.ItemConsumeEvent;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.item.ItemUtil;
import me.fromgate.reactions.util.item.VirtualItem;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

public class ItemConsumeActivator extends Activator {

    private String item;


    public ItemConsumeActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }

    public ItemConsumeActivator(String name, String item) {
        super(name, "activators");
        this.item = item;
    }

    public boolean activate(Event event) {
        if (!this.item.isEmpty() && ItemUtil.parseItemStack(this.item) != null) {
            if (event instanceof ItemConsumeEvent) {
                ItemConsumeEvent ie = (ItemConsumeEvent) event;
                if (ItemUtil.compareItemStr(ie.getItem(), this.item)) {
                    VirtualItem vi = ItemUtil.itemFromItemStack(ie.getItem());
                    if (vi != null) {
                        Variables.setTempVar("item", vi.toString());
                        Variables.setTempVar("item-str", vi.toDisplayString());
                    }

                    return Actions.executeActivator(ie.getPlayer(), this);
                }
            }

            return false;
        } else {
            ReActions.util.logOnce(this.name + "activatoritemempty", "Failed to parse item of activator " + this.name);
            return false;
        }
    }

    public boolean isLocatedAt(Location loc) {
        return false;
    }

    public void save(String root, YamlConfiguration cfg) {
        cfg.set(root + ".item", this.item);
    }

    public void load(String root, YamlConfiguration cfg) {
        this.item = cfg.getString(root + ".item");
    }

    public ActivatorType getType() {
        return ActivatorType.ITEM_CONSUME;
    }

    public String toString() {
        StringBuilder sb = (new StringBuilder(this.name)).append(" [").append(this.getType()).append("]");
        if (!this.getFlags().isEmpty()) {
            sb.append(" F:").append(this.getFlags().size());
        }

        if (!this.getActions().isEmpty()) {
            sb.append(" A:").append(this.getActions().size());
        }

        if (!this.getReactions().isEmpty()) {
            sb.append(" R:").append(this.getReactions().size());
        }

        sb.append(" (").append(this.item).append(")");
        return sb.toString();
    }
}
