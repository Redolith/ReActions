package me.fromgate.reactions.actions;

import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Created by MaxDikiy on 5/7/2017.
 */
public class ActionFile extends Action {
    @Override
    public boolean execute(Player p, Param params) {
        String action = params.getParam("action", "");
        String fileName = params.getParam("fileName", "");
        String fileNameTo = params.getParam("fileNameTo", "");
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

        } else {
            if (fileNameTo.isEmpty()) return false;
            File fileTo = new File(dir + "/" + fileNameTo);
            try {
                File fileToDir = new File(fileTo.getCanonicalPath());
                if (!fileToDir.exists()) fileToDir.mkdirs();
                if (file.isFile()) {
                    if (action.equalsIgnoreCase("copy")) {
                        Files.copy(file.toPath(), fileTo.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                    if (action.equalsIgnoreCase("move")) {
                        Files.move(file.toPath(), fileTo.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                    return true;
                }
            } catch (IOException e) {
                Variables.setTempVar("filedebug", e.getLocalizedMessage());
            }

        }
        return false;
    }
}
