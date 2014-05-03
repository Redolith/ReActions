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

package me.fromgate.reactions.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.fromgate.reactions.RAUtil;
import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.activators.Activator;
import me.fromgate.reactions.activators.Activator.FlagVal;
import me.fromgate.reactions.externals.RAEconomics;
import me.fromgate.reactions.flags.Flags;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum Placeholders {
	TIME_SERVER ("servertime",false),
	TIME_INGAME("curtime",false),
	PLAYER_LOC,
	PLAYER_LOC_EYE,
	PLAYER_LOC_VIEW,
	PLAYER_NAME ("player",true),
	PLAYER_DISPLAY ("dplayer",true),
	HEALTH,
	PLAYER_LOC_DEATH ("deathpoint", true),
	TARGET_PLAYER ("targetplayer", true),
	MONEY,
	RANDOM ("rnd",false);
	
	private String alias;
	boolean needPlayer;
	
	Placeholders (String alias, boolean needPlayer){
		this.alias = alias;
		this.needPlayer = needPlayer;
	}
	
	Placeholders (){
		this.alias = this.name();
		this.needPlayer = true;
	}
	
	public static Placeholders getByName(Player player, String id){
		Placeholders ph = null;
		for (Placeholders p : Placeholders.values()){
			if (p.needPlayer&&(player == null)) continue;
			if (p.alias.equalsIgnoreCase(id)||p.name().equalsIgnoreCase(id)
					||(p==Placeholders.MONEY&&id.toUpperCase().startsWith("MONEY"))) {
				ph = p;
				break;
			}
		}
		return ph;
	}
	

    private static RAUtil u(){
        return ReActions.util;
    }

    public static Map<String,String> replaceAllPlaceholders (Player p, Activator a, Map<String,String> params){
        Map<String,String> newparams = new HashMap<String,String>();
        for (String key : params.keySet())
            newparams.put(key, replacePlaceholders(p,a,params.get(key)));
        return newparams;
    }
    
    
    public static String replacePlaceholders(Player player, Activator activator, String string){
    	String result = string;
    	Pattern pattern = Pattern.compile("(%\\w+:\\S+%)|(%\\w+%)");
    	Matcher matcher = pattern.matcher(string);
    	while (matcher.find()){
    		result = replacePlaceholder (player,activator,matcher.group(), result);
    	}
    	result = Variables.replacePlaceholders(player, result);
    	return result;
    }
    
    public static String replacePlaceholder(Player player, Activator activator, String field, String string){
    	String resultField = field;
    	String key = (field.replaceFirst("%", "")).replaceAll("(:\\S+%$)|(%$)","");
    	Placeholders placeholder = Placeholders.getByName(player, key);
    	if (placeholder != null){
    		switch (placeholder){
			case HEALTH:
				resultField = Double.toString(BukkitCompatibilityFix.getEntityHealth(player));
				break;
			case MONEY:
            	Map<String,String> params = RAEconomics.getBalances(player);
            	String mkey = field.replaceAll("%", "");
				resultField = params.containsKey(mkey) ? params.get(mkey) : field;
				break;
			case PLAYER_DISPLAY:
				resultField = player.getDisplayName();
				break;
			case PLAYER_LOC:
				resultField = Util.locationToString(player.getLocation());
				break;
			case PLAYER_LOC_DEATH:
                Location loc = RAPVPRespawn.getLastDeathPoint(player);
                if (loc == null) loc = player.getLocation();
                resultField = Util.locationToString(loc);
				break;
			case PLAYER_LOC_EYE:
				resultField = Util.locationToString(player.getEyeLocation());
				break;
			case PLAYER_LOC_VIEW:
				resultField = Util.locationToString(getViewLocation(player));
				break;
			case PLAYER_NAME:
				resultField = player.getName();
				break;
			case RANDOM:
				String value = field.replaceFirst("(%\\w+:)", "").replaceAll("%$", "");
				resultField = random(value);
				break;
			case TARGET_PLAYER:
				resultField = activator.getTargetPlayer();
				break;
			case TIME_INGAME:
				resultField = Util.timeToString ( (player==null ? Bukkit.getWorlds().get(0).getTime() : player.getWorld().getTime()),false);
				break;
			case TIME_SERVER:
				resultField = Util.timeToString(System.currentTimeMillis(), false);
				break;
			default:
				break;
    		}
    	} else {
    		for (FlagVal flg : activator.getFlags())
                if (flg.flag.equals(key)) {
                	resultField = formatFlagParam (key, flg.value);
                	break;
                }
    	}
    	if (!resultField.equalsIgnoreCase(field)) return string.replace(field, resultField);
    	return string;
    }
    
    @SuppressWarnings("deprecation")
    private static Location getViewLocation (Player p){
        Block b = p.getTargetBlock(null, 100);
        if (b== null) return p.getLocation();
        return b.getLocation().add(0.5, 0.5, 0.5);
    }


    private static String random (String rndStr){
    	if (rndStr.matches("\\d+")) return Integer.toString(u().getRandomInt(Integer.parseInt(rndStr)));
    	if (rndStr.matches("\\d+\\-\\d+")) return Integer.toString(Util.getMinMaxRandom(rndStr));
    	if (rndStr.matches("[\\S,]*[\\S]")){
    		String [] ln = rndStr.split(",");
    		if (ln.length==0) return rndStr;
    		return ln[u().getRandomInt(ln.length)];
    	}
    	return rndStr;
    }
    
    private static String formatFlagParam(String flag, String value) {
        String rst = value;
        Flags f = Flags.getByName(flag);
        switch (f){
        case TIME:
            if (!(value.equals("day")||value.equals("night"))){
                String [] ln = value.split(",");
                String r = "";
                if (ln.length>0)
                    for (int i = 0; i<ln.length;i++){
                        if (!u().isInteger(ln[i])) continue;
                        String tmp = String.format("%02d:00", Integer.parseInt(ln[i]));
                        if (i == 0) r = tmp;
                        else r = r+", "+tmp;
                    }
                if (!r.isEmpty()) rst = r;
            }
            break;
        case CHANCE: 
            rst = value +"%";
            break;
        default:
            break;
        }
        return rst; 
    }

    public static void listPlaceholders (CommandSender sender, int pageNum) {
    	List<String> phList= new ArrayList<String>();
    	for (Placeholders ph : Placeholders.values()){
    		String name = ph.name();
    		String alias = ph.alias.equalsIgnoreCase(name) ? " " : " ("+ph.alias+") ";
    		String description = ReActions.util.getMSGnc("placeholder_"+name);
    		phList.add("&6"+name+"&e"+alias+"&3: &a"+description);
    	}
    	for (Flags f : Flags.values()){
    		if (f != Flags.TIME && f != Flags.CHANCE) continue;
    		String name = f.name();
    		String description = ReActions.util.getMSGnc("placeholder_"+name);
    		phList.add("&6"+name+"&3: &a"+description);
    	}
    	phList.add("&6VAR&3: &a"+ReActions.util.getMSGnc("placeholder_VAR"));
    	ReActions.util.printPage(sender, phList, pageNum, "msg_placeholderlisttitle", "", false, sender instanceof Player ? 10 : 1000);
    }

}

