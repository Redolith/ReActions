package me.fromgate.reactions.commands;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.activators.Activator;
import me.fromgate.reactions.activators.Activators;
import me.fromgate.reactions.menu.InventoryMenu;
import me.fromgate.reactions.util.Locator;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

@CmdDefine(command = "react", description = "cmd_info", permission = "reactions.config",
        subCommands = {"info"}, allowConsole = true, shortDescription = "&3/react info <activator> [f|a|r]")
public class CmdInfo extends Cmd {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        String id = args.length > 1 ? args[1] : "";
        if (id.isEmpty()) return false;
        String far = args.length > 2 ? args[2] : "";
        if (Activators.contains(id)) {
            printActInfo(sender, id, far);
        } else if (id.equalsIgnoreCase("menu")) {
            InventoryMenu.printMenu(sender, far);
        } else ReActions.getUtil().printMSG(sender, "cmd_unknownbutton", id);
        return true;
    }


    private void printActInfo(CommandSender p, String actname, String far) {
        Activator act = Activators.get(actname);
        boolean f = false;
        boolean a = false;
        boolean r = false;
        if (far.isEmpty()) {
            f = true;
            a = true;
            r = true;
        } else {
            f = far.contains("f") || far.equalsIgnoreCase("flag") || far.equalsIgnoreCase("flags");
            a = far.contains("a") || far.equalsIgnoreCase("action") || far.equalsIgnoreCase("actions");
            r = far.contains("r") || far.equalsIgnoreCase("reaction") || far.equalsIgnoreCase("me/fromgate/reactions");
        }

        ReActions.getUtil().printMsg(p, "&5☆ &d&l" + ReActions.getUtil().getMSGnc("msg_actinfotitle") + " &r&5☆");
        ReActions.getUtil().printMSG(p, "msg_actinfo", act.getName(), act.getType(), act.getGroup());
        ReActions.getUtil().printMSG(p, "msg_actinfo2", act.getFlags().size(), act.getActions().size(), act.getReactions().size());
        if (f && (!act.getFlags().isEmpty())) {
            List<String> flg = new ArrayList<String>();
            for (int i = 0; i < act.getFlags().size(); i++)
                flg.add((act.getFlags().get(i).not ? "&4! &e" : "  &e") + act.getFlags().get(i).flag + " &3= &a" + act.getFlags().get(i).value);
            ReActions.getUtil().printPage(p, flg, 1, "lst_flags", "", true, 100);
        }
        if (a && (!act.getActions().isEmpty())) {
            List<String> flg = new ArrayList<String>();
            for (int i = 0; i < act.getActions().size(); i++) {
                String action = act.getActions().get(i).flag;
                String param = act.getActions().get(i).value;
                if (action.equalsIgnoreCase("tp")) {
                    Location loc = Locator.parseCoordinates(param);//Util.parseLocation(param);
                    if (loc != null) param = Locator.locationToStringFormated(loc);
                }
                flg.add("  &e" + action + " &3= &a" + param);
            }
            ReActions.getUtil().printPage(p, flg, 1, "lst_actions", "", true, 100);
        }
        if (r && (!act.getReactions().isEmpty())) {
            List<String> flg = new ArrayList<String>();
            for (int i = 0; i < act.getReactions().size(); i++) {
                String action = act.getReactions().get(i).flag;
                String param = act.getReactions().get(i).value;
                if (action.equalsIgnoreCase("tp")) {
                    Location loc = Locator.parseCoordinates(param);
                    if (loc != null) param = Locator.locationToStringFormated(loc);
                }
                flg.add("  &e" + action + " &3= &a" + param);
            }
            ReActions.getUtil().printPage(p, flg, 1, "lst_reactions", "", true, 100);
        }
    }
}
