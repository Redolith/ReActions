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
import me.fromgate.reactions.util.TempOp;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ActionCommand extends Action {

    public final static int NORMAL = 0;
    public final static int OP = 1;
    public final static int CONSOLE = 2;
    public final static int CHAT = 3;

    private int commandAs = NORMAL;


    public ActionCommand(int commandAs) {
        this.commandAs = commandAs;
    }

    @Override
    public boolean execute(Player player, Param params) {
        if (commandAs != CONSOLE && player == null) return false;
        String commandLine = ChatColor.translateAlternateColorCodes('&', params.getParam("param-line"));
        switch (commandAs) {
            case NORMAL:
                dispatchCommand(false, player, commandLine);
                break;
            case OP:
                dispatchCommand(true, player, commandLine);
                break;
            case CONSOLE:
                dispatchCommand(false, Bukkit.getConsoleSender(), commandLine);
                break;
            case CHAT:
                commandLine = commandLine.replaceFirst("/", "");
                player.chat("/" + commandLine);
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
        TempOp.setTempOp(sender);
        Bukkit.getServer().dispatchCommand(sender, commandLine);
        TempOp.removeTempOp(sender);
    }

}
