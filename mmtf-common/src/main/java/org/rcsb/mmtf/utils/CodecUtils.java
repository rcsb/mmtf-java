package org.rcsb.mmtf.utils;

import java.util.List;

public class CodecUtils {

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

	/**
	 * Find the highest value in an integer array.
	 * @param intArray the integer array
	 * @return the highester value in the array
	 */
	public static int findMaxInIntArray(int[] intArray){
		int max=intArray[0];

		for (int i = 0; i < intArray.length; i++) {
			if (intArray[i] > max) {
				max = intArray[i];
			}
		}
		return max;
	}
}
