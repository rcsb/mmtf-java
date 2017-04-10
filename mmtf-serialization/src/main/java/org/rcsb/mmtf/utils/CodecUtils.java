package org.rcsb.mmtf.utils;

import java.util.List;

/**
 * A utility class of static methods and constants
 * for the codec project.
 * @author Anthony Bradley
 *
 */
public class CodecUtils {
	
	
	/** The base url for this version of MMTF. */
	public static final String BASE_URL = "http://mmtf.rcsb.org/v1.0/full/";
	
	/**
	 * Path to MMTF files compatible with MMTF Specification version 1.0
	 */
    private static final String MMTF_PATH = "mmtf.rcsb.org/v1.0/";

	/** The maximum number of chars in a chain entry. */
	public static final int MAX_CHARS_PER_CHAIN_ENTRY= 4;
	
	/**
	 * Returns the RESTful URL to an MMTF encoded PDB entry. This methods support http and https protocols and two MMTF representations: full and reduced.
	 * reduced: C-alpha atoms for polypeptides, P for polynucleotides, and all atom for all other groups (residues) at 0.1 A coordinate precision;
	 * full: all atoms at 0.001 A coordinate precision
	 * @param pdbId PDB Id to 
	 * @param https if true, use HTTPS instead of HTTP
	 * @param reduced if true, use reduced version of MMTF file instead of full version
	 * @return
	 */
	public static String getMmtfEntryUrl(String pdbId, boolean https, boolean reduced) {
		return getMmtfBaseUrl(https, reduced) + pdbId;
	}

	/**
	 * Returns the base URL to retrieve MMTF files 
	 * @param https if true, returns https URL, otherwise http URL
	 * @param reduced if true, returns URL to 
	 * reduced MMTF files (C-alpha: polypeptides, P polynucleotides, all other groups: all atom, limited precision), 
	 * otherwise full version (all atoms, full precision)
	 * @return base URL
	 */
	public static String getMmtfBaseUrl(boolean https, boolean reduced) {
		String url = "http://";
		
		if (https) {
			url = "https://";
		}
		
		if (reduced) {
			url = url + MMTF_PATH + "reduced/";
		} else {
			url = url + MMTF_PATH + "full/";
		}
		
		return url;
	}
	
	

	/**
	 * Converts an integer list to an integer array
	 * @param integerList the input list
	 * @return the output array
	 */
	public static int[] convertToIntArray(List<Integer> integerList) {
		int[] integerArray = new int[integerList.size()];
		for(int i=0; i<integerList.size(); i++){
			integerArray[i] = integerList.get(i);
		}
		return integerArray;
	}
	

}
