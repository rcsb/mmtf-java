package org.rcsb.mmtf.codec;

import org.rcsb.mmtf.encoder.ArrayConverters;
import org.rcsb.mmtf.encoder.ArrayEncoders;

/**
 * An enum defining the encoding and decoding strategies.
 * @author Anthony Bradley
 *
 */
public enum Codecs implements CodecInterface {
	
	

	/**
	 * Encoding first performs delta encoding and then run length
	 * encoding on top. This is appropriate for serial numbers. 
	 * 1,2,3,4,5,6 -> 1,1,1,1,1,1 -> 1,6
	 */
	RUN_LENGTH_DELTA((byte) 1, "Run length delta") {

		@Override
		public <D> byte[] encode(D[] inputData) {
			return ArrayConverters.convertIntegersToFourByte(
					ArrayEncoders.runlengthEncode(
							ArrayEncoders.deltaEncode((Integer[]) inputData)));
		}

		@Override
		public <D> D[] decode(byte[] inputData) {
			return null;
		}
		
 	};
	
	/**
	 * Get the codec from an input byte. 
	 * @param inputByte the byte defining the coding
	 * @return the enum of the codec
	 */
	public static Codecs getCodecFromByte(byte inputByte){
		for(Codecs codecs : Codecs.values())
		{
			if(inputByte==codecs.codecId)
			{
				return codecs;
			}
		}
		// Return a null entry.
		return  null;
	}
	private byte codecId;
	private String codecName;
	
	/**
	 * Constructor sets the codec type from a short.
	 * @param codecId the input short (byte) indicating the strategy
	 */
	private Codecs(byte codecId, String codecName) {
		this.setCodecId(codecId);
		this.setCodecName(codecName);
	}

	/**
	 * @return the codec name - a string naming the codec
	 */
	public String getCodecName() {
		return codecName;
	}

	/**
	 * @param codecName the codec name - a string naming the codec
	 */
	public void setCodecName(String codecName) {
		this.codecName = codecName;
	}

	/**
	 * @return the codecId a short for the codec
	 */
	public short getCodecId() {
		return codecId;
	}

	/**
	 * @param codecId the codec id - a short for the codec
	 */
	public void setCodecId(byte codecId) {
		this.codecId = codecId;
	}
	
}
