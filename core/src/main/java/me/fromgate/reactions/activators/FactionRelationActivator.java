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
import me.fromgate.reactions.event.FactionRelationEvent;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class FactionRelationActivator extends Activator {

    private Set<String> factions;
    private String oldRelation;
    private String newRelation;

    public FactionRelationActivator(String name, String group, YamlConfiguration cfg) {
        super(name, group, cfg);
    }

    public FactionRelationActivator(String name, String param) {
        super(name, "activators");
        this.factions = new HashSet<>();
        Param params = new Param(param, "newrelation");
        this.factions.add(params.getParam("faction", params.getParam("faction1", "ANY")).toUpperCase());
        this.factions.add(params.getParam("faction2", params.getParam("otherfaction", "ANY")).toUpperCase());
        this.newRelation = params.getParam("faction2", "ANY");
        this.newRelation = params.getParam("newrelation", "ANY");
        this.oldRelation = params.getParam("oldrelation", "ANY");
    }


    public boolean mustExecute(String faction1, String faction2, String oldRelation, String newRelation) {
        if (!isFactionRelated(faction1, faction2)) return false;
        return isRelation(this.oldRelation, oldRelation) && isRelation(this.newRelation, newRelation);
    }

    public boolean isRelation(String relation1, String relation2) {
        if (relation1.isEmpty()) return true;
        if (relation1.equalsIgnoreCase("any")) return true;
        return relation1.equalsIgnoreCase(relation2);
    }

    public boolean isFactionRelated(String faction1, String faction2) {
        if (factions.isEmpty()) return true;
        if ((factions.size() == 1) && factions.contains("ANY")) return true;
        if (!factions.contains("ANY"))
            return factions.contains(faction1.toUpperCase()) && factions.contains(faction2.toUpperCase());
        return factions.contains(faction1.toUpperCase()) || factions.contains(faction2.toUpperCase());
    }

    @Override
    public boolean activate(Event event) {
        if (!(event instanceof FactionRelationEvent)) return false;
        FactionRelationEvent fe = (FactionRelationEvent) event;
        Variables.setTempVar("faction1", fe.getFaction());
        Variables.setTempVar("faction2", fe.getOtherFaction());
        Variables.setTempVar("oldrelation", fe.getOldRelation());
        Variables.setTempVar("newrelation", fe.getNewRelation());
        if (this.mustExecute(fe.getFaction(), fe.getOtherFaction(), fe.getOldRelation(), fe.getNewRelation())) {
            return Actions.executeActivator(null, this);
        }
        return false;
    }

    @Override
    public boolean isLocatedAt(Location loc) {
        return false;
    }

    @Override
    public void save(String root, YamlConfiguration cfg) {
        List<String> factionList = new ArrayList<>();
        factionList.addAll(this.factions);
        cfg.set(root + ".factions", factionList);
        cfg.set(root + ".old-relation", this.oldRelation);
        cfg.set(root + ".new-relation", this.newRelation);
    }

    @Override
    public void load(String root, YamlConfiguration cfg) {
        this.factions = new HashSet<>();
        this.factions.addAll(cfg.getStringList(root + ".factions"));
        this.oldRelation = cfg.getString(root + ".old-relation", "ANY");
        this.newRelation = cfg.getString(root + ".new-relation", "ANY");
    }

    @Override
    public ActivatorType getType() {
        return ActivatorType.FCT_RELATION;
    }

    @Override
    public boolean isValid() {
        return true;
    }

}
