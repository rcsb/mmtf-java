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
	 * @param param the input param
	 * @return the encoded byte array
	 */
	public byte[] encode(int[] inputData, int param);
	
	/**
	 * Decode a byte array to a int array.
	 * @param inputData the input byte array
	 * @param param the input param
	 * @return the decoded int array
	 */
	public int[] decode(byte[] inputData, int param);
}
