package org.rcsb.mmtf.codec;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

/**
 * General codec utility methods.
 * @author Anthony Bradley
 *
 */
public class CodecUtils {
	
	/**
	 * Method to prepend a byte array with a byte.
	 * @param inputArr the array to prepend
	 * @param inputByte the byte to prepend in the array
	 * @return the updated array
	 */
	public static byte[] prependByteArr(byte[] inputArr, byte inputByte){
		byte[] prepend = new byte[] {inputByte};
		byte[] combined = new byte[inputArr.length+1];
		System.arraycopy(prepend,0,combined,0,1);
		System.arraycopy(inputArr,0,combined,1,inputArr.length);
		return combined;
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
