package org.rcsb.mmtf.dataholders;

import java.io.Serializable;

/**
 * Entity level data store.
 * An Entity is defined as each of the distinct molecules present 
 * in a PDB structure. It can be of type polymer, non-polymer, macrolide or water.
 * 
 * @author Anthony Bradley
 */
public class Entity implements Serializable {

	/** Serial id for this version of the format. */
	private static final long serialVersionUID = 9090730105071948103L;
	
	/** The description based on the PDBx model*/
    private String description;
    /** The type (polymer, non-polymer, water)*/
    private String type;
    /** The chain index list - referencing the entity of the asym id*/
    private int[] chainIndexList;
    /** The sequence for this entity*/
	private String entitySequence;
    
    
    
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * The type of this Entity, one of: polymer, non-polymer, macrolide or water.
	 * @return
	 */
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
	/**
	 * @return the entitySequence
	 */
	public String getEntitySequence() {
		return entitySequence;
	}
	/**
	 * @param entitySequence the entitySequence to set
	 */
	public void setEntitySequence(String entitySequence) {
		this.entitySequence = entitySequence;
	}
}
