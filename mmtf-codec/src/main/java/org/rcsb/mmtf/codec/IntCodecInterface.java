package org.rcsb.mmtf.codec;

/**
 * Interface all integer codecs must implement.
 * @author Anthony Bradley
 *
 */
public interface IntCodecInterface {

	
	/**
	 * Encode a int array to a byte array. 
	 * @param inputData the input int array
	 * @return the encoded byte array
	 */
	public byte[] encode(int[] inputData);
	
	/**
	 * Decode a byte array to a int array.
	 * @param inputData the input byte array
	 * @return the decoded int array
	 */
	public int[] decode(byte[] inputData);
}
