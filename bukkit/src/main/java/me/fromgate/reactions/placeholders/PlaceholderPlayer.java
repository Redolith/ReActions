package me.fromgate.reactions.placeholders;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.util.BukkitCompatibilityFix;
import me.fromgate.reactions.util.Locator;
import me.fromgate.reactions.util.PlayerRespawner;
import me.fromgate.reactions.util.item.ItemUtil;
import me.fromgate.reactions.util.item.VirtualItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Set;

@PlaceholderDefine(id = "BasicPlayer", needPlayer = true,
        keys = {"PLAYER_LOC", "PLAYER_LOC_EYE", "PLAYER_LOC_VIEW", "PLAYER_NAME", "player", "PLAYER_DISPLAY", "dplayer", "PLAYER_ITEM_HAND", "itemplayer", "PLAYER_INV", "invplayer", "HEALTH", "PLAYER_LOC_DEATH", "deathpoint"})
public class PlaceholderPlayer extends Placeholder {
    @Override
    public String processPlaceholder(Player player, String key, String param) {
        if (player == null) return null;
        Key k = Key.valueOf(key.toUpperCase());
        if (k == null) return null;
        switch (k) {
            case HEALTH:
                return Double.toString(BukkitCompatibilityFix.getEntityHealth((LivingEntity) player));
            case PLAYER_INV:
            case INVPLAYER:
                return getPlayerInventory(player, param);
            case PLAYER_ITEM_HAND:
            case ITEMPLAYER:
                return getPlayerItemInHand(player);
            case PLAYER_DISPLAY:
            case DPLAYER:
                return player.getDisplayName();
            case PLAYER_LOC:
                return Locator.locationToString(player.getLocation());
            case PLAYER_LOC_DEATH:
            case DEATHPOINT:
                Location loc = PlayerRespawner.getLastDeathPoint(player);
                if (loc == null) loc = player.getLocation();
                return Locator.locationToString(loc);
            case PLAYER_LOC_EYE:
                return Locator.locationToString(player.getEyeLocation());
            case PLAYER_LOC_VIEW:
                return Locator.locationToString(getViewLocation(player));
            case PLAYER_NAME:
            case PLAYER:
                return player.getName();
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    private Location getViewLocation(Player p) {
        Block b = p.getTargetBlock((Set<Material>) null, 100);
        if (b == null) return p.getLocation();
        return b.getLocation().add(0.5, 0.5, 0.5);
    }


    private String getPlayerItemInHand(Player player) {
        VirtualItem vi = ItemUtil.itemFromItemStack(player.getItemInHand()); //VirtualItem.fromItemStack(player.getItemInHand());
        if (vi == null) return "";
        return vi.toString();
    }

    private String getPlayerInventory(Player player, String value) {
        VirtualItem vi = null;
        if (ReActions.util.isInteger(value)) {
            int slotNum = Integer.parseInt(value);
            if (slotNum < 0 || slotNum >= player.getInventory().getSize()) return "";
            vi = ItemUtil.itemFromItemStack(player.getInventory().getItem(slotNum));
        } else {
            if (value.equalsIgnoreCase("hand")) return getPlayerItemInHand(player);
            else if (value.equalsIgnoreCase("helm") || value.equalsIgnoreCase("helmet"))
                vi = ItemUtil.itemFromItemStack(player.getInventory().getHelmet());
            else if (value.equalsIgnoreCase("chestplate") || value.equalsIgnoreCase("chest"))
                vi = ItemUtil.itemFromItemStack(player.getInventory().getChestplate());
            else if (value.equalsIgnoreCase("Leggings") || value.equalsIgnoreCase("legs"))
                vi = ItemUtil.itemFromItemStack(player.getInventory().getLeggings());
            else if (value.equalsIgnoreCase("boot") || value.equalsIgnoreCase("boots"))
                vi = ItemUtil.itemFromItemStack(player.getInventory().getBoots());
        }
        if (vi == null) return "";
        return vi.toString();
    }

    enum Key {
        PLAYER_LOC,
        PLAYER_LOC_EYE,
        PLAYER_LOC_VIEW,
        PLAYER_NAME, PLAYER,
        PLAYER_DISPLAY, DPLAYER,
        PLAYER_ITEM_HAND, ITEMPLAYER,
        PLAYER_INV, INVPLAYER,
        HEALTH,
        PLAYER_LOC_DEATH, DEATHPOINT
    }

	
	/*
     * TODO
	 *     	Location l = PushBack.getPlayerPrevLoc1(p);
    	if (l!=null) Variables.setTempVar("backloc1", Locator.locationToString(l));
    	l = PushBack.getPlayerPrevLoc2(p);
    	if (l!=null) Variables.setTempVar("backloc2", Locator.locationToString(l));

	 * 
	 */

}
