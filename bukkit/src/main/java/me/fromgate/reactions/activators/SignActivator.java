/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2014, fromgate, fromgate@gmail.com
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

package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.SignEvent;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

public class SignActivator extends Activator {
    private List<String> maskLines;
    private ClickType click;

    public SignActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }

    public SignActivator(String name, String param, Sign sign) {
        super(name, "activators");
        Param params = new Param(param);
        maskLines = new ArrayList<String>();
        maskLines.add(params.getParam("line1", sign.getLine(0)));
        maskLines.add(params.getParam("line2", sign.getLine(1)));
        maskLines.add(params.getParam("line3", sign.getLine(2)));
        maskLines.add(params.getParam("line4", sign.getLine(3)));
        click = ClickType.getByName(params.getParam("click", "RIGHT"));
    }

    public SignActivator(String name, String param) {
        super(name, "activators");
        Param params = new Param(param);
        maskLines = new ArrayList<String>();
        maskLines.add(params.getParam("line1", ""));
        maskLines.add(params.getParam("line2", ""));
        maskLines.add(params.getParam("line3", ""));
        maskLines.add(params.getParam("line4", ""));
        click = ClickType.getByName(params.getParam("click", "RIGHT"));
    }


    public boolean checkMask(String[] sign) {
        if (maskLines.isEmpty()) return false;
        int emptyLines = 0;
        for (int i = 0; i < Math.min(4, maskLines.size()); i++) {
            if (maskLines.get(i).isEmpty()) {
                emptyLines++;
                continue;
            }
            if (!ChatColor.translateAlternateColorCodes('&', maskLines.get(i)).equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', sign[i])))
                return false;
        }
        if (emptyLines >= 4) return false;
        return true;
    }

    @Override
    public boolean activate(Event event) {
        if (!(event instanceof SignEvent)) return false;
        SignEvent signEvent = (SignEvent) event;
        if (!clickCheck(signEvent.isLeftClicked())) return false;
        if (!checkMask(signEvent.getSignLines())) return false;
        for (int i = 0; i < signEvent.getSignLines().length; i++)
            Variables.setTempVar("sign_line" + Integer.toString(i + 1), signEvent.getSignLines()[i]);
        Variables.setTempVar("sign_loc", signEvent.getSignLocation());
        Variables.setTempVar("click", signEvent.isLeftClicked() ? "left" : "right");
        return Actions.executeActivator(signEvent.getPlayer(), this);
    }

    @Override
    public boolean isLocatedAt(Location loc) {
        return false;
    }

    @Override
    public void save(String root, YamlConfiguration cfg) {
        cfg.set(root + ".sign-mask", maskLines);
        cfg.set(root + ".click-type", click.name());
    }

    @Override
    public void load(String root, YamlConfiguration cfg) {
        maskLines = cfg.getStringList(root + ".sign-mask");
        click = ClickType.getByName(cfg.getString(root + ".click-type", "RIGHT"));
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.SIGN;
    }

    enum ClickType {
        RIGHT,
        LEFT,
        ANY;

        public static ClickType getByName(String clickStr) {
            if (clickStr.equalsIgnoreCase("left")) return ClickType.LEFT;
            if (clickStr.equalsIgnoreCase("any")) return ClickType.ANY;
            return ClickType.RIGHT;
        }
    }

    private boolean clickCheck(boolean leftClick) {
        switch (click) {
            case ANY:
                return true;
            case LEFT:
                return leftClick;
            case RIGHT:
                return !leftClick;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name).append(" [").append(getType()).append("]");
        if (!getFlags().isEmpty()) sb.append(" F:").append(getFlags().size());
        if (!getActions().isEmpty()) sb.append(" A:").append(getActions().size());
        if (!getReactions().isEmpty()) sb.append(" R:").append(getReactions().size());
        sb.append(" (");
        sb.append("click:").append(this.click.name());
        sb.append(" sign:");
        if (this.maskLines.isEmpty()) sb.append("[][][][]");
        else {
            for (String s : maskLines)
                sb.append("[").append(s).append("]");
        }
        sb.append(")");
        return sb.toString();
    }

}
