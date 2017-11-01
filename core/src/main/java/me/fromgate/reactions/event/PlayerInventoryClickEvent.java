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
    private SlotType slotType;
    private InventoryType inventory;
    private ItemStack item;
    private Integer numberKey;
    private Integer slot;
    private InventoryView inventoryView;
    private String inventoryName;

    public PlayerInventoryClickEvent(Player p, InventoryAction action, ClickType click, Inventory inventory, SlotType slotType, ItemStack item, Integer numberKey, InventoryView inventoryView, Integer slot) {
        super(p);
        this.inventoryName = inventory.getName();
        this.action = action;
        this.click = click;
        this.inventory = inventory.getType();
        this.slotType = slotType;
        this.item = item;
        this.numberKey = numberKey;
        this.slot = slot;
        this.inventoryView = inventoryView;
    }

    public String getInventoryName() {
        return this.inventoryName;
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
        return this.slotType;
    }

    public ItemStack getItemStack() {
        return this.item;
    }

    public Integer getNumberKey() {
        return this.numberKey;
    }

    public Integer getSlot() {
        return this.slot;
    }

    public Inventory getBottomInventory() {
        return this.inventoryView.getBottomInventory();
    }

}
