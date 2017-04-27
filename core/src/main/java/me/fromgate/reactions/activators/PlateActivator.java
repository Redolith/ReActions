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
import me.fromgate.reactions.event.PlateEvent;
import me.fromgate.reactions.util.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

public class PlateActivator extends Activator {
    String world;
    int x;
    int y;
    int z;

    PlateActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }

    PlateActivator(String name, Block targetBlock) {
        super(name, "activators");
        if (targetBlock != null && (targetBlock.getType() == Material.STONE_PLATE) || (targetBlock.getType() == Material.WOOD_PLATE)) {
            this.world = targetBlock.getWorld().getName();
            this.x = targetBlock.getX();
            this.y = targetBlock.getY();
            this.z = targetBlock.getZ();
        }
    }

    public PlateActivator(String name, Block targetBlock, String param) {
        this(name, targetBlock);
    }


    /*
     * Очередная залипуха, надо будет потом переделать
     */
    @Override
    public boolean activate(Event event) {
        if (!(event instanceof PlateEvent)) return false;
        PlateEvent be = (PlateEvent) event;
        if (!isLocatedAt(be.getPlateLocation())) return false;
        return Actions.executeActivator(be.getPlayer(), this);
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
    }

    @Override
    public void load(String root, YamlConfiguration cfg) {
        world = cfg.getString(root + ".world");
        x = cfg.getInt(root + ".x");
        y = cfg.getInt(root + ".y");
        z = cfg.getInt(root + ".z");
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.PLATE;
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
        sb.append(" (").append(world).append(", ").append(x).append(", ").append(y).append(", ").append(z).append(")");
        return sb.toString();
    }

}
