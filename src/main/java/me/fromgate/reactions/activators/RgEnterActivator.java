/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2013, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/reactions/
 *   * 
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

import java.util.List;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.RegionEnterEvent;
import me.fromgate.reactions.util.RAWorldGuard;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

public class RgEnterActivator extends Activator {

    private String region;

    RgEnterActivator (String name, String group, YamlConfiguration cfg){
        super (name, group, cfg);
    }

    public RgEnterActivator (String name, String group, String region){
        super (name,group);
        this.region = region;
    }

    public RgEnterActivator (String name, String region){
        super (name,"activators");
        this.region = region;
    }


    @Override
    public void activate(Event event) {
        if (!(event instanceof RegionEnterEvent)) return;
        RegionEnterEvent be = (RegionEnterEvent) event;
        if (be.getRegion().equals(this.region))	Actions.executeActivator(be.getPlayer(), this);
    }

    @Override
    public boolean isLocatedAt(Location loc) {
        if (!RAWorldGuard.isConnected()) return false;
        List<String> rgs = RAWorldGuard.getRegions(loc);
        if (rgs.isEmpty()) return false;
        return rgs.contains(this.region);
    }

    @Override
    public void save(String root, YamlConfiguration cfg) {
        cfg.set(root+".region", this.region);
    }

    @Override
    public void load(String root, YamlConfiguration cfg) {
        this.region=cfg.getString(root+".region");
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.REGION_ENTER;
    }


}
