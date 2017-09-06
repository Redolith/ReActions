package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.PickupItemEvent;
import me.fromgate.reactions.util.Locator;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.item.ItemUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.regex.Pattern;

/**
 * Created by MaxDikiy on 2017-09-04.
 */
public class PickupItemActivator extends Activator {
    private final static Pattern FLOAT = Pattern.compile("\\d+\\.?\\d*");

    private String itemStr;

    public PickupItemActivator(String name, String param) {
        super(name, "activators");
        Param params = new Param(param);
        this.itemStr = params.getParam("item");
    }

    public PickupItemActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }

    @Override
    public boolean activate(Event event) {
        if (!(event instanceof PickupItemEvent)) return false;
        PickupItemEvent pie = (PickupItemEvent) event;
        if (!checkItem(pie.getItemStack())) return false;
        Variables.setTempVar("droplocation", Locator.locationToString(pie.getPlayer().getLocation()));
        Variables.setTempVar("pickupDelay", Double.toString(pie.getPickupDelay()));
        Variables.setTempVar("item", ItemUtil.itemToString(pie.getItemStack()));
        boolean result = Actions.executeActivator(pie.getPlayer(), this);
        String pickupDelayStr = Variables.getTempVar("pickupDelay");
        if (FLOAT.matcher(pickupDelayStr).matches()) pie.setPickupDelay(Double.parseDouble(pickupDelayStr));
        Param itemParam = new Param(Variables.getTempVar("item"));
        if (!itemParam.isEmpty()) {
            String itemType = itemParam.getParam("type", "0");
            if (itemType.equalsIgnoreCase("AIR") || itemType.equalsIgnoreCase("null") || itemType.equalsIgnoreCase("0") || itemType.isEmpty()) {
                pie.setItemStack(new ItemStack(Material.getMaterial("AIR"), 1));
            } else {
                pie.setItemStack(ItemUtil.parseItemStack(itemParam.getParam("param-line", "")));
            }
        }
        return result;
    }

    @Override
    public boolean isLocatedAt(Location loc) {
        return false;
    }

    @Override
    public void save(String root, YamlConfiguration cfg) {
        cfg.set(root + ".item", this.itemStr);
    }

    @Override
    public void load(String root, YamlConfiguration cfg) {
        this.itemStr = cfg.getString(root + ".item", "");
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.PICKUP_ITEM;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    private boolean checkItem(ItemStack item) {
        if (this.itemStr.isEmpty()) return true;
        return ItemUtil.compareItemStr(item, this.itemStr, true);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name).append(" [").append(getType()).append("]");
        if (!getFlags().isEmpty()) sb.append(" F:").append(getFlags().size());
        if (!getActions().isEmpty()) sb.append(" A:").append(getActions().size());
        if (!getReactions().isEmpty()) sb.append(" R:").append(getReactions().size());
        sb.append(" (");
        sb.append("item:").append(this.itemStr);
        sb.append(")");
        return sb.toString();
    }
}
