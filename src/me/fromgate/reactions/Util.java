package me.fromgate.reactions;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

public class Util {

    private static ReActions plg;
    static Random rnd = new Random();

    public static void init(ReActions plugin){
        plg = plugin;
        rnd = plg.u.random;
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

    public static Location getRandomLocationInRadius(Location l, int radius, boolean land){
        Location loc = l;
        if (radius>0) {
            List<Location> emptyloc = new ArrayList<Location>();
            for (int x = l.getBlockX()-radius; x<=l.getBlockX()+radius; x++)
                for (int y = l.getBlockY()-radius; y<=l.getBlockY()+radius; y++)
                    for (int z = l.getBlockZ()-radius; z<=l.getBlockZ()+radius; z++){
                        Location t = new Location (l.getWorld(),x,y,z,l.getYaw(),l.getPitch());
                        if (t.getBlock().isEmpty()&&t.getBlock().getRelative(BlockFace.UP).isEmpty()){
                            if (land&&t.getBlock().getRelative(BlockFace.DOWN).isEmpty()) continue;
                            emptyloc.add(t);
                        }
                    }
            if (!emptyloc.isEmpty()) {
                loc = emptyloc.get(rnd.nextInt(emptyloc.size()));
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
        if (max>min) return min + Util.rnd.nextInt(max-min);
        else return min;
    }

    public static String getParam(Map<String,String> params, String key, String defparam){
        if (!params.containsKey(key)) return defparam;
        return params.get(key);
    }

    public static int getParam(Map<String,String> params, String key, int defparam){
        if (!params.containsKey(key)) return defparam;
        String str = params.get(key);
        if (!str.matches("[1-9]+[0-9]*")) return defparam;
        return Integer.parseInt(str);
    }

    public static float getParam(Map<String,String> params, String key, float defparam){
        if (!params.containsKey(key)) return defparam;
        String str = params.get(key);
        if (!str.matches("[0-9]+-?\\.[0-9]*")) return defparam;
        return Float.parseFloat(str);
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
        ItemStack item = plg.u.parseItemStack(ln[rnd.nextInt(ln.length)]);
        if (item == null) return new ItemStack (Material.AIR);
        item.setAmount(1);
        //item = colorizeRndLeather(item);
        return item;
    }



}
