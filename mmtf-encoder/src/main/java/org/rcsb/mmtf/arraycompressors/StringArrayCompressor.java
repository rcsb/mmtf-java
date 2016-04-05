package org.rcsb.mmtf.arraycompressors;

import java.util.List;

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
	List<String> compressStringArray(List<String> inArray);
}
