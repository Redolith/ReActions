/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2014, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/reactions/
 *    
 *  This file is part of ReActions.
 *  
 *  ReActions is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ReActions is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with ReActions.  If not, see <http://www.gnorg/licenses/>.
 * 
 */

package me.fromgate.reactions.actions;

import java.util.List;
import java.util.Map;

import me.fromgate.reactions.util.ItemUtil;
import me.fromgate.reactions.util.Locator;
import me.fromgate.reactions.util.ParamUtil;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.Variables;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ActionItems extends Action {
    private int actionType=0;
    public ActionItems (int actionType){
        this.actionType=actionType;
    }

    @Override
    public boolean execute(Player p, Map<String, String> params) {
        switch (actionType){
        case 0: return giveItemPlayer(p,ParamUtil.getParam(params, "param-line", ""));
        case 1: return removeItemInHand(p,params);
        case 2: return removeItemInInventory(p,params);
        case 3: return dropItems(p,params);
        case 4: return wearItem(p,params);
        case 5: return openInventory(p,ParamUtil.getParam(params, "param-line", ""));
        }
        return true;
    }

	/**
     * Реализует действие ITEM_WEAR
     * @param p - игрок 
     * @param params - параметры: item - одеваемый предмет
     *                            slot - слот куда одеваем (helmet, chestplate, leggins, boots, auto)
     *                            exist - что делаем с уже надетым предметов (remove, undress, drop, keep)
     * @return - возвращает true если удалось нацепить предмет на игрока 
     */
    private boolean wearItem(Player p, Map<String, String> params) {
        String itemStr = ParamUtil.getParam(params, "item", "");
        int slot = 4; //4 - auto, 3 - helmete, 2 - chestplate, 1 - leggins, 0 - boots
        int existDrop = 1; // 0 - remove, 1 - undress, 2 - drop
        
        if (itemStr.isEmpty()) itemStr = ParamUtil.getParam(params, "param-line", "");
        else {
            String slotStr = ParamUtil.getParam(params, "slot", "auto");
            if (slotStr.equalsIgnoreCase("helmet")||slotStr.equalsIgnoreCase("helm")) slot = 3;
            else if (slotStr.equalsIgnoreCase("chestplate")||slotStr.equalsIgnoreCase("chest")) slot = 2;
            else if (slotStr.equalsIgnoreCase("leggins")||slotStr.equalsIgnoreCase("leg")) slot = 1;
            else if (slotStr.equalsIgnoreCase("boots")||slotStr.equalsIgnoreCase("boot")) slot = 0;
            else slot = 4;
            
            String existStr= ParamUtil.getParam(params, "exist", "undress");
            if (existStr.equalsIgnoreCase("remove")) existDrop = 0;
            else if (existStr.equalsIgnoreCase("undress")) existDrop = 1;
            else if (existStr.equalsIgnoreCase("drop")) existDrop = 2;
            else if (existStr.equalsIgnoreCase("keep")) existDrop = 3;
            else existDrop = 1;
            
        }
        ItemStack item = ItemUtil.parseItemStack(itemStr);
        if (item == null) return false;
        if (slot==4) slot = getSlotByItem (item);
        return setArmourItem (p, slot,item,existDrop);
    }
    
    
    private boolean setArmourItem (Player player, int slot, ItemStack item, int existDrop){
        ItemStack[] armour = player.getInventory().getArmorContents().clone();
        ItemStack oldItem = armour[slot].clone();
        if (oldItem!=null&&oldItem.getType()!=Material.AIR&&(existDrop==3)) return false; // сохраняем и уходим
        armour[slot] = item;
        player.getInventory().setArmorContents(armour);
        if (existDrop==1) ItemUtil.giveItemOrDrop(player, oldItem);
        else if (existDrop==2) player.getWorld().dropItemNaturally(player.getLocation(), oldItem);
        return true;
    }
    

    private int getSlotByItem (ItemStack item){
        switch (item.getType()){
        case LEATHER_BOOTS:
        case CHAINMAIL_BOOTS:
        case IRON_BOOTS:
        case GOLD_BOOTS:
        case DIAMOND_BOOTS:
            return 0;
        case LEATHER_LEGGINGS:
        case CHAINMAIL_LEGGINGS:
        case IRON_LEGGINGS:
        case GOLD_LEGGINGS:
        case DIAMOND_LEGGINGS:
            return 1;
        case LEATHER_CHESTPLATE:
        case CHAINMAIL_CHESTPLATE:
        case IRON_CHESTPLATE:
        case GOLD_CHESTPLATE:
        case DIAMOND_CHESTPLATE:
            return 2;
        case LEATHER_HELMET:
        case CHAINMAIL_HELMET:
        case IRON_HELMET:
        case GOLD_HELMET:
        case DIAMOND_HELMET:
        case PUMPKIN:
            return 3;
        default:
            return 3;
        }
    }
    
    
    private boolean removeItemInHand(Player p, Map<String,String>params){
        String istr = ParamUtil.getParam(params, "param-line", "");
        if (istr.isEmpty()) return false; 
        if (!ItemUtil.removeItemInHand(p, istr)) return false;
        ItemStack item = ItemUtil.parseItemStack(istr);
    	String actionItems = Util.itemToString(item);
        setMessageParam(actionItems);
        Variables.setTempVar("action_items", actionItems);
        return true;
    }
    private boolean removeItemInInventory(Player p, Map<String,String>params){
        String istr = ParamUtil.getParam(params, "param-line", "");
        if (istr.isEmpty()) return false; 
        ItemUtil.removeItemInInventory(p, istr);
        ItemStack item = ItemUtil.parseItemStack(istr);
    	String actionItems = Util.itemToString(item);
        setMessageParam(actionItems);
        Variables.setTempVar("action_items", actionItems);
        return true;
    }

    private boolean giveItemPlayer(final Player p, final String param) {
        final List<ItemStack> items = Util.parseRandomItems(param);
        if (!items.isEmpty()){
        	String actionItems = Util.itemsToString(items);
            setMessageParam(actionItems);
            Variables.setTempVar("action_items", actionItems);
            Bukkit.getScheduler().scheduleSyncDelayedTask(plg(), new Runnable(){
                public void run(){
                    for (ItemStack i : items)
                        ItemUtil.giveItemOrDrop(p, i);
                }
            }, 1);
            return true;
        }
        return false;
    }
    
    private boolean openInventory(Player p, String itemStr) {
    	List<ItemStack> items = Util.parseRandomItems(itemStr);
    	if (items.isEmpty())return false;
    	String actionItems = Util.itemsToString(items);
        setMessageParam(actionItems);
        Variables.setTempVar("action_items", actionItems);
        int size = Math.min(items.size(),36);
        Inventory inv = Bukkit.createInventory(null, size);
    	for (int i = 0; i<size; i++)
    		inv.setItem(i, items.get(i));
		return true;
	}


    public boolean dropItems(Player p, Map<String, String> params) {
        int radius = ParamUtil.getParam(params, "radius", 0);
        Location loc = Locator.parseLocation(ParamUtil.getParam(params, "loc", ""),p.getLocation());
        if (loc == null) loc = p.getLocation();
        boolean scatter = ParamUtil.getParam(params, "scatter", true);
        boolean land = ParamUtil.getParam(params, "land", true);
        List<ItemStack> items = Util.parseRandomItems(ParamUtil.getParam(params, "item", ""));
        if (items.isEmpty()) return false;
        if (radius==0) scatter = false;
        Location l = Locator.getRadiusLocation(loc, radius, land);
        for (ItemStack i : items){
            loc.getWorld().dropItemNaturally(l, i);
            if (scatter) l = Locator.getRadiusLocation(loc, radius, land);
        }
    	String actionItems = Util.itemsToString(items);
        setMessageParam(actionItems);
        Variables.setTempVar("action_items", actionItems);
        return true;
    }

}
