package org.rcsb.mmtf.encoder;

import java.io.Serializable;

/**
 * Class to store summary data about a structure.
 * Using boxed types so they can be null
 * @author Anthony Bradley
 *
 */
public class SummaryData implements Serializable {
	private static final long serialVersionUID = -7724752942891007770L;
	/** The number of bonds in the structure.*/
	public Integer numBonds;
	/** The number of atoms in the structure.*/
	public Integer numAtoms;
	/** The number of groups in the structure.*/
	public Integer numGroups;
	/** The number of chains in the structure.*/
	public Integer numChains;
	/** The number of models in the structure.*/
	public Integer numModels;

}
