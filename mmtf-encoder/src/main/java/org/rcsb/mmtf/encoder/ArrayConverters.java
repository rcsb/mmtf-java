package org.rcsb.mmtf.encoder;


import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.rcsb.mmtf.utils.CodecUtils;

/**
 * A class of array converters.
 * e.g. converting integer arrays to byte arrays.
 * @author Anthony Bradley
 *
 */
public class ArrayConverters {


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
	 * Unpack an array of shorts using the XX algorithm.
	 * @param inputArr the input array of Shorts
	 * @return the unpacked array of integers
	 */
	public static int[] bitUnpack(short[] inputArr){
		int[] storeList = new int[inputArr.length];
		int counter = 0;
		int currInt = 0;
		for(short shortToUnpack : inputArr){
			if(shortToUnpack==Short.MAX_VALUE || shortToUnpack==Short.MIN_VALUE){
				currInt+=shortToUnpack;
			}
			else{
				currInt += shortToUnpack;
				storeList[counter] = currInt;
				// Reset the counters
				counter++;
				currInt = 0;
			}
		}
		int[] outList = new int[counter];
		System.arraycopy(storeList, 0, outList, 0, counter);
		return storeList;
	}

	/**
	 * Convert an array  of integers to shorts.
	 * @param inputArr the input array of integers
	 * @return the converted array of shorts
	 */
	public static short[] bitPack(int[] inputArr) {
		List<Short> outList = new ArrayList<>();
		for(int intToPack : inputArr) {
			outList.addAll(findRemainder(intToPack));
		}
		return ArrayUtils.toPrimitive(outList.toArray(new Short[outList.size()]));
		
	}

	/**
	 * Convert a single integer into multiple shorts that sum to it's value.
	 * @param intToPack the integer to encode
	 * @return the list of Shorts that encode the original number
	 */
	private static Collection<? extends Short> findRemainder(int intToPack) {
		List<Short> outList = new ArrayList<>();
		// Keep removing the min and the max value 
		while(true){
			if(intToPack>=Short.MAX_VALUE){
				intToPack = intToPack - Short.MAX_VALUE;
				outList.add(Short.MAX_VALUE);
			}
			else if(intToPack<=Short.MIN_VALUE){
				intToPack = intToPack - Short.MIN_VALUE;
				outList.add(Short.MIN_VALUE);
			}
			else{
				outList.add((short) intToPack);
				break;
			}
		}
		return outList;
	}




}
