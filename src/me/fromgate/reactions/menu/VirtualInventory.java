package me.fromgate.reactions.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.fromgate.reactions.util.ItemUtil;
import me.fromgate.reactions.util.ParamUtil;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class VirtualInventory {

	int size;
	String title;
	List<String> slots;
	List<String> execs;
	
	public VirtualInventory(int size, String title){
		this.size = (size%9 ==0) ? size : ((size/9)+1)*9;
		this.title = title;
		this.slots = InventoryMenu.getEmptyList(this.size);
		this.execs= InventoryMenu.getEmptyList(this.size);
	}
	
	public void save(YamlConfiguration cfg, String root){
		cfg.set(root+".title", title);
		cfg.set(root+".size", size);
		for (int i = 0; i<size; i++){
			if (!slots.get(i).isEmpty())
				cfg.set(root+".slot"+Integer.toString(i+1)+".item", slots.get(i));
			if (!execs.get(i).isEmpty())
				cfg.set(root+".slot"+Integer.toString(i+1)+".activator", execs.get(i));
		}
	}
	
	public VirtualInventory(YamlConfiguration cfg, String root){
		this(9,"&4Re&6Actions menu");
		load (cfg,root);
	}

	
	public void load(YamlConfiguration cfg, String root){
		this.title = cfg.getString(root+".title","&4Re&6Actions menu");
		this.size = cfg.getInt(root+".size", 9);
		size = (size%9 ==0) ? size : ((size/9)+1)*9;
		this.slots = InventoryMenu.getEmptyList(this.size);
		this.execs= InventoryMenu.getEmptyList(this.size);		
		for (int i = 1; i<=size; i++){
			this.slots.set(i-1, cfg.getString(root+".slot"+Integer.toString(i)+".item", ""));
			this.execs.set(i-1, cfg.getString(root+".slot"+Integer.toString(i)+".activator", ""));
		}
	}

	public Inventory getInventory(){
		Inventory inv= Bukkit.createInventory(null, (size%9 ==0) ? size : ((size/9)+1)*9, ChatColor.translateAlternateColorCodes('&', title));
		for (int i = 0; i<slots.size();i++){
			if (slots.get(i).isEmpty()) continue;
			ItemStack item = ItemUtil.parseItemStack(slots.get(i));
			if (item == null) continue;
			inv.setItem(i, item);
		}
		return inv;
	}
	
	public List<String> getActivators(){
		return this.execs;
	}
	
	public VirtualInventory(Map<String,String> params){
		title = ParamUtil.getParam(params, "title", "&4Re&6Actions &eMenu");
		size = ParamUtil.getParam(params, "size", 9);
		size = (size%9 ==0) ? size : ((size/9)+1)*9;
		slots = new ArrayList<String>();
		execs= new ArrayList<String>();
		
		for (int i = 1; i<=size; i++){
			execs.add(ParamUtil.getParam(params, "exec"+Integer.toString(i), ""));
			slots.add(ParamUtil.getParam(params, "exec"+Integer.toString(i), ""));
		}
	}

}
