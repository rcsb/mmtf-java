package org.rcsb.mmtf.arraydecompressors;

import org.rcsb.mmtf.dataholders.MmtfBean;

/**
 * Decode string arrays that have been run length encoded.
 * The input values are in pairs. The first value in each pair
 * is the string to be used in the output array. The second value
 * is the number of repetitions of that value.
 * @author Anthony Bradley
 *
 */
public class RunLengthDecodeString {


	/**
	 * Runlength decode an int[] and return as a char[].
	 * @param inputIntArray an input array of integers. Integers are  in pairs. The first in each pair
	 * is the ASCII code of the character. The second in each pair is the number of copies of this character to
	 * be added to the list.
	 * @return a char array of values run length decoded.
	 */
	public final char[] intArrayToCharArray(final int[] inputIntArray) {
		// If it's only one long - just take the char
		if (inputIntArray.length == 1) {
			char[] outArray = new char[1];
			if (Character.toChars(inputIntArray[0])[0] == MmtfBean.UNAVAILABLE_CHAR_VALUE) {
				outArray[0] = '?';
			} else {
				outArray[0]= Character.toChars(inputIntArray[0])[0];
			}
			return outArray;
		}
		// If it's longer - count the total number to be put in (to assign the char[]).
		int totNum = 0;
		for (int i = 0; i < inputIntArray.length; i += 2) {
			totNum += inputIntArray[i + 1];
		}
		// Assign the output char array.
		char[] outArray = new char[totNum];
		// The total counter for the output.
		int totCounter = 0;
		// Define an empty char with the scope to be used in the for loop.
		char outChar = '\0';
		for (int i = 0; i < inputIntArray.length; i += 2) {
			if (inputIntArray[i] == MmtfBean.UNAVAILABLE_CHAR_VALUE) {
				outChar = "?".charAt(0);
			} else {
				// Otherwise get this char
				outChar = Character.toChars(inputIntArray[i])[0];
			}
			// Find the number of repeats.
			int numRepeats = inputIntArray[i + 1];
			// Loop through and add these to the list
			for (int j = 0; j < numRepeats; j++) {
				outArray[totCounter] = outChar;
				totCounter++;
			}
		}
		return outArray;
	}


}
