package org.rcsb.mmtf.codec;

/**
 * An interface all codecs must implement
 * @author Anthony Bradley
 *
 */
public interface CodecInterface {

	
	/**
	 * Encode an array of generic input data into an output.
	 * @param the decoded data
	 * @return the encoded output data
	 */
	public <D> byte[] encode(D[] atomIds);
	
	
	/**
	 * Decode an array of generic input data into a decoded output.
	 * @param inputData the encoded data
	 * @return the decoded data
	 */
	public <D> D[] decode(byte[] inputData);

}
