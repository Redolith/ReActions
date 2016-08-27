package me.fromgate.reactions.commands;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.activators.Activators;
import me.fromgate.reactions.menu.InventoryMenu;
import me.fromgate.reactions.timer.Timers;
import me.fromgate.reactions.util.Delayer;
import me.fromgate.reactions.util.Locator;
import me.fromgate.reactions.util.Variables;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;


@CmdDefine(command = "react", description = "cmd_list", permission = "reactions.config",
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
        if (ReActions.getUtil().isIntegerGZ(arg1)) printAct(sender, 1, lpp);
        else {
            String mask = "";
            if (ReActions.getUtil().isIntegerGZ(arg2)) {
                page = Integer.parseInt(arg2);
                mask = arg3;
            } else if (ReActions.getUtil().isIntegerGZ(arg3)) {
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
                //ReActions.getUtil().printMSG(sender,"msg_listcount",Activators.size(),Locator.sizeTpLoc());
            }
        }
        return true;
    }

    public void printAct(CommandSender p, int page, int lpp) {
        List<String> ag = Activators.getActivatorsList();
        ReActions.getUtil().printPage(p, ag, page, "msg_actlist", "", true);
        ReActions.getUtil().printMSG(p, "msg_listcount", Activators.size(), Locator.sizeTpLoc());
    }

    public void printActGroup(CommandSender p, String group, int page, int lpp) {
        List<String> ag = Activators.getActivatorsListGroup(group);
        ReActions.getUtil().printPage(p, ag, page, "&6" + ReActions.getUtil().getMSG("msg_actlistgrp", group), "", true);
    }

    public void printActType(CommandSender p, String type, int page, int lpp) {
        List<String> ag = Activators.getActivatorsList(type);
        ReActions.getUtil().printPage(p, ag, page, "&6" + ReActions.getUtil().getMSG("msg_actlisttype", type), "", true);
    }


}
