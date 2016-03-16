package org.rcsb.mmtf.dataholders;


/**
 * A basic class to hold the basic information about a protein.
 *
 * @author Anthony Bradley
 */
public class CoreDataStruct {
	
	/** The pdb code. */
	// The core data in all modules
	protected String pdbCode = null;
	
	/**
	 * Gets the pdb code.
	 *
	 * @return the pdb code
	 */
	public String getPdbCode() {
		return pdbCode;
	}
	
	/**
	 * Sets the pdb code.
	 *
	 * @param pdbCode the new pdb code
	 */
	public void setPdbCode(String pdbCode) {
		this.pdbCode = pdbCode;
	}
	
	/** The number of models. */
	private int numModels = 0;
	
	/**
	 * Gets the num models.
	 *
	 * @return the num models
	 */
	public int getNumModels() {
		return numModels;
	}
	
	/**
	 * Sets the num models.
	 *
	 * @param numModels the new num models
	 */
	public void setNumModels(int numModels) {
		this.numModels = numModels;
	}

}
