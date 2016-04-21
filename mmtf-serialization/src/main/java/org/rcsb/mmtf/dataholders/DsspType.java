package org.rcsb.mmtf.dataholders;


/**
 * The encoding of secondary structure types.
 * Based on DSSP: Kabsch W, Sander C (1983). 
 * "Dictionary of protein secondary structure: pattern recognition of hydrogen-bonded and geometrical features".
 * Biopolymers 22 (12): 2577–637. doi:10.1002/bip.360221211. PMID 6667333.
 * @author Anthony Bradley
 */
public enum DsspType {

	/** A pi helix.*/
	PI_HELIX("pi Helix",0),
	/** A bend*/
	BEND("Bend",1),
	/** An alpha helix.*/
	ALPHA_HELIX("alpha Helix",2),
	/** An extended loop. */
	EXTENDED("Extended",3),
	/** A 3-10 alpha helix. */
	HELIX_3_10("3-10 Helix",4),
	/** A bridge. */
	BRIDGE("Bridge",5),
	/** A turn. */
	TURN("Turn",6),
	/** A coil.*/
	COIL("Coil",7),
	/** A null  entry - or an entry not recognised. */
	NULL_ENTRY("NA",-1);
	
	/** The String type of the DSSP*/
	private String dsspType;
	
	/** 
	 * The index used for encoding secondary structure 
	 * information. One for each type
	 * */
	private int dsspIndex;

	/**
	 * Constructor (private) of DSSP type
	 * @param inputDsspType The string type of the DSSP input.
	 * @param inputDsspIndex The integer index of hte DSSP input.
	 */
	private DsspType(String inputDsspType, int inputDsspIndex) {
		this.setDsspType(inputDsspType);
		this.setDsspIndex(inputDsspIndex);

	}

	/**
	 * Sets the DSSP index given a particular input  value.
	 * @param inputDsspIndex The input index (as specified above).
	 */
	private void setDsspIndex(int inputDsspIndex) {
		this.dsspIndex = inputDsspIndex;
	}

	/**
	 * Returns the type of the DSSP as a String
	 * @return String representation of the DSSP type.
	 */
	public String getDsspType() {
		return dsspType;
	}

	
	/** Returns the index of the Dssp as an integer
	 * @return String representation of the Dssp type.
	 */
	public int getDsspIndex() {
		return dsspIndex;
	}
	
	/**
	 * Sets the DSSP type using an input String.
	 * @param inputDsspType The input string used to define the DSSP type.
	 */
	private void setDsspType(String inputDsspType) {
		this.dsspType = inputDsspType;
	}

	/**
	 * Creates a new DsspType from a String value.
	 * Returns DsspType.NULL_ENTRY if dsspType is "NA" or not one of the supported
	 * standard types.
	 * @param dsspType String value , should be one of 	"pi Helix","Bend","alpha Helix","Extended",
	 * "3-10 Helix","Bridge","Turn","Coil","NA"
	 * @return an DsspType object
	 */
	public static DsspType dsspTypeFromString(String dsspType)
	{

		if (dsspType == null)
			return DsspType.NULL_ENTRY;

		for(DsspType et : DsspType.values())
		{
			if(dsspType.equals(et.dsspType))
			{
				return et;
			}
		}
		// Return a null entry.
		return DsspType.NULL_ENTRY;
	}
	
	/**
	 * Creates a new DsspType from an int val
	 * Returns DsspType.NULL_ENTRY if dsspType is -1 or not one of the supported
	 * standard types.
	 * @param dsspType int value , should be an integer value from -1 to 7
	 * @return an DsspType object
	 */
	public static DsspType dsspTypeFromInt(int dsspType)
	{

		for(DsspType et : DsspType.values())
		{
			if(dsspType==et.dsspIndex)
			{
				return et;
			}
		}
		// Return a null entry.
		return DsspType.NULL_ENTRY;
	}

}