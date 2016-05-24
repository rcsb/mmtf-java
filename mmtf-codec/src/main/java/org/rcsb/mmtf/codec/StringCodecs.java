package org.rcsb.mmtf.codec;

import java.util.Arrays;

/**
 * An enum defining the string encoding and decoding strategies.
 * @author Anthony Bradley
 *
 */
public enum StringCodecs implements StringCodecsInterface, CodecInterface {

	
	/**
	 * Encode an array of chars to a byte array.
	 */
	ENCOODE_CHAINS((byte) 9, "Encode chains") {

		@Override
		public byte[] encode(String[] inputData) {
			return CodecUtils.prependByteArr(ArrayConverters.encodeChainList(inputData),this.getCodecId());
		}

		@Override
		public String[] decode(byte[] inputData) {
			return org.rcsb.mmtf.codec.ArrayConverters.decodeChainList(inputData);
		}
		
	};
	
	private final byte codecId;
	private final String codecName;
	
	private StringCodecs(byte inputId, String name) {
		this.codecId = inputId;
		this.codecName = name;
	}
	
	

	/**
	 * Decode a byte array from an input array.
	 * @param inputData the byte specifying the encoding strategy
	 * @return the decoded array as a String array
	 */
	public static StringCodecs getCodecFromByte(byte inputByte){
		for(StringCodecs codecs : StringCodecs.values())
		{
			if(inputByte==codecs.codecId)
			{
				return codecs;
			}
		}
		// Return a null entry.
		throw new IllegalArgumentException(inputByte+" not recognised as codec strategy.");
	}
	
	
	/**
	 * Decode a byte array from an input array.
	 * @param inputData the byte array of data
	 * @return the decoded array as a int array
	 */
	public static String[] decodeArr(byte[] inputData){
		StringCodecs stringCodecs = getCodecFromByte(inputData[0]);
		return stringCodecs.decode(Arrays.copyOfRange(inputData, 1, inputData.length));
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
