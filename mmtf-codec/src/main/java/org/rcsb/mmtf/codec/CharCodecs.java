package org.rcsb.mmtf.codec;

import org.rcsb.mmtf.decoder.ArrayDecoders;
import org.rcsb.mmtf.encoder.ArrayEncoders;

/**
 * An enum of char codecs.
 * @author Anthony Bradley
 *
 */
public enum CharCodecs implements CharCodecInterface, CodecInterface{
	
	/**
	 * Run length codec for an array of chars (using ASCII code) http://www.asciitable.com/.
	 * For example: ["A", "A", "A"] goes to [10,10,10] goest to [10,3].
	 */
	RUN_LENGTH(6, "Run length"){

		@Override
		public byte[] encode(char[] inputData, int param) {
			return ArrayConverters.convertIntegersToFourByte(
					ArrayEncoders.runlengthEncode(
							ArrayConverters.convertCharToIntegers(inputData)));
		}

		@Override
		public char[] decode(byte[] inputData, int param) {
			return org.rcsb.mmtf.codec.ArrayConverters.convertIntegerToChar(
					ArrayDecoders.runlengthDecode(
							org.rcsb.mmtf.codec.ArrayConverters.convertFourByteToIntegers(inputData)));
		}
		
		
	};
	
	private final int codecId;
	private final String codecName;
	
	private CharCodecs(int inputId, String name) {
		this.codecId = inputId;
		this.codecName = name;
	}
	
	/**
	 * Get the codec from an input integer. 
	 * @param inputInt the integer defining the coding
	 * @return the enum of the codec
	 */
	public static CharCodecs getCodec(int inputInt){
		for(CharCodecs codecs : CharCodecs.values())
		{
			if(inputInt==codecs.codecId)
			{
				return codecs;
			}
		}
		throw new IllegalArgumentException(inputInt+" not recognised as codec strategy.");
	}
	

	/**
	 * Decode a byte array from an input array.
	 * @param inputData the byte array of data
	 * @return the decoded array as a char array
	 */
	public static char[] decodeArr(byte[] inputData){
		OptionParser optionParser = new OptionParser(inputData);
		CharCodecs codecs = CharCodecs.getCodec(optionParser.methodNumber);
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
