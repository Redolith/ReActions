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

package me.fromgate.reactions.actions;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.util.Param;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ActionCommand extends Action {
    private int commandAs = 0; //0 - normal, 1 - op, 2 - console

    public ActionCommand(int commandAs) {
        this.commandAs = commandAs;
    }

    @Override
    public boolean execute(Player player, Param params) {
        if (commandAs != 2 && player == null) return false;
        String commandLine = ChatColor.translateAlternateColorCodes('&', params.getParam("param-line"));
        switch (commandAs) {
            case 0:
                dispatchCommand(false, player, commandLine);
                break;
            case 1:
                dispatchCommand(true, player, commandLine);
                break;
            case 2:
                dispatchCommand(false, Bukkit.getConsoleSender(), commandLine);
                break;
        }
        return true;
    }

    public static void dispatchCommand(final boolean setOp, final CommandSender sender, final String commandLine) {
        if (Bukkit.isPrimaryThread()) {
            dispatchCmd(setOp, sender, commandLine);
        } else {
            Bukkit.getScheduler().runTask(ReActions.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    dispatchCmd(setOp, sender, commandLine);
                }
            });
        }
    }

    private static void dispatchCmd(final boolean setOp, final CommandSender sender, final String commandLine) {
        boolean isOp = sender.isOp();
        if (setOp) {
            sender.setOp(true);
        }
        Bukkit.getServer().dispatchCommand(sender, commandLine);
        if (setOp) {
            sender.setOp(isOp);
        }
    }

}
