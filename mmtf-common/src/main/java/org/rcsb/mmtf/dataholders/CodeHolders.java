package org.rcsb.mmtf.dataholders;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The encoding of secondary structure types.
 * @author Anthony Bradley
 *
 */
public class CodeHolders implements Serializable {

	/** Serial id for this version of the format. */
	private static final long serialVersionUID = 8214511379505123391L;

	/** The Constant PI_HELIX_IND. */
	private static final int PI_HELIX_IND = 0;

	/** The Constant BEND_IND. */
	private static final int BEND_IND = 1;

	/** The Constant ALPHA_HELIX_IND. */
	private static final int ALPHA_HELIX_IND = 2;

	/** The Constant EXTENDED_IND. */
	private static final int EXTENDED_IND = 3;

	/** The Constant HELIX_3_10_IND. */
	private static final int HELIX_3_10_IND = 4;

	/** The Constant BRIDGE_IND. */
	private static final int BRIDGE_IND = 5;

	/** The Constant TURN_IND. */
	private static final int TURN_IND = 6;

	/** The Constant COIL_IND. */
	private static final int COIL_IND = 7;

	/** The Constant NULL_ENTRY_IND. */
	private static final int NULL_ENTRY_IND = -1;


	/**
	 * A map to store secondary structure  to integer mappings.
	 */
	private Map<String, Integer> dsspMap;

	/**
	 * Constructor initialises the map.
	 */
	public CodeHolders() {
		Map<String, Integer> aMap = new HashMap<String, Integer>();
		aMap.put("pi Helix", PI_HELIX_IND);
		aMap.put("Bend", BEND_IND);
		aMap.put("alpha Helix", ALPHA_HELIX_IND);
		aMap.put("Extended", EXTENDED_IND);
		aMap.put("3-10 Helix", HELIX_3_10_IND);
		aMap.put("Bridge", BRIDGE_IND);
		aMap.put("Turn", TURN_IND);
		aMap.put("Coil", COIL_IND);
		aMap.put("NA", NULL_ENTRY_IND);
		setDsspMap(Collections.unmodifiableMap(aMap));
	}

	/**
	 * Gets the dssp map.
	 *
	 * @return the dsspMap
	 */
	public final Map<String, Integer> getDsspMap() {
		return dsspMap;
	}

	/**
	 * Sets the dssp map.
	 *
	 * @param inputDsspMap the dsspMap to set
	 */
	public final void setDsspMap(final Map<String, Integer> inputDsspMap) {
		this.dsspMap = inputDsspMap;
	}
}
