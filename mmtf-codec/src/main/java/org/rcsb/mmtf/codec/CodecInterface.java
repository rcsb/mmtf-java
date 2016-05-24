package org.rcsb.mmtf.codec;

/**
 * Generic interface for an untyped codec strategy
 * @author Anthony Bradley
 *
 */
public interface CodecInterface {
	

	/**
	 * @return the codec name - a string naming the codec
	 */
	public String getCodecName();
	/**
	 * @return the codecId a byte for the codec
	 */
	public byte getCodecId();

}
