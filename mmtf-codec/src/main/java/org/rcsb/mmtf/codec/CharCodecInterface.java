package org.rcsb.mmtf.codec;

/**
 * An interface all char codecs must implement
 * @author Anthony Bradley
 *
 */
public interface CharCodecInterface {

	/**
	 * Encode a char array to a byte array. 
	 * @param inputData the input char array
	 * @param param the input param
	 * @return the encoded byte array
	 */
	public byte[] encode(char[] inputData, int param);
	
	/**
	 * Decode a byte array to a char array.
	 * @param inputData the input byte array
	 * @param param the input param
	 * @return the decoded char array
	 */
	public char[] decode(byte[] inputData, int param);
}
