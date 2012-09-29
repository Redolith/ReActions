package fromgate.reactions;


import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
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
}




