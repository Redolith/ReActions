package me.fromgate.reactions.externals;

import me.fromgate.reactions.event.EventManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import com.massivecraft.factions.event.FactionsEventMembershipChange;
import com.massivecraft.factions.event.FactionsEventRelationChange;

public class FactionListener implements Listener {

    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
	public void onFactionChange (FactionsEventMembershipChange event){
		EventManager.raiseFactionEvent(event.getUPlayer().getPlayer(), 
				event.getUPlayer().getFaction().isDefault() ? "default" : event.getUPlayer().getFactionName(),
						event.getNewFaction().isDefault() ? "default" : event.getNewFaction().getName());
	}
    
    
    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
	public void onRelationChange (FactionsEventRelationChange event){
    	EventManager.raiseFactionRelationEvent(event.getFaction().getName(),
    			event.getOtherFaction().getName(),
    			event.getFaction().getRelationWish(event.getOtherFaction()).name(),
    			event.getNewRelation().name());
    	
    	/*
    	Rel.ALLY;
    	Rel.ENEMY;
    	Rel.LEADER;
    	Rel.MEMBER;
    	Rel.NEUTRAL;
    	Rel.OFFICER;
    	Rel.RECRUIT;
    	Rel.TRUCE;*/
    	
    	
    	
    	
    }

}
