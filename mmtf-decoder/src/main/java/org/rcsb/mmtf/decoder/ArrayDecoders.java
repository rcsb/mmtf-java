package org.rcsb.mmtf.decoder;

import org.rcsb.mmtf.dataholders.MmtfBean;

public class ArrayDecoders {


	public static int[] deltaDecode(int[] integerArray) {
		return integerArray;

	}


	public static int[] runLengthDecodeIntegers(int[] integerArray) {
		return integerArray;

	}
	
	
	/**
	 * Runlength decode an int[] and return as a char[].
	 * @param integerArray an input array of integers. Integers are  in pairs. The first in each pair
	 * is the ASCII code of the character. The second in each pair is the number of copies of this character to
	 * be added to the list.
	 * @return a char array of values run length decoded.
	 */
	public static char[] runLengthDecodeStrings(int[] integerArray) {
		// If it's only one long - just take the char
		if (integerArray.length == 1) {
			char[] outArray = new char[1];
			if (Character.toChars(integerArray[0])[0] == MmtfBean.UNAVAILABLE_CHAR_VALUE) {
				outArray[0] = '?';
			} else {
				outArray[0]= Character.toChars(integerArray[0])[0];
			}
			return outArray;
		}
		// If it's longer - count the total number to be put in (to assign the char[]).
		int totNum = 0;
		for (int i = 0; i < integerArray.length; i += 2) {
			totNum += integerArray[i + 1];
		}
		// Assign the output char array.
		char[] outArray = new char[totNum];
		// The total counter for the output.
		int totCounter = 0;
		// Define an empty char with the scope to be used in the for loop.
		char outChar = '\0';
		for (int i = 0; i < integerArray.length; i += 2) {
			if (integerArray[i] == MmtfBean.UNAVAILABLE_CHAR_VALUE) {
				outChar = "?".charAt(0);
			} else {
				// Otherwise get this char
				outChar = Character.toChars(integerArray[i])[0];
			}
			// Find the number of repeats.
			int numRepeats = integerArray[i + 1];
			// Loop through and add these to the list
			for (int j = 0; j < numRepeats; j++) {
				outArray[totCounter] = outChar;
				totCounter++;
			}
		}
		return outArray;
	}

}
