/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2013, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/reactions/
 *   * 
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

package me.fromgate.reactions;

import java.text.DecimalFormat;
import java.util.Map;
import me.fromgate.reactions.activators.Activator;
import me.fromgate.reactions.activators.Activator.FlagVal;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;



public class Actions {
    static String atypes = "tp,velocity,sound,potion,rmvpot,grpadd,grprmv,msg,dmg,townset,townkick,itemrmv,invitemrmv,itemgive,cmdplr,cmdop,cmdsrv,moneypay,moneygive,delay,pdelay,back,mob,effect";

    static ReActions plg;
    static RAUtil u;

    public static void init(ReActions plugin){
        plg = plugin;
        u = plg.u;
    }


    public static void executeActivator (Player p, Activator act){
        if (Flag.checkFlags(p, act)){
            if (act.getActions().size()>0)
                for (int i = 0; i<act.getActions().size(); i++)
                    performAction (p, act,true, act.getActions().get(i).flag,act.getActions().get(i).value);
        } else {
            if (act.getReactions().size()>0)
                for (int i = 0; i<act.getReactions().size(); i++)
                    performAction (p, act,false, act.getReactions().get(i).flag,act.getReactions().get(i).value);
        }
    }

    //String atypes = "tp,grpadd,grprmv,msg,dmg,townset,townkick,itemrmv,itemgive,cmdplr,cmdsrv,moneypay,moneygive";
    public static void performAction(Player p, Activator a, boolean doact, String act, String param){
        String actname = a.getName();
        String actkey = "act_"+act;
        boolean annoying = a.isAnnoying();
        Map<String,String> params = Util.parseActionParam(param);
        String msgparam =param;

        if (act.equalsIgnoreCase("tp")){
            Location loc = teleportPlayer (p,params);
            if (loc!= null) msgparam = Util.locationToStringFormated(loc);
            else actkey=actkey+"fail";
        } else if (act.equalsIgnoreCase("effect")){
            playEffect(p,params);
        } else if (act.equalsIgnoreCase("velocity")){
            setPlayerVelocity(p,params);
        } else if (act.equalsIgnoreCase("mob")){
            mobSpawn(p, params);
        } else if (act.equalsIgnoreCase("back")){
            int prev = 1;
            if (u.isIntegerGZ(param)) prev = Integer.parseInt(param);
            RAPushBack.teleportToPrev(p, prev);
        } else if (act.equalsIgnoreCase("sound")&&(!param.isEmpty())){
            soundPlay (p, params);
        } else if (act.equalsIgnoreCase("potion")&&(!param.isEmpty())){
            potionEffect (p, params);
        } else if (act.equalsIgnoreCase("rmvpot")&&(!param.isEmpty())){
            removePotionEffect (p, param);
        } else if (act.equalsIgnoreCase("grpadd")&&plg.vault.isPermissionConected()&&(!param.isEmpty())){
            if (!plg.vault.playerAddGroup(p, param)) actkey=actkey+"fail";
        } else if (act.equalsIgnoreCase("grprmv")&&plg.vault.isPermissionConected()&&(!param.isEmpty())){
            if (plg.vault.playerInGroup(p, param)) {
                if (!plg.vault.playerRemoveGroup(p, param)) actkey=actkey+"fail";
            }
        } else if (act.equalsIgnoreCase("msg")&&(!param.isEmpty())){
            sendMessage (p, actname, annoying, doact,  replacePlaceholders(p,a,param));
        } else if (act.equalsIgnoreCase("dmg")&&(!param.isEmpty())&&(param.matches("[1-9]+[0-9]*"))){
            p.damage(Integer.parseInt(param));
        } else if (act.equalsIgnoreCase("dmg")){
            p.playEffect(EntityEffect.HURT);
            actkey=actkey+"hit";
        } else if (act.equalsIgnoreCase("msgall")&&(!param.isEmpty())){
            sendBroadCastMsg (p,replacePlaceholders(p,a,param));
        } else if (act.equalsIgnoreCase("townset")&&(!param.isEmpty())){
            addToTown(p, param);
        } else if (act.equalsIgnoreCase("townkick")){
            kickFromTown (p);
        } else if (act.equalsIgnoreCase("invitemrmv")&&(!param.isEmpty())){
            removeItemFromInventory(p, param);
        } else if (act.equalsIgnoreCase("itemrmv")&&(!param.isEmpty())){
            removeItemInHand(p, param);
        } else if (act.equalsIgnoreCase("itemgive")&&(!param.isEmpty())){
            giveItemPlayer(p,param);
        } else if (act.equalsIgnoreCase("cmdop")&&(!param.isEmpty())){
            executeCommandAsOp (p,replacePlaceholders(p,a,param));
        } else if (act.equalsIgnoreCase("cmdplr")&&(!param.isEmpty())){
            executeCommand (p,false,replacePlaceholders(p,a,param));
        } else if (act.equalsIgnoreCase("cmdsrv")&&(!param.isEmpty())){
            executeCommand (p,true,replacePlaceholders(p,a,param));
        } else if (act.equalsIgnoreCase("moneypay")&&(plg.vault.isEconomyConected())&&(!param.isEmpty())){
            //msgparam = Integer.toString(moneyPay (p, param))+" "+plg.economy.currencyNamePlural();
            msgparam = plg.vault.formatMoney(Integer.toString(moneyPay (p, param)));
        } else if (act.equalsIgnoreCase("moneygive")&&(plg.vault.isEconomyConected())&&(!param.isEmpty())){
            //msgparam = Integer.toString(moneyGive (p, param))+" "+plg.economy.currencyNamePlural();
            msgparam  = plg.vault.formatMoney(Integer.toString(moneyGive (p, param)));
        } else if (act.equalsIgnoreCase("delay")&&(!param.isEmpty())){
            setDelay(p, param);
        } else if (act.equalsIgnoreCase("pdelay")&&(!param.isEmpty())){
            setPersonalDelay(p, param);
        }
        if (u.isWordInList(act, plg.actionmsg)) u.printMSG(p, "act_"+act,msgparam);
    }


    private static void playEffect(Player p, Map<String,String> params) {
        RAEffects.playEffect(p, params);
    }


    private static void setPlayerVelocity(Player p, Map<String,String> params) {
        String velstr = "";
        boolean multiply = false;
        if (params.containsKey("param")){
            velstr = Util.getParam(params, "param", "");
        } else {
            velstr = Util.getParam(params, "direction","");
            multiply = Util.getParam(params, "multiply", false);
        }
        
        if (velstr.isEmpty()) return;
        Vector v = p.getVelocity();
        String [] ln = velstr.split(",");
        if ((ln.length == 1)&&(velstr.matches("-?(([0-9]+\\.[0-9]*)|([0-9]+))"))) {
            double power = Double.parseDouble(velstr);
            v.setY(Math.min(10, multiply ? power*p.getVelocity().getY() : power));
        } else if ((ln.length == 3)&&
                ln[0].matches("-?(([0-9]+\\.[0-9]*)|([0-9]+))")&&
                ln[1].matches("-?(([0-9]+\\.[0-9]*)|([0-9]+))")&&
                ln[2].matches("-?(([0-9]+\\.[0-9]*)|([0-9]+))")) {
            double powerx = Double.parseDouble(ln[0]);
            double powery = Double.parseDouble(ln[1]);
            double powerz = Double.parseDouble(ln[2]);
            if (multiply){
             powerx = powerx*p.getVelocity().getX();
             powery = powery*p.getVelocity().getY();
             powerz = powerz*p.getVelocity().getZ();
            }
            
            v = new Vector (Math.min(10,powerx),Math.min(10,powery),Math.min(10,powerz));
        }
        p.setVelocity(v);
    }


    private static void mobSpawn(Player p, Map<String,String> params) {
        String mob = Util.getParam(params, "type", "PIG");
        String locstr = Util.getParam(params, "loc", "");
        Location loc = locToLocation(p,locstr);
        int radius = Util.getParam(params, "radius", 0);
        int num=Util.getMinMaxRandom(Util.getParam(params, "num", "1"));
        String hparam = Util.getParam(params, "health", "1");
        double health = Util.getMinMaxRandom(hparam);
        String playeffect = Util.getParam(params, "effect", "");
        String chest = Util.getParam(params, "chest", "");
        String leg = Util.getParam(params, "leg", "");
        String helm = Util.getParam(params, "helm", "");
        String boot = Util.getParam(params, "boot", "");
        String weapon = Util.getParam(params, "weapon", "");
        boolean land = Util.getParam(params, "land", true);
        
        
        String name = Util.getParam(params, "name", ""); 
        
        for (int i = 0; i<num; i++){
            if (loc == null) loc = p.getLocation();
            Location l = Util.getRandomLocationInRadius(loc, radius,land);
            Entity e = loc.getWorld().spawnEntity(l, EntityType.fromName(mob));
            if (e == null) break;
            if (!(e instanceof LivingEntity)) {
                e.remove();
                break;
            }
            
            if (!playeffect.isEmpty()){
                int data = 0;
                if (playeffect.equalsIgnoreCase("smoke")) data = 9;
                RAEffects.playEffect(loc, playeffect, data);
            }
            
            LivingEntity le = (LivingEntity)e;
            if (health>0){
                le.setMaxHealth(health);
                le.setHealth(health);
            }
            if (!name.isEmpty()){
                le.setCustomName(name);    
                le.setCustomNameVisible(true);
            }
            
            if (u.isWordInList(le.getType().name(), "zombie,skeleton")){
                
                if (!helm.isEmpty()){
                    ItemStack item = Util.getRndItem(helm);
                    if (item != null) le.getEquipment().setHelmet(item);
                }
                
                if (!chest.isEmpty()){
                    ItemStack item = Util.getRndItem(chest);
                    if (item != null) le.getEquipment().setChestplate(item);
                }

                if (!leg.isEmpty()){
                    ItemStack item = Util.getRndItem(leg);
                    if (item != null) le.getEquipment().setLeggings(item);
                }
                
                if (!boot.isEmpty()){
                    ItemStack item = Util.getRndItem(boot);
                    if (item != null) le.getEquipment().setBoots(item);
                }
                
                if (!weapon.isEmpty()){
                    ItemStack item = Util.getRndItem(weapon);
                    if (item != null) le.getEquipment().setItemInHand(item);
                }
                
            }
            
            
            //TODO chest,helm,leg,boot,weapon
            //flyloc
            
        }
    }


    public static String replacePlaceholders (Player p, Activator a, String param){
        String rst = param;
        String placeholders = "curtime,player,dplayer,health,"+Flag.ftypes;
        String [] phs = placeholders.split(",");
        for (String ph : phs){
            String key = "%"+ph+"%ph";
            rst = rst.replaceAll(key, getFlagParam(p,a,key));
        }
        return rst;
    }

    public static String getFlagParam (Player p, Activator a, String placeholder){
        if (placeholder.startsWith("%")&&placeholder.endsWith("%")){
            String flag = placeholder.substring(1, placeholder.length()-1);
            if (placeholder.equalsIgnoreCase("%curtime%")) return timeToString(p.getWorld().getTime());
            else if (placeholder.equals("%player%")) return p.getName();
            else if (placeholder.equals("%dplayer%")) return p.getDisplayName();
            else if (placeholder.equals("%health%")) return String.valueOf(p.getHealth());
            else for (FlagVal flg : a.getFlags())
                if (flg.flag.equals(flag)) return formatFlagParam (flag, flg.value);
        }
        return placeholder;
    }

    private static String formatFlagParam(String flag, String value) {
        String rst = value;
        if (flag.equalsIgnoreCase("time")) {
            if (!(value.equals("day")||value.equals("night"))){
                String [] ln = value.split(",");
                String r = "";
                DecimalFormat fmt = new DecimalFormat("00:00");
                if (ln.length>0)
                    for (int i = 0; i<ln.length;i++){
                        if (!u.isInteger(ln[i])) continue;
                        String tmp = fmt.format(Integer.parseInt(ln[i]));
                        if (i == 0) r = tmp;
                        else r = r+", "+tmp;
                    }
                if (!r.isEmpty()) rst = r;	
            }
        } else if (flag.equalsIgnoreCase("money")) {
            if (plg.vault.isEconomyConected()&&u.isIntegerSigned(value)){
                rst = plg.vault.formatMoney(value);
            }
        } else if (flag.equalsIgnoreCase("chance")) {
            rst = value +"%";
        }
        return rst;
    }


    private static void sendMessage(Player p, String actname, boolean annoying, boolean doact, String param){
        String key = "reactions-msg-"+actname+(doact ? "act" : "react");
        boolean showmsg = false;
        if (annoying){
            if (!p.hasMetadata(key)) {
                showmsg = true;
                p.setMetadata(key, new FixedMetadataValue(plg,System.currentTimeMillis()));
            } else {
                Long before = p.getMetadata(key).get(0).asLong();
                Long now = System.currentTimeMillis();
                if ((now-before)>(plg.same_msg_delay*1000)){
                    showmsg = true;
                    p.setMetadata(key, new FixedMetadataValue(plg,now));
                }
            }
        } else showmsg = true;
        if (showmsg) u.printMsg(p, param);
    }

    private static void addToTown(Player p, String param){
        if (plg.towny_conected) plg.towny.addToTown(p, param);
    }

    private static void kickFromTown(Player p){
        if (plg.towny_conected)	plg.towny.kickFromTown (p);
    }

    private static void executeCommand (Player p, boolean console, String cmd){
        CommandSender sender = p;
        if (console) sender = plg.getServer().getConsoleSender();
        plg.getServer().dispatchCommand(sender, ChatColor.translateAlternateColorCodes('&', cmd.replaceAll("%player%", p.getName())));
    }

    private static void executeCommandAsOp(Player p, String cmd){
        boolean isop = p.isOp();
        p.setOp(true);
        executeCommand (p, false, cmd);
        p.setOp(isop);
    }



    private static void removePotionEffect(Player p, String param) {
        if (param.equalsIgnoreCase("all")||param.equalsIgnoreCase("*")){
            for (PotionEffect pe :p.getActivePotionEffects()) p.removePotionEffect(pe.getType());
        } else {
            String [] pefs = param.split(",");
            if (pefs.length>0){
                for (int i = 0; i<pefs.length; i++){
                    PotionEffectType pef = parsePotionEffect (pefs[i]);
                    if (pef == null) continue;
                    if (p.hasPotionEffect(pef)) p.removePotionEffect(pef);
                    //p.addPotionEffect(new PotionEffect (pef, 0,0,false));
                }
            }
        }
    }

    // <POTION EFFECT>/<duration>/<amplifier>
    private static void potionEffect(Player p, Map<String,String> params) {
        if (params.isEmpty()) return;
        String peffstr = "";
        int duration=20;
        int amplifier = 1;
        boolean ambient = false;
        if (params.containsKey("param")){
            String param = Util.getParam(params, "param", "");
            if (param.isEmpty()) return;
            if (param.contains("/")){
                String[] prm = param.split("/");
                if (prm.length>1){
                    peffstr = prm[0];
                    if (u.isIntegerGZ(prm[1])) duration = Integer.parseInt(prm[1]);
                    if ((prm.length>2)&&u.isIntegerGZ(prm[2])) amplifier= Integer.parseInt(prm[2]);
                }
            } else peffstr = param;            
        } else {
            peffstr = Util.getParam(params, "type", "");
            duration = Util.getParam(params, "time", 20);
            amplifier = Util.getParam(params, "level", 1);
            ambient = Util.getParam(params, "ambient", false);
        }
        
        PotionEffectType pef = parsePotionEffect (peffstr);
        if (pef == null) return;
        PotionEffect pe = new PotionEffect (pef, duration, amplifier,ambient);
        p.addPotionEffect(pe);
    }

    private static PotionEffectType parsePotionEffect (String name) {
        PotionEffectType pef = null;
        try{
            pef = PotionEffectType.getByName(name);
        } catch(Exception e){
        }
        return pef;
    }


    // <SOUNDNAME>/<volume>/<yaw>
    private static void soundPlay (Player p, Map<String,String> params){
        if (params.isEmpty()) return;
        String sndstr = "";
        String strvolume ="1";
        String strpitch = "1";
        float pitch = 1;
        float volume = 1;
        if (params.containsKey("param")){
            String param = Util.getParam(params, "param", "");
            if (param.isEmpty()) return;
            if (param.contains("/")){
                String[] prm = param.split("/");
                if (prm.length>1){
                    sndstr = prm[0];
                    strvolume = prm[1];
                    if (prm.length>2) strpitch = prm[2];
                }
            } else sndstr = param;
            if (strvolume.matches("[0-9]+-?\\.[0-9]*")) volume = Float.parseFloat(strvolume);
            if (strpitch.matches("[0-9]+-?\\.[0-9]*")) pitch = Float.parseFloat(strpitch);            
        } else {
            sndstr = Util.getParam(params, "type", "");
            pitch = Util.getParam(params, "pitch", 1.0f);
            volume = Util.getParam(params, "volume", 1.0f);
        }
        Sound sound = getSoundStr (sndstr);
        p.getWorld().playSound(p.getLocation(), sound, volume, pitch);
    }


    private static Sound getSoundStr(String param) {
        Sound snd = null;
        try{
            snd= Sound.valueOf(param.toUpperCase());
        } catch(Exception e){
        }
        if (snd == null) snd = Sound.CLICK; 
        return snd;
    }



    private static void setPersonalDelay(Player p, String mstr){
        String seconds = "";
        String varname = p.getName();
        if (mstr.isEmpty()) return;
        if (mstr.contains("/")){
            String[] m = mstr.split("/");
            if (m.length>=2){
                seconds = m[0];
                varname = m[1];
            }
        } else seconds = mstr;
        if (seconds.isEmpty()) return;
        if (!u.isInteger(seconds)) return;
        int sec = Integer.parseInt(seconds);
        RAFlagDelay.setPersonalDelay(p,varname, sec);		
    }


    // формат delay 60/variable
    private static void setDelay(Player p, String mstr){
        String seconds = "";
        String varname = p.getName();
        if (mstr.isEmpty()) return;
        if (mstr.contains("/")){
            String[] m = mstr.split("/");
            if (m.length>=2){
                seconds = m[0];
                varname = m[1];
            }
        } else seconds = mstr;
        if (seconds.isEmpty()) return;
        if (!u.isInteger(seconds)) return;
        int sec = Integer.parseInt(seconds);
        RAFlagDelay.setDelay(varname, sec);
    }

    private static int moneyPay (Player p, String mstr){
        if (mstr.isEmpty()) return 0;
        String money="";
        String target="";
        if (mstr.contains("/")) {
            String [] m = mstr.split("/");
            if (m.length>=2){
                money = m[0];	
                target = m[1];
            }
        } else money = mstr;
        if (!money.matches("[0-9]*")) return 0;
        int amount = Integer.parseInt(money);
        if ((amount<=0)||(amount>plg.vault.getBalance(p.getName()))) return 0;
        plg.vault.withdrawPlayer(p.getName(), amount);
        if (!target.isEmpty()) plg.vault.depositPlayer(target, amount);
        return amount;
    }

    private static int moneyGive (Player p, String mstr){
        if (mstr.isEmpty()) return 0;
        String money="";
        String source="";
        if (mstr.contains("/")) {
            String [] m = mstr.split("/");
            if (m.length>=2){
                money = m[0];	
                source = m[1];
            }
        } else money = mstr;
        if (!money.matches("[0-9]*")) return 0;
        int amount = Integer.parseInt(money);
        if (amount<=0) return 0;		

        if (!source.isEmpty()){
            if (amount<plg.vault.getBalance(source)) return 0;
            plg.vault.withdrawPlayer(source, amount);
        } 

        plg.vault.depositPlayer(p.getName(), amount);
        return amount;
    }

    private static void giveItemPlayer(final Player p, final String param) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plg, new Runnable(){
            public void run(){
                ItemStack item = u.parseItemStack(param);
                if (item!=null)	u.giveItemOrDrop(p, item);
            }
        }, 1);

    }


    public static void sendBroadCastMsg(Player sender, String msg){
        for (Player p : plg.getServer().getOnlinePlayers())
            u.printMsg(p, msg);
    }

    /*public void executeClickerDelayed(final Player p, final RAActivator c){
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
			public void run(){
				performActions(p, (RAFlag.checkFlags(p,c)), c);	}
		}, 1);
	}*/


    /*public static void executeClicker(Player p, RAActivator c){
		performActions(p, (RAFlag.checkFlags(p,c)), c);
	}*/

    public static boolean checkLoc(String locstr){
        return (locstr.equalsIgnoreCase("player")||
                locstr.equalsIgnoreCase("viewpoint")||
                plg.tports.containsKey(locstr)||
                (Util.parseLocation(locstr)!=null));
    }

    public static Location locToLocation(Player p, String locstr){
        Location loc = null;
        if (locstr.equalsIgnoreCase("player")) loc = p.getLocation();
        else if (locstr.equalsIgnoreCase("viewpoint")) loc = p.getTargetBlock(null, 100).getLocation(); 
        else if (plg.tports.containsKey(locstr)) loc = plg.tports.get(locstr).getLocation();
        else loc = Util.parseLocation(locstr);
        return loc;
    }



    //if (ln[1].matches("[0-9]+\\.[0-9]+")||ln[1].matches("[1-9]+[0-9]*")) {
    public static String locToString(Player p, String locstr){
        String loc = u.getMSGnc("loc_unknown");
        Location tl = locToLocation (p, locstr);
        if (tl!=null) loc = "["+tl.getWorld().getName()+"] ("+tl.getBlockX()+", "+tl.getBlockY()+", "+tl.getBlockZ()+")";
        return loc;
    }


    private static Location teleportPlayer (Player p, Map<String,String> params){
        Location loc = null;
        int radius = 0;
        if (params.isEmpty()) return null;
        if (params.containsKey("param")) {
            loc = locToLocation (p, Util.getParam(params, "param", ""));
            
        } else { 
            loc = locToLocation (p, Util.getParam(params, "loc", ""));
            radius = Util.getParam(params, "radius", 0);
        }
        boolean land = Util.getParam(params, "land", true);
        
        if (loc != null){
            if (radius>0) loc = Util.getRandomLocationInRadius(loc, radius,land);
            if (plg.tp_center_coors) {
                loc.setX(loc.getBlockX()+0.5);
                loc.setZ(loc.getBlockZ()+0.5);
            }
            p.teleport(loc);
            String playeffect = Util.getParam(params, "effect", "");
            if (!playeffect.isEmpty()){
                int data = 0;
                if (playeffect.equalsIgnoreCase("smoke")) data = 9;
                RAEffects.playEffect(loc, playeffect, data);
            }

            
        }
        return loc;
    }


    private static boolean removeItemInHand (Player p, String item){
        return u.removeItemInHand(p, item);
    }

    private static void removeItemFromInventory (Player p, String item){
        u.removeItemInInventory(p, item);
    }

    public static String timeToString(long time) {
        int hours = (int) ((time / 1000 + 8) % 24);
        int minutes = (int) (60 * (time % 1000) / 1000);
        return String.format("%02d:%02d", hours, minutes);
    }


}