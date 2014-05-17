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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.fromgate.reactions.actions.Actions;
import me.fromgate.reactions.event.SignEvent;
import me.fromgate.reactions.util.ParamUtil;
import me.fromgate.reactions.util.Variables;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;

public class SignActivator extends Activator {
	private List<String> maskLines;

	public SignActivator(String name, String group, YamlConfiguration cfg) {
		super(name, group, cfg);
	}

	public SignActivator(String name, String param) {
		super (name, "activators");
		Map<String,String>  params = ParamUtil.parseParams(param);
		maskLines = new ArrayList<String>();
		maskLines.add(ParamUtil.getParam(params, "line1", ""));
		maskLines.add(ParamUtil.getParam(params, "line2", ""));
		maskLines.add(ParamUtil.getParam(params, "line3", ""));
		maskLines.add(ParamUtil.getParam(params, "line4", ""));
	}

	
	public boolean checkMask (String [] sign){
		if (maskLines.isEmpty()) return false;
		int emptyLines = 0;
		for (int i = 0; i<Math.min(4, maskLines.size());i++){
			if (maskLines.get(i).isEmpty()) {
				emptyLines++;
				continue;
			}
			if (!maskLines.get(i).equalsIgnoreCase(sign[i])) return false;
		}
		if (emptyLines>=4) return false;
		return true;
	}
	
	@Override
	public boolean activate(Event event) {
		if (!(event instanceof SignEvent)) return false;
		SignEvent signEvent = (SignEvent) event;
		if (!checkMask (signEvent.getSignLines())) return false;
		for (int i = 0; i<signEvent.getSignLines().length; i++)
			Variables.setTempVar("sign_line"+Integer.toString(i+1), signEvent.getSignLines()[i]);
		Variables.setTempVar("sign_loc", signEvent.getSignLocation());
		return Actions.executeActivator(signEvent.getPlayer(), this);
	}

	@Override
	public boolean isLocatedAt(Location loc) {
		return false;
	}

	@Override
	public void save(String root, YamlConfiguration cfg) {
		cfg.set(root+".sign-mask", maskLines);
	}

	@Override
	public void load(String root, YamlConfiguration cfg) {
		maskLines = cfg.getStringList(root+".sign-mask");
	}

	@Override
	public ActivatorType getType() {
		return ActivatorType.SIGN;
	}

}
