package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.DropEvent;
import me.fromgate.reactions.util.Locator;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.item.ItemUtil;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

/**
 * Created by MaxDikiy on 2017-05-01.
 */
public class DropActivator extends Activator  {
    private String itemStr;

    public DropActivator(String name, String param) {
        super(name, "activators");
        Param params = new Param(param);
        this.itemStr = params.getParam("item");
    }

    public DropActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }

    @Override
    public boolean activate(Event event) {
        if (!(event instanceof DropEvent )) return false;
        DropEvent de = (DropEvent ) event;
        if (!checkItem(de.getItemStack())) return false;
        Variables.setTempVar("droplocation", Locator.locationToString(de.getPlayer().getLocation()));
        Variables.setTempVar("pickupDelay", Double.toString(de.getPickupDelay()));
        boolean result = Actions.executeActivator(de.getPlayer(), this);
        String pickupDelayStr = Variables.getTempVar("pickupDelay");
        if (pickupDelayStr.matches("\\d+\\.?\\d*")) de.setPickupDelay(Double.parseDouble(pickupDelayStr));
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
        return ActivatorType.DROP;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    private boolean checkItem(ItemStack item) {
        if (this.itemStr.isEmpty()) return true;
        return ItemUtil.compareItemStr(item, this.itemStr, true);
    }
}
