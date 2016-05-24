package org.rcsb.mmtf.codec;

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
}
