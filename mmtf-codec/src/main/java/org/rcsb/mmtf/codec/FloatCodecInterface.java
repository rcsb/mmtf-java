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
	 * @param param the input param
	 * @return the encoded byte array
	 */
	public byte[] encode(float[] inputData, int param);
	
	/**
	 * Decode a byte array to a float array.
	 * @param inputData the input byte array
	 * @param param the input param
	 * @return the decoded float array
	 */
	public float[] decode(byte[] inputData, int param);

}
