/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2016, fromgate, fromgate@gmail.com
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

import me.fromgate.reactions.RAUtil;
import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.activators.Activator;
import me.fromgate.reactions.activators.ActivatorType;
import me.fromgate.reactions.activators.Activators;
import me.fromgate.reactions.activators.ItemHoldActivator;
import me.fromgate.reactions.activators.ItemWearActivator;
import me.fromgate.reactions.activators.MessageActivator;
import me.fromgate.reactions.activators.PlayerDeathActivator;
import me.fromgate.reactions.activators.SignActivator;
import me.fromgate.reactions.externals.RAWorldGuard;
import me.fromgate.reactions.util.BukkitCompatibilityFix;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.item.ItemUtil;
import me.fromgate.reactions.util.playerselector.PlayerSelectors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Button;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EventManager {
    private static ReActions plg() {
        return ReActions.instance;
    }

    private static RAUtil u() {
        return ReActions.util;
    }

    public static boolean raiseFactionEvent(Player p, String oldFaction, String newFaction) {
        FactionEvent e = new FactionEvent(p, oldFaction, newFaction);
        Bukkit.getServer().getPluginManager().callEvent(e);
        return true;
    }


    public static boolean raiseFactionCreateEvent(String factionName, Player player) {
        FactionCreateEvent e = new FactionCreateEvent(factionName, player);
        Bukkit.getServer().getPluginManager().callEvent(e);
        return true;
    }

    public static boolean raiseFactionDisbandEvent(String factionName, Player player) {
        FactionDisbandEvent e = new FactionDisbandEvent(factionName, player);
        Bukkit.getServer().getPluginManager().callEvent(e);
        return true;
    }


    public static boolean raiseFactionRelationEvent(String faction, String factionOther, String oldRelation, String newRelation) {
        FactionRelationEvent e = new FactionRelationEvent(faction, factionOther, oldRelation, newRelation);
        Bukkit.getServer().getPluginManager().callEvent(e);
        return true;
    }

    public static boolean raiseMobClickEvent(Player p, LivingEntity mob) {
        if (mob == null) return false;
        MobClickEvent e = new MobClickEvent(p, mob);
        Bukkit.getServer().getPluginManager().callEvent(e);
        return true;
    }

    public static boolean raiseMobKillEvent(Player p, LivingEntity mob) {
        if (mob == null) return false;
        MobKillEvent e = new MobKillEvent(p, mob);
        Bukkit.getServer().getPluginManager().callEvent(e);
        return true;
    }


    public static boolean raiseJoinEvent(Player player, boolean joinfirst) {
        JoinEvent e = new JoinEvent(player, joinfirst);
        Bukkit.getServer().getPluginManager().callEvent(e);
        return true;
    }

    public static boolean raiseDoorEvent(PlayerInteractEvent event) {
        if (!((event.getAction() == Action.RIGHT_CLICK_BLOCK) || (event.getAction() == Action.LEFT_CLICK_BLOCK)))
            return false;
        if (!Util.isDoorBlock(event.getClickedBlock())) return false;
        DoorEvent e = new DoorEvent(event.getPlayer(), Util.getDoorBottomBlock(event.getClickedBlock()));
        Bukkit.getServer().getPluginManager().callEvent(e);
        return e.isCancelled();
    }

    public static boolean raiseItemConsumeEvent(PlayerItemConsumeEvent event) {
        if (event.getItem() == null) return false;
        ItemConsumeEvent ce = new ItemConsumeEvent(event.getPlayer());
        Bukkit.getServer().getPluginManager().callEvent(ce);
        return ce.isCancelled();
    }

    public static boolean raiseItemClickEvent(PlayerInteractEntityEvent event) {
        ItemStack itemInHand = BukkitCompatibilityFix.getItemInHand(event.getPlayer());
        if (itemInHand == null || itemInHand.getType() == Material.AIR) return false;
        ItemClickEvent ice = new ItemClickEvent(event.getPlayer());
        Bukkit.getServer().getPluginManager().callEvent(ice);
        return true;
    }

    public static boolean raiseItemClickEvent(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return false;
        }
        ItemStack itemInHand = BukkitCompatibilityFix.getItemInHand(event.getPlayer());
        if (itemInHand == null || itemInHand.getType() == Material.AIR) return false;
        ItemClickEvent ice = new ItemClickEvent(event.getPlayer());
        Bukkit.getServer().getPluginManager().callEvent(ice);
        return true;
    }


    public static boolean raiseLeverEvent(PlayerInteractEvent event) {
        if (!((event.getAction() == Action.RIGHT_CLICK_BLOCK) || (event.getAction() == Action.LEFT_CLICK_BLOCK)))
            return false;
        if (event.getClickedBlock().getType() != Material.LEVER) return false;
        LeverEvent e = new LeverEvent(event.getPlayer(), event.getClickedBlock());
        Bukkit.getServer().getPluginManager().callEvent(e);
        return e.isCancelled();
    }


    // PVP Kill Event
    public static void raisePVPKillEvent(PlayerDeathEvent event) {
        Player deadplayer = event.getEntity();
        Player killer = Util.getKiller(deadplayer.getLastDamageCause());
        if (killer == null) return;
        PVPKillEvent pe = new PVPKillEvent(killer, deadplayer);
        Bukkit.getServer().getPluginManager().callEvent(pe);
    }

    // PVP Death Event
    public static void raisePVPDeathEvent(PlayerDeathEvent event) {
        Player deadplayer = event.getEntity();
        LivingEntity killer = Util.getAnyKiller(deadplayer.getLastDamageCause());
        PlayerDeathActivator.DeathCause ds = (killer == null) ? PlayerDeathActivator.DeathCause.OTHER : (killer instanceof Player) ? PlayerDeathActivator.DeathCause.PVP : PlayerDeathActivator.DeathCause.PVE;
        PlayerWasKilledEvent pe = new PlayerWasKilledEvent(killer, deadplayer, ds);
        Bukkit.getServer().getPluginManager().callEvent(pe);
    }

    // Button Event
    public static boolean raiseButtonEvent(PlayerInteractEvent event) {
        if (!((event.getAction() == Action.RIGHT_CLICK_BLOCK) || (event.getAction() == Action.LEFT_CLICK_BLOCK)))
            return false;
        if (!((event.getClickedBlock().getType() == Material.STONE_BUTTON) || (event.getClickedBlock().getType() == Material.WOOD_BUTTON)))
            return false;
        BlockState state = event.getClickedBlock().getState();
        if (state.getData() instanceof Button) {
            Button button = (Button) state.getData();
            if (button.isPowered()) return false;
        }
        ButtonEvent be = new ButtonEvent(event.getPlayer(), event.getClickedBlock().getLocation());
        Bukkit.getServer().getPluginManager().callEvent(be);
        return be.isCancelled();
    }

    public static boolean raiseSignEvent(Player player, String[] lines, Location loc, boolean leftClick) {
        for (Activator act : Activators.getActivators(ActivatorType.SIGN)) {
            SignActivator sign = (SignActivator) act;
            if (sign.checkMask(lines)) {
                SignEvent se = new SignEvent(player, lines, loc, leftClick);
                Bukkit.getServer().getPluginManager().callEvent(se);
                return true;
            }
        }
        return false;
    }

    public static boolean raiseCommandEvent(Player p, String command, boolean canceled) {
        if (command.isEmpty()) return false;
        String[] args = command.split(" ");
        CommandEvent ce = new CommandEvent(p, command, args, canceled);
        Bukkit.getServer().getPluginManager().callEvent(ce);
        if (ce.isCancelled()) return true;
        return false;
    }

    public static boolean raiseExecEvent(CommandSender sender, String param) {
        if (param.isEmpty()) return false;
        return raiseExecEvent(sender, new Param(param, "player"));
    }

    public static boolean raiseExecEvent(CommandSender sender, Param param) {
        if (param.isEmpty()) return false;
        final Player senderPlayer = (sender instanceof Player) ? (Player) sender : null;
        final String id = param.getParam("activator", param.getParam("exec"));
        if (id.isEmpty()) return false;
        Activator act = plg().getActivator(id);
        if (act == null) {
            u().logOnce("wrongact_" + id, "Failed to run exec activator " + id + ". Activator not found.");
            return false;
        }
        if (act.getType() != ActivatorType.EXEC) {
            u().logOnce("wrongactype_" + id, "Failed to run exec activator " + id + ". Wrong activator type.");
            return false;
        }
        int repeat = Math.min(param.getParam("repeat", 1), 1);

        long delay = u().timeToTicks(u().parseTime(param.getParam("delay", "1t")));

        final Set<Player> target = new HashSet<Player>();

        if (param.isParamsExists("player"))
            target.addAll(PlayerSelectors.getPlayerList(new Param(param.getParam("player"), "player")));
        target.addAll(PlayerSelectors.getPlayerList(param));   // Оставляем для совместимости со старым вариантом

        if (target.isEmpty() && !param.hasAnyParam(PlayerSelectors.getAllKeys())) target.add(senderPlayer);

        for (int i = 0; i < repeat; i++) {
            Bukkit.getScheduler().runTaskLater(plg(), new Runnable() {
                @Override
                public void run() {
                    for (Player p : target) {
                        if (Activators.isStopped(p, id, true)) continue;
                        ExecEvent ce = new ExecEvent(senderPlayer, p, id);
                        Bukkit.getServer().getPluginManager().callEvent(ce);
                    }
                }
            }, delay * repeat);
        }
        return true;
    }

    // Plate Event
    public static boolean raisePlateEvent(PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL) return false;
        if (!((event.getClickedBlock().getType() == Material.WOOD_PLATE) || (event.getClickedBlock().getType() == Material.STONE_PLATE)))
            return false;
        final Player p = event.getPlayer();
        final Location l = event.getClickedBlock().getLocation();
        Bukkit.getScheduler().runTaskLater(plg(), new Runnable() {
            @Override
            public void run() {
                PlateEvent pe = new PlateEvent(p, l);
                Bukkit.getServer().getPluginManager().callEvent(pe);
            }
        }, 1);
        return false;
    }

    public static void raiseAllRegionEvents(final Player player, final Location to, final Location from) {
        if (!RAWorldGuard.isConnected()) return;
        Bukkit.getScheduler().runTaskLaterAsynchronously(ReActions.instance, new Runnable() {
            @Override
            public void run() {

                final List<String> regionsTo = RAWorldGuard.getRegions(to);
                final List<String> regionsFrom = RAWorldGuard.getRegions(from);

                Bukkit.getScheduler().runTask(ReActions.instance, new Runnable() {
                    @Override
                    public void run() {
                        raiseRegionEvent(player, regionsTo);
                        raiseRgEnterEvent(player, regionsTo, regionsFrom);
                        raiseRgLeaveEvent(player, regionsTo, regionsFrom);
                    }
                });
            }
        }, 1);
    }

    private static void raiseRgEnterEvent(Player player, List<String> regionTo, List<String> regionFrom) {
        if (regionTo.isEmpty()) return;
        for (String rg : regionTo)
            if (!regionFrom.contains(rg)) {
                RegionEnterEvent wge = new RegionEnterEvent(player, rg);
                Bukkit.getServer().getPluginManager().callEvent(wge);
            }
    }

    private static void raiseRgLeaveEvent(Player player, List<String> regionTo, List<String> regionFrom) {
        if (regionFrom.isEmpty()) return;
        for (String rg : regionFrom)
            if (!regionTo.contains(rg)) {
                RegionLeaveEvent wge = new RegionLeaveEvent(player, rg);
                Bukkit.getServer().getPluginManager().callEvent(wge);
            }
    }

    private static void raiseRegionEvent(Player player, List<String> to) {
        if (to.isEmpty()) return;
        for (String region : to) {
            setFutureRegionCheck(player.getName(), region, false);
        }
    }

    private static void setFutureRegionCheck(final String playerName, final String region, boolean repeat) {
        @SuppressWarnings("deprecation")
        Player player = Bukkit.getPlayerExact(playerName);
        if (player == null) return;
        if (!player.isOnline()) return;
        if (player.isDead()) return;
        if (!RAWorldGuard.isPlayerInRegion(player, region)) return;
        String rg = "rg-" + region;
        if (!isTimeToRaiseEvent(player, rg, plg().worlduardRecheck, repeat)) return;

        RegionEvent wge = new RegionEvent(player, region);
        Bukkit.getServer().getPluginManager().callEvent(wge);

        Bukkit.getScheduler().runTaskLater(plg(), new Runnable() {
            @Override
            public void run() {
                setFutureRegionCheck(playerName, region, true);
            }
        }, 20 * plg().worlduardRecheck);
    }


    private static void setFutureItemWearCheck(final String playerName, final String itemStr, boolean repeat) {
        @SuppressWarnings("deprecation")
        Player player = Bukkit.getPlayerExact(playerName);
        if (player == null) return;
        if (!player.isOnline()) return;
        String rg = "iw-" + itemStr;
        if (!isTimeToRaiseEvent(player, rg, plg().itemWearRecheck, repeat)) return;
        ItemWearEvent iwe = new ItemWearEvent(player);
        if (!iwe.isItemWeared(itemStr)) return;
        Bukkit.getServer().getPluginManager().callEvent(iwe);
        Bukkit.getScheduler().runTaskLater(plg(), new Runnable() {
            @Override
            public void run() {
                setFutureItemWearCheck(playerName, itemStr, true);
            }
        }, 20 * plg().itemWearRecheck);
    }


    public static void raiseItemWearEvent(Player player) {
        final String playerName = player.getName();
        Bukkit.getScheduler().runTaskLater(plg(), new Runnable() {
            @Override
            public void run() {
                for (ItemWearActivator iw : Activators.getItemWearActivatos())
                    setFutureItemWearCheck(playerName, iw.getItemStr(), false);
            }
        }, 1);
    }

    public static void raiseItemHoldEvent(Player player) {
        final String playerName = player.getName();
        Bukkit.getScheduler().runTaskLater(plg(), new Runnable() {
            @Override
            public void run() {
                for (ItemHoldActivator ih : Activators.getItemHoldActivatos())
                    setFutureItemHoldCheck(playerName, ih.getItemStr(), false);
            }
        }, 1);
    }


    private static boolean setFutureItemHoldCheck(final String playerName, final String itemStr, boolean repeat) {
        @SuppressWarnings("deprecation")
        Player player = Bukkit.getPlayerExact(playerName);
        if (player == null || !player.isOnline() || player.isDead()) return false;
        ItemStack itemInHand = BukkitCompatibilityFix.getItemInHand(player);
        if (itemInHand == null || itemInHand.getType() == Material.AIR) return false;
        String rg = "ih-" + itemStr;
        if (!isTimeToRaiseEvent(player, rg, plg().itemHoldRecheck, repeat)) return false;
        if (!ItemUtil.compareItemStr(itemInHand, itemStr)) return false;
        ItemHoldEvent ihe = new ItemHoldEvent(player);
        Bukkit.getServer().getPluginManager().callEvent(ihe);

        Bukkit.getScheduler().runTaskLater(plg(), new Runnable() {
            @Override
            public void run() {
                setFutureItemHoldCheck(playerName, itemStr, true);
            }
        }, 20 * plg().itemHoldRecheck);
        return true;
    }

    public static boolean isTimeToRaiseEvent(Player p, String id, int seconds, boolean repeat) {
        Long curtime = System.currentTimeMillis();
        Long prevtime = p.hasMetadata("reactions-rchk-" + id) ? p.getMetadata("reactions-rchk-" + id).get(0).asLong() : 0;
        boolean needUpdate = repeat || ((curtime - prevtime) >= (1000 * seconds));
        if (needUpdate) p.setMetadata("reactions-rchk-" + id, new FixedMetadataValue(plg(), curtime));
        return needUpdate;
    }

    public static boolean raiseMessageEvent(CommandSender sender, MessageActivator.Source source, String message) {
        Player player = sender != null && (sender instanceof Player) ? (Player) sender : null;
        for (MessageActivator a : Activators.getMessageActivators()) {
            if (a.filterMessage(source, message)) {
                MessageEvent me = new MessageEvent(player, a, message);
                Bukkit.getServer().getPluginManager().callEvent(me);
                return me.isCancelled();
            }
        }
        return false;
    }

    public static void raiseVariableEvent(String var, String playerName, String newValue, String prevValue) {
        if (newValue.equalsIgnoreCase(prevValue)) return;
        @SuppressWarnings("deprecation")
        Player player = Bukkit.getPlayerExact(playerName);
        if (!playerName.isEmpty() && player == null) return;
        VariableEvent ve = new VariableEvent(player, var, newValue, prevValue);
        Bukkit.getServer().getPluginManager().callEvent(ve);
    }

    public static boolean raiseMobDamageEvent(EntityDamageEvent event, Player damager) {
        if (damager == null) return false;
        if (!(event.getEntity() instanceof LivingEntity)) return false;
        double damage = BukkitCompatibilityFix.getEventDamage(event);
        MobDamageEvent mde = new MobDamageEvent((LivingEntity) event.getEntity(), damager, damage, event.getCause());
        Bukkit.getServer().getPluginManager().callEvent(mde);
        BukkitCompatibilityFix.setEventDamage(event, mde.getDamage());
        return mde.isCancelled();
    }

    public static void raiseQuitEvent(PlayerQuitEvent event) {
        QuitEvent qu = new QuitEvent(event.getPlayer(), event.getQuitMessage());
        Bukkit.getServer().getPluginManager().callEvent(qu);
        event.setQuitMessage(qu.getQuitMessage() == null || qu.getQuitMessage().isEmpty() ? null : ChatColor.translateAlternateColorCodes('&', qu.getQuitMessage()));
    }

    public static boolean raiseBlockClickEvent(PlayerInteractEvent event) {
        Boolean leftClick;
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) leftClick = false;
        else if (event.getAction() == Action.LEFT_CLICK_BLOCK) leftClick = true;
        else return false;
        BlockClickEvent e = new BlockClickEvent(event.getPlayer(), event.getClickedBlock(), leftClick);
        Bukkit.getServer().getPluginManager().callEvent(e);
        return e.isCancelled();
    }

    public static boolean raiseInventoryClickEvent(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        PlayerInventoryClickEvent e = new PlayerInventoryClickEvent(p, event.getAction(), event.getClick(), event.getInventory(), event.getSlotType(), event.getCurrentItem(), event.getHotbarButton());
        Bukkit.getServer().getPluginManager().callEvent(e);
        return e.isCancelled();
    }

    public static boolean raiseDropEvent(PlayerDropItemEvent event) {
        Item item = event.getItemDrop();
        double pickupDelay = BukkitCompatibilityFix.getItemPickupDelay(item);
        DropEvent e = new DropEvent(event.getPlayer(), event.getItemDrop(), pickupDelay);
        Bukkit.getServer().getPluginManager().callEvent(e);
        BukkitCompatibilityFix.setItemPickupDelay(item, e.getPickupDelay());
        return e.isCancelled();
    }

    public static boolean raiseFlightEvent(PlayerToggleFlightEvent event) {
        FlightEvent e = new FlightEvent(event.getPlayer(), event.isFlying());
        Bukkit.getServer().getPluginManager().callEvent(e);
        return e.isCancelled();
    }

}
