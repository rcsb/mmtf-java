package org.rcsb.mmtf.arraycompressors;

import java.util.ArrayList;

/**
 * The Interface StringArrayCompressor.
 */
public interface StringArrayCompressor {

	/**
	 * Generic function to compress a string array.
	 *
	 * @param inArray the in array
	 * @return the array list
	 */
	ArrayList<String> compressStringArray(ArrayList<String> inArray);
}
