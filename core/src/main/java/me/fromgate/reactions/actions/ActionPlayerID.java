package me.fromgate.reactions.actions;

import com.google.common.base.Charsets;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by MaxDikiy on 5/6/2017.
 */
public class ActionPlayerID extends Action  {
    private Boolean isOnline;

    @Override
    public boolean execute(Player p, Param params) {
        String playerName = params.getParam("player", "");
        this.isOnline = params.getParam("online", false);
        String varID = params.getParam("varid", "");
        String varName = params.getParam("varname", "");

        if (playerName.isEmpty()) {
            UUID uniqueID = getUUID(p, p.getName());
            Variables.setVar(p.getName(), varID, uniqueID.toString());
            Variables.setVar(p.getName(), varName, p.getName());
            return true;
        } else {
            UUID uniqueID = null;
            Player player = null;
            String[] components = playerName.split("-");
            if (components.length == 5) uniqueID = UUID.fromString(playerName);
            if (uniqueID == null) {
                //noinspection deprecation
                player = Bukkit.getPlayer(playerName);
            } else {
                player = Bukkit.getPlayer(uniqueID);
            }
            if (player != null){
                if (uniqueID == null) uniqueID = getUUID(player, player.getName());
                Variables.setVar(playerName, varID, uniqueID.toString());
                Variables.setVar(playerName, varName, player.getName());
                Variables.setTempVar("playerid", uniqueID.toString());
                Variables.setTempVar("playername", player.getName());
                return true;

            } else {
                OfflinePlayer offPlayer = null;
                if (uniqueID == null) {
                    //noinspection deprecation
                    offPlayer = Bukkit.getOfflinePlayer(playerName);
                } else {
                    offPlayer = Bukkit.getOfflinePlayer(uniqueID);
                }
                if (uniqueID == null) uniqueID = getOfflineUUID(offPlayer, offPlayer.getName());

                Variables.setVar(playerName, varID, uniqueID.toString());
                Variables.setVar(playerName, varName, offPlayer.getName());
                Variables.setTempVar("playerid", uniqueID.toString());
                Variables.setTempVar("playername", offPlayer.getName());
                return true;
            }
        }
    }

    public UUID getUUID (Player p, String playerName){
        if (!isOnline) //noinspection unused
        {
            UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes(Charsets.UTF_8));
            return uuid;
        }
        return p.getUniqueId();
    }

    public UUID getOfflineUUID (OfflinePlayer p, String playerName){
        if (!isOnline) //noinspection unused
        {
            UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes(Charsets.UTF_8));
            return uuid;
        }
        return p.getUniqueId();
    }

}
