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
	
	private final byte codecId;
	private final String codecName;
	
	private CharCodecs(byte inputId, String name) {
		this.codecId = inputId;
		this.codecName = name;
	}
	
	/**
	 * Get the codec from an input byte. 
	 * @param inputByte the byte defining the coding
	 * @return the enum of the codec
	 */
	public static CharCodecs getCodecFromByte(byte inputByte){
		for(CharCodecs codecs : CharCodecs.values())
		{
			if(inputByte==codecs.codecId)
			{
				return codecs;
			}
		}
		throw new IllegalArgumentException(inputByte+" not recognised as codec strategy.");
	}
	

	/**
	 * Decode a byte array from an input array.
	 * @param inputData the byte array of data
	 * @return the decoded array as a char array
	 */
	public static char[] decodeArr(byte[] inputData){
		CharCodecs codecs = CharCodecs.getCodecFromByte(inputData[0]);
		return codecs.decode(Arrays.copyOfRange(inputData, 1, inputData.length));
	}
	
	@Override
	public String getCodecName() {
		return codecName;
	}

	@Override
	public byte getCodecId() {
		return codecId;
	}


}
