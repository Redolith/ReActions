/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2014, fromgate, fromgate@gmail.com
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


import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.VariableEvent;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

public class VariableActivator extends Activator {
    private String id;
    private boolean personal;

    public VariableActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }

    public VariableActivator(String name, String param) {
        super(name, "activators");
        Param params = new Param(param, "id");
        id = params.getParam("id", "UnknownVariable");
        personal = params.getParam("personal", false);
    }

    @Override
    public boolean activate(Event event) {
        if (!(event instanceof VariableEvent)) return false;
        VariableEvent ve = (VariableEvent) event;
        if (!this.id.equalsIgnoreCase(ve.getVariableId())) return false;
        if (personal && ve.getPlayer() != null) return false;
        Variables.setTempVar("var-id", ve.getVariableId());
        Variables.setTempVar("var-old", ve.getOldValue());
        Variables.setTempVar("var-new", ve.getNewValue());
        return Actions.executeActivator(ve.getPlayer(), this);
    }


    @Override
    public void save(String root, YamlConfiguration cfg) {
        cfg.set(root + ".variable-id", id);
        cfg.set(root + ".personal", personal);
    }

    @Override
    public void load(String root, YamlConfiguration cfg) {
        id = cfg.getString(root + ".variable-id", "UnknownVariable");
        personal = cfg.getBoolean(root + ".personal", false);
    }

    @Override
    public boolean isLocatedAt(Location loc) {
        return false;
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.VARIABLE;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name).append(" [").append(getType()).append("]");
        if (!getFlags().isEmpty()) sb.append(" F:").append(getFlags().size());
        if (!getActions().isEmpty()) sb.append(" A:").append(getActions().size());
        if (!getReactions().isEmpty()) sb.append(" R:").append(getReactions().size());
        sb.append(" (");
        sb.append("variable id:").append(this.id);
        if (this.personal) sb.append(" personal:true");
        sb.append(")");
        return sb.toString();
    }

}
