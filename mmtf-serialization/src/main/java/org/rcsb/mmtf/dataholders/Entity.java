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
	private String sequence;
    
    
	/**
	 * Get the description of the entity.
	 * @return a strign describing the entity, e.g. The enzymatically competent form of HIV 
	 * protease is a dimer. This entity  corresponds to one monomer of an active dimer.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * Set the description of this entity.
	 * @param description the string describing the entity.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * The type of this Entity, one of: polymer, non-polymer, macrolide or water.
	 * @return the type of entity as a string. 
	 */
	public String getType() {
		return type;
	}
	/**
	 * Set the type of the entity, one of: polymer, non-polymer, macrolide or water.
	 * @param type the string defining the type of entity.
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * Get a list of integers specifying the indices of the chains related to this entity.
	 * @return a list of integers - specifying the indices of the chains related to this entity.
	 */
	public int[] getChainIndexList() {
		return chainIndexList;
	}
	/**
	 * Set a list of integers specifying the indices of the chains related to this entity.
	 * @param chainIndexList  a list of integers - specifying the indices of the chains related to this entity.
	 */
	public void setChainIndexList(int[] chainIndexList) {
		this.chainIndexList = chainIndexList;
	}
	/**
	 * Get the sequence of the entity.
	 * @return the entity sequence as a string
	 */
	public String getSequence() {
		return sequence;
	}
	/**
	 * Set the sequence of the entity.
	 * @param entitySequence the entity sequence as a string.
	 */
	public void setSequence(String entitySequence) {
		this.sequence = entitySequence;
	}
}
