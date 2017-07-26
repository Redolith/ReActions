/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2017, fromgate, fromgate@gmail.com
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

package me.fromgate.reactions.event;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;


public class PlayerInventoryClickEvent extends RAEvent {
    private InventoryAction action;
    private ClickType click;
    private SlotType slot;
    private InventoryType inventory;
    private ItemStack item;
    private Integer numberKey;
    private InventoryView inventoryView;

    public PlayerInventoryClickEvent(Player p, InventoryAction action, ClickType click, Inventory inventory, SlotType slot, ItemStack item, Integer numberKey, InventoryView inventoryView) {
        super(p);
        this.action = action;
        this.click = click;
        this.inventory = inventory.getType();
        this.slot = slot;
        this.item = item;
        this.numberKey = numberKey;
        this.inventoryView = inventoryView;
    }

    public InventoryAction getAction() {
        return this.action;
    }

    public ClickType getClickType() {
        return this.click;
    }

    public InventoryType getInventoryType() {
        return this.inventory;
    }

    public SlotType getSlotType() {
        return this.slot;
    }

    public ItemStack getItemStack() {
        return this.item;
    }

    public Integer getNumberKey() {
        return this.numberKey;
    }

    public Inventory getBottomInventory() {
        return this.inventoryView.getBottomInventory();
    }

}
