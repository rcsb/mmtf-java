package org.rcsb.mmtf.codec;

import org.rcsb.mmtf.encoder.ArrayConverters;
import org.rcsb.mmtf.encoder.ArrayEncoders;

/**
 * An enum of char codecs.
 * @author Anthony Bradley
 *
 */
public enum CharCodecs implements CharCodecInterface, CodecInterface{
	
	/**
	 * Run length encode an array of chars.
	 */
	RUN_LENGTH((byte) 1, "Run length"){

		@Override
		public byte[] encode(char[] inputData) {
			return ArrayConverters.convertIntegersToFourByte(
					ArrayEncoders.runlengthEncode(
							ArrayConverters.convertCharToIntegers(inputData)));
		}

		@Override
		public char[] decode(byte[] inputData) {
			// TODO Auto-generated method stub
			return null;
		}
		
		
	};
	
	private byte codecId;
	private String codecName;
	
	private CharCodecs(byte inputId, String name) {
		this.codecId = inputId;
		this.codecName = name;
	}
	

	/**
	 * Decode a byte array from an input array.
	 * @param inputData the byte array of data
	 * @return the decoded array as a char array
	 */
	public static char[] decodeArr(byte[] inputData){
		for(CharCodecs codecs : CharCodecs.values())
		{
			if(inputData[0]==codecs.codecId)
			{
				return codecs.decode(inputData);
			}
		}
		// Return a null entry.
		return  null;
	}
	
	@Override
	public String getCodecName() {
		return codecName;
	}

	@Override
	public void setCodecName(String codecName) {
		this.codecName = codecName;
	}

	@Override
	public byte getCodecId() {
		return codecId;
	}

	@Override
	public void setCodecId(byte codecId) {
		this.codecId =codecId;
	}

}
