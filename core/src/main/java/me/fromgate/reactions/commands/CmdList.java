package me.fromgate.reactions.commands;

import me.fromgate.reactions.activators.Activators;
import me.fromgate.reactions.menu.InventoryMenu;
import me.fromgate.reactions.timer.Timers;
import me.fromgate.reactions.util.Delayer;
import me.fromgate.reactions.util.Locator;
import me.fromgate.reactions.util.Util;
import me.fromgate.reactions.util.Variables;
import me.fromgate.reactions.util.message.M;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;


@CmdDefine(command = "react", description = M.CMD_LIST, permission = "reactions.config",
        subCommands = {"list"}, allowConsole = true, shortDescription = "&3/react list [loc|group|type] [page]")
public class CmdList extends Cmd {


    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (sender instanceof Player) ? (Player) sender : null;
        int lpp = (player == null) ? 1000 : 15;
        int page = 1;
        String arg1 = args.length >= 2 ? args[1] : "";
        String arg2 = args.length >= 3 ? args[2] : "";
        String arg3 = args.length >= 4 ? args[3] : "";
        if (Util.isIntegerGZ(arg1)) printAct(sender, 1, lpp);
        else {
            String mask = "";
            if (Util.isIntegerGZ(arg2)) {
                page = Integer.parseInt(arg2);
                mask = arg3;
            } else if (Util.isIntegerGZ(arg3)) {
                page = Integer.parseInt(arg3);
                mask = arg2;
            }

            if (arg1.equalsIgnoreCase("all")) {
                printAct(sender, page, lpp);
            } else if (arg1.equalsIgnoreCase("type")) {
                printActType(sender, mask, page, lpp);
            } else if (arg1.equalsIgnoreCase("group")) {
                printActGroup(sender, mask, page, lpp);
            } else if (arg1.equalsIgnoreCase("timer") || arg1.equalsIgnoreCase("timers")) {
                Timers.listTimers(sender, page);
            } else if (arg1.equalsIgnoreCase("delay") || arg1.equalsIgnoreCase("delays")) {
                Delayer.printDelayList(sender, page, lpp);
            } else if (arg1.equalsIgnoreCase("loc") || arg1.equalsIgnoreCase("location")) {
                Locator.printLocList(sender, page, lpp);
            } else if (arg1.equalsIgnoreCase("var") || arg1.equalsIgnoreCase("variables") || arg1.equalsIgnoreCase("variable")) {
                Variables.printList(sender, page, mask);
            } else if (arg1.equalsIgnoreCase("menu") || arg1.equalsIgnoreCase("menus")) {
                InventoryMenu.printMenuList(sender, page, mask);
            } else {
                printAct(sender, page, lpp);
            }
        }
        return true;
    }

    public void printAct(CommandSender sender, int page, int lpp) {
        List<String> ag = Activators.getActivatorsList();
        M.printPage(sender, ag, M.MSG_ACTLIST, page, lpp, true);
        M.MSG_LISTCOUNT.print(sender, Activators.size(), Locator.sizeTpLoc());
    }

    public void printActGroup(CommandSender sender, String group, int page, int lpp) {
        List<String> ag = Activators.getActivatorsListGroup(group);
        M.MSG_ACTLISTGRP.print(sender, group, '6', '6');
        M.printPage(sender, ag, null, page, lpp, true);
    }

    public void printActType(CommandSender sender, String type, int page, int lpp) {
        List<String> ag = Activators.getActivatorsList(type);
        M.MSG_ACTLISTTYPE.print(sender, type, '6', '6');
        M.printPage(sender, ag, null, page, lpp, true);
    }


}
