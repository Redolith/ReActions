package me.fromgate.reactions.event;

public class FactionRelationEvent extends RAEvent {
	String faction1;
	String faction2;
	String oldRelation;
	String newRelation;

	public FactionRelationEvent(String faction1, String faction2, String oldRelation, String newRelation) {
		super(null);
		this.oldRelation = oldRelation;
		this.newRelation = newRelation;
		this.faction1 = faction1;
		this.faction2 = faction2;
	}
	
	
	public String getFaction(){
		return this.faction1;
	}
	
	public String getOtherFaction(){
		return this.faction2;
	}
	
	public String getOldRelation(){
		return this.oldRelation;
	}
	
	public String getNewRelation(){
		return this.newRelation;
	}
	

}
