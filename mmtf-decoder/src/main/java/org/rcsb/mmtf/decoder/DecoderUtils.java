package org.rcsb.mmtf.decoder;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class DecoderUtils {


	/** The number of bytes in an integer. */
	private static final int NUM_BYTES_IN_INT = 4;
	/** The maximum number of chars in a chain entry. */
	private static final int MAX_CHARS_PER_CHAIN_ENTRY = 4;

	/**
	 * Return the String of a chain id for a given chain, from a byte array.
	 * @param chainList the input byte array. Each chain id is stored as four bytes. Each byte encodes a char.
	 * @param chainIndex the index of the chain for which the id is required.
	 * @return a String of the chain id for the given chain.
	 */
	public final String getChainId(final byte[] chainList, final int chainIndex) {
		int incrementor = 0;
		StringBuilder sb = new StringBuilder();
		byte chainIdOne = chainList[chainIndex * MAX_CHARS_PER_CHAIN_ENTRY + incrementor];
		sb.append((char) chainIdOne);
		// Now get the next byte
		incrementor += 1;
		byte chainIdTwo = chainList[chainIndex * MAX_CHARS_PER_CHAIN_ENTRY + incrementor];
		if (chainIdTwo != (byte) 0) {
			sb.append((char) chainIdTwo);
		}
		incrementor += 1;
		byte chainIdThree = chainList[chainIndex * MAX_CHARS_PER_CHAIN_ENTRY + incrementor];
		if (chainIdThree != (byte) 0) {
			sb.append((char) chainIdThree);
		}
		incrementor += 1;
		byte chainIdFour = chainList[chainIndex * MAX_CHARS_PER_CHAIN_ENTRY + incrementor];
		if (chainIdFour != (byte) 0) {
			sb.append((char) chainIdFour);
		}
		return sb.toString();
	}

	/**
	 * Convert a byte array (each four byte encodes a different integer)  to an integer array.
	 * @param inArray the input byte array
	 * @return the decoded integer array
	 * @throws IOException due to byte array not being accesible
	 */
	public final int[] bytesToInts(final byte[] inArray) throws IOException {
		DataInputStream bis = new DataInputStream(new ByteArrayInputStream(inArray));
		int numIntsInArr = inArray.length / NUM_BYTES_IN_INT;
		// Define an array to return
		int[] outArray = new int[numIntsInArr];
		for (int i = 0; i < numIntsInArr; i++) {
			outArray[i] = bis.readInt();
		}
		return outArray;
	}

	/**
	 * Convert a byte array (each byte encodes a different integer) to an integer array.
	 * @param inArray the input byte array
	 * @return the decoded integer array
	 * @throws IOException due to byte array not being accesible
	 */
	public final int[] bytesToByteInts(final byte[] inArray) throws IOException {
		DataInputStream bis = new DataInputStream(new ByteArrayInputStream(inArray));
		// Define an array to return
		int[] outArray = new int[inArray.length];
		for (int i = 0; i < inArray.length; i++) {
			outArray[i] = (int) bis.readByte();
		}
		return outArray;

	}


	/**
	 * Find all the chain ids from a single byte array. Each byte encodes a different ASCII character.
	 * @param currentChainList the byte array of the chain list input. Each chain takes up 4 bytes.
	 * @return the string array of the parsed chain ids
	 */
	public final String[] decodeChainList(byte[] currentChainList) {
		int outputLength = currentChainList.length/4;
		String[] outArray = new String[outputLength];
		for (int i = 0; i < outputLength; i++){
			outArray[i] = getChainId(currentChainList, i);
		}
		return outArray;
	}

	/**
	 * Convert an integer array to a float array by dividing by a float.
	 * @param inputIntArray the input integer array to be divided
	 * @param floatDivider the float divider to divide the integers by.
	 * @return a float array converted from the input.
	 */
	public final float[] convertIntsToFloats(int[] inputIntArray, float floatDivider) {
		// Assign the output array to write
		float[] outArray = new float[inputIntArray.length];
		for (int i=0; i<inputIntArray.length; i++) {
			outArray[i] = inputIntArray[i] / floatDivider;
		}
		return outArray;
	}
}
