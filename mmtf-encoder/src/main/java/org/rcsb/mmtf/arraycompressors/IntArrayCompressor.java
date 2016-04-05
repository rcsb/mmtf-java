package org.rcsb.mmtf.arraycompressors;

import java.util.List;


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
	public List<Integer> compressIntArray(List<Integer> inArray);
}
