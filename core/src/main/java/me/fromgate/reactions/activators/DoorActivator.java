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


package me.fromgate.reactions.activators;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.DoorEvent;
import me.fromgate.reactions.util.Util;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

public class DoorActivator extends Activator {
    String state; //open, close
    //координаты нижнего блока двери
    String world;
    int x;
    int y;
    int z;

    public DoorActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }

    public DoorActivator(String name, Block b, String param) {
        super(name, "activators");
        this.state = "ANY";
        if (param.equalsIgnoreCase("open")) state = "OPEN";
        if (param.equalsIgnoreCase("close")) state = "CLOSE";
        this.world = b.getWorld().getName();
        this.x = b.getX();
        this.y = b.getY();
        this.z = b.getZ();
    }

    @Override
    public boolean activate(Event event) {
        if (!(event instanceof DoorEvent)) return false;
        DoorEvent de = (DoorEvent) event;
        if (de.getDoorBlock() == null) return false;
        if (!isLocatedAt(de.getDoorLocation())) return false;
        if (this.state.equalsIgnoreCase("open") && de.isDoorOpened()) return false;
        if (this.state.equalsIgnoreCase("close") && (!de.isDoorOpened())) return false;
        return Actions.executeActivator(de.getPlayer(), this);
    }

    @Override
    public boolean isLocatedAt(Location l) {
        if (l == null) return false;
        if (!world.equals(l.getWorld().getName())) return false;
        if (x != l.getBlockX()) return false;
        if (y != l.getBlockY()) return false;
        return (z == l.getBlockZ());
    }


    @Override
    public void save(String root, YamlConfiguration cfg) {
        cfg.set(root + ".world", this.world);
        cfg.set(root + ".x", x);
        cfg.set(root + ".y", y);
        cfg.set(root + ".z", z);
        cfg.set(root + ".state", state);
        cfg.set(root + ".lever-state", null);
    }

    @Override
    public void load(String root, YamlConfiguration cfg) {
        world = cfg.getString(root + ".world");
        x = cfg.getInt(root + ".x");
        y = cfg.getInt(root + ".y");
        z = cfg.getInt(root + ".z");
        this.state = cfg.getString(root + ".state", cfg.getString(root + ".lever-state", "ANY"));
        if ((!this.state.equalsIgnoreCase("open")) && (!state.equalsIgnoreCase("close"))) state = "ANY";
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.DOOR;
    }

    @Override
    public boolean isValid() {
        return !Util.emptySting(world);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name).append(" [").append(getType()).append("]");
        if (!getFlags().isEmpty()) sb.append(" F:").append(getFlags().size());
        if (!getActions().isEmpty()) sb.append(" A:").append(getActions().size());
        if (!getReactions().isEmpty()) sb.append(" R:").append(getReactions().size());
        sb.append(" (").append(world).append(", ").append(x).append(", ").append(y).append(", ").append(z);
        sb.append(" state:").append(this.state.toUpperCase()).append(")");
        return sb.toString();
    }


}
