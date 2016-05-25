package org.rcsb.mmtf.codec;

/**
 * An enum defining the string encoding and decoding strategies.
 * @author Anthony Bradley
 *
 */
public enum StringCodecs implements StringCodecsInterface, CodecInterface {

	
	/**
	 * Encode an array of Strings to a byte array. Each String should be less than 
	 * five characters long.
	 */
	ENCOODE_CHAINS(8, "Encode chains") {

		@Override
		public byte[] encode(String[] inputData, int param) {
			return ArrayConverters.encodeChainList(inputData);
		}

		@Override
		public String[] decode(byte[] inputData, int param) {
			return org.rcsb.mmtf.codec.ArrayConverters.decodeChainList(inputData);
		}
		
	};
	
	private final int codecId;
	private final String codecName;
	
	private StringCodecs(int inputId, String name) {
		this.codecId = inputId;
		this.codecName = name;
	}
	
	

	/**
	 * Decode a byte array from an input array.
	 * @param codecId the int specifying the encoding strategy
	 * @return the decoded array as a String array
	 */
	public static StringCodecs getCodec(int codecId){
		for(StringCodecs codecs : StringCodecs.values())
		{
			if(codecId==codecs.codecId)
			{
				return codecs;
			}
		}
		throw new IllegalArgumentException(codecId+" not recognised as codec strategy.");
	}
	
	
	/**
	 * Decode a byte array from an input array.
	 * @param inputData the byte array of data
	 * @return the decoded array as a int array
	 */
	public static String[] decodeArr(byte[] inputData){
		OptionParser optionParser = new OptionParser(inputData);
		StringCodecs codecs = getCodec(optionParser.methodNumber);
		return codecs.decode(optionParser.data, optionParser.param);
	}

	@Override
	public String getCodecName() {
		return codecName;
	}
	
	@Override
	public int getCodecId() {
		return codecId;
	}

}
