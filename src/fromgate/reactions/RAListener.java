/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/weatherman/
 *   * 
 *  This file is part of ReActions.
 *  
 *  WeatherMan is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  WeatherMan is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with WeatherMan.  If not, see <http://www.gnorg/licenses/>.
 * 
 */

package fromgate.reactions;


import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.material.Button;


public class RAListener implements Listener{
	ReActions plg;

	public RAListener (ReActions plg){
		this.plg = plg;
	}


	@EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerInteract (PlayerInteractEvent event){
		if ((event.getAction() == Action.RIGHT_CLICK_BLOCK)||
				(event.getAction() == Action.LEFT_CLICK_BLOCK)&&
				(event.getClickedBlock().getType()==Material.STONE_BUTTON)){
			BlockState state = event.getClickedBlock().getState();
			if (state.getData() instanceof Button){
				Button button = (Button) state.getData();
				if (button.isPowered()) return;
				for (String clicker : plg.clickers.keySet())
					if (plg.clickers.get(clicker).equalLoc(event.getClickedBlock().getLocation()))
						plg.executeClicker(event.getPlayer(), plg.clickers.get(clicker));
			}
		}
	}
	
	
	@EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = false)
	public void onPlayerJoin (PlayerJoinEvent event){
		plg.debug.offPlayerDebug(event.getPlayer());
	}
}




