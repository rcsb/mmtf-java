package org.rcsb.mmtf.dataholders;

public class Entity {

	/** The description based on the PDBx model*/
    private String description;
    /** The type (polymer, non-polymer, water)*/
    private String type;
    /** The chain index list - referencing the entity of the asym id*/
    private int[] chainIndexList;
    /** The sequence for this entity*/
    
    
    
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int[] getChainIndexList() {
		return chainIndexList;
	}
	public void setChainIndexList(int[] chainIndexList) {
		this.chainIndexList = chainIndexList;
	}
}
