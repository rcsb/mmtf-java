package org.rcsb.mmtf.codec;

import java.util.Arrays;

import org.rcsb.mmtf.decoder.ArrayDecoders;
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
			return CodecUtils.prependByteArr(ArrayConverters.convertIntegersToFourByte(
					ArrayEncoders.runlengthEncode(
							ArrayConverters.convertCharToIntegers(inputData))),
					this.getCodecId());
		}

		@Override
		public char[] decode(byte[] inputData) {
			return org.rcsb.mmtf.decoder.ArrayConverters.convertIntegerToChar(
					ArrayDecoders.runlengthDecode(
							org.rcsb.mmtf.decoder.ArrayConverters.convertFourByteToIntegers(inputData)));
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
				return codecs.decode(Arrays.copyOfRange(inputData, 1, inputData.length));
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
