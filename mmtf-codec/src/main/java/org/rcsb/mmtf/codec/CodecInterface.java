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
	 * @param codecName the codec name - a string naming the codec
	 */
	public void setCodecName(String codecName);
	/**
	 * @return the codecId a byte for the codec
	 */
	public byte getCodecId();

	/**
	 * @param codecId the codec id - a short for the codec
	 */
	public void setCodecId(byte codecId);
}
