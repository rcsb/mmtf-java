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
	 * Function to get the chain id for this chain.
	 *
	 * @param chainList the chain list
	 * @param thisChain the this chain
	 * @return the chain id
	 */
	public final String getChainId(final byte[] chainList, final int thisChain) {

		int incrementor = 0;
		StringBuilder sb = new StringBuilder();
		byte chainIdOne = chainList[thisChain * MAX_CHARS_PER_CHAIN_ENTRY + incrementor];
		sb.append((char) chainIdOne);
		// Now get the next byte
		incrementor += 1;
		byte chainIdTwo = chainList[thisChain * MAX_CHARS_PER_CHAIN_ENTRY + incrementor];
		if (chainIdTwo != (byte) 0) {
			sb.append((char) chainIdTwo);
		}
		incrementor += 1;
		byte chainIdThree = chainList[thisChain * MAX_CHARS_PER_CHAIN_ENTRY + incrementor];
		if (chainIdThree != (byte) 0) {
			sb.append((char) chainIdThree);
		}
		incrementor += 1;
		byte chainIdFour = chainList[thisChain * MAX_CHARS_PER_CHAIN_ENTRY + incrementor];
		if (chainIdFour != (byte) 0) {
			sb.append((char) chainIdFour);
		}
		return sb.toString();
	}

	/**
	 * Function to convert a byte array to an int array.
	 *
	 * @param inArray the in array
	 * @return the int[]
	 * @throws IOException Signals that an I/O exception has occurred.
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
	 * Function to convert a byte array to byte encoded .
	 *
	 * @param inArray the in array
	 * @return the int[]
	 * @throws IOException Signals that an I/O exception has occurred.
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
	 * Decode chain ids from byte arrays
	 * @param currentChainList the byte array of the chain list input. Each chain takes up 4 bytes.
	 * @return the string array of the parsed chain ids
	 */
	public String[] decodeChainList(byte[] currentChainList) {
		int outputLength = currentChainList.length/4;
		String[] outArray = new String[outputLength];
		for (int i = 0; i < outputLength; i++){
			outArray[i] = getChainId(currentChainList, i);
		}
		return outArray;
	}

	/**
	 * Decode integers to floats by dividing by this float.
	 * @param decompressByteArray
	 * @param floatDivider
	 * @return
	 */
	public float[] decodeIntsToFloats(int[] inputIntArray, float floatDivider) {
		// Assign the output array to write
		float[] outArray = new float[inputIntArray.length];
		for (int i=0; i<inputIntArray.length; i++) {
			outArray[i] = inputIntArray[i] / floatDivider;
		}
		return outArray;
	}
}
