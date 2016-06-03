package org.rcsb.mmtf.codec;

/**
 * Generic interface for an untyped codec strategy
 * @author Anthony Bradley
 *
 */
public interface CodecInterface {
	

	/**
	 * @return the codec name. A string naming the codec.
	 */
	public String getCodecName();
	
	
	/**
	 * @return the codec id. currently an integer defining the codec.
	 */
	public int getCodecId();

}
