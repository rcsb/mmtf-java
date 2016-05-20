package org.rcsb.mmtf.codec;
/**
 * Interface all String codecs must implement.
 * @author Anthony Bradley
 *
 */
public interface StringCodecsInterface {

	
	/**
	 * Encode a String array to a byte array. 
	 * @param inputData the input String array
	 * @return the encoded byte array
	 */
	public byte[] encode(String[] inputData);
	
	/**
	 * Decode a byte array to a String array.
	 * @param inputData the input byte array
	 * @return the decoded String array
	 */
	public String[] decode(byte[] inputData);
}
