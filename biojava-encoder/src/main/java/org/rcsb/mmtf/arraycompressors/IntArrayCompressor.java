package org.rcsb.mmtf.arraycompressors;

import java.util.ArrayList;


/**
 * The Interface IntArrayCompressor.
 */
public interface IntArrayCompressor {
	
	/**
	 * Generic function to compress an integer array.
	 *
	 * @param inArray the in array
	 * @return the array list
	 */
	public ArrayList<Integer> compressIntArray(ArrayList<Integer> inArray);
}
