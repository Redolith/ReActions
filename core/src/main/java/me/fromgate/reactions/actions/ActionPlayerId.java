package me.fromgate.reactions.actions;

import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.Variables;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by MaxDikiy on 5/6/2017.
 */
public class ActionPlayerId extends Action {
    private Boolean isOnlineMode;

    @Override
    public boolean execute(Player p, Param params) {
        String playerName = params.getParam("player", "");
        this.isOnlineMode = params.getParam("online", false);
        String varID = params.getParam("varid", "");
        String varName = params.getParam("varname", "");

        UUID uniqueID = null;
        String uuid;
        String pName;

        if (playerName.isEmpty()) {
            uniqueID = getUUID(p);
            uuid = uniqueID.toString();
            pName = p.getName();
        } else {
            Player player;
            String[] components = playerName.split("-");
            if (components.length == 5) uniqueID = UUID.fromString(playerName);
            if (uniqueID == null) {
                //noinspection deprecation
                player = Bukkit.getPlayer(playerName);
            } else {
                player = Bukkit.getPlayer(uniqueID);
            }
            if (player != null) {
                pName = player.getName();
                if (uniqueID == null) uniqueID = getUUID(player);
            } else {
                OfflinePlayer offPlayer;
                if (uniqueID == null) {
                    //noinspection deprecation
                    offPlayer = Bukkit.getOfflinePlayer(playerName);
                } else {
                    offPlayer = Bukkit.getOfflinePlayer(uniqueID);
                }
                pName = offPlayer.getName();
                if (uniqueID == null) uniqueID = getUUID(offPlayer);
            }
            uuid = uniqueID.toString();
        }
        if (pName == null) pName = "";
        Variables.setVar(playerName, varID, uuid);
        Variables.setVar(playerName, varName, pName);
        Variables.setTempVar("playerid", uuid);
        Variables.setTempVar("playername", pName);
        return true;
    }


    private UUID getUUID(OfflinePlayer p) {
        return Util.getUUID(p, p.getName(), isOnlineMode);
    }

}
