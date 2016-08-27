package me.fromgate.reactions.menu;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.event.EventManager;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.item.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.*;

public class InventoryMenu implements Listener {
    private static Map<Integer, List<String>> activeMenus = new HashMap<Integer, List<String>>();
    private static Map<String, VirtualInventory> menu = new TreeMap<String, VirtualInventory>(String.CASE_INSENSITIVE_ORDER);

    public static void init() {
        load();
        save();
    }

    public static void save() {
        File f = new File(ReActions.instance.getDataFolder() + File.separator + "menu.yml");
        if (f.exists()) f.delete();
        YamlConfiguration cfg = new YamlConfiguration();
        for (String key : menu.keySet()) {
            menu.get(key).save(cfg, key);
        }
        try {
            cfg.save(f);
        } catch (Exception e) {
            ReActions.util.log("Failed to save menu configuration file");
        }
    }

    public static void load() {
        menu.clear();
        File f = new File(ReActions.instance.getDataFolder() + File.separator + "menu.yml");
        if (!f.exists()) return;
        YamlConfiguration cfg = new YamlConfiguration();
        try {
            cfg.load(f);
            for (String key : cfg.getKeys(false)) {
                VirtualInventory vi = new VirtualInventory(cfg, key);
                menu.put(key, vi);
            }
        } catch (Exception e) {
            ReActions.util.log("Failed to load menu configuration file");
        }
    }

    public static boolean add(String id, int size, String title) {
        if (menu.keySet().contains(id)) return false;
        menu.put(id, new VirtualInventory(size, title));
        save();
        return true;
    }

    public static boolean set(String id, Param params) {
        if (!menu.keySet().contains(id)) return false;
        VirtualInventory vi = menu.get(id);
        String title = params.getParam("title", vi.title);
        int size = params.getParam("size", vi.size);
        size = (size % 9 == 0) ? size : ((size / 9) + 1) * 9;

        List<String> activators = vi.execs;
        if (activators.size() < size)
            for (int i = activators.size(); i < size; i++) activators.add("");
        List<String> slots = vi.slots;
        if (slots.size() < size)
            for (int i = slots.size(); i < size; i++) slots.add("");
        for (int i = 1; i <= size; i++) {
            if (params.isParamsExists("activator" + Integer.toString(i)))
                activators.set(i - 1, params.getParam("activator" + Integer.toString(i), ""));
            if (params.isParamsExists("item" + Integer.toString(i)))
                slots.set(i - 1, params.getParam("item" + Integer.toString(i), ""));
        }
        vi.title = title;
        vi.size = size;
        vi.slots = slots;
        vi.execs = activators;
        menu.put(id, vi);
        save();
        return true;
    }

    public static boolean remove(String id) {
        if (!menu.containsKey(id)) return false;
        menu.remove(id);
        return true;
    }


    public static List<String> getActivators(Param param) {
        if (param.isParamsExists("menu")) {
            String id = param.getParam("menu", "");
            if (menu.containsKey(id)) return menu.get(id).getActivators();
        } else {
            int size = param.getParam("size", 9);
            if (size > 0) {
                List<String> activators = new ArrayList<String>();
                for (int i = 1; i <= size; i++)
                    activators.add(param.getParam("exec" + Integer.toString(i), ""));
                return activators;
            }
        }
        return new ArrayList<String>();
    }

    public static Inventory getInventory(Param param) {
        Inventory inv = null;
        if (param.isParamsExists("menu")) {
            String id = param.getParam("menu", "");
            if (menu.containsKey(id)) inv = menu.get(id).getInventory();
        } else {
            String title = param.getParam("title", "ReActions Menu");
            int size = param.getParam("size", 9);
            if (size <= 0) return null;
            List<String> activators = new ArrayList<String>();
            inv = Bukkit.createInventory(null, size, title);
            for (int i = 1; i <= size; i++) {
                activators.add(param.getParam("exec" + Integer.toString(i), ""));
                String slotStr = "slot" + Integer.toString(i);
                if (!param.isParamsExists(slotStr)) continue;
                ItemStack slotItem = ItemUtil.parseItemStack(param.getParam(slotStr, ""));
                if (slotItem == null) continue;
                inv.setItem(i - 1, slotItem);
            }
        }
        return inv;
    }

    public static boolean createAndOpenInventory(Player player, Param params) {
        Inventory inv = getInventory(params);
        if (inv == null) return false;
        activeMenus.put(getInventoryCode(player, inv), getActivators(params));
        openInventory(player, inv);
        return true;
    }

    public static void openInventory(final Player player, final Inventory inv) {
        Bukkit.getScheduler().runTaskLater(ReActions.instance, new Runnable() {
            @Override
            public void run() {
                if (player.isOnline()) player.openInventory(inv);
                else activeMenus.remove(getInventoryCode(player, inv));
            }
        }, 1);
    }

    public static boolean isMenu(Inventory inventory) {
        return activeMenus.containsKey(getInventoryCode(inventory));
    }

    public static void removeInventory(Inventory inv) {
        int code = getInventoryCode(inv);
        if (activeMenus.containsKey(code)) activeMenus.remove(code);
    }


    public static List<String> getActivators(Inventory inventory) {
        if (isMenu(inventory)) return activeMenus.get(getInventoryCode(inventory));
        return new ArrayList<String>();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!InventoryMenu.isMenu(event.getInventory())) return;
        Player player = (Player) event.getWhoClicked();
        int clickedSlot = event.getRawSlot();
        if (clickedSlot < 0 || clickedSlot >= event.getInventory().getSize()) return;
        List<String> activators = getActivators(event.getInventory());
        if (activators.size() > clickedSlot) {
            String activator = activators.get(clickedSlot);
            if (!activator.isEmpty())
                EventManager.raiseExecEvent(player, new Param(activator, "activator"));
        }
        event.setCancelled(true);
        InventoryMenu.removeInventory(event.getInventory());
        player.closeInventory();
    }

    public static List<String> getEmptyList(int size) {
        List<String> l = new ArrayList<String>();
        for (int i = 0; i < size; i++) l.add("");
        return l;
    }

    public static boolean exists(String id) {
        return menu.containsKey(id);
    }

    public static void printMenu(CommandSender sender, String id) {
        if (menu.containsKey(id)) {
            VirtualInventory vi = menu.get(id);
            ReActions.util.printMSG(sender, "msg_menuinfotitle", 'e', '6', id, vi.size, vi.title);
            for (int i = 0; i < vi.size; i++) {
                String exec = vi.execs.get(i);
                String slot = vi.slots.get(i);
                if (exec.isEmpty() && slot.isEmpty()) continue;
                slot = itemToString(slot);
                ReActions.util.printMSG(sender, "msg_menuinfoslot", i + 1, exec.isEmpty() ? "N/A" : exec, slot.isEmpty() ? "AIR" : slot);
            }
        } else ReActions.util.printMSG(sender, "msg_menuidfail", id);
    }

    public static void printMenuList(CommandSender sender, int page, String mask) {
        int maxPage = (sender instanceof Player) ? 15 : 10000;
        List<String> menuList = new ArrayList<String>();
        for (String id : menu.keySet())
            if (mask.isEmpty() || id.toLowerCase().contains(mask.toLowerCase()))
                menuList.add(id + " : " + menu.get(id).title);
        ReActions.util.printPage(sender, menuList, page, "msg_menulist", "", false, maxPage);
    }

    public static String itemToString(String itemStr) {
        if (itemStr.isEmpty()) return "AIR";
        ItemStack item = ItemUtil.parseItemStack(itemStr);
        if (item == null || item.getType() == Material.AIR) return "AIR";
        String returnStr = item.hasItemMeta() && item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : "";
        String itemTypeData = item.getType().name() + (item.getDurability() == 0 ? "" : ":" + item.getDurability()) + (item.getAmount() == 1 ? "" : "*" + item.getAmount());
        return ChatColor.stripColor(returnStr.isEmpty() ? itemTypeData : returnStr + "[" + itemTypeData + "]");
    }

    public static int getInventoryCode(InventoryClickEvent event) {
        if (event.getViewers().size() != 1) return -1;
        HumanEntity human = event.getViewers().get(0);
        return getInventoryCode((Player) human, event.getInventory());
    }

    public static int getInventoryCode(Inventory inv) {
        if (inv.getViewers().size() != 1) return -1;
        HumanEntity human = inv.getViewers().get(0);
        return getInventoryCode((Player) human, inv);
    }

    public static int getInventoryCode(Player player, Inventory inv) {
        if (player == null || inv == null) return -1;
        StringBuilder sb = new StringBuilder();
        sb.append(player.getName());
        sb.append(inv.getTitle());
        for (ItemStack i : inv.getContents()) {
            String iStr = "emptyslot";
            if (i != null && i.getType() != Material.AIR) {
                if (i.hasItemMeta()) {
                    ItemMeta im = i.getItemMeta();
                    if (im.hasDisplayName()) sb.append(im.getDisplayName());
                    if (im.hasLore())
                        for (String str : im.getLore())
                            sb.append(str);
                }
                sb.append(i.getType().name());
                sb.append(":");
                sb.append(i.getDurability());
                sb.append(":");
                sb.append(i.getAmount());
            }
            sb.append(iStr);
        }
        return sb.toString().hashCode();
    }
}
