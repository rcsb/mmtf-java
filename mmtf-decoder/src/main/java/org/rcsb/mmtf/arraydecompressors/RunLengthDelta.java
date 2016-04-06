package org.rcsb.mmtf.arraydecompressors;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * Functions to decompress input arrays using delta and run
 * length decoding.
 * The input is first run length decoded and then delta decoded.
 * This is particularly useful for sequential numbers.
 * @author Anthony Bradley
 *
 */
public class RunLengthDelta {


	/**
	 * The number of bytes in a four byte integers.
	 */
	private static final int BIG_INT_BYTES = 4;

	/**
	 * Decompress a byte array  using run length and delta decoding.
	 * @param inArray The input byte array
	 * @return A decompressed array of integers.
	 * @throws IOException If no more data can be read from the byte array.
	 */
	public final int[] decompressByteArray(final byte[] inArray)
			throws IOException {
		// The length of the array
		int lengthOfBigIntArr = inArray.length / (BIG_INT_BYTES * 2);
		// Array to store all the different numbers
		int[] numArr = new int[lengthOfBigIntArr];
		int[] countArr = new int[lengthOfBigIntArr];
		// Get the size
		int totCount = 0;
		DataInputStream bis = new DataInputStream(new
				ByteArrayInputStream(inArray));
		for (int i = 0; i < lengthOfBigIntArr; i++) {
			// Get the numbers
			int getNum = bis.readInt();
			// Get the number of repeats
			int getCount = bis.readInt();
			if (getCount < 0) {
				System.out.println(getCount);
			}
			// Get the total count
			totCount += getCount;
			// Fill the number array
			numArr[i] = getNum;
			// Fill the count array
			countArr[i] = getCount;
		}
		// Now set this output array
		int[] outArr = new int[totCount];
		int totCounter = 0;
		int totAns = 0;
		for (int i = 0; i < numArr.length; i++) {
			// Get the number that is to be repeared
			int thisAns = numArr[i];
			// Get the number of repeats
			for (int j = 0; j < countArr[i]; j++) {
				// Add the delta to get this answer
				totAns += thisAns;
				// And then add t is to the array
				outArr[totCounter] = totAns;
				// Now add to the counter
				totCounter++;
			}
		}
		return outArr;
	}

}
