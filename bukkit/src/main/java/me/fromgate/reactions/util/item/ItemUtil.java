package me.fromgate.reactions.util.item;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.util.Param;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ItemUtil {

    private static Random random = new Random();
    private static boolean itemVersion = determineVersion(); // false - old, true - new;

    private static boolean determineVersion() {
        String versionStr = Bukkit.getBukkitVersion().replaceAll("[^0-9]", "");
        return versionStr.matches("15\\d*|16\\d*|17\\d*");
    }

    public static void giveItemOrDrop(Player player, ItemStack item) {
        for (ItemStack i : player.getInventory().addItem(item).values())
            player.getWorld().dropItemNaturally(player.getLocation(), i);
    }

    public static VirtualItem itemFromString(String itemStr) {
        return itemVersion ? VirtualItem.fromString(itemStr) : VirtualItem18.fromString(itemStr);
    }

    public static void giveItemOrDrop(Player player, String itemStr) {
        VirtualItem vi = itemFromString(itemStr);
        if (vi == null) return;
        giveItemOrDrop(player, vi);
    }

    public static boolean removeItemInHand(Player player, String itemStr) {
        if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) return false;
        VirtualItem hand = VirtualItem.fromItemStack(player.getItemInHand());
        VirtualItem vi = removeItemFromStack(hand, itemStr);
        if (vi == null) return false;
        player.setItemInHand(vi.getType() == Material.AIR ? null : vi);
        return true;
    }


    public static boolean removeItemInInventory(Inventory inventory, String itemStr) {
        Map<String, String> itemParams = VirtualItem.parseParams(itemStr);
        return removeItemInInventory(inventory, itemParams);
    }

    private static boolean removeItemInInventory(Inventory inventory, Map<String, String> itemParams) {
        int amountToRemove = Integer.parseInt(VirtualItem.getParam(itemParams, "amount", "1"));
        //int countItems =  countItemsInventory (inventory, itemParams);
        //if (amountToRemove>countItems) return false;
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null || inventory.getItem(i).getType() == Material.AIR) continue;
            VirtualItem vi = ItemUtil.itemFromItemStack(inventory.getItem(i));
            if (!vi.compare(itemParams, 1)) continue;
            if (vi.getAmount() <= amountToRemove) {
                amountToRemove -= vi.getAmount();
                inventory.setItem(i, null);
            } else {
                vi.setAmount(vi.getAmount() - amountToRemove);
                inventory.setItem(i, vi);
                amountToRemove = 0;
            }
            if (amountToRemove == 0) return true;
        }
        return false;
    }

    private static int countItemsInventory(Inventory inventory, Map<String, String> itemParams) {
        int count = 0;
        for (ItemStack slot : inventory) {
            if (slot == null || slot.getType() == Material.AIR) continue;
            VirtualItem vi = ItemUtil.itemFromItemStack(slot);
            if (!vi.compare(itemParams)) continue;
            count += vi.getAmount();
        }
        return count;
    }

	/*public static boolean removeItemInInventory(Inventory inventory, ItemStack item) {
        if (countItemsInInventory(inventory,item)<item.getAmount()) return false;
		int amountToRemove = item.getAmount();
		ItemStack ii = item.clone();
		ii.setAmount(1);
		
		for (ItemStack slot : inventory.getContents()){
			if (slot==null||slot.getType() == Material.AIR) continue;
			VirtualItem vi = itemFromItemStack(slot);
			if (!vi.compare(ii)) continue;
			//if (!slot.isSimilar(item)) continue;
			if (vi.getAmount()<=amountToRemove){
				amountToRemove -=slot.getAmount();
				slot.setType(null);
			} else {
				slot.setAmount(slot.getAmount()-amountToRemove);
				amountToRemove = 0;
			}
			if (amountToRemove == 0) return true;
		}
		return false;
	}
	*/

    /**
     * @param stack   - source item
     * @param itemStr - item description to remove
     * @return - item stack contained left items (if all items removed - remove
     */
    private static VirtualItem removeItemFromStack(VirtualItem stack, String itemStr) {
        if (!ItemUtil.compareItemStr(stack, itemStr)) return null;
        int amountToRemove = getAmount(itemStr);
        if (amountToRemove <= 0) return null;
        int leftAmount = stack.getAmount() - amountToRemove;
        if (leftAmount < 0) return null;
        VirtualItem result = VirtualItem.fromItemStack(stack);
        if (leftAmount == 0) result.setType(Material.AIR);
        else result.setAmount(leftAmount);
        return result;
    }

    private static int getAmount(String itemStr) {
        Map<String, String> itemMap = VirtualItem.parseParams(itemStr);
        String amountStr = VirtualItem.getParam(itemMap, "amount", "1");
        if (amountStr.matches("\\d+")) return Integer.parseInt(amountStr);
        return 1;
    }

	/*
    public static boolean removeItemInHand(Player player, ItemStack item){
		if (item == null||item.getType() == Material.AIR) return false;
		ItemStack handItem = player.getItemInHand();
		if (handItem == null || handItem.getType()==Material.AIR) return false;
		if (!handItem.isSimilar(item)) return false;
		int leftAmount = handItem.getAmount()-item.getAmount();
		if (leftAmount<0) return false;
		if (leftAmount == 0) handItem.setType(Material.AIR);
		else handItem.setAmount(leftAmount);
		player.setItemInHand(handItem);
		return true;
	} */


    public static boolean hasItemInInventory(Player player, String itemStr) {
        return hasItemInInventory(player.getInventory(), itemStr);
    }


    public static boolean hasItemInInventory(Inventory inventory, String itemStr) {
        int countAmount = countItemsInInventory(inventory, itemStr);
        int amount = getAmount(itemStr);
        return countAmount >= amount;
    }

    public static int countItemsInInventory(Inventory inventory, String itemStr) {
        Map<String, String> itemMap = VirtualItem.parseParams(itemStr);
        return countItemsInventory(inventory, itemMap);
    }

    public static VirtualItem itemFromItemStack(ItemStack item) {
        return itemVersion ? VirtualItem.fromItemStack(item) : VirtualItem18.fromItemStack(item);
    }

	/*
	public static int countItemsInInventory (Inventory inventory, ItemStack item){
		int count = 0;
		ItemStack ii = item.clone();
		ii.setAmount(1);
		
		for (ItemStack i : inventory.getContents()){
			if (i==null||i.getType() == Material.AIR) continue;
			VirtualItem vi = itemFromItemStack(i);
			if (vi.compare(ii)) count+=i.getAmount();
			//if (i.isSimilar(item)) count+=i.getAmount();
		}
		return count;
	} */

    public static ItemStack parseItemStack(String string) {
        VirtualItem vi = itemFromString(string);
        ReActions.getUtil().logOnce(string, "Failed to parse item: " + string);
        return vi == null ? null : vi;
    }

    public static boolean compareItemStr(ItemStack item, String itemStr) {
        if (item == null || item.getType() == Material.AIR) return false;
        return itemFromItemStack(item).compare(itemStr);
    }

    public static boolean compareItemStr(ItemStack item, String itemStr, boolean allowHand) {
        if (item != null && item.getType() != Material.AIR) return compareItemStr(item, itemStr);
        if (!allowHand) return false;
        return (itemStr.equalsIgnoreCase("HAND") || itemStr.equalsIgnoreCase("AIR"));
    }

    public static boolean removeItemInInventory(Player player, String itemStr) {
        return removeItemInInventory(player.getInventory(), itemStr);
    }

	/*
	public static boolean removeItemInInventory(Player player, ItemStack item) {
		return removeItemInInventory (player.getInventory(), item);
	} */


    //////////////////////////////////

    public static ItemStack getRndItem(String str) {
        if (str.isEmpty()) return new ItemStack(Material.AIR);
        String[] ln = str.split(",");
        if (ln.length == 0) return new ItemStack(Material.AIR);

        ItemStack item = ItemUtil.parseItemStack(ln[tryChance(ln.length)]);

        if (item == null) return new ItemStack(Material.AIR);
        item.setAmount(1);
        return item;
    }


	/*
	public static List<ItemStack> parseRandomItems (String stacks){
		return parseItemStacks (parseRandomItemsStr(stacks));
	} */


    /*
     *  <item>;<item>;<item>[%<chance>]/<item>;<item>;<item>[%<chance>]
     *
     */
    public static List<ItemStack> parseItemStacksOld(String items) {
        List<ItemStack> stacks = new ArrayList<ItemStack>();
        String[] ln = items.split(";"); // ВОТ ЭТО ЛОМАЕТ К ЧЕРТЯМ НОВЫЙ ФОРМАТ!!!
        for (String item : ln) {
            VirtualItem vi = itemFromString(item);
            if (vi != null) stacks.add(vi);
        }
        return stacks;
    }


    public static String itemToString(ItemStack item) {
        VirtualItem vi = itemFromItemStack(item);
        return vi == null ? "" : vi.toString();
    }


    public static String toDisplayString(List<ItemStack> items) {
        StringBuilder sb = new StringBuilder();
        for (ItemStack i : items) {
            VirtualItem vi = VirtualItem.fromItemStack(i);
            if (sb.length() > 0) sb.append(", ");
            sb.append(vi.toDisplayString());
        }
        return sb.toString();
    }

    //item:{item1:{[...] chance:50} item2:{} item3:{}

    public static VirtualItem itemFromMap(Param params) {
        return itemVersion ? VirtualItem.fromMap(params.getMap()) : VirtualItem18.fromMap(params.getMap());

    }

    public static List<ItemStack> parseItemsSet(Param params) {
        List<ItemStack> items = new ArrayList<ItemStack>();
        for (String key : params.keySet()) {
            if (key.matches("item\\d+|ITEM\\d+")) {
                String itemStr = params.getParam(key, "");
                VirtualItem vi = itemFromString(itemStr);
                if (vi != null) items.add(vi);
            }
        }
        if (items.isEmpty()) {
            VirtualItem item = itemFromMap(params);
            if (item != null) items.add(item);
        }
        return items;
    }

    /*
     * set1:{item1:{}  item2:{} item3:{} chance:50}  set2:{item1:{}  item2:{} item3:{} chance:50}
     *
     *
     */
    public static List<ItemStack> parseRandomItemsStr(String items) {
        Param params = new Param(items);
        if (params.matchAnyParam("set\\d+|SET\\d+")) {
            Map<List<ItemStack>, Integer> sets = new HashMap<List<ItemStack>, Integer>();
            int maxChance = 0;
            int nochcount = 0;
            for (String key : params.keySet()) {
                if (!key.matches("set\\d+|SET\\d+")) continue;
                Param itemParams = new Param(params.getParam(key));
                List<ItemStack> itemList = parseItemsSet(itemParams);
                if (itemList == null || itemList.isEmpty()) continue;
                int chance = itemParams.getParam("chance", -1);
                if (chance > 0) maxChance += chance;
                else nochcount++;
                sets.put(itemList, chance);
            }
            int eqperc = (nochcount * 100) / sets.size();
            maxChance = maxChance + eqperc * nochcount;
            int rnd = tryChance(maxChance);
            int curchance = 0;
            for (List<ItemStack> stack : sets.keySet()) {
                curchance = curchance + (sets.get(stack) < 0 ? eqperc : sets.get(stack));
                if (rnd <= curchance) return stack;
            }
        } else if (params.matchAnyParam("item\\d+|ITEM\\d+")) {
            return parseItemsSet(params);
        } else {
            VirtualItem vi = itemFromString(items);
            if (vi != null) {
                List<ItemStack> iList = new ArrayList<ItemStack>();
                iList.add(vi);
                return iList;
            }

        }
        return null;
    }


    //id:data*amount@enchant:level,color;id:data*amount%chance/id:data*amount@enchant:level,color;id:data*amount%chance
    public static String parseRandomItemsStrOld(String items) {
        if (items.isEmpty()) return "";
        String[] loots = items.split("/");
        Map<String, Integer> drops = new HashMap<String, Integer>();
        int maxchance = 0;
        int nochcount = 0;
        for (String loot : loots) {
            String[] ln = loot.split("%");
            if (ln.length > 0) {
                String stacks = ln[0];
                if (stacks.isEmpty()) continue;
                int chance = -1;
                if ((ln.length == 2) && (ln[1].matches("[1-9]+[0-9]*"))) {
                    chance = Integer.parseInt(ln[1]);
                    maxchance += chance;
                } else nochcount++;
                drops.put(stacks, chance);
            }
        }
        if (drops.isEmpty()) return "";
        int eqperc = (nochcount * 100) / drops.size();
        maxchance = maxchance + eqperc * nochcount;
        int rnd = tryChance(maxchance);
        int curchance = 0;
        for (String stack : drops.keySet()) {
            curchance = curchance + (drops.get(stack) < 0 ? eqperc : drops.get(stack));
            if (rnd <= curchance) return stack;
        }
        return "";
    }

    private static int tryChance(int chance) {
        return random.nextInt(chance);
    }

    public static String toDisplayString(String itemStr) {
        VirtualItem vi = itemFromString(itemStr);
        if (vi != null) return vi.toDisplayString();
        Map<String, String> itemMap = VirtualItem.parseParams(itemStr);
        String name = itemMap.containsKey("name") ? itemMap.get("name") : itemMap.containsKey("type") ? itemMap.get("type") : null;
        if (name == null) return itemStr;
        int amount = getAmount(itemStr);
        String data = VirtualItem.getParam(itemMap, "data", "0");
        StringBuilder sb = new StringBuilder(name);
        if (!itemMap.containsKey("name") && !data.equals("0")) sb.append(":").append(data);
        if (amount > 1) sb.append("*").append(amount);
        return ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', sb.toString()));
    }

}
