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


package me.fromgate.reactions;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("deprecation")
public abstract class FGUtilCore {
    JavaPlugin plg;
    //конфигурация утилит
    private String px = "";
    private String permprefix = "fgutilcore.";
    private String language = "english";
    private String plgcmd = "<command>";
    // Сообщения+перевод
    YamlConfiguration lng;
    private boolean savelng = false;
    //String lngfile = this.language+".lng";
    protected HashMap<String, String> msg = new HashMap<String, String>(); //массив сообщений
    private char c1 = 'a'; //цвет 1 (по умолчанию для текста)
    private char c2 = '2'; //цвет 2 (по умолчанию для значений)
    protected String msglist = "";
    protected boolean colorconsole = false;  // надо будет добавить методы для конфигурации "из вне"
    private Set<String> log_once = new HashSet<String>();
    protected HashMap<String, Cmd> cmds = new HashMap<String, Cmd>();
    protected String cmdlist = "";
    PluginDescriptionFile des;
    private Logger log = Logger.getLogger("Minecraft");
    Random random = new Random();
    BukkitTask chId;

    //newupdate-checker
    private boolean project_check_version = true;
    private String project_id = ""; //66204 - PlayEffect
    private String project_name = "";
    private String project_current_version = "";
    private String project_last_version = "";
    private String project_curse_url = "";
    private String version_info_perm = permprefix + "config"; // кого оповещать об обнволениях
    private String project_bukkitdev = "";


    public FGUtilCore(JavaPlugin plg, boolean savelng, String lng, String plgcmd, String permissionPrefix) {
        this.permprefix = permissionPrefix.endsWith(".") ? permissionPrefix : permissionPrefix + ".";
        this.plg = plg;
        this.des = plg.getDescription();
        this.language = lng;
        this.InitMsgFile();
        this.initStdMsg();
        this.fillLoadedMessages();
        this.savelng = savelng;
        this.plgcmd = plgcmd;
        this.px = ChatColor.translateAlternateColorCodes('&', "&3[" + des.getName() + "]&f ");
    }

    public void initUpdateChecker(String plugin_name, String project_id, String bukkit_dev_name, boolean enable) {
        this.project_id = project_id;
        this.project_curse_url = "https://api.curseforge.com/servermods/files?projectIds=" + this.project_id;
        this.project_name = plugin_name;
        this.project_current_version = des.getVersion();
        this.project_check_version = enable && (!this.project_id.isEmpty());
        this.project_bukkitdev = "http://dev.bukkit.org/bukkit-plugins/" + bukkit_dev_name + "/";

        if (this.project_check_version) {
            updateMsg();
            Bukkit.getScheduler().runTaskTimerAsynchronously(plg, new Runnable() {
                @Override
                public void run() {
                    updateMsg();
                }
            }, (40 + getRandomInt(20)) * 1200, 60 * 1200);
        }

    }

    /* Вывод сообщения о выходе новой версии, вызывать из
     * обработчика события PlayerJoinEvent
     */
    public void updateMsg(Player p) {
        if (isUpdateRequired() && p.hasPermission(this.version_info_perm)) {
            printMSG(p, "msg_outdated", 'e', '6', "&6" + project_name + " v" + des.getVersion());
            printMSG(p, "msg_pleasedownload", 'e', '6', this.project_current_version);
            printMsg(p, "&3" + this.project_bukkitdev);
        }
    }

    /* Вызывается автоматом при старте плагина,
     * пишет сообщение о выходе новой версии в лог-файл
     */
    public void updateMsg() {
        plg.getServer().getScheduler().runTaskAsynchronously(plg, new Runnable() {
            public void run() {
                updateLastVersion();
                if (isUpdateRequired()) {
                    log.info("[" + des.getName() + "] " + project_name + " v" + des.getVersion() + " is outdated! Recommended version is v" + project_last_version);
                    log.info("[" + des.getName() + "] " + project_bukkitdev);
                }
            }
        });
    }

    private void updateLastVersion() {
        if (!this.project_check_version) return;
        URL url = null;
        try {
            url = new URL(this.project_curse_url);
        } catch (Exception e) {
            this.log("Failed to create URL: " + this.project_curse_url);
            return;
        }

        try {
            URLConnection conn = url.openConnection();
            conn.addRequestProperty("X-API-Key", null);
            conn.addRequestProperty("User-Agent", this.project_name + " using FGUtilCore (by fromgate)");
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = reader.readLine();
            JSONArray array = (JSONArray) JSONValue.parse(response);
            if (array.size() > 0) {
                JSONObject latest = (JSONObject) array.get(array.size() - 1);
                String plugin_name = (String) latest.get("name");
                this.project_last_version = plugin_name.replace(this.project_name + " v", "").trim();
                //String plugin_jar_url = (String) latest.get("downloadUrl");
                //this.project_file_url = plugin_jar_url.replace("http://servermods.cursecdn.com/", "http://dev.bukkit.org/media/");
            }
        } catch (Exception e) {
            this.log("Failed to check last version");
        }
    }


    private boolean isUpdateRequired() {
        if (!project_check_version) return false;
        if (project_id.isEmpty()) return false;
        if (project_last_version.isEmpty()) return false;
        if (project_current_version.isEmpty()) return false;
        if (project_current_version.equalsIgnoreCase(project_last_version)) return false;
        double current_version = Double.parseDouble(project_current_version.replaceFirst("\\.", "").replace("/", ""));
        double last_version = Double.parseDouble(project_last_version.replaceFirst("\\.", "").replace("/", ""));
        return (last_version > current_version);
    }


    /* 
     * Инициализация стандартных сообщений
     */
    private void initStdMsg() {
        addMSG("msg_outdated", "%1% is outdated!");
        addMSG("msg_pleasedownload", "Please download new version (%1%) from ");
        addMSG("hlp_help", "Help");
        addMSG("hlp_thishelp", "%1% - this help");
        addMSG("hlp_execcmd", "%1% - execute command");
        addMSG("hlp_typecmd", "Type %1% - to get additional help");
        addMSG("hlp_typecmdpage", "Type %1% - to see another page of this help");
        addMSG("hlp_commands", "Command list:");
        addMSG("hlp_cmdparam_command", "command");
        addMSG("hlp_cmdparam_page", "page");
        addMSG("hlp_cmdparam_parameter", "parameter");
        addMSG("cmd_unknown", "Unknown command: %1%");
        addMSG("cmd_cmdpermerr", "Something wrong (check command, permissions)");
        addMSG("enabled", "enabled");
        msg.put("enabled", ChatColor.DARK_GREEN + msg.get("enabled"));
        addMSG("disabled", "disabled");
        msg.put("disabled", ChatColor.RED + msg.get("disabled"));
        addMSG("lst_title", "String list:");
        addMSG("lst_footer", "Page: [%1% / %2%]");
        addMSG("lst_listisempty", "List is empty");
        addMSG("msg_config", "Configuration");
        addMSG("cfgmsg_general_check-updates", "Check updates: %1%");
        addMSG("cfgmsg_general_language", "Language: %1%");
        addMSG("cfgmsg_general_language-save", "Save translation file: %1%");
    }


    public void setConsoleColored(boolean colorconsole) {
        this.colorconsole = colorconsole;
    }

    public boolean isConsoleColored() {
        return this.colorconsole;
    }

    public void addCmd(String cmd, String perm, String desc_id, String desc_key) {
        addCmd(cmd, perm, desc_id, desc_key, this.c1, this.c2, false);
    }

    public void addCmd(String cmd, String perm, String desc_id, String desc_key, char color) {
        addCmd(cmd, perm, desc_id, desc_key, this.c1, color, false);
    }

    public void addCmd(String cmd, String perm, String desc_id, String desc_key, boolean console) {
        addCmd(cmd, perm, desc_id, desc_key, this.c1, this.c2, console);
    }

    public void addCmd(String cmd, String perm, String desc_id, String desc_key, char color, boolean console) {
        addCmd(cmd, perm, desc_id, desc_key, this.c1, color, console);
    }

    public void addCmd(String cmd, String perm, String desc_id, String desc_key, char color1, char color2) {
        addCmd(cmd, perm, desc_id, desc_key, color1, color2, false);
    }

    public void addCmd(String cmd, String perm, String desc_id, String desc_key, char color1, char color2, boolean console) {
        String desc = getMSG(desc_id, desc_key, color1, color2);
        cmds.put(cmd, new Cmd(this.permprefix + perm, desc, console));
        if (cmdlist.isEmpty()) cmdlist = cmd;
        else cmdlist = cmdlist + ", " + cmd;
    }

    /* 
     * Проверка пермишенов и наличия команды
     */

    public boolean checkCmdPerm(CommandSender sender, String cmd) {
        if (!cmds.containsKey(cmd.toLowerCase())) return false;
        Cmd cm = cmds.get(cmd.toLowerCase());
        if (sender instanceof Player) return (cm.perm.isEmpty() || sender.hasPermission(cm.perm));
        else return cm.console;
    }

    /* Класс, описывающий команду:
     * perm - постфикс пермишена
     * desc - описание команды
     */
    public class Cmd {
        String perm;
        String desc;
        boolean console;

        public Cmd(String perm, String desc) {
            this.perm = perm;
            this.desc = desc;
            this.console = false;
        }

        public Cmd(String perm, String desc, boolean console) {
            this.perm = perm;
            this.desc = desc;
            this.console = console;
        }
    }

    public class PageList {
        private List<String> ln;
        private int lpp = 15;
        private String title_msgid = "lst_title";
        private String footer_msgid = "lst_footer";
        private boolean shownum = false;

        public void setLinePerPage(int lpp) {
            this.lpp = lpp;
        }

        public PageList(List<String> ln, String title_msgid, String footer_msgid, boolean shownum) {
            this.ln = ln;
            if (!title_msgid.isEmpty()) this.title_msgid = title_msgid;
            if (!footer_msgid.isEmpty()) this.footer_msgid = footer_msgid;
            this.shownum = shownum;
        }

        public void addLine(String str) {
            ln.add(str);
        }

        public boolean isEmpty() {
            return ln.isEmpty();
        }

        public void setTitle(String title_msgid) {
            this.title_msgid = title_msgid;

        }

        public void setShowNum(boolean shownum) {
        }

        public void setFooter(String footer_msgid) {
            this.footer_msgid = footer_msgid;
        }

        public void printPage(CommandSender p, int pnum) {
            printPage(p, pnum, this.lpp);
        }

        public void printPage(CommandSender p, int pnum, int linesperpage) {
            if (ln.size() > 0) {

                int maxp = ln.size() / linesperpage;
                if ((ln.size() % linesperpage) > 0) maxp++;
                if (pnum > maxp) pnum = maxp;
                int maxl = linesperpage;
                if (pnum == maxp) {
                    maxl = ln.size() % linesperpage;
                    if (maxp == 1) maxl = ln.size();
                }
                if (maxl == 0) maxl = linesperpage;
                if (msg.containsKey(title_msgid)) printMsg(p, "&6&l" + getMSGnc(title_msgid));
                else printMsg(p, title_msgid);

                String numpx = "";
                for (int i = 0; i < maxl; i++) {
                    if (shownum) numpx = "&3" + Integer.toString(1 + i + (pnum - 1) * linesperpage) + ". ";
                    printMsg(p, numpx + "&a" + ln.get(i + (pnum - 1) * linesperpage));
                }
                if (maxp > 1) printMSG(p, this.footer_msgid, 'e', '6', pnum, maxp);
            } else printMSG(p, "lst_listisempty", 'c');
        }

    }

    public void printPage(CommandSender p, List<String> ln, int pnum, String title, String footer, boolean shownum) {
        PageList pl = new PageList(ln, title, footer, shownum);
        pl.printPage(p, pnum);
    }

    public void printPage(CommandSender p, List<String> ln, int pnum, String title, String footer, boolean shownum, int lineperpage) {
        PageList pl = new PageList(ln, title, footer, shownum);
        pl.printPage(p, pnum, lineperpage);
    }


    /*
     * Разные полезные процедурки 
     * 
     */

    /* Функция проверяет входит ли число (int)
     * в список чисел представленных в виде строки вида n1,n2,n3,...nN
     */
    public boolean isIdInList(int id, String str) {
        if (!str.isEmpty()) {
            String[] ln = str.split(",");
            if (ln.length > 0)
                for (int i = 0; i < ln.length; i++)
                    if ((!ln[i].isEmpty()) && ln[i].matches("[0-9]*") && (Integer.parseInt(ln[i]) == id)) return true;
        }
        return false;
    }

    /* 
     * Функция проверяет входят ли все числа массива (int)
     * в список чисел представленных в виде строки вида n1,n2,n3,...nN
     */
    public boolean isAllIdInList(int[] ids, String str) {
        for (int id : ids)
            if (!isIdInList(id, str)) return false;
        return true;
    }


    /* 
     * Функция проверяет входит ли слово (String) в список слов
     * представленных в виде строки вида n1,n2,n3,...nN
     */
    public boolean isWordInList(String word, String str) {
        String[] ln = str.split(",");
        if (ln.length > 0)
            for (int i = 0; i < ln.length; i++)
                if (ln[i].equalsIgnoreCase(word)) return true;
        return false;
    }

    /* 
     * Функция проверяет входит есть ли item (блок) с заданным id и data в списке,
     * представленным в виде строки вида id1:data1,id2:data2,MATERIAL_NAME:data
     * При этом если data может быть опущена
     */
    public boolean isItemInList(int id, int data, String str) {
        String[] ln = str.split(",");
        if (ln.length > 0)
            for (int i = 0; i < ln.length; i++)
                if (compareItemStr(id, data, ln[i])) return true;
        return false;
    }



    /*public boolean compareItemStr (ItemStack item, String itemstr){
        return compareItemStr (item.getTypeId(), item.getDurability(), item.getAmount(), itemstr);
    }*/

    @Deprecated
    public boolean compareItemStr(int item_id, int item_data, String itemstr) {
        return compareItemStrIgnoreName(item_id, item_data, 1, itemstr);
    }

    // Надо использовать маску: id:data*amount, id:data, id*amount

    public boolean compareItemStr(ItemStack item, String str) {
        String itemstr = str;
        String name = "";
        if (itemstr.contains("$")) {
            name = str.substring(0, itemstr.indexOf("$"));
            name = ChatColor.translateAlternateColorCodes('&', name.replace("_", " "));
            itemstr = str.substring(name.length() + 1);
        }
        if (itemstr.isEmpty()) return false;
        if (!name.isEmpty()) {
            String iname = item.hasItemMeta() ? item.getItemMeta().getDisplayName() : "";
            if (!name.equals(iname)) return false;
        }
        return compareItemStrIgnoreName(item.getTypeId(), item.getDurability(), item.getAmount(), itemstr); // ;compareItemStr(item, itemstr);
    }


    public boolean compareItemStrIgnoreName(int item_id, int item_data, int item_amount, String itemstr) {
        if (!itemstr.isEmpty()) {
            int id = -1;
            int amount = 1;
            int data = -1;
            String[] si = itemstr.split("\\*");
            if (si.length > 0) {
                if ((si.length == 2) && si[1].matches("[1-9]+[0-9]*")) amount = Integer.parseInt(si[1]);
                String ti[] = si[0].split(":");
                if (ti.length > 0) {
                    if (ti[0].matches("[0-9]*")) id = Integer.parseInt(ti[0]);
                    else {
                        try {
                            id = Material.getMaterial(ti[0]).getId();
                        } catch (Exception e) {
                            logOnce("unknownitem" + ti[0], "Unknown item: " + ti[0]);
                        }
                    }
                    if ((ti.length == 2) && (ti[1]).matches("[0-9]*")) data = Integer.parseInt(ti[1]);
                    return ((item_id == id) && ((data < 0) || (item_data == data)) && (item_amount >= amount));
                }
            }
        }
        return false;
    }


    public boolean hasItemInInventory(Player p, String itemstr) {
        ItemStack item = parseItemStack(itemstr);
        if (item == null) return false;
        if (item.getType() == Material.AIR) return false;
        return (countItemInInventory(p, itemstr) >= item.getAmount());
    }


    public int countItemInInventory(Player p, String itemstr) {
        return countItemInInventory(p.getInventory(), itemstr);
    }

    public void removeItemInInventory(final Player p, final String itemstr) {
        Bukkit.getScheduler().runTaskLater(plg, new Runnable() {
            @Override
            public void run() {
                removeItemInInventory(p.getInventory(), itemstr);
            }
        }, 1);
    }


    private boolean itemHasName(ItemStack item) {
        if (!item.hasItemMeta()) return false;
        return item.getItemMeta().hasDisplayName();
    }

    private boolean compareItemName(ItemStack item, String istrname) {
        if (istrname.isEmpty() && (!itemHasName(item))) return true;
        if ((!istrname.isEmpty()) && itemHasName(item)) {
            String name = ChatColor.translateAlternateColorCodes('&', istrname.replace("_", " "));
            return item.getItemMeta().getDisplayName().equals(name);
        }
        return false;
    }

    public int removeItemInInventory(Inventory inv, String istr) {
        String itemstr = istr;
        int left = 1;
        if (left <= 0) return -1;
        int id = -1;
        int data = -1;
        String name = "";

        if (itemstr.contains("$")) {
            name = itemstr.substring(0, itemstr.indexOf("$"));
            itemstr = itemstr.substring(name.length() + 1);
        }

        String[] si = itemstr.split("\\*");
        if (si.length == 0) return left;
        if ((si.length == 2) && si[1].matches("[1-9]+[0-9]*")) left = Integer.parseInt(si[1]);
        String ti[] = si[0].split(":");

        if (ti.length > 0) {
            if (ti[0].matches("[0-9]*")) id = Integer.parseInt(ti[0]);
            else id = Material.getMaterial(ti[0]).getId();
            if ((ti.length == 2) && (ti[1]).matches("[0-9]*")) data = Integer.parseInt(ti[1]);
        }
        if (id <= 0) return left;
        for (int i = 0; i < inv.getContents().length; i++) {
            ItemStack slot = inv.getItem(i);
            if (slot == null) continue;
            if (!compareItemName(slot, name)) continue;
            if (id != slot.getTypeId()) continue;
            if ((data > 0) && (data != slot.getDurability())) continue;
            int slotamount = slot.getAmount();
            if (slotamount == 0) continue;
            if (slotamount <= left) {
                left = left - slotamount;
                inv.setItem(i, null);
            } else {
                slot.setAmount(slotamount - left);
                left = 0;
            }
            if (left == 0) return 0;
        }
        return left;
    }


    public int countItemInInventory(Inventory inv, String istr) {
        String itemstr = istr;
        int count = 0;
        int id = -1;
        int data = -1;
        String name = "";
        if (itemstr.contains("$")) {
            name = itemstr.substring(0, itemstr.indexOf("$"));
            itemstr = itemstr.substring(name.length() + 1);
        }

        String[] si = itemstr.split("\\*");
        if (si.length == 0) return 0;

        String ti[] = si[0].split(":");
        if (ti.length > 0) {
            try {
                if (ti[0].matches("[0-9]*")) id = Integer.parseInt(ti[0]);
                else id = Material.getMaterial(ti[0].toUpperCase()).getId();
            } catch (Exception e) {
                logOnce(istr, "Wrong material type/id " + ti[0] + " at line " + istr);
                return 0;
            }
            if ((ti.length == 2) && (ti[1]).matches("[0-9]*")) data = Integer.parseInt(ti[1]);
        }
        if (id <= 0) return 0;

        for (ItemStack slot : inv.getContents()) {
            if (slot == null) continue;
            if (!compareItemName(slot, name)) continue;
            if (id == slot.getTypeId()) {
                if ((data < 0) || (data == slot.getDurability())) count += slot.getAmount();
            }
        }
        return count;
    }


    public boolean removeItemInHand(Player p, String itemstr) {
        if (!itemstr.isEmpty()) {
            int id = -1;
            int amount = 1;
            int data = -1;
            String[] si = itemstr.split("\\*");
            if (si.length > 0) {
                if ((si.length == 2) && si[1].matches("[1-9]+[0-9]*")) amount = Integer.parseInt(si[1]);
                String ti[] = si[0].split(":");
                if (ti.length > 0) {
                    if (ti[0].matches("[0-9]*")) id = Integer.parseInt(ti[0]);
                    else id = Material.getMaterial(ti[0]).getId();
                    if ((ti.length == 2) && (ti[1]).matches("[0-9]*")) data = Integer.parseInt(ti[1]);
                    return removeItemInHand(p, id, data, amount);
                }
            }
        }
        return false;
    }


    public boolean removeItemInHand(Player p, int item_id, int item_data, int item_amount) {
        if ((p.getItemInHand() != null) &&
                (p.getItemInHand().getTypeId() == item_id) &&
                (p.getItemInHand().getAmount() >= item_amount) &&
                ((item_data < 0) || (item_data == p.getItemInHand().getDurability()))) {

            if (p.getItemInHand().getAmount() > item_amount)
                p.getItemInHand().setAmount(p.getItemInHand().getAmount() - item_amount);
            else p.setItemInHand(new ItemStack(Material.AIR));

            return true;
        }
        return false;
    }

    public void giveItemOrDrop(Player p, ItemStack item) {
        for (ItemStack i : p.getInventory().addItem(item).values())
            p.getWorld().dropItemNaturally(p.getLocation(), i);
    }

    /*
     * Вывод сообщения пользователю 
     */
    public void printMsg(CommandSender p, String msg) {
        String message = ChatColor.translateAlternateColorCodes('&', msg);
        if ((!(p instanceof Player)) && (!colorconsole)) message = ChatColor.stripColor(message);
        p.sendMessage(message);
    }

    /*
     *  Вывод сообещения пользователю (с префиксом)
     */
    public void printPxMsg(CommandSender p, String msg) {
        printMsg(p, px + msg);
    }


    /*
     * Бродкаст сообщения, использую при отладке 
     */
    public void BC(String msg) {
        plg.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', px + msg));
    }

    public void broadcastMSG(String perm, Object... s) {
        for (Player p : Bukkit.getOnlinePlayers())
            if (p.hasPermission(permprefix + perm)) printMSG(p, s);
    }

    public void broadcastMsg(String perm, String msg) {
        for (Player p : Bukkit.getOnlinePlayers())
            if (p.hasPermission(permprefix + perm)) printMsg(p, msg);
    }

    /*
     * 	public void printMSG (CommandSender p, Object... s){
		String message = getMSG (s); 
		if ((!(p instanceof Player))&&(!colorconsole)) message = ChatColor.stripColor(message);
		p.sendMessage(message);
	}
     */

    /*
     * Запись сообщения в лог 
     */
    public void log(String msg) {
        log.info(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', px + msg)));
    }

    public void logOnce(String error_id, String msg) {
        if (log_once.contains(error_id)) return;
        log_once.add(error_id);
        log.info(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', px + msg)));
    }


    /*
     * Отправка цветного сообщения в консоль 
     */
    public void SC(String msg) {
        plg.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', px + msg));
    }



    /*
     * Перевод
     * 
     */

    /*
     *  Инициализация файла с сообщениями
     */
    public void InitMsgFile() {
        try {
            lng = new YamlConfiguration();
            File f = new File(plg.getDataFolder() + File.separator + this.language + ".lng");
            if (f.exists()) lng.load(f);
            else {
                InputStream is = plg.getClass().getResourceAsStream("/language/" + this.language + ".lng");
                if (is != null) lng.load(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fillLoadedMessages() {
        if (lng == null) return;
        for (String key : lng.getKeys(true))
            addMSG(key, lng.getString(key));
    }


    /*
     * Добавлене сообщения в список
     * Убираются цвета.
     * Параметры:
     * key - ключ сообщения
     * txt - текст сообщения
     */
    public void addMSG(String key, String txt) {
        msg.put(key, ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', lng.getString(key, txt))));
        if (msglist.isEmpty()) msglist = key;
        else msglist = msglist + "," + key;
    }


    /*
     * Сохранение сообщений в файл 
     */
    public void SaveMSG() {
        String[] keys = this.msglist.split(",");
        try {
            File f = new File(plg.getDataFolder() + File.separator + this.language + ".lng");
            if (!f.exists()) f.createNewFile();
            YamlConfiguration cfg = new YamlConfiguration();
            for (int i = 0; i < keys.length; i++)
                cfg.set(keys[i], msg.get(keys[i]));
            cfg.save(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     *  getMSG (String id, [char color1, char color2], Object param1, Object param2, Object param3... )
     */
    public String getMSG(Object... s) {
        String str = "&4Unknown message";
        String color1 = "&" + this.c1;
        String color2 = "&" + this.c2;
        if (s.length > 0) {
            String id = s[0].toString();
            str = "&4Unknown message (" + id + ")";
            if (msg.containsKey(id)) {
                int px = 1;
                if ((s.length > 1) && (s[1] instanceof Character)) {
                    px = 2;
                    color1 = "&" + (Character) s[1];
                    if ((s.length > 2) && (s[2] instanceof Character)) {
                        px = 3;
                        color2 = "&" + (Character) s[2];
                    }
                }
                str = color1 + msg.get(id);
                if (px < s.length)
                    for (int i = px; i < s.length; i++) {
                        String f = s[i].toString();
                        if (s[i] instanceof Location) {
                            Location loc = (Location) s[i];
                            f = loc.getWorld().getName() + "[" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + "]";
                        }
                        str = str.replace("%" + Integer.toString(i - px + 1) + "%", color2 + f + color1);
                    }

            } else if (this.savelng) {
                addMSG(id, str);
                SaveMSG();
            }
        }
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public void printMSG(CommandSender p, Object... s) {
        String message = getMSG(s);
        if ((!(p instanceof Player)) && (!colorconsole)) message = ChatColor.stripColor(message);
        p.sendMessage(message);
    }

    /* 
     * Печать справки
     */
    public void PrintHLP(Player p) {
        printMsg(p, "&6&l" + this.project_name + " v" + des.getVersion() + " &r&6| " + getMSG("hlp_help", '6'));
        printMSG(p, "hlp_thishelp", "/" + plgcmd + " help");
        printMSG(p, "hlp_execcmd", "/" + plgcmd + " <" + getMSG("hlp_cmdparam_command", '2') + "> [" + getMSG("hlp_cmdparam_parameter", '2') + "]");
        printMSG(p, "hlp_typecmd", "/" + plgcmd + " help <" + getMSG("hlp_cmdparam_command", '2') + ">");
        printMsg(p, getMSG("hlp_commands") + " &2" + cmdlist);
    }


    /* 
     * Печать справки по команде
     */
    public void printHLP(Player p, String cmd) {
        if (cmds.containsKey(cmd)) {
            printMsg(p, "&6&l" + this.project_name + " v" + des.getVersion() + " &r&6| " + getMSG("hlp_help", '6'));
            printMsg(p, cmds.get(cmd).desc);
        } else printMSG(p, "cmd_unknown", 'c', 'e', cmd);
    }

    public void PrintHlpList(CommandSender p, int page, int lpp) {
        String title = "&6&l" + this.project_name + " v" + des.getVersion() + " &r&6| " + getMSG("hlp_help", '6');
        List<String> hlp = new ArrayList<String>();
        hlp.add(getMSG("hlp_thishelp", "/" + plgcmd + " help"));
        hlp.add(getMSG("hlp_execcmd", "/" + plgcmd + " <" + getMSG("hlp_cmdparam_command", '2') + "> [" + getMSG("hlp_cmdparam_parameter", '2') + "]"));
        if (p instanceof Player)
            hlp.add(getMSG("hlp_typecmdpage", "/" + plgcmd + " help <" + getMSG("hlp_cmdparam_page", '2') + ">"));

        String[] ks = (cmdlist.replace(" ", "")).split(",");
        if (ks.length > 0) {
            for (String cmd : ks)
                hlp.add(cmds.get(cmd).desc);
        }
        printPage(p, hlp, page, title, "", false, lpp);
    }

    /* 
     * Возврат логической переменной в виде текста выкл./вкл.
     */
    public String EnDis(boolean b) {
        return b ? getMSG("enabled", '2') : getMSG("disabled", 'c');
    }

    public String EnDis(String str, boolean b) {
        String str2 = ChatColor.stripColor(str);
        return b ? ChatColor.DARK_GREEN + str2 : ChatColor.RED + str2;
    }

    /* 
     * Печать значения логической переменной 
     */
    public void printEnDis(CommandSender p, String msg_id, boolean b) {
        p.sendMessage(getMSG(msg_id) + ": " + EnDis(b));
    }


    /* 
     * Дополнительные процедуры
     */

    /*
     * Переопределение префикса пермишенов 
     */
    public void setPermPrefix(String ppfx) {
        this.permprefix = ppfx + ".";
        this.version_info_perm = this.permprefix + "config";
    }

    /*
     * Проверка соответствия пермишена (указывать без префикса)
     * заданной команде 
     */
    public boolean equalCmdPerm(String cmd, String perm) {
        return (cmds.containsKey(cmd.toLowerCase())) &&
                ((cmds.get(cmd.toLowerCase())).perm.equalsIgnoreCase(permprefix + perm));
    }


    /*
     * Преобразует строку вида <id>:<data>[*<amount>] в ItemStack
     * Возвращает null если строка кривая
     */
    @Deprecated
    public ItemStack parseItemStack(String itemstr) {
        if (itemstr.isEmpty()) return null;

        String istr = itemstr;
        String enchant = "";
        String name = "";

        if (istr.contains("$")) {
            name = istr.substring(0, istr.indexOf("$"));
            istr = istr.substring(name.length() + 1);
        }
        if (istr.contains("@")) {
            enchant = istr.substring(istr.indexOf("@") + 1);
            istr = istr.substring(0, istr.indexOf("@"));
        }
        int id = -1;
        int amount = 1;
        short data = 0;
        String[] si = istr.split("\\*");

        if (si.length > 0) {
            if (si.length == 2) amount = Math.max(getMinMaxRandom(si[1]), 1);
            String ti[] = si[0].split(":");
            if (ti.length > 0) {
                if (ti[0].matches("[0-9]*")) id = Integer.parseInt(ti[0]);
                else {
                    Material m = Material.getMaterial(ti[0].toUpperCase());
                    if (m == null) {
                        logOnce("wrongitem" + ti[0], "Could not parse item material name (id) " + ti[0]);
                        return null;
                    }
                    id = m.getId();
                }
                if ((ti.length == 2) && (ti[1]).matches("[0-9]*")) data = Short.parseShort(ti[1]);
                ItemStack item = new ItemStack(id, amount, data);
                if (!enchant.isEmpty()) {
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
        return null;
    }

    public ItemStack setEnchantments(ItemStack item, String enchants) {
        ItemStack i = item.clone();
        if (enchants.isEmpty()) return i;
        String[] ln = enchants.split(",");
        for (String ec : ln) {
            if (ec.isEmpty()) continue;
            Color clr = colorByName(ec);
            if (clr != null) {
                if (isIdInList(item.getTypeId(), "298,299,300,301")) {
                    LeatherArmorMeta meta = (LeatherArmorMeta) i.getItemMeta();
                    meta.setColor(clr);
                    i.setItemMeta(meta);
                }
            } else {
                String ench = ec;
                int level = 1;
                if (ec.contains(":")) {
                    ench = ec.substring(0, ec.indexOf(":"));
                    level = Math.max(1, getMinMaxRandom(ec.substring(ench.length() + 1)));
                }
                Enchantment e = Enchantment.getByName(ench.toUpperCase());
                if (e == null) continue;
                i.addUnsafeEnchantment(e, level);
            }
        }
        return i;
    }

    public Color colorByName(String colorname) {
        Color[] clr = {Color.WHITE, Color.SILVER, Color.GRAY, Color.BLACK,
                Color.RED, Color.MAROON, Color.YELLOW, Color.OLIVE,
                Color.LIME, Color.GREEN, Color.AQUA, Color.TEAL,
                Color.BLUE, Color.NAVY, Color.FUCHSIA, Color.PURPLE};
        String[] clrs = {"WHITE", "SILVER", "GRAY", "BLACK",
                "RED", "MAROON", "YELLOW", "OLIVE",
                "LIME", "GREEN", "AQUA", "TEAL",
                "BLUE", "NAVY", "FUCHSIA", "PURPLE"};
        for (int i = 0; i < clrs.length; i++)
            if (colorname.equalsIgnoreCase(clrs[i])) return clr[i];
        return null;
    }

    /*public ItemStack parseItemStack (String itemstr){
        if (!itemstr.isEmpty()){
            //int id = -1;
            Material m = Material.AIR;
            int amount =1;
            short data =0;			
            String [] si = itemstr.split("\\*");
            if (si.length>0){
                if ((si.length==2)&&si[1].matches("[1-9]+[0-9]*")) amount = Integer.parseInt(si[1]);
                String ti[] = si[0].split(":");
                if (ti.length>0){
                    if (ti[0].matches("[0-9]*")) m = Material.getMaterial(Integer.parseInt(ti[0]));//id=Integer.parseInt(ti[0]);
                    else m=Material.getMaterial(ti[0].toUpperCase());						
                    if ((ti.length==2)&&(ti[1]).matches("[0-9]*")) data = Short.parseShort(ti[1]);
                    return new ItemStack (m,amount,data);
                }
            }
        }
        return null;
    }*/


    /*
     * Проверяет, есть ли игроки в пределах заданного радиуса
     */
    public boolean isPlayerAround(Location loc, int radius) {
        for (Player p : loc.getWorld().getPlayers()) {
            if (p.getLocation().distance(loc) <= radius) return true;
        }
        return false;
    }

    /*
     *  Тоже, что и MSG, но обрезает цвет
     */
    public String getMSGnc(Object... s) {
        return ChatColor.stripColor(getMSG(s));
    }


    /*
     * Установка блока с проверкой на приват
     */
    public boolean placeBlock(Location loc, Player p, Material newType, byte newData, boolean phys) {
        return placeBlock(loc.getBlock(), p, newType, newData, phys);
    }

    /*
     * Установка блока с проверкой на приват
     */
    public boolean placeBlock(Block block, Player p, Material newType, byte newData, boolean phys) {
        BlockState state = block.getState();
        block.setTypeIdAndData(newType.getId(), newData, phys);
        BlockPlaceEvent event = new BlockPlaceEvent(state.getBlock(), state, block, p.getItemInHand(), p, true);
        plg.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) state.update(true);
        return event.isCancelled();
    }

    public boolean rollDiceChance(int chance) {
        return (random.nextInt(100) < chance);
    }

    public int tryChance(int chance) {
        return random.nextInt(chance);
    }


    public int getRandomInt(int maxvalue) {
        return random.nextInt(maxvalue);
    }


    /*
     * Проверка формата строкового представления целых чисел 
     */
    public boolean isIntegerSigned(String str) {
        return (str.matches("-?[0-9]+[0-9]*"));
    }

    public boolean isIntegerSigned(String... str) {
        if (str.length == 0) return false;
        for (String s : str)
            if (!s.matches("-?[0-9]+[0-9]*")) return false;
        return true;
    }

    public boolean isInteger(String str) {
        return (str.matches("[0-9]+[0-9]*"));
    }

    public boolean isInteger(String... str) {
        if (str.length == 0) return false;
        for (String s : str)
            if (!s.matches("[0-9]+[0-9]*")) return false;
        return true;
    }


    public boolean isIntegerGZ(String str) {
        return (str.matches("[1-9]+[0-9]*"));
    }

    public boolean isIntegerGZ(String... str) {
        if (str.length == 0) return false;
        for (String s : str)
            if (!s.matches("[1-9]+[0-9]*")) return false;
        return true;
    }

    public void printConfig(CommandSender p, int page, int lpp, boolean section, boolean usetranslation) {
        List<String> cfgprn = new ArrayList<String>();
        if (!plg.getConfig().getKeys(true).isEmpty())
            for (String k : plg.getConfig().getKeys(true)) {
                Object objvalue = plg.getConfig().get(k);
                String value = objvalue.toString();
                String str = k;
                if ((objvalue instanceof Boolean) && (usetranslation)) value = EnDis((Boolean) objvalue);
                if (objvalue instanceof MemorySection) {
                    if (!section) continue;
                } else str = k + " : " + value;
                if (usetranslation) str = getMSG("cfgmsg_" + k.replaceAll("\\.", "_"), value);
                cfgprn.add(str);
            }
        String title = "&6&l" + this.project_current_version + " v" + des.getVersion() + " &r&6| " + getMSG("msg_config", '6');
        printPage(p, cfgprn, page, title, "", false);
    }

    public int getMinMaxRandom(String minmaxstr) {
        int min = 0;
        int max = 0;
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

    public Long timeToTicks(Long time) {
        //1000 ms = 20 ticks
        return Math.max(1, (time / 50));
    }

    public Long parseTime(String time) {
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

    public String itemToString(ItemStack item) {
        String str = "";
        String n = item.getItemMeta().hasDisplayName() ? ChatColor.stripColor(item.getItemMeta().getDisplayName()) : item.getType().name();
        String a = item.getAmount() > 1 ? "*" + item.getAmount() : "";
        str = n + a;
        return str;
    }

    public int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE) return Integer.MIN_VALUE;
        if (l > Integer.MAX_VALUE) return Integer.MAX_VALUE;
        return (int) l;
    }

    public boolean returnMSG(boolean result, CommandSender p, Object... s) {
        if (p != null) this.printMSG(p, s);
        return result;
    }


}


