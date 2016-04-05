package org.rcsb.mmtf.dataholders;


/**
 * The encoding of secondary structure types.
 * @author Anthony Bradley
 *
 */
public enum DsspType {

	PI_HELIX("pi Helix",0),
	BEND("Bend",1),
	ALPHA_HELIX("alpha Helix",2),
	EXTENDED("Extended",3),
	HELIX_3_10("3-10 Helix",4),
	BRIDGE("Bridge",5),
	TURN("Turn",6),
	COIL("Coil",7),
	NULL_ENTRY("NA",-1);
	
	/** The String type of the DSSP*/
	private String dsspType;
	/** The index used for encoding secondary structure 
	 * information. One for each type*/
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

	/** Returns the type of the DSSP as a String
	 *
	 * @return String representation of the DSSP type.
	 */
	public String getDsspType() {
		return dsspType;
	}

	
	/** Returns the index of the Dssp as an integer
	 *
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

	/** Creates a new DsspType from a String value.
	 *  Returns DsspType.NULL_ENTRY if dsspType is "NA" or not one of the supported
	 *  standard types.
	 *
	 * @param dsspType String value , should be one of 	"pi Helix","Bend","alpha Helix","Extended",
	 * "3-10 Helix","Bridge","Turn","Coil","NA"
	 * @return an DsspType object
	 */
	public static DsspType dsspTypeFromString(String dsspType)
	{

		if ( dsspType == null)
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

}