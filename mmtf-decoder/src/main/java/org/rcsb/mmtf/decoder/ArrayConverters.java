package org.rcsb.mmtf.decoder;

import java.nio.ByteBuffer;

import org.rcsb.mmtf.utils.CodecUtils;

/**
 * Class of functions to convert arrays to readable types.
 * e.g. byte arrays to integer arrays.
 * @author Anthony Bradley
 *
 */
public class ArrayConverters {

	/**
	 * Find all the chain ids from a single byte array. Each byte encodes a different ASCII character.
	 * @param currentChainList the byte array of the chain list input. Each chain takes up 4 bytes.
	 * @return the string array of the parsed chain ids
	 */
	public static String[] decodeChainList(byte[] currentChainList) {
		int outputLength = currentChainList.length/4;
		String[] outArray = new String[outputLength];
		for (int i = 0; i < outputLength; i++){
			outArray[i] = getChainId(currentChainList, i);
		}
		return outArray;
	}

	/**
	 * Convert an integer array to a float array by dividing by a float.
	 * @param intArray the input integer array to be divided
	 * @param floatDivider the float divider to divide the integers by.
	 * @return a float array converted from the input.
	 */
	public static float[] convertIntsToFloats(int[] intArray, float floatDivider) {
		// Assign the output array to write
		float[] outArray = new float[intArray.length];
		for (int i=0; i<intArray.length; i++) {
			outArray[i] = intArray[i] / floatDivider;
		}
		return outArray;
	}

	/**
	 * Convert a byte array containing two bytes to integers in an integer array.
	 * @param byteArray the input byte array
	 * @return the converted integer array
	 */
	public static int[] convertByteToIntegers(byte[] byteArray) {
		
		ByteBuffer bb = ByteBuffer.wrap(byteArray);
		
		int outLength = byteArray.length;
		int[] outArray = new int[outLength];
		for (int i=0; i<outLength; i++) {
			outArray[i] = bb.get();
		}
		return outArray;
	}

	/**
	 * Convert a byte array containing two bytes to integers in an integer array.
	 * @param byteArray the input byte array
	 * @return the converted integer array
	 */
	@Deprecated
	public static int[] convertTwoByteToIntegers(byte[] byteArray) {
		
		ByteBuffer bb = ByteBuffer.wrap(byteArray);
	
		int outLength = byteArray.length/2;
		int[] outArray = new int[outLength];
		for (int i=0; i<outLength; i++) {
			outArray[i] = bb.getShort();
		}
		return outArray;
	}
	
	/**
	 * Convert to byte integers to a short array.
	 * @param byteArray the input byte array
	 * @return the short array converted
	 */
	public static short[] convertTwoBytesToShorts(byte[] byteArray) {
		ByteBuffer bb = ByteBuffer.wrap(byteArray);
		
		int outLength = byteArray.length/2;
		short[] outArray = new short[outLength];
		for (int i=0; i<outLength; i++) {
			outArray[i] = bb.getShort();
		}
		return outArray;
	}


	/**
	 * Convert a byte array containing four bytes to integers in an integer array.
	 * @param byteArray the input byte array
	 * @return the converted integer array
	 */
	public static int[] convertFourByteToIntegers(byte[] byteArray) {

		ByteBuffer bb = ByteBuffer.wrap(byteArray);
		
		int outLength = byteArray.length/4;
		int[] outArray = new int[outLength];
		for (int i=0; i<outLength; i++) {
			outArray[i] = bb.getInt();
		}
		return outArray;
	}

	/**
	 * Combine integer arrays.  The first is an array purely of integers to be added. 
	 * The second contains integers in pairs. The first in the pair is to be added.
	 * The second in the pair is the number of integers to read from the first array.
	 * @param twoByteIntArray an array of integers
	 * @param fourByteIntArray an array of integers in pairs. The first in the pair 
	 * is to be added to the output array. The second in the pair is the number of 
	 * integers to read from the first array.
	 * @return the integer array output.
	 */
	public static int[] combineIntegers(int[] twoByteIntArray, int[] fourByteIntArray) {
		int[] outArray = new int[twoByteIntArray.length+fourByteIntArray.length/2];
		int outIndex=0;
		int twoByteIndex=0;
		for (int fourByteIndex=0; fourByteIndex<fourByteIntArray.length; fourByteIndex+=2){
			outArray[outIndex] = fourByteIntArray[fourByteIndex];
			outIndex++;
			for(int i=0; i<fourByteIntArray[fourByteIndex+1]; i++){
				outArray[outIndex] = twoByteIntArray[twoByteIndex];
				outIndex++;
				twoByteIndex++;
			}
		}
		return outArray;
	}

	/**
	 * Convert an integer array to a char array. Cast each integer to a char
	 * using the int as the ASCII value.
	 * @param integerArray the input integer array
	 * @return the converted char array
	 */
	public static char[] convertIntegerToChar(int[] integerArray) {
		// Set the output array
		char[] outArray = new char[integerArray.length];
		for (int i=0; i<integerArray.length; i++) {
			outArray[i] = (char) integerArray[i];
		}
		return outArray;
	}
	
	/**
	 * Return the String of a chain id for a given chain, from a byte array.
	 * @param byteArr the input byte array. Each chain id is stored as four bytes. Each byte encodes a char.
	 * @param chainIndex the index of the chain for which the id is required.
	 * @return a String of the chain id for the given chain.
	 */
	private static String getChainId(byte[] byteArr, int chainIndex) {
		int incrementor = 0;
		StringBuilder sb = new StringBuilder();
		byte chainIdOne = byteArr[chainIndex * CodecUtils.MAX_CHARS_PER_CHAIN_ENTRY + incrementor];
		sb.append((char) chainIdOne);
		// Now get the next byte
		incrementor += 1;
		byte chainIdTwo = byteArr[chainIndex * CodecUtils.MAX_CHARS_PER_CHAIN_ENTRY + incrementor];
		if (chainIdTwo != (byte) 0) {
			sb.append((char) chainIdTwo);
		}
		incrementor += 1;
		byte chainIdThree = byteArr[chainIndex * CodecUtils.MAX_CHARS_PER_CHAIN_ENTRY + incrementor];
		if (chainIdThree != (byte) 0) {
			sb.append((char) chainIdThree);
		}
		incrementor += 1;
		byte chainIdFour = byteArr[chainIndex * CodecUtils.MAX_CHARS_PER_CHAIN_ENTRY + incrementor];
		if (chainIdFour != (byte) 0) {
			sb.append((char) chainIdFour);
		}
		return sb.toString();
	}
}