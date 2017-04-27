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
import me.fromgate.reactions.event.BlockClickEvent;
import me.fromgate.reactions.util.Locator;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

public class BlockClickActivator extends Activator {
    private String blockType;
    private String blockLocation;
    private ClickType click;


    public BlockClickActivator(String name, Block targetBlock, String param) {
        super(name, "activators");
        this.blockLocation = "";
        if (targetBlock != null && blockLocation != null && !blockLocation.isEmpty()) {
            blockLocation = Locator.locationToString(targetBlock.getLocation());
        }
        this.blockType = param;
        Param params = new Param(param);
        this.blockType = params.getParam("type", "");
        this.blockLocation = params.getParam("loc", "");
        this.click = ClickType.getByName(params.getParam("click", "ANY"));
    }

    public BlockClickActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }


    @Override
    public boolean activate(Event event) {
        if (!(event instanceof BlockClickEvent )) return false;
        BlockClickEvent  bce = (BlockClickEvent ) event;
        if (bce.getBlockClick() == null) return false;
        if (!isActivatorBlock(bce.getBlockClick())) return false;
        if (!clickCheck(bce.isLeftClicked())) return false;
        Variables.setTempVar("blocklocation", Locator.locationToString(bce.getBlockClick().getLocation()));
        Variables.setTempVar("blocktype", bce.getBlockClick().getType().name());
        Variables.setTempVar("click", bce.isLeftClicked() ? "left" : "right");
        return Actions.executeActivator(bce.getPlayer(), this);
    }

    private boolean checkLocations(Block block) {
        if (this.blockLocation.isEmpty()) return true;
        return this.isLocatedAt(block.getLocation());
    }

    private boolean isActivatorBlock(Block block) {
        if (!(this.blockType).isEmpty() && !(block.getType()).toString().equalsIgnoreCase(this.blockType)) return false;
        if (!checkLocations(block)) return false;
        return true;
    }


    @Override
    public boolean isLocatedAt(Location l) {
        if (this.blockLocation.isEmpty()) return false;
        Location loc = Locator.parseCoordinates(this.blockLocation);
        if (loc == null) return false;
        return l.getWorld().equals(loc.getWorld()) &&
                l.getBlockX() == loc.getBlockX() &&
                l.getBlockY() == loc.getBlockY() &&
                l.getBlockZ() == loc.getBlockZ();
    }


    @Override
    public void save(String root, YamlConfiguration cfg) {
        cfg.set(root + ".block-type", this.blockType);
        cfg.set(root + ".click-type", click.name());
        cfg.set(root + ".location", this.blockLocation.isEmpty() ? null : this.blockLocation);
    }

    @Override
    public void load(String root, YamlConfiguration cfg) {
        this.blockType = cfg.getString(root + ".block-type", "");
        click = ClickType.getByName(cfg.getString(root + ".click-type", "ANY"));
        this.blockLocation = cfg.getString(root + ".location", "");
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.BLOCK_CLICK;
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
        sb.append("type:").append(blockType.isEmpty() ? "-" : blockType.toUpperCase());
        sb.append(" click:").append(this.click.name());
        sb.append(" loc:").append(blockLocation.isEmpty() ? "-" : blockLocation);
        sb.append(")");
        return sb.toString();
    }

    public boolean isValid() {
        return (this.blockType == null || this.blockType.isEmpty()) && (this.blockLocation == null || this.blockLocation.isEmpty());
    }

}
