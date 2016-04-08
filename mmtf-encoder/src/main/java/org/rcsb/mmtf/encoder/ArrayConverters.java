package org.rcsb.mmtf.encoder;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.rcsb.mmtf.utils.CodecUtils;

/**
 * A class of array converters.
 * @author Anthony Bradley
 *
 */
public class ArrayConverters {


	/**
	 * Convert an integer array to byte array, where each integer is encoded by a 
	 * single byte.
	 * @param intArray the input array of integers
	 * @return the byte array of the integers
	 * @throws IOException the byte array cannot be read
	 */
	public static byte[] convertIntegersToBytes(int[] intArray) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		for(int i=0; i < intArray.length; ++i)
		{
			dos.writeByte(intArray[i]);
		}

		return baos.toByteArray();
	}
	
	/**
	 * Convert an integer array to byte array, where each integer is encoded by a
	 * two bytes.
	 * @param intArray the input array of integers
	 * @return the byte array of the integers
	 * @throws IOException the byte array cannot be read
	 */
	public static byte[] convertIntegersToTwoBytes(int[] intArray) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		for(int i=0; i < intArray.length; ++i)
		{
			dos.writeShort(intArray[i]);
		}

		return baos.toByteArray();
	}

	/**
	 * Convert an integer array to byte array, where each integer is encoded by a
	 * four bytes.
	 * @param intArray the input array of integers
	 * @return the byte array of the integers
	 * @throws IOException the byte array cannot be read
	 */
	public static byte[] convertIntegersToFourByte(int[] intArray) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		for(int i=0; i < intArray.length; ++i)
		{
			dos.writeShort(intArray[i]);
		}

		return baos.toByteArray();
	}

	/**
	 * Convert an input array of integers to two arrays. The first output array is a 
	 * four byte integer array. The integers in this array are in pairs. The first in
	 * each pair is part of the 
	 * @param inputArray
	 * @return
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
			outArray[i] = charArray[i];
		}
		return outArray;
	}
}
