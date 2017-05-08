package me.fromgate.reactions.actions;

import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.entity.Player;

import java.io.File;

/**
 * Created by MaxDikiy on 5/7/2017.
 */
public class ActionFile extends Action {
    @Override
    public boolean execute(Player p, Param params) {
        String action = params.getParam("action", "");
        String fileName = params.getParam("fileName", "");
        if (action.isEmpty() || fileName.isEmpty()) return false;


        File path = new File("");
        String dir = path.getAbsolutePath();

        File file = new File(dir + "/" + fileName);
        Variables.setTempVar("fullpath", file.getAbsolutePath());

        if (action.equalsIgnoreCase("remove")) {
            int c = 0;
            if (file.isDirectory()) {
                String[] files = file.list();
                for (int i = 0; i < files.length; i++) {
                    File f = new File(file, files[i]);
                    if (f.delete()) c++;
                }
            } else {
                if (file.delete()) c = 1;
            }
            Variables.setTempVar("removecount", Integer.toString(c));
            return true;
        }
        return false;
    }
}
