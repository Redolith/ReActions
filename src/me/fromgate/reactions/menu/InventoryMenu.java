package me.fromgate.reactions.menu;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.event.EventManager;
import me.fromgate.reactions.util.ItemUtil;
import me.fromgate.reactions.util.ParamUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryMenu implements Listener{
	private static Map<Inventory,List<String>> activeMenus = new HashMap<Inventory,List<String>>();

	private static Map<String,VirtualInventory> menu = new HashMap<String,VirtualInventory>();



	public static void init(){
		load();
		save();
	}

	public static void save(){
		File f = new File (ReActions.instance.getDataFolder()+File.separator+"menu.yml");
		if (f.exists()) f.delete();
		YamlConfiguration cfg = new YamlConfiguration();
		for (String key : menu.keySet()){
			menu.get(key).save(cfg, key);
		}
		try {
			cfg.save(f);
		} catch (Exception e) {
			ReActions.util.log("Failed to save menu configuration file");
		}
	}

	public static void load(){
		menu.clear();
		File f = new File (ReActions.instance.getDataFolder()+File.separator+"menu.yml");
		if (!f.exists()) return;
		YamlConfiguration cfg = new YamlConfiguration();
		try {
			cfg.load(f);
			for (String key : cfg.getKeys(false)){
				VirtualInventory vi = new VirtualInventory (cfg, key);
				menu.put(key, vi);
			}
		} catch (Exception e) {
			ReActions.util.log("Failed to load menu configuration file");
		}
	}

	public static boolean add (String id, int size, String title){
		if (menu.keySet().contains(id)) return false;
		menu.put(id, new VirtualInventory (size, title));
		save();
		return true;
	}

	public static boolean set (String id, Map<String,String> params){
		if (!menu.keySet().contains(id)) return false;
		VirtualInventory vi = menu.get(id);
		String title = ParamUtil.getParam(params, "title", vi.title);
		int size = ParamUtil.getParam(params, "size", vi.size);
		size = (size%9 ==0) ? size : ((size/9)+1)*9;

		List<String> activators = vi.execs;
		if (activators.size()<size)
			for (int i = activators.size(); i<size; i++) activators.add("");
		List<String> slots = vi.slots;
		if (slots.size()<size)
			for (int i = slots.size(); i<size; i++) slots.add("");
		for (int i = 1; i<=size; i++){
			if (ParamUtil.isParamExists(params, "activator"+Integer.toString(i)))
				activators.set(i-1, ParamUtil.getParam(params, "activator"+Integer.toString(i), ""));
			if (ParamUtil.isParamExists(params, "item"+Integer.toString(i)))
				slots.set(i-1, ParamUtil.getParam(params, "item"+Integer.toString(i), ""));
		}
		vi.title = title;
		vi.size = size;
		vi.slots = slots;
		vi.execs = activators;
		menu.put(id, vi);
		save();
		return true;
	}

	public static boolean remove (String id){

		return false;
	}


	public static List<String> getActivators (Map<String,String> params){
		if (ParamUtil.isParamExists(params, "menu")){
			String id = ParamUtil.getParam(params, "menu", "");
			if (menu.containsKey(id)) return menu.get(id).getActivators();
		} else {
			int size = ParamUtil.getParam(params, "size", 9);
			if (size>0) {
				List<String> activators = new ArrayList<String>();
				for (int i = 1; i<=size; i++)
					activators.add(ParamUtil.getParam(params, "exec"+Integer.toString(i), ""));
				return activators;
			}
		}
		return new ArrayList<String>();
	}

	public static Inventory getInventory (Map<String,String> params){
		Inventory inv = null;
		if (ParamUtil.isParamExists(params, "menu")){
			String id = ParamUtil.getParam(params, "menu", "");
			if (menu.containsKey(id)) inv = menu.get(id).getInventory();
		} else {
			String title = ParamUtil.getParam(params, "title", "ReActions Menu");
			int size = ParamUtil.getParam(params, "size", 9);
			if (size<=0) return null;
			List<String> activators = new ArrayList<String>();
			inv = Bukkit.createInventory(null, size, title);
			for (int i = 1; i<=size; i++){
				activators.add(ParamUtil.getParam(params, "exec"+Integer.toString(i), ""));
				String slotStr = "slot"+Integer.toString(i);
				if (!ParamUtil.isParamExists(params, slotStr)) continue;
				ItemStack slotItem = ItemUtil.parseItemStack(ParamUtil.getParam(params, slotStr, ""));
				if (slotItem == null) continue;
				inv.setItem(i-1, slotItem);
			}
		}
		return inv;
	}

	public static boolean createAndOpenInventory(Player player, Map<String,String> params){
		Inventory inv = getInventory (params);
		if (inv == null) return false;
		activeMenus.put(inv, getActivators(params));
		player.openInventory(inv);
		return true;
	}

	public static boolean isMenu (Inventory inventory){
		return activeMenus.containsKey(inventory);
	}

	public static void removeInventory (Inventory inv){
		if (activeMenus.containsKey(inv)) activeMenus.remove(inv);
	}


	public static List<String> getActivators (Inventory inventory){
		if (isMenu(inventory)) return activeMenus.get(inventory);
		return new ArrayList<String>();
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (!InventoryMenu.isMenu(event.getInventory())) return;
		Player player = (Player) event.getWhoClicked();
		int clickedSlot = event.getSlot();
		List<String> activators = getActivators (event.getInventory());
		if (activators.size()>clickedSlot){
			String activator = activators.get(clickedSlot);
			if (!activator.isEmpty())
				//EventManager.raiseExecEvent(player, "activator:"+activator);
				EventManager.raiseExecEvent(player, ParamUtil.parseParams(activator, "activator"));
		}
		event.setCancelled(true);
		InventoryMenu.removeInventory(event.getInventory());
		player.closeInventory();
	}

	public static List<String> getEmptyList(int size){
		List<String> l = new ArrayList<String>();
		for (int i = 0; i<size;i++) l.add("");
		return l;
	}

	public static boolean exists(String id) {
		return menu.containsKey(id);
	}

	public static void printMenu (CommandSender sender, String id){
		if (menu.containsKey(id)) {
			VirtualInventory vi = menu.get(id);
			ReActions.util.printMSG(sender, "msg_menuinfotitle",'e','6',id,vi.size,vi.title);
			for (int i = 0; i<vi.size;i++){
				String exec = vi.execs.get(i);
				String slot = vi.slots.get(i); 
				if (exec.isEmpty()&&slot.isEmpty()) continue;
				slot = itemToString(slot);
				ReActions.util.printMSG(sender, "msg_menuinfoslot",i+1,exec.isEmpty() ? "N/A" : exec, slot.isEmpty() ? "AIR" : slot);
			}
		} else ReActions.util.printMSG (sender, "msg_menuidfail",id);
	}

	public static void printMenuList(CommandSender sender, int page, String mask) {
		int maxPage = (sender instanceof Player) ? 15 : 10000;
		List<String> menuList = new ArrayList<String>();
		for (String id : menu.keySet())
			if (mask.isEmpty()||id.toLowerCase().contains(mask.toLowerCase()))
				menuList.add(id+" : "+menu.get(id).title);
		ReActions.util.printPage(sender, menuList, page, "msg_menulist", "", false, maxPage);
	}
	
	public static String itemToString (String itemStr){
		if (itemStr.isEmpty()) return "AIR";
		ItemStack item = ItemUtil.parseItemStack(itemStr);
		if (item == null||item.getType()==Material.AIR) return "AIR";
		String returnStr = item.hasItemMeta() && item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : "";
		String itemTypeData = item.getType().name()+(item.getDurability() == 0 ? "" : ":"+item.getDurability())+(item.getAmount()==1 ? "" : "*"+item.getAmount());
		return ChatColor.stripColor(returnStr.isEmpty() ? itemTypeData : returnStr+"["+itemTypeData+"]");
	}

}
