package me.fromgate.reactions.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.fromgate.reactions.RAUtil;
import me.fromgate.reactions.ReActions;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Variables {
	static RAUtil  u(){
		return ReActions.util;
	}

	private static HashMap<String,String> vars = new HashMap<String,String>();
	private static HashMap<String,String> tempvars = new HashMap<String,String>();

	private static String varId(Player player, String var){
		return player == null ? "general."+var : player.getName()+"."+var;
	}
	
	private static String varId(String player, String var){
		return player.isEmpty() ? "general."+var : player+"."+var;
	}

	public static void setVar (String player, String var, String value){
		vars.put(varId (player,var), value);
		save();
	}
	
	public static void setVar (Player player, String var, String value){
		vars.put(varId (player,var), value);
		save();
	}
	
	public static boolean removeVar(String playerName, String id){
		String varId = varId(playerName, id);
		if (!vars.containsKey(varId)) return false;
		vars.remove(varId);
		return true;
	}

	public static void clearVar (Player player, String var){
		String id = varId(player, var);
		if (vars.containsKey(id)) vars.remove(id);
		save();
	}

	public static String getVar (String player, String var, String defvar){
		String id = varId(player, var);
		if (vars.containsKey(id)) return vars.get(id);
		return defvar;
	}
	
	public static String getVar (Player player, String var, String defvar){
		String id = varId(player, var);
		if (vars.containsKey(id)) return vars.get(id); 
		return defvar;
	}

	public static boolean cmpVar (Player player, String var, String cmpvalue){
		String id = varId(player, var);
		if (!vars.containsKey(id)) return false;
		String value = getVar(player,var,"");
		if (u().isIntegerSigned(cmpvalue,value)) return (Integer.parseInt(cmpvalue)==Integer.parseInt(value));
		return value.equalsIgnoreCase(cmpvalue);
	}

	public static boolean cmpGreaterVar (Player player, String var, String cmpvalue){
		String id = varId(player, var);
		if (!vars.containsKey(id)) return false;
		if (!u().isIntegerSigned(vars.get(id),cmpvalue)) return false;
		return Integer.parseInt(vars.get(id))>Integer.parseInt(cmpvalue);
	}

	public static boolean cmpLowerVar (Player player, String var, String cmpvalue){
		String id = varId(player, var);
		if (!vars.containsKey(id)) return false;
		if (!u().isIntegerSigned(vars.get(id),cmpvalue)) return false;
		return Integer.parseInt(vars.get(id))<Integer.parseInt(cmpvalue);
	}

	public static boolean existVar(Player player, String var){
		return (vars.containsKey(varId(player, var)));
	}

	public static boolean incVar (Player player, String var){
		return incVar (player, var, 1);
	}

	public static boolean decVar (Player player, String var){
		return incVar (player, var, -1);
	}

	public static boolean incVar (Player player, String var, int addValue){
		String id = varId(player, var);
		if (!vars.containsKey(id)) setVar(player,var,"0");
		String valueStr = vars.get(id);
		if (!u().isIntegerSigned(valueStr)) return false; 
		setVar(player,var,String.valueOf(Integer.parseInt(valueStr)+addValue));
		return true;
	}
	public static boolean decVar (Player player, String var, int decValue){
		return incVar (player, var, decValue*(-1));
	}

	public static boolean mergeVar(Player player, String var, String stringToMerge,boolean spaceDivider){
		String space = spaceDivider ? " " : "";
		String id = varId(player, var);
		if (!vars.containsKey(id)) setVar(player,var,"");
		setVar(player,var,getVar(player,var,"")+space+stringToMerge);
		return false;
	}


	public static void save(){
		try {
			YamlConfiguration cfg = new YamlConfiguration();
			File f = new File (ReActions.instance.getDataFolder()+File.separator+"variables.yml");
			if (f.exists()) f.delete();
			f.createNewFile();
			for (String key : vars.keySet())
				cfg.set(key, vars.get(key));
			cfg.save(f);
		} catch (Exception e){
		}
	}

	public static void load(){
		vars.clear();
		try {
			YamlConfiguration cfg = new YamlConfiguration();
			File f = new File (ReActions.instance.getDataFolder()+File.separator+"variables.yml");
			if (!f.exists()) return;
			cfg.load(f);
			for (String key : cfg.getKeys(true)){
				if (!key.contains(".")) continue;
				vars.put(key,cfg.getString(key));
			}
		} catch (Exception e){
		}
	}

	public static String replacePlaceholders(Player p, String str){
		String newStr = str;
		if (!str.isEmpty()&&(str.contains("%var:")||str.contains("%varp:"))){
			for (String key : vars.keySet()){
				if (key.startsWith("general")){
					String id = key.replaceFirst("general.", "");
					newStr = newStr.replaceAll("%var:"+id+"%", vars.get(key));
				} else if ((p!=null)&&(key.startsWith(p.getName()))){
					String id = key.replaceFirst(p.getName()+".", "");
					newStr = newStr.replaceAll("%varp:"+id+"%", vars.get(key));
				}
			}
		}
		return newStr;
	}


	/*
	 *  Temporary variables - replacement for place holders
	 */

	public static String replaceTempVars(String str){
		if (str.isEmpty()) return str;
		String newStr = str;
		for (String key : tempvars.keySet()){
			newStr = newStr.replaceAll("%"+key+"%", tempvars.get(key));
		}
		return newStr;
	}

	public static void setTempVar (String varId, String value){
		vars.put(varId, value);
	}

	public static void clearTempVar (String varId){
		if (vars.containsKey(varId)) vars.remove(varId);
	}

	public static void clearAllTempVar (String varId){
		tempvars.clear();
	}

	public static String getTempVar (String varId){
		return getTempVar (varId,"");
	}
	public static String getTempVar (String varId, String defvar){
		if (tempvars.containsKey(varId)) return tempvars.get(varId);
		return defvar;
	}

	public static void printList(CommandSender sender, int page, String mask) {
		int maxPage = (sender instanceof Player) ? 15 : 10000;
		List<String> varList = new ArrayList<String> ();
		for (String key : vars.keySet())
				if (mask.isEmpty()||key.toLowerCase().contains(mask.toLowerCase()))
					varList.add(key+" : "+vars.get(key));
		u().printPage(sender, varList, page, "msg_varlist", "", false, maxPage);
	}




}
