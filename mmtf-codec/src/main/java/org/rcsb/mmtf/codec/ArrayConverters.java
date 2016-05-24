package org.rcsb.mmtf.codec;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
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
	
	/**
	 * Convert an integer array to byte array, where each integer is encoded by a 
	 * single byte.
	 * @param intArray the input array of integers
	 * @return the byte array of the integers
	 */
	public static byte[] convertIntegersToBytes(int[] intArray) {
		
		ByteBuffer bb = ByteBuffer.allocate(intArray.length);
		
		for(int i=0; i < intArray.length; ++i)
		{
			bb.put((byte)(int) intArray[i]);
		}

		return bb.array();
	}
	
	/**
	 * Convert an integer array to byte array, where each integer is encoded by a
	 * two bytes.
	 * @param intArray the input array of integers
	 * @return the byte array of the integers
	 */
	public static byte[] convertIntegersToTwoBytes(int[] intArray) {

		ByteBuffer bb = ByteBuffer.allocate(intArray.length * 2);
		
		for(int i=0; i < intArray.length; ++i)
		{
			bb.putShort((short)(int) intArray[i]);
		}

		return bb.array();
	}
	
	/**
	 * Convert a short array to byte array, where each short is encoded by a
	 * two bytes.
	 * @param shortArray the input array of integers
	 * @return the byte array of the integers
	 */
	public static byte[] convertShortsToTwoBytes(short[] shortArray) {

		ByteBuffer bb = ByteBuffer.allocate(shortArray.length * 2);
		
		for(int i=0; i < shortArray.length; ++i)
		{
			bb.putShort(shortArray[i]);
		}

		return bb.array();
	}

	/**
	 * Convert an integer array to byte array, where each integer is encoded by a
	 * four bytes.
	 * @param intArray the input array of integers
	 * @return the byte array of the integers
	 */
	public static byte[] convertIntegersToFourByte(int[] intArray) {
		
		ByteBuffer bb = ByteBuffer.allocate(intArray.length * 4);
		
		for(int i=0; i < intArray.length; ++i)
		{
			bb.putInt(intArray[i]);
		}

		return bb.array();
	}
	
	/**
	 * Convert an integer array to a float array by multiplying by a float.
	 * @param floatArray the input float array to be converted to ints
	 * @param floatMultiplier the float divider to multiply the floats by.
	 * @return an int array converted from the input.
	 */
	public static int[] convertFloatsToInts(float[] floatArray, float floatMultiplier) {
		// Assign the output array to write
		int[] outArray = new int[floatArray.length];
		for (int i=0; i<floatArray.length; i++) {
			outArray[i] = (int) Math.round(floatArray[i] * floatMultiplier);
		}
		return outArray;
	}

	/**
	 * Convert an input array of integers to two arrays. The first output array is a 
	 * four byte integer array. The integers in this array are in pairs. The first in
	 * each pair is part of the 
	 * @param inputArray the array of integers to be split.
	 * @return a list of two integer arrays. The first is of four byte integers.
	 */
	public static List<int[]> splitIntegers(int[] inputArray) {
		// set the two output arrays
		List<Integer> fourByteInts = new ArrayList<>();
		List<Integer> twoByteInts = new ArrayList<>();
		// First element goes in the four byte integer array.
		fourByteInts.add(inputArray[0]);
		// Set the counter
		int counter =0;
		for(int i=1;i<inputArray.length;i++){
			if(inputArray[i]>Short.MAX_VALUE || inputArray[i] < Short.MIN_VALUE){
				// Add the counter
				fourByteInts.add(counter);
				// Add the new four byte integer
				fourByteInts.add(inputArray[i]);				
				// Counter set to zero
				counter = 0;
			}
			else{
				// Little number added to little list
				twoByteInts.add(inputArray[i]);
				// Add to the counter
				counter++;
			}
		}
		// Finally add the counter to the big list 
		fourByteInts.add(counter);
		// Now add these to a list - big first
		List<int[]> outputList = new ArrayList<>();
		outputList.add(CodecUtils.convertToIntArray(fourByteInts));
		outputList.add(CodecUtils.convertToIntArray(twoByteInts));
		return outputList;
	}

	/**
	 * Convert a char array to an integer array using the ASCII code for characters
	 * @param charArray the input character array
	 * @return an integer array of ASCII decoded chars
	 */
	public static int[] convertCharToIntegers(char[] charArray) {
		int[] outArray = new int[charArray.length];
		for (int i=0; i<charArray.length; i++) {
			outArray[i] = (int) charArray[i];
		}
		return outArray;
	}

	
	/**
	 * Convert the chain names to a byte array
	 * @param chainNames the list of chain names as strings. Max length of 4 characters.
	 * @return the byte array of the chain names.
	 */
	public static byte[] encodeChainList(String[] chainNames) {
		byte[] outArr = new byte[chainNames.length*4];
		for(int i=0; i<chainNames.length;i++) {
			setChainId(chainNames[i], outArr, i);
		}
		return outArr;
	}
	

	/**
	 * Add the String chain id to a byte array
	 * @param chainId the chain id string
	 * @param byteArr the byte array to add to
	 * @param chainIndex the index of this chain
	 */
	private static void setChainId(String chainId, byte[] byteArr, int chainIndex) {
		// A char array to store the chars
		char[] outChar = new char[4];
		// The length of this chain id
		if(chainId==null){
			return;
		}
		int chainIdLen =  chainId.length();
		chainId.getChars(0, chainIdLen, outChar, 0);
		// Set the byte array - chain ids can be up to 4 chars - pad with empty bytes
		byteArr[chainIndex*4+0] = (byte) outChar[0];
		if(chainIdLen>1){
			byteArr[chainIndex*4+1] = (byte) outChar[1];
		}
		else{
			byteArr[chainIndex*4+1] = (byte) 0;
		}
		if(chainIdLen>2){
			byteArr[chainIndex*4+2] = (byte) outChar[2];
		}				
		else{
			byteArr[chainIndex*4+2] = (byte) 0;
		}
		if(chainIdLen>3){
			byteArr[chainIndex*4+3] = (byte) outChar[3];
		}				
		else{
			byteArr[chainIndex*4+3] =  (byte) 0;
		}		
	}

	/**
	 * Encodes an input array of integers following a Recursive Indexing strategy 
	 * (uses a reduced alphabet with fixed limits). 
	 * To encode a number N that exceed an upper limit of the reduced alphabet (Smax), 
	 * the encoder keeps reducing this number by subtracting (adding if N is negative) Smax from N,
	 * stopping when the number lies in the limits of a reduced alphabet.  
	 * 
	 * @param in the array of integer values to be encoded
	 * @return the encoded array
	 */
	public static int[] recursiveIndexEncode(int[] in) {
	
		List<Integer> outArr = new ArrayList<>();
	
		for ( int i=0; i < in.length; i++ ) {	
			int curr = in[i];
			if ( curr >= 0) {
				while (curr >= Short.MAX_VALUE) {
					outArr.add((int) Short.MAX_VALUE);
					curr -= Short.MAX_VALUE;
				}
			}
			else{
				while (curr <= Short.MIN_VALUE) {
					outArr.add((int) Short.MIN_VALUE);
					curr += Math.abs(Short.MIN_VALUE);
				}
			}
			outArr.add(curr);
		}
		return ArrayUtils.toPrimitive(outArr.toArray(new Integer[outArr.size()]));	
	}

	/**
	 * Decodes an input array of integers following a Recursive Indexing strategy.
	 * To decode the array, the coder keeps adding all the elements of the index, 
	 * stopping when the index value is between (inclusive of ends) 
	 * the limits of the reduced alphabet.
	 * 
	 * @param in the array of integer values to be decoded
	 * @return the decoded array
	 */
	public static int[] recursiveIndexDecode(int[] in) {
	
		int[] original = new int[in.length];
	
		int encodedInd = 0;
		int decodedInd = 0;
	
		while (encodedInd < in.length) {
	
			int decodedVal = 0;
	
			while (in[encodedInd] == Short.MAX_VALUE || in[encodedInd] == Short.MIN_VALUE) {
				decodedVal += in[encodedInd];
				encodedInd ++;
				if (in[encodedInd] == 0) {
					break;
				}
			}
			decodedVal += in[encodedInd];
			encodedInd ++;
	
			original[decodedInd] = decodedVal;
			decodedInd ++;
		}
		int[] out = new int[decodedInd];
		System.arraycopy(original, 0, out, 0, decodedInd);
		return out;
	}


}