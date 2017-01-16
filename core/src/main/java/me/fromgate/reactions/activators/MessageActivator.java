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
import me.fromgate.reactions.event.MessageEvent;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

public class MessageActivator extends Activator {
    Type type;
    Source source;
    String mask;

    public MessageActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }

    public MessageActivator(String name, String param) {
        super(name, "activators");
        Param params = new Param(param, "mask");
        this.type = Type.getByName(params.getParam("type", "EQUAL"));
        this.source = Source.getByName(params.getParam("source", "CHAT_MESSAGE"));
        this.mask = params.getParam("mask", params.getParam("message", "<message mask>"));
    }

    @Override
    public void save(String root, YamlConfiguration cfg) {
        cfg.set(root + ".mask", mask);
        cfg.set(root + ".type", type.name());
        cfg.set(root + ".source", source.name());
    }

    @Override
    public void load(String root, YamlConfiguration cfg) {
        mask = cfg.getString(root + ".mask", "Unknown mask");
        this.type = Type.getByName(cfg.getString(root + ".type", Type.EQUAL.name()));
        this.source = Source.getByName(cfg.getString(root + ".source", Source.CHAT_INPUT.name()));
    }


    public enum Type {
        REGEX,
        CONTAINS,
        EQUAL,
        START,
        END;

        public static Type getByName(String name) {
            if (name.equalsIgnoreCase("contain")) return Type.CONTAINS;
            if (name.equalsIgnoreCase("equals")) return Type.EQUAL;
            for (Type t : Type.values()) {
                if (t.name().equalsIgnoreCase(name)) return t;
            }
            return Type.EQUAL;
        }

        public static boolean isValid(String name) {
            for (Type t : Type.values()) {
                if (t.name().equalsIgnoreCase(name)) return true;
            }
            return false;
        }
    }


    public enum Source {
        ALL,
        CHAT_INPUT,
        CONSOLE_INPUT,
        CHAT_OUTPUT,
        LOG_OUTPUT;
        //ANSWER;

        public static Source getByName(String name) {
            for (Source source : Source.values()) {
                if (source.name().equalsIgnoreCase(name)) return source;
            }
            return Source.ALL;
        }

        public static boolean isValid(String name) {
            for (Source source : Source.values()) {
                if (source.name().equalsIgnoreCase(name)) return true;
            }
            return false;
        }
    }


    @Override
    public boolean activate(Event event) {
        if (!(event instanceof MessageEvent)) return false;
        MessageEvent e = (MessageEvent) event;
        if (!e.isForActivator(this)) return false;
        setTempVars(e.getMessage());
        return Actions.executeActivator(e.getPlayer(), this);
    }

    @Override
    public boolean isLocatedAt(Location loc) {
        return false;
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.MESSAGE;
    }

    public boolean filterMessage(Source source, String message) {
        if (source != this.source && this.source != Source.ALL) return false;
        return filter(message);
    }

    private boolean filter(String message) {
        switch (type) {
            case CONTAINS:
                return message.toLowerCase().contains(this.mask.toLowerCase());
            case END:
                return message.toLowerCase().endsWith(this.mask.toLowerCase());
            case EQUAL:
                return message.equalsIgnoreCase(this.mask);
            case REGEX:
                return message.matches(this.mask);
            case START:
                return message.toLowerCase().startsWith(this.mask.toLowerCase());
        }
        return false;
    }


    private void setTempVars(String message) {
        Variables.setTempVar("message", message);
        String[] args = message.split(" ");
        int countInt = 0;
        int countNum = 0;
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                Variables.setTempVar("word" + Integer.toString(i + 1), args[i]);
                Variables.setTempVar("wnum" + Integer.toString(i + 1), args[i].replaceAll("\\D+", ""));
                if (args[i].matches("-?[1-9]+[0-9]*")) {
                    countInt++;
                    Variables.setTempVar("int" + countInt, args[i]);
                }
                if (args[i].matches("-?[0-9]+\\.?[0-9]*")) {
                    countNum++;
                    Variables.setTempVar("num" + countNum, args[i]);
                }

            }
        }
        Variables.setTempVar("word-count", Integer.toString(args.length));
        Variables.setTempVar("int-count", Integer.toString(countInt));
        Variables.setTempVar("num-count", Integer.toString(countNum));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name).append(" [").append(getType()).append("]");
        if (!getFlags().isEmpty()) sb.append(" F:").append(getFlags().size());
        if (!getActions().isEmpty()) sb.append(" A:").append(getActions().size());
        if (!getReactions().isEmpty()) sb.append(" R:").append(getReactions().size());
        sb.append(" (");
        sb.append("type:").append(this.type.name());
        sb.append(" source:").append(this.source.name());
        sb.append(" mask:").append(this.mask);
        sb.append(")");
        return sb.toString();
    }


}
