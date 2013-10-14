package me.fromgate.reactions.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.fromgate.reactions.RAUtil;
import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.activators.Activator;
import me.fromgate.reactions.activators.Activator.FlagVal;
import me.fromgate.reactions.flags.Flags;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffectType;

public class Util {

    private static ReActions plg(){
        return ReActions.instance;
    }
    
    
    private static RAUtil u(){
        return ReActions.util;
    }

    // world,x,y,z,[yaw,pitch]
    // world,x,y,z,[yaw,pitch]@radius
    public static Location parseLocation (String strloc){
        // Радиус пока игнорируем....
        Location loc = null;
        if (strloc.isEmpty()) return null;
        String [] lr = strloc.split("@");
        String sloc = lr[0];
        int radius = 0;
        if ((lr.length==2)&&(lr[1].matches("[1-9]+[0-9]*"))) radius = Integer.parseInt(lr[1]);
        String [] ln = sloc.split(",");
        if (!((ln.length==4)||(ln.length==6))) return null;
        World w = Bukkit.getWorld(ln[0]);
        if (w==null) return null;
        for (int i = 1; i<ln.length; i++)
            if (!(ln[i].matches("-?[0-9]+[0-9]*\\.[0-9]+")||ln[i].matches("-?[1-9]+[0-9]*"))) return null;
        loc = new Location (w, Double.parseDouble(ln[1]),Double.parseDouble(ln[2]),Double.parseDouble(ln[3]));
        if (ln.length==6){
            loc.setYaw(Float.parseFloat(ln[4]));
            loc.setPitch(Float.parseFloat(ln[5]));
        }
        if (radius >0) loc = getRandomLocationInRadius (loc, radius,true);
        return loc;
    }


    public static List<Location> getLocationsRadius (Location center, int radius, boolean land){
        List<Location> locs = new ArrayList<Location>();
        if (radius>0){
            for (int x = center.getBlockX()-radius; x<=center.getBlockX()+radius; x++)
                for (int y = center.getBlockY()-radius; y<=center.getBlockY()+radius; y++)
                    for (int z = center.getBlockZ()-radius; z<=center.getBlockZ()+radius; z++){
                        Location t = new Location (center.getWorld(),x,y,z,center.getYaw(),center.getPitch());
                        if (t.getBlock().isEmpty()&&t.getBlock().getRelative(BlockFace.UP).isEmpty()){
                            if (land&&t.getBlock().getRelative(BlockFace.DOWN).isEmpty()) continue;
                            t.add(center.getX()-center.getBlockX(),center.getY()-center.getBlockY(),center.getZ()-center.getBlockZ());
                            t.setY(center.getY());
                            t.setPitch(center.getPitch());
                            locs.add(t);
                        }
                    }
        } 
        return locs;
    }

    public static List<Location> getLocationsRegion (String region, boolean land){
        return RAWorldGuard.getRegionLocations(region, land);
    }


    public static Location getRandomLocationList(List<Location> locs){
        if ((locs == null)||locs.isEmpty()) return null;
        return locs.get(u().tryChance(locs.size()));
    }

    public static Location getRandomLocationInRadius(Location l, int radius, boolean land){
        Location loc = l;
        if (radius>0) {
            List<Location> emptyloc = getLocationsRadius(l,radius,land);
            /*for (int x = l.getBlockX()-radius; x<=l.getBlockX()+radius; x++)
                for (int y = l.getBlockY()-radius; y<=l.getBlockY()+radius; y++)
                    for (int z = l.getBlockZ()-radius; z<=l.getBlockZ()+radius; z++){
                        Location t = new Location (l.getWorld(),x,y,z,l.getYaw(),l.getPitch());
                        if (t.getBlock().isEmpty()&&t.getBlock().getRelative(BlockFace.UP).isEmpty()){
                            if (land&&t.getBlock().getRelative(BlockFace.DOWN).isEmpty()) continue;
                            emptyloc.add(t);
                        }
                    }*/
            if (!emptyloc.isEmpty()) {
                loc = emptyloc.get(u().tryChance(emptyloc.size()));
                loc.add(l.getX()-l.getBlockX(), l.getY()-l.getBlockY(), l.getZ()-l.getBlockZ());
                loc.setYaw(l.getYaw());
                loc.setPitch(l.getPitch());
            }
        }
        return loc;
    }

    public static String locationToStringFormated(Location loc){
        if (loc == null) return "";
        DecimalFormat fmt = new DecimalFormat("####0.##");
        String lstr = loc.toString();
        try {
            lstr = "["+loc.getWorld().getName()+"] "+fmt.format(loc.getX())+", "+fmt.format(loc.getY())+", "+fmt.format(loc.getZ());
        } catch (Exception e){
        }
        return lstr;
    }

    public static String locationToString(Location loc){
        if (loc == null) return "";
        return loc.getWorld().getName()+","+
        trimDouble(loc.getX())+","+
        trimDouble(loc.getY())+","+
        trimDouble(loc.getZ())+","+
        (float)trimDouble(loc.getYaw())+","+
        (float)trimDouble(loc.getPitch());
    }

    public static double trimDouble(double d){
        int i = (int) (d*1000);
        return ((double)i)/1000;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    // Парсер
    // преобразуем строку вида <команда> <параметр=значение> <параметр=значение> <параметр=значение> в Map {команда="", параметр=значеие....}
    public static Map<String,String> parseActionParam(String param){
        Map<String,String> params = new HashMap<String,String>();
        if (!param.isEmpty()){
            params.put("param-line", param);
            String[]ln = param.split(" ");
            if (ln.length>0)
                for (int i = 0; i < ln.length; i++){
                    String key = ln[i];
                    String value = "";
                    if (ln[i].contains(":")){
                        key = ln[i].substring(0,ln[i].indexOf(":"));
                        value = ln[i].substring(ln[i].indexOf(":")+1);
                    }
                    
                    if (value.isEmpty()){
                        value = key;
                        key = "param";
                    } 
                    params.put(key, value);
                }
        }
        return params;
    }

    public static int getMinMaxRandom(String minmaxstr){
        int min = 0;
        int max = 0;
        String strmin = minmaxstr;
        String strmax = minmaxstr;

        if (minmaxstr.contains("-")){
            strmin = minmaxstr.substring(0,minmaxstr.indexOf("-"));
            strmax = minmaxstr.substring(minmaxstr.indexOf("-")+1);
        }
        if (strmin.matches("[1-9]+[0-9]*")) min = Integer.parseInt(strmin);
        max = min;
        if (strmax.matches("[1-9]+[0-9]*")) max = Integer.parseInt(strmax);
        if (max>min) return min + u().tryChance(1+max-min);
        else return min;
    }

    public static String getParam(Map<String,String> params, String key, String defparam){
        if (!params.containsKey(key)) return defparam;
        return params.get(key);
    }

    public static int getParam(Map<String,String> params, String key, int defparam){
        if (!params.containsKey(key)) return defparam;
        String str = params.get(key);
        //if (!str.matches("[1-9]+[0-9]*")) return defparam;
        if (!u().isIntegerGZ(str)) return defparam;
        return Integer.parseInt(str);
    }

    public static float getParam(Map<String,String> params, String key, float defparam){
        if (!params.containsKey(key)) return defparam;
        String str = params.get(key);
        if (!str.matches("[0-9]+\\.?[0-9]*")) return defparam;
        return Float.parseFloat(str);
    }

    public static double getParam(Map<String,String> params, String key, double defparam){
        if (!params.containsKey(key)) return defparam;
        String str = params.get(key);
        if (!str.matches("[0-9]+\\.?[0-9]*")) return defparam;
        return Double.parseDouble(str);
    }


    public static boolean getParam(Map<String,String> params, String key, boolean defparam){
        if (!params.containsKey(key)) return defparam;
        String str = params.get(key);
        return (str.equalsIgnoreCase("true")||str.equalsIgnoreCase("on")||str.equalsIgnoreCase("yes"));
    }


    public static ItemStack getRndItem (String str){
        if (str.isEmpty()) return new ItemStack (Material.AIR);
        String [] ln = str.split(",");
        if (ln.length==0) return new ItemStack (Material.AIR);
        ItemStack item = u().parseItemStack(ln[u().tryChance(ln.length)]);
        if (item == null) return new ItemStack (Material.AIR);
        item.setAmount(1);
        //item = colorizeRndLeather(item);
        return item;
    }


    public static String soundPlay (Location loc, Map<String,String> params){
        if (params.isEmpty()) return "";
        String sndstr = "";
        String strvolume ="1";
        String strpitch = "1";
        float pitch = 1;
        float volume = 1;
        if (params.containsKey("param")){
            String param = Util.getParam(params, "param", "");
            if (param.isEmpty()) return "";
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
        loc.getWorld().playSound(loc, sound, volume, pitch);
        return sound.name();
    }

    public static void soundPlay (Location loc, String param){
        if (param.isEmpty()) return;
        Map<String,String> params = new HashMap<String,String>();
        params.put("param", param);
        soundPlay (loc, params);
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

    public static Long timeToTicks(Long time){
        //1000 ms = 20 ticks
        return Math.max(1, (time/50));
    }

    /* HH:MM:SS
     * MM:SS
     * Xms
     * Xs
     * Xm
     * Xh
     * Xt
     */

    public static Long parseTime(String time){
        int hh = 0; // часы
        int mm = 0; // минуты
        int ss = 0; // секунды
        int tt = 0; // тики
        int ms = 0; // миллисекунды
        if (ReActions.util.isInteger(time)){
            ss = Integer.parseInt(time);
        } else if (time.matches("^[0-5][0-9]:[0-5][0-9]$")){
            String [] ln = time.split(":");
            if (ReActions.util.isInteger(ln[0])) mm = Integer.parseInt(ln[0]);
            if (ReActions.util.isInteger(ln[1])) ss = Integer.parseInt(ln[1]);
        } else if (time.matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$")){
            String [] ln = time.split(":");
            if (ReActions.util.isInteger(ln[0])) hh = Integer.parseInt(ln[0]);
            if (ReActions.util.isInteger(ln[1])) mm = Integer.parseInt(ln[1]);
            if (ReActions.util.isInteger(ln[2])) ss = Integer.parseInt(ln[2]);
        } else if (time.endsWith("ms")) {
            String s = time.replace("ms", "");
            if (ReActions.util.isInteger(s)) ms = Integer.parseInt(s);
        } else if (time.endsWith("h")) {
            String s = time.replace("h", "");
            if (ReActions.util.isInteger(s)) hh = Integer.parseInt(s);
        } else if (time.endsWith("m")) {
            String s = time.replace("m", "");
            if (ReActions.util.isInteger(s)) mm = Integer.parseInt(s);
        } else if (time.endsWith("s")) {
            String s = time.replace("s", "");
            if (ReActions.util.isInteger(s)) ss = Integer.parseInt(s);
        } else if (time.endsWith("t")) {
            String s = time.replace("t", "");
            if (ReActions.util.isInteger(s)) tt = Integer.parseInt(s);
        }

        /*
        ms = ms
        ticks = tt*50
        sec = ss*1000
        min = mm*1000*60 = 60000
        hour == hh*60*60*1000 =3600000
         */
        return (long) ((hh*3600000)+(mm*60000)+(ss*1000)+(tt*50)+ms);
    }

    public static int safeLongToInt(long l) {
        if (l<Integer.MIN_VALUE) return Integer.MIN_VALUE;
        if (l > Integer.MAX_VALUE) return Integer.MAX_VALUE;         
        return (int) l;
    }


    /* 
     * Преобразует строку вида <id>:<data>[*<amount>] в ItemStack
     * Возвращает null если строка кривая
     * 
     * 
     * Преобразует строку вида <id>:<data>[*<amount>]@color,enchantment:<lvl>,enchantment&<lvl> в ItemStack
     * 
     */
    @SuppressWarnings("deprecation")
    public static ItemStack parseItemStack (String itemstr){
        if (!itemstr.isEmpty()){
            String enchant = "";
            String name = "";
            
            String istr = itemstr;

            if (itemstr.contains("$")){
                name =itemstr.substring(0,itemstr.indexOf("$"));
                itemstr = itemstr.substring(name.length()+1);
            }
            
            if (itemstr.contains("@")){
                istr = itemstr.substring(0,itemstr.indexOf("@"));
                enchant = itemstr.substring(istr.length()+1);
            }
            int id = -1;
            int amount =1;
            short data =0;          
            String [] si = istr.split("\\*");

            if (si.length>0){
                if (si.length==2) amount = Math.max(getMinMaxRandom (si[1]), 1);
                String ti[] = si[0].split(":");
                if (ti.length>0){
                    if (ti[0].matches("[0-9]*")) id=Integer.parseInt(ti[0]);
                    else {
                        Material m = Material.getMaterial(ti[0]);
                        if (m== null) {
                            u().logOnce("wrongitem"+ti[0], "Could not parse item material name (id) "+ti[0]);
                            return null;
                        }
                        id=m.getId();
                    }
                    if ((ti.length==2)&&(ti[1]).matches("[0-9]*")) data = Short.parseShort(ti[1]);
                    ItemStack item = new ItemStack (id,amount,data);
                    if (!enchant.isEmpty()){
                        item = setEnchantments(item, enchant);
                    }
                    if (!name.isEmpty()) {
                        ItemMeta im = item.getItemMeta();
                        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name.replace("_", " ")));
                        item.setItemMeta(im);
                    }
                    return item;
                }
            }
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    public static ItemStack setEnchantments (ItemStack item, String enchants){
        ItemStack i = item.clone();
        if (enchants.isEmpty()) return i;
        String [] ln = enchants.split(",");
        for (String ec : ln){
            if (ec.isEmpty()) continue;
            Color clr = colorByName (ec);
            if (clr != null){
                if (u().isIdInList(item.getTypeId(), "298,299,300,301")){
                    LeatherArmorMeta meta = (LeatherArmorMeta) i.getItemMeta();
                    meta.setColor(clr);
                    i.setItemMeta(meta);
                }
            } else {
                String ench = ec;
                int level = 1;
                if (ec.contains(":")){
                    ench = ec.substring(0,ec.indexOf(":"));
                    level = Math.max(1, getMinMaxRandom (ec.substring(ench.length()+1)));
                }
                Enchantment e = Enchantment.getByName(ench.toUpperCase());
                if (e == null) continue;
                i.addUnsafeEnchantment(e, level);
            }
        }
        return i;
    }

    public static Color colorByName(String colorname){
        Color [] clr = {Color.WHITE, Color.SILVER, Color.GRAY, Color.BLACK, 
                Color.RED, Color.MAROON, Color.YELLOW, Color.OLIVE,
                Color.LIME, Color.GREEN, Color.AQUA, Color.TEAL,
                Color.BLUE,Color.NAVY,Color.FUCHSIA,Color.PURPLE};
        String [] clrs = {"WHITE","SILVER", "GRAY", "BLACK", 
                "RED", "MAROON", "YELLOW", "OLIVE",
                "LIME", "GREEN", "AQUA", "TEAL",
                "BLUE","NAVY","FUCHSIA","PURPLE"};
        for (int i = 0; i<clrs.length;i++)
            if (colorname.equalsIgnoreCase(clrs[i])) return clr[i];
        return null;
    }

    public static List<Entity> getEntities (Location l1, Location l2){
        List<Entity> entities = new ArrayList<Entity>();
        if (!l1.getWorld().equals(l2.getWorld())) return entities;
        int x1 = Math.min(l1.getBlockX(), l2.getBlockX());
        int x2 = Math.max(l1.getBlockX(), l2.getBlockX());
        int y1 = Math.min(l1.getBlockY(), l2.getBlockY());
        int y2 = Math.max(l1.getBlockY(), l2.getBlockY());
        int z1 = Math.min(l1.getBlockZ(), l2.getBlockZ());
        int z2 = Math.max(l1.getBlockZ(), l2.getBlockZ());
        int chX1 = x1>>4;
        int chX2 = x2>>4;
        int chZ1 = z1>>4;
        int chZ2 = z2>>4;
        for (int x = chX1; x<=chX2; x++)
            for (int z = chZ1; z<=chZ2; z++){
                for (Entity e : l1.getWorld().getChunkAt(x, z).getEntities()){
                    double ex = e.getLocation().getX();
                    double ey = e.getLocation().getY();
                    double ez = e.getLocation().getZ();
                    if ((x1<=ex)&&(ex<=x2)&&(y1<=ey)&&(ey<=y2)&&(z1<=ez)&&(ez<=z2))
                        entities.add(e);
                }
            }
        return entities;
    }

    public static List<ItemStack> parseRandomItems (String stacks){
        return parseItemStacks (Util.parseRandomItemsStr(stacks));
    }

    public static List<ItemStack> parseItemStacks (String items){
        List<ItemStack> stacks = new ArrayList<ItemStack>();
        String[] ln = items.split(";");
        for (String item : ln){
            ItemStack stack = ReActions.util.parseItemStack(item);
            if (stack != null) stacks.add(stack);
        }
        return stacks;
    }
    public static String itemToString (ItemStack item){
        String str ="";
        String n = item.getItemMeta().hasDisplayName() ? ChatColor.stripColor(item.getItemMeta().getDisplayName()) : item.getType().name();
        String a = item.getAmount()>1 ? "*"+item.getAmount() : "";
        str = n+a;
        return str;
        
    }
    
    public static String itemsToString (List<ItemStack> items){
        String str ="";
        for (ItemStack i : items){
            String n = i.getItemMeta().hasDisplayName() ? ChatColor.stripColor(i.getItemMeta().getDisplayName()) : i.getType().name();
            String a = i.getAmount()>1 ? "*"+i.getAmount() : "";
            if (str.isEmpty()) str = n+a;
            else str = str+", "+n+a;
        }
        return str;        
    }

    //id:data*amount@enchant:level,color;id:data*amount@chance/id:data*amount@enchant:level,color;id:data*amount@chance
    public static String parseRandomItemsStr (String items){
        if (items.isEmpty()) return "";
        String [] loots = items.split("/");
        Map<String,Integer> drops = new HashMap<String,Integer>();
        int maxchance = 0;
        int nochcount = 0;
        for (String loot: loots){
            String [] ln = loot.split("%");
            if (ln.length>0){
                String stacks = ln[0];
                if (stacks.isEmpty()) continue;
                int chance =-1;
                if ((ln.length==2)&&ReActions.util.isInteger(ln[1])) {
                    chance = Integer.parseInt(ln[1]);
                    maxchance += chance; 
                } else nochcount++;
                drops.put(stacks, chance);
            }
        }

        if (drops.isEmpty()) return "";
        int eqperc = (nochcount*100)/drops.size();
        maxchance = maxchance+eqperc*nochcount;
        int rnd = ReActions.util.tryChance(maxchance);
        int curchance = 0;
        for (String stack : drops.keySet()){
            curchance = curchance+ (drops.get(stack)<0 ? eqperc : drops.get(stack));
            if (rnd<=curchance) return stack;
        }
        return "";
    }


    /*
     *         //id:data*amount,id:dat*amount@chance;id:data*amount;id:dat*amount@chance;id:data*amount;id:dat*amount@chance
        if (drop.isEmpty()) return;
        String [] loots = drop.split(";");
        Map<String,Integer> drops = new HashMap<String,Integer>();
        int maxchance = 0;
        int nochcount = 0;
        for (String loot: loots){
            String [] ln = loot.split("@");
            if (ln.length>0){
                String stacks = ln[0];
                if (stacks.isEmpty()) continue;
                int chance =-1;
                if ((ln.length==2)&&ReActions.util.isInteger(ln[1])) {
                    chance = Integer.parseInt(ln[1]);
                    maxchance += chance; 
                } else nochcount++;
                drops.put(stacks, chance);
            }
        }

        if (drops.isEmpty()) return;
        int eqperc = (nochcount*100)/drops.size();
        maxchance = maxchance+eqperc*nochcount;
        int rnd = ReActions.util.random.nextInt(maxchance);
        int curchance = 0;
        for (String stack : drops.keySet()){
            curchance = curchance+ (drops.get(stack)<0 ? eqperc : drops.get(stack));
            if (rnd<=curchance) {
                setMobDropStack (e,stack);
                return;
            }
        }
     */


    @SuppressWarnings("deprecation")
    public static Location locToLocation(Player p, String locstr){
        Location loc = null;
        if (locstr.equalsIgnoreCase("player")) loc = p.getLocation();
        else if (locstr.equalsIgnoreCase("viewpoint")) loc = p.getTargetBlock(null, 100).getLocation(); 
        else if (plg().containsTpLoc(locstr)) loc = plg().getTpLoc(locstr);
        else loc = Util.parseLocation(locstr);
        return loc;
    }

    public static String locToString(Player p, String locstr){
        String loc = u().getMSGnc("loc_unknown");
        Location tl = locToLocation (p, locstr);
        if (tl!=null) loc = "["+tl.getWorld().getName()+"] ("+tl.getBlockX()+", "+tl.getBlockY()+", "+tl.getBlockZ()+")";
        return loc;
    }
    
    public static PotionEffectType parsePotionEffect (String name) {
        PotionEffectType pef = null;
        try{
            pef = PotionEffectType.getByName(name);
        } catch(Exception e){
        }
        return pef;
    }
    
    public static String timeToString(long time) {
        int hours = (int) ((time / 1000 + 8) % 24);
        int minutes = (int) (60 * (time % 1000) / 1000);
        return String.format("%02d:%02d", hours, minutes);
    }
   
    
    public static Map<String,String> replaceAllPlaceholders (Player p, Activator a, Map<String,String> params){
        Map<String,String> newparams = new HashMap<String,String>();
        for (String key : params.keySet())
            newparams.put(key, replacePlaceholders(p,a,params.get(key)));
        return newparams;
    }
    
    public static String replacePlaceholders (Player p, Activator a, String param){
        String rst = param;
        String placeholders = "curtime,player,dplayer,health,deathpoint,"+Flags.getFtypes().toLowerCase();
        String [] phs = placeholders.split(",");
        for (String ph : phs){
            String key = "%"+ph+"%";
            rst = rst.replaceAll(key, getFlagParam(p,a,key));
            rst = rst.replaceAll(key.toUpperCase(), getFlagParam(p,a,key));
        }
        return rst;
    }

    public static String getFlagParam (Player p, Activator a, String placeholder){
        if (placeholder.startsWith("%")&&placeholder.endsWith("%")){
            String flag = placeholder.substring(1, placeholder.length()-1);
            if (placeholder.equalsIgnoreCase("%curtime%")) return Util.timeToString(p.getWorld().getTime());
            else if (placeholder.equalsIgnoreCase("%player%")) return p.getName();
            else if (placeholder.equalsIgnoreCase("%dplayer%")) return p.getDisplayName();
            else if (placeholder.equalsIgnoreCase("%deathpoint%")) {
                Location loc = RAPVPDeath.getLastDeathPoint(p);
                if (loc == null) loc = p.getLocation();
                return Util.locationToString(loc);
            } else if (placeholder.equalsIgnoreCase("%health%")) {
                String hlth = "0";
                try {
                    hlth = String.valueOf(p.getHealth());
                } catch (Throwable ex){
                    ReActions.util.logOnce("plr_health", "Failed to get Player health. This feature is not compatible with CB 1.5.2 (and older)...");
                }
                return hlth;
            }
            else for (FlagVal flg : a.getFlags())
                if (flg.flag.equals(flag)) return formatFlagParam (flag, flg.value);
        }
        return placeholder;
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
            case MONEY:
                if (RAVault.isEconomyConected()&&u().isIntegerSigned(value))
                    rst = RAVault.formatMoney(value);
                break;
            default:
                break;
            }
        return rst; 

    }
    /*
    @SuppressWarnings("deprecation")
    public static Location locToLocation(Player p, String locstr){
        Location loc = null;
        if (locstr.equalsIgnoreCase("player")) loc = p.getLocation();
        else if (locstr.equalsIgnoreCase("viewpoint")) loc = p.getTargetBlock(null, 100).getLocation(); 
        else if (plg.tports.containsKey(locstr)) loc = plg.tports.get(locstr).getLocation();
        else loc = Util.parseLocation(locstr);
        return loc;
    }

    public static String locToString(Player p, String locstr){
        String loc = u.getMSGnc("loc_unknown");
        Location tl = locToLocation (p, locstr);
        if (tl!=null) loc = "["+tl.getWorld().getName()+"] ("+tl.getBlockX()+", "+tl.getBlockY()+", "+tl.getBlockZ()+")";
        return loc;
    }    
   
*/
    
    public static Player getKiller(EntityDamageEvent event){
        if (event instanceof EntityDamageByEntityEvent){
            EntityDamageByEntityEvent evdmg = (EntityDamageByEntityEvent)event;
            if (evdmg.getDamager().getType() == EntityType.PLAYER) return (Player) evdmg.getDamager();
            if (evdmg.getCause() == DamageCause.PROJECTILE){
                Projectile prj = (Projectile) evdmg.getDamager();    
                if (prj.getShooter() instanceof Player) return (Player) prj.getShooter();    
            }
        }
        return null;
    }
    
    
    public static LivingEntity getDamagerEntity(EntityDamageEvent event){
        if (event instanceof EntityDamageByEntityEvent){
            EntityDamageByEntityEvent evdmg = (EntityDamageByEntityEvent)event;
            if (evdmg.getCause() == DamageCause.PROJECTILE){
                Projectile prj = (Projectile) evdmg.getDamager();    
                if (prj.getShooter() instanceof LivingEntity) return (LivingEntity) prj.getShooter();    
            } else if (evdmg.getDamager() instanceof LivingEntity) return (LivingEntity) evdmg.getDamager();
        }
        return null;
    }
    
    public static boolean isAnyParamExist (Map<String,String> params, String... param){
        for (String key : params.keySet())
            for (String prm : param){
                if (key.equalsIgnoreCase(prm)) return true;
            }
        return false;
    }
}
