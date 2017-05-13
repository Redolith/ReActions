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

package me.fromgate.reactions.util;

import me.fromgate.reactions.util.item.ItemUtil;
import me.fromgate.reactions.util.message.M;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.ChatPaginator;
import org.bukkit.util.ChatPaginator.ChatPage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    static Random random = new Random();

    public static int getMinMaxRandom(String minmaxstr) {
        int min = 0;
        int max;
        String strmin = minmaxstr;
        String strmax = minmaxstr;

        if (minmaxstr.contains("-")) {
            strmin = minmaxstr.substring(0, minmaxstr.indexOf("-"));
            strmax = minmaxstr.substring(minmaxstr.indexOf("-") + 1);
        }
        if (strmin.matches("[1-9]+[0-9]*")) min = Integer.parseInt(strmin);
        max = min;
        if (strmax.matches("[1-9]+[0-9]*")) max = Integer.parseInt(strmax);
        if (max > min) return min + tryChance(1 + max - min);
        else return min;
    }

    public static String soundPlay(Location loc, Param params) {
        if (params.isEmpty()) return "";
        Location soundLoc = loc;
        String sndstr = "";
        String strvolume = "1";
        String strpitch = "1";
        float pitch = 1;
        float volume = 1;
        if (params.hasAnyParam("param")) {
            String param = params.getParam("param", "");
            if (param.isEmpty()) return "";
            if (param.contains("/")) {
                String[] prm = param.split("/");
                if (prm.length > 1) {
                    sndstr = prm[0];
                    strvolume = prm[1];
                    if (prm.length > 2) strpitch = prm[2];
                }
            } else sndstr = param;
            if (strvolume.matches("([0-9]+\\.[0-9]+)|([0-9]+)")) volume = Float.parseFloat(strvolume);
            if (strpitch.matches("([0-9]+\\.[0-9]+)|([0-9]+)")) pitch = Float.parseFloat(strpitch);
        } else {
            String locationStr = params.getParam("loc");
            soundLoc = locationStr.isEmpty() ? loc : Locator.parseLocation(locationStr, null);
            sndstr = params.getParam("type", "");
            pitch = params.getParam("pitch", 1.0f);
            volume = params.getParam("volume", 1.0f);
        }
        Sound sound = getSoundStr(sndstr);
        if (soundLoc != null) soundLoc.getWorld().playSound(soundLoc, sound, volume, pitch);
        return sound.name();
    }

    public static void soundPlay(Location loc, String param) {
        if (param.isEmpty()) return;
        /*Map<String,String> params = new HashMap<String,String>();
        params.put("param", param); */
        Param params = new Param(param, "param");
        soundPlay(loc, params);
    }


    private static Sound getSoundStr(String param) {
        Sound snd = null;
        try {
            snd = Sound.valueOf(param.toUpperCase());
        } catch (Exception ignored) {
        }
        if (snd == null) snd = Sound.UI_BUTTON_CLICK;
        return snd;
    }


    public static List<Entity> getEntities(Location l1, Location l2) {
        List<Entity> entities = new ArrayList<>();
        if (!l1.getWorld().equals(l2.getWorld())) return entities;
        int x1 = Math.min(l1.getBlockX(), l2.getBlockX());
        int x2 = Math.max(l1.getBlockX(), l2.getBlockX());
        int y1 = Math.min(l1.getBlockY(), l2.getBlockY());
        int y2 = Math.max(l1.getBlockY(), l2.getBlockY());
        int z1 = Math.min(l1.getBlockZ(), l2.getBlockZ());
        int z2 = Math.max(l1.getBlockZ(), l2.getBlockZ());
        int chX1 = x1 >> 4;
        int chX2 = x2 >> 4;
        int chZ1 = z1 >> 4;
        int chZ2 = z2 >> 4;
        for (int x = chX1; x <= chX2; x++)
            for (int z = chZ1; z <= chZ2; z++) {
                for (Entity e : l1.getWorld().getChunkAt(x, z).getEntities()) {
                    double ex = e.getLocation().getX();
                    double ey = e.getLocation().getY();
                    double ez = e.getLocation().getZ();
                    if ((x1 <= ex) && (ex <= x2) && (y1 <= ey) && (ey <= y2) && (z1 <= ez) && (ez <= z2))
                        entities.add(e);
                }
            }
        return entities;
    }


    @SuppressWarnings("deprecation")
    public static String replaceStandartLocations(Player p, String param) {
        if (p == null) return param;
        Location targetBlock = null;
        try {
            targetBlock = p.getTargetBlock((Set<Material>) null, 100).getLocation();
        } catch (Exception ignored) {
        }
        Map<String, Location> locs = new HashMap<>();
        locs.put("%here%", p.getLocation());
        locs.put("%eye%", p.getEyeLocation());
        locs.put("%head%", p.getEyeLocation());
        locs.put("%viewpoint%", targetBlock);
        locs.put("%view%", targetBlock);
        locs.put("%selection%", Selector.getSelectedLocation(p));
        locs.put("%select%", Selector.getSelectedLocation(p));
        locs.put("%sel%", Selector.getSelectedLocation(p));
        String newparam = param;
        for (String key : locs.keySet()) {
            Location l = locs.get(key);
            if (l == null) continue;
            newparam = newparam.replace(key, Locator.locationToString(l));
        }
        return newparam;
    }

    public static PotionEffectType parsePotionEffect(String name) {
        PotionEffectType pef = null;
        try {
            pef = PotionEffectType.getByName(name);
        } catch (Exception ignored) {
        }
        return pef;
    }

    public static LivingEntity getAnyKiller(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent evdmg = (EntityDamageByEntityEvent) event;
            if (evdmg.getDamager() instanceof LivingEntity) return (LivingEntity) evdmg.getDamager();
            if (evdmg.getCause() == DamageCause.PROJECTILE) {
                Projectile prj = (Projectile) evdmg.getDamager();
                LivingEntity shooterEntity = BukkitCompatibilityFix.getShooter(prj);
                if (shooterEntity == null) return null;
                if (shooterEntity instanceof LivingEntity) return shooterEntity;
            }
        }
        return null;
    }


    public static Player getKiller(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent evdmg = (EntityDamageByEntityEvent) event;
            if (evdmg.getDamager().getType() == EntityType.PLAYER) return (Player) evdmg.getDamager();
            if (evdmg.getCause() == DamageCause.PROJECTILE) {
                Projectile prj = (Projectile) evdmg.getDamager();
                LivingEntity shooterEntity = BukkitCompatibilityFix.getShooter(prj);
                if (shooterEntity == null) return null;
                if (shooterEntity instanceof Player) return (Player) shooterEntity;
            }
        }
        return null;
    }


    public static LivingEntity getDamagerEntity(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent evdmg = (EntityDamageByEntityEvent) event;
            if (evdmg.getCause() == DamageCause.PROJECTILE) {
                Projectile prj = (Projectile) evdmg.getDamager();
                LivingEntity shooterEntity = BukkitCompatibilityFix.getShooter(prj);
                if (shooterEntity == null) return null;
                return shooterEntity;
            } else if (evdmg.getDamager() instanceof LivingEntity) return (LivingEntity) evdmg.getDamager();
        }
        return null;
    }

    public static boolean isAnyParamExist(Map<String, String> params, String... param) {
        for (String key : params.keySet())
            for (String prm : param) {
                if (key.equalsIgnoreCase(prm)) return true;
            }
        return false;
    }

    public static boolean setOpen(Block b, boolean open) {
        BlockState state = b.getState();
        if (!(state.getData() instanceof Openable)) return false;
        Openable om = (Openable) state.getData();
        om.setOpen(open);
        state.setData((MaterialData) om);
        state.update();
        return true;
    }

    public static boolean isOpen(Block b) {
        if (b.getState().getData() instanceof Openable) {
            Openable om = (Openable) b.getState().getData();
            return om.isOpen();
        }
        return false;
    }

    public static Block getDoorBottomBlock(Block b) {
        if (b.getType() != Material.WOODEN_DOOR) return b;
        if (b.getRelative(BlockFace.DOWN).getType() == Material.WOODEN_DOOR)
            return b.getRelative(BlockFace.DOWN);
        return b;
    }

    public static boolean isDoorBlock(Block b) {
        if (b.getType() == Material.WOODEN_DOOR) return true;
        if (b.getType() == Material.TRAP_DOOR) return true;
        return b.getType() == Material.FENCE_GATE;
    }

    public static String join(String... s) {
        StringBuilder sb = new StringBuilder();
        for (String str : s)
            sb.append(str);
        return sb.toString();
    }

    public static boolean emptySting(String str) {
        return str == null || str.isEmpty();
    }


    public static void printPage(CommandSender sender, List<String> list, M title, int page) {
        int pageHeight = (sender instanceof Player) ? 9 : 1000;
        if (title != null) title.print(sender);
        ChatPage chatPage = paginate(list, page, Cfg.chatLength, pageHeight);
        for (String str : chatPage.getLines()) {
            M.printMessage(sender, str);
        }

        if (pageHeight == 9) {
            M.LST_FOOTER.print(sender, 'e', '6', chatPage.getPageNumber(), chatPage.getTotalPages());
        }
    }

    public static ChatPage paginate(List<String> unpaginatedStrings, int pageNumber, int lineLength, int pageHeight) {
        List<String> lines = new ArrayList<>();
        for (String str : unpaginatedStrings) {
            lines.addAll(Arrays.asList(ChatPaginator.wordWrap(str, lineLength)));
        }
        int totalPages = lines.size() / pageHeight + (lines.size() % pageHeight == 0 ? 0 : 1);
        int actualPageNumber = pageNumber <= totalPages ? pageNumber : totalPages;
        int from = (actualPageNumber - 1) * pageHeight;
        int to = from + pageHeight <= lines.size() ? from + pageHeight : lines.size();
        String[] selectedLines = Arrays.copyOfRange(lines.toArray(new String[lines.size()]), from, to);
        return new ChatPage(selectedLines, actualPageNumber, totalPages);
    }


    /* Функция проверяет входит ли число (int)
     * в список чисел представленных в виде строки вида n1,n2,n3,...nN
     */
    public static boolean isIdInList(int id, String str) {
        if (!str.isEmpty()) {
            String[] ln = str.split(",");
            if (ln.length > 0)
                for (String numStrInList : ln) {
                    if ((!numStrInList.isEmpty()) && numStrInList.matches("[0-9]*") && (Integer.parseInt(numStrInList) == id)) {
                        return true;
                    }
                }
        }
        return false;
    }

    /*
     * Функция проверяет входит ли слово (String) в список слов
     * представленных в виде строки вида n1,n2,n3,...nN
     */
    public static boolean isWordInList(String word, String str) {
        String[] ln = str.split(",");
        if (ln.length > 0)
            for (String wordInList : ln) {
                if (wordInList.equalsIgnoreCase(word)) return true;
            }
        return false;
    }

    /*
     * Функция проверяет входит есть ли item (блок) с заданным id и data в списке,
     * представленным в виде строки вида id1:data1,id2:data2,MATERIAL_NAME:data
     * При этом если data может быть опущена
     */
    public static boolean isItemInList(int id, int data, String str) {
        String[] ln = str.split(",");
        if (ln.length > 0)
            for (String itemInList : ln) {
                if (compareItemIdDataStr(id, data, itemInList)) return true;
            }

        return false;
    }

    @SuppressWarnings("deprecation")
    public static boolean compareItemIdDataStr(int id, int data, String itemStr) {
        ItemStack item = ItemUtil.parseItemStack(itemStr);
        if (item == null) return false;
        if (item.getTypeId() != id) return false;
        if (data < 0) return true;
        return data == item.getDurability();
    }

    public static boolean rollDiceChance(int chance) {
        return (random.nextInt(100) < chance);
    }

    public static int tryChance(int chance) {
        return random.nextInt(chance);
    }


    public static int getRandomInt(int maxvalue) {
        return random.nextInt(maxvalue);
    }

    public static boolean isIntegerSigned(String... str) {
        if (str.length == 0) return false;
        for (String s : str)
            if (!s.matches("-?[0-9]+[0-9]*")) return false;
        return true;
    }

    public static boolean isInteger(String str) {
        return (str.matches("[0-9]+[0-9]*"));
    }

    public static boolean isInteger(String... str) {
        if (str.length == 0) return false;
        for (String s : str)
            if (!s.matches("[0-9]+[0-9]*")) return false;
        return true;
    }


    public static boolean isIntegerGZ(String str) {
        return (str.matches("[1-9]+[0-9]*"));
    }

    public static boolean isIntegerGZ(String... str) {
        if (str.length == 0) return false;
        for (String s : str)
            if (!s.matches("[1-9]+[0-9]*")) return false;
        return true;
    }

    public static Long timeToTicks(Long time) {
        //1000 ms = 20 ticks
        return Math.max(1, (time / 50));
    }

    public static Long parseTime(String time) {
        int dd = 0; // дни
        int hh = 0; // часы
        int mm = 0; // минуты
        int ss = 0; // секунды
        int tt = 0; // тики
        int ms = 0; // миллисекунды
        if (isInteger(time)) {
            ss = Integer.parseInt(time);
        } else if (time.matches("^[0-5][0-9]:[0-5][0-9]$")) {
            String[] ln = time.split(":");
            if (isInteger(ln[0])) mm = Integer.parseInt(ln[0]);
            if (isInteger(ln[1])) ss = Integer.parseInt(ln[1]);
        } else if (time.matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$")) {
            String[] ln = time.split(":");
            if (isInteger(ln[0])) hh = Integer.parseInt(ln[0]);
            if (isInteger(ln[1])) mm = Integer.parseInt(ln[1]);
            if (isInteger(ln[2])) ss = Integer.parseInt(ln[2]);
        } else {
            Pattern pattern = Pattern.compile("\\d+(ms|d|h|m|s)");
            Matcher matcher = pattern.matcher(time);
            while (matcher.find()) {
                String foundTime = matcher.group();
                if (foundTime.matches("^\\d+ms")) {
                    ms = Integer.parseInt(time.replace("ms", ""));
                } else if (foundTime.matches("^\\d+d")) {
                    dd = Integer.parseInt(time.replace("d", ""));
                } else if (foundTime.matches("^\\d+h")) {
                    hh = Integer.parseInt(time.replace("h", ""));
                } else if (foundTime.matches("^\\d+m$")) {
                    mm = Integer.parseInt(time.replace("m", ""));
                } else if (foundTime.matches("^\\d+s$")) {
                    ss = Integer.parseInt(time.replace("s", ""));
                } else if (foundTime.matches("^\\d+t$")) {
                    tt = Integer.parseInt(time.replace("t", ""));
                }
            }
        }
        return (dd * 86400000L) + (hh * 3600000L) + (mm * 60000L) + (ss * 1000L) + (tt * 50L) + ms;
    }

    public static String itemToString(ItemStack item) {
        String str;
        String n = item.getItemMeta().hasDisplayName() ? ChatColor.stripColor(item.getItemMeta().getDisplayName()) : item.getType().name();
        String a = item.getAmount() > 1 ? "*" + item.getAmount() : "";
        str = n + a;
        return str;
    }

    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE) return Integer.MIN_VALUE;
        if (l > Integer.MAX_VALUE) return Integer.MAX_VALUE;
        return (int) l;
    }
}
