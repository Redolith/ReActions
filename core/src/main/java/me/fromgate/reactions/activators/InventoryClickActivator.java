package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.PlayerInventoryClickEvent;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.item.ItemUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryClickActivator extends Activator {
    private String inventoryName;
    private ClickType click;
    private InventoryAction action;
    private InventoryType inventory;
    private SlotType slotType;
    private String itemStr;
    private String numberKey;
    private String slotStr;


    public InventoryClickActivator(String name, String param) {
        super(name, "activators");
        Param params = new Param(param);
        this.inventoryName = params.getParam("name", "");
        this.click = ClickType.getByName(params.getParam("click", "ANY"));
        this.action = InventoryAction.getByName(params.getParam("action", "ANY"));
        this.inventory = InventoryType.getByName(params.getParam("inventory", "ANY"));
        this.slotType = SlotType.getByName(params.getParam("slotType", "ANY"));
        this.numberKey = getNumberKeyByName(params.getParam("key", "ANY"));
        this.slotStr = getSlotByName(params.getParam("slot", "ANY"));
        this.itemStr = params.getParam("item");
    }

    public InventoryClickActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }


    @Override
    public boolean activate(Event event) {
        if (!(event instanceof PlayerInventoryClickEvent)) return false;
        PlayerInventoryClickEvent pice = (PlayerInventoryClickEvent) event;
        if (!inventoryName.isEmpty() && !pice.getInventoryName().equalsIgnoreCase(inventoryName)) return false;
        if (pice.getClickType() == null) return false;
        if (!clickCheck(pice.getClickType())) return false;
        if (!actionCheck(pice.getAction())) return false;
        if (!inventoryCheck(pice.getInventoryType())) return false;
        if (!slotTypeCheck(pice.getSlotType())) return false;
        Integer key = pice.getNumberKey();
        if (!checkItem(pice.getItemStack(), key, pice.getBottomInventory())) return false;
        if (!checkNumberKey(key)) return false;
        Integer slot = pice.getSlot();
        if (!checkSlot(slot)) return false;
        Variables.setTempVar("name", pice.getInventoryName());
        Variables.setTempVar("click", pice.getClickType().toString());
        Variables.setTempVar("action", pice.getAction().toString());
        Variables.setTempVar("slotType", pice.getSlotType().toString());
        Variables.setTempVar("inventory", pice.getInventoryType().toString());
        Variables.setTempVar("item", ItemUtil.itemToString(pice.getItemStack()));
        Variables.setTempVar("key", Integer.toString(key + 1));
        Variables.setTempVar("itemkey", (key > -1) ? ItemUtil.itemToString(pice.getBottomInventory().getItem(key)) : "");
        Variables.setTempVar("slot", Integer.toString(slot));
        boolean result = Actions.executeActivator(pice.getPlayer(), this);
        Param itemParam = new Param(Variables.getTempVar("item"));
        if (!itemParam.isEmpty()) {
            String itemType = itemParam.getParam("type", "0");
            if (itemType.equalsIgnoreCase("AIR") || itemType.equalsIgnoreCase("null") || itemType.equalsIgnoreCase("0") || itemType.isEmpty()) {
                pice.setItemStack(new ItemStack(Material.getMaterial("AIR"), 1));
            } else {
                pice.setItemStack(ItemUtil.parseItemStack(itemParam.getParam("param-line", "")));
            }
        }
        return result;
    }

    @Override
    public boolean isLocatedAt(Location l) {
        return false;
    }


    @Override
    public void save(String root, YamlConfiguration cfg) {
        cfg.set(root + ".name", this.inventoryName);
        cfg.set(root + ".click-type", click.name());
        cfg.set(root + ".action-type", action.name());
        cfg.set(root + ".inventory-type", inventory.name());
        cfg.set(root + ".slot-type", slotType.name());
        cfg.set(root + ".key", this.numberKey);
        cfg.set(root + ".slot", this.slotStr);
        cfg.set(root + ".item", this.itemStr);
    }

    @Override
    public void load(String root, YamlConfiguration cfg) {
        this.inventoryName = cfg.getString(root + ".name", "");
        this.click = ClickType.getByName(cfg.getString(root + ".click-type", "ANY"));
        this.action = InventoryAction.getByName(cfg.getString(root + ".action-type", "ANY"));
        this.inventory = InventoryType.getByName(cfg.getString(root + ".inventory-type", "ANY"));
        this.slotType = SlotType.getByName(cfg.getString(root + ".slot-type", "ANY"));
        this.numberKey = cfg.getString(root + ".key", "");
        this.slotStr = cfg.getString(root + ".slot", "");
        this.itemStr = cfg.getString(root + ".item", "");
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
            if (clickStr != null) {
                for (ClickType clickType : values()) {
                    if (clickStr.equalsIgnoreCase(clickType.name())) {
                        return clickType;
                    }
                }
            }
            return ClickType.ANY;
        }
    }

    enum InventoryAction {
        ANY,
        CLONE_STACK,
        COLLECT_TO_CURSOR,
        DROP_ALL_CURSOR,
        DROP_ALL_SLOT,
        DROP_ONE_CURSOR,
        DROP_ONE_SLOT,
        HOTBAR_MOVE_AND_READD,
        HOTBAR_SWAP,
        MOVE_TO_OTHER_INVENTORY,
        NOTHING,
        PICKUP_ALL,
        PICKUP_HALF,
        PICKUP_ONE,
        PICKUP_SOME,
        PLACE_ALL,
        PLACE_ONE,
        PLACE_SOME,
        SWAP_WITH_CURSOR,
        UNKNOWN;

        public static InventoryAction getByName(String actionStr) {
            if (actionStr != null) {
                for (InventoryAction action : values()) {
                    if (actionStr.equalsIgnoreCase(action.name())) {
                        return action;
                    }
                }
            }
            return InventoryAction.ANY;
        }
    }


    enum InventoryType {
        ANY,
        ANVIL,
        BEACON,
        BREWING,
        CHEST,
        CRAFTING,
        CREATIVE,
        DISPENSER,
        DROPPER,
        ENCHANTING,
        ENDER_CHEST,
        HOPPER,
        MERCHANT,
        PLAYER,
        SHULKER_BOX,
        WORKBENCH;

        public static InventoryType getByName(String inventoryStr) {
            if (inventoryStr != null) {
                for (InventoryType inventoryType : values()) {
                    if (inventoryStr.equalsIgnoreCase(inventoryType.name())) {
                        return inventoryType;
                    }
                }
            }
            return InventoryType.ANY;
        }
    }

    enum SlotType {
        ANY,
        ARMOR,
        CONTAINER,
        CRAFTING,
        FUEL,
        OUTSIDE,
        QUICKBAR,
        RESULT;

        public static SlotType getByName(String slotStr) {
            if (slotStr != null) {
                for (SlotType slotType : values()) {
                    if (slotStr.equalsIgnoreCase(slotType.name())) {
                        return slotType;
                    }
                }
            }
            return SlotType.ANY;
        }
    }

    private static String getNumberKeyByName(String keyStr) {
        if (keyStr.equalsIgnoreCase("ANY")) return "ANY";
        Integer key = Integer.parseInt(keyStr);
        if (key > 0) {
            for (int i = 1; i < 10; i++) {
                if (key == i) return String.valueOf(i);
            }
        }
        return "ANY";
    }

    private static String getSlotByName(String slotStr) {
        Integer slot = Integer.parseInt(slotStr);
        if (slot > -1) {
            for (int i = 0; i < 36; i++) {
                if (slot == i) return String.valueOf(i);
            }
        }
        return "ANY";
    }

    private boolean clickCheck(org.bukkit.event.inventory.ClickType ct) {
        if (click.name().equals("ANY")) return true;
        return ct.name().equals(click.name());
    }

    private boolean actionCheck(org.bukkit.event.inventory.InventoryAction act) {
        if (action.name().equals("ANY")) return true;
        return act.name().equals(action.name());
    }

    private boolean inventoryCheck(org.bukkit.event.inventory.InventoryType it) {
        if (inventory.name().equals("ANY")) return true;
        return it.name().equals(inventory.name());
    }

    private boolean slotTypeCheck(org.bukkit.event.inventory.InventoryType.SlotType sl) {
        if (slotType.name().equals("ANY")) return true;
        return sl.name().equals(slotType.name());
    }

    private boolean checkItem(ItemStack item, Integer key, Inventory bottomInventory) {
        if (this.itemStr.isEmpty()) return true;
        Boolean result = ItemUtil.compareItemStr(item, this.itemStr, true);
        if (!result && key > -1) return ItemUtil.compareItemStr(bottomInventory.getItem(key), this.itemStr, true);
        return result;
    }

    private boolean checkNumberKey(Integer key) {
        if (numberKey.isEmpty() || numberKey.equals("ANY") || Integer.parseInt(numberKey) <= 0) return true;
        return key == Integer.parseInt(numberKey) - 1;
    }

    private boolean checkSlot(Integer slot) {
        if (slotStr.isEmpty() || slotStr.equals("ANY") || Integer.parseInt(slotStr) <= 0) return true;
        return slot == Integer.parseInt(slotStr);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name).append(" [").append(getType()).append("]");
        if (!getFlags().isEmpty()) sb.append(" F:").append(getFlags().size());
        if (!getActions().isEmpty()) sb.append(" A:").append(getActions().size());
        if (!getReactions().isEmpty()) sb.append(" R:").append(getReactions().size());
        sb.append(" (");
        sb.append("name:").append(this.inventoryName);
        sb.append(" click:").append(this.click.name());
        sb.append(" action:").append(this.action.name());
        sb.append(" inventory:").append(this.inventory.name());
        sb.append(" slotType:").append(this.slotType.name());
        sb.append(" key:").append(this.numberKey);
        sb.append(" slot:").append(this.slotStr);
        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
