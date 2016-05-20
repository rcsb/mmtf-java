package org.rcsb.mmtf.codec;

import org.rcsb.mmtf.encoder.ArrayConverters;

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
			return ArrayConverters.encodeChainList(inputData);
		}

		@Override
		public String[] decode(byte[] inputData) {
			// TODO Auto-generated method stub
			return null;
		}
		
	};
	
	private byte codecId;
	private String codecName;
	
	private StringCodecs(byte inputId, String name) {
		this.codecId = inputId;
		this.codecName = name;
	}
	
	

	/**
	 * Decode a byte array from an input array.
	 * @param inputData the byte array of data
	 * @return the decoded array as a String array
	 */
	public static String[] decodeArr(byte[] inputData){
		for(StringCodecs codecs : StringCodecs.values())
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
