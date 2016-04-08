package org.rcsb.mmtf.utils;

import java.util.List;

public class CodecUtils {

	/**
	 * Convert a List<Integer> to an int[]
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
