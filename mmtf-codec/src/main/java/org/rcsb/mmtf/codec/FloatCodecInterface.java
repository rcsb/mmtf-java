package org.rcsb.mmtf.codec;

/**
 * An interface all float codecs must implement
 * @author Anthony Bradley
 *
 */
public interface FloatCodecInterface {

	
	/**
	 * Encode a float array to a byte array. 
	 * @param inputData the input float array
	 * @return the encoded byte array
	 */
	public byte[] encode(float[] inputData);
	
	/**
	 * Decode a byte array to a float array.
	 * @param inputData the input byte array
	 * @return the decoded float array
	 */
	public float[] decode(byte[] inputData);

}
