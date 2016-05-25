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
	public static final String BASE_URL = "http://mmtf.rcsb.org/v0/full/";
	

	/** The maximum number of chars in a chain entry. */
	public static final int MAX_CHARS_PER_CHAIN_ENTRY= 4;
	


	/**
	 * Convert an integer list to an integer array
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
