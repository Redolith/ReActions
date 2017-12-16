package me.fromgate.reactions.placeholders;

import me.fromgate.reactions.util.BukkitCompatibilityFix;
import me.fromgate.reactions.util.Locator;
import me.fromgate.reactions.util.PlayerRespawner;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.item.ItemUtil;
import me.fromgate.reactions.util.item.VirtualItem;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

@PlaceholderDefine(id = "BasicPlayer", needPlayer = true,
        keys = {"player_loc", "player_loc_eye", "player_loc_view", "player_name", "player",
                "player_display", "dplayer", "player_item_hand", "itemplayer", "player_inv", "invplayer",
                "health", "player_loc_death", "deathpoint"})
public class PlaceholderPlayer extends Placeholder {
    @Override
    public String processPlaceholder(Player player, String key, String param) {
        if (player == null) return null;
        switch (key.toLowerCase()) {
            case "health":
                return Double.toString(BukkitCompatibilityFix.getEntityHealth(player));
            case "player_inv":
            case "invplayer":
                return getPlayerInventory(player, param);
            case "player_item_hand":
            case "itemplayer":
                return getPlayerItemInHand(player);
            case "player_display":
            case "dplayer":
                return player.getDisplayName();
            case "player_loc":
                return Locator.locationToString(player.getLocation());
            case "player_loc_death":
            case "deathpoint":
                Location loc = PlayerRespawner.getLastDeathPoint(player);
                if (loc == null) loc = player.getLocation();
                return Locator.locationToString(loc);
            case "player_loc_eye":
                return Locator.locationToString(player.getEyeLocation());
            case "player_loc_view":
                return Locator.locationToString(getViewLocation(player));
            case "player_name":
            case "player":
                return player.getName();
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    private Location getViewLocation(Player p) {
        Block b = p.getTargetBlock(null, 100);
        if (b == null) return p.getLocation();
        return b.getLocation().add(0.5, 0.5, 0.5);
    }


    @SuppressWarnings("deprecation")
    private String getPlayerItemInHand(Player player) {
        VirtualItem vi = ItemUtil.itemFromItemStack(player.getItemInHand()); //VirtualItem.fromItemStack(player.getItemInHand());
        if (vi == null) return "";
        return vi.toString();
    }

    private String getPlayerInventory(Player player, String value) {
        VirtualItem vi = null;
        if (Util.isInteger(value)) {
            int slotNum = Integer.parseInt(value);
            if (slotNum < 0 || slotNum >= player.getInventory().getSize()) return "";
            vi = ItemUtil.itemFromItemStack(player.getInventory().getItem(slotNum));
        } else {
            switch (value.toLowerCase()) {
                case "hand":
                    return getPlayerItemInHand(player);
                case "helm":
                case "helmet":
                    vi = ItemUtil.itemFromItemStack(player.getInventory().getHelmet());
                    break;
                case "chestplate":
                case "chest":
                    vi = ItemUtil.itemFromItemStack(player.getInventory().getChestplate());
                    break;
                case "leggings":
                case "legs":
                    vi = ItemUtil.itemFromItemStack(player.getInventory().getLeggings());
                    break;
                case "boots":
                case "boot":
                    vi = ItemUtil.itemFromItemStack(player.getInventory().getBoots());
                    break;
            }
        }
        if (vi == null) return "";
        return vi.toString();
    }
}
