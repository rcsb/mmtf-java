package org.rcsb.mmtf.codec;


import org.rcsb.mmtf.decoder.ArrayDecoders;
import org.rcsb.mmtf.encoder.ArrayEncoders;

/**
 * An enum defining the encoding and decoding strategies.
 * @author Anthony Bradley
 *
 */
public enum FloatCodecs implements FloatCodecInterface, CodecInterface {
	
	/**
	 * Encoding a list of floats (e.g. coordinates) using integer encoding (3DP accuracy), 
	 * delta encoding and then recursive indexing.
	 */
	INT_DELTA_RECURSIVE(10, "Delta") {

		@Override
		public byte[] encode(float[] inputData, int param) {
			return ArrayConverters.convertIntegersToTwoBytes(ArrayConverters.recursiveIndexEncode(
					ArrayEncoders.deltaEncode(
							ArrayConverters.convertFloatsToInts(inputData,param))));
			}

		@Override
		public float[] decode(byte[] inputData, int param) {
			int[] intArr = ArrayDecoders.deltaDecode(
					ArrayConverters.recursiveIndexDecode(
							ArrayConverters.convertTwoByteToIntegers(inputData)));
			return ArrayConverters.convertIntsToFloats(intArr,param);
			}
 	},
	
	
	/**
	 * Encoding a list of floats (e.g. occupancy) using integer encoding (2DP accuracy) 
	 * and then run length encoding.
	 */
	INT_RUNLENGTH(9, "Run length") {

		@Override
		public byte[] encode(float[] inputData, int param) {
			return ArrayConverters.convertIntegersToFourByte(
					ArrayEncoders.runlengthEncode(
							ArrayConverters.convertFloatsToInts(inputData, param)));
		}


		@Override
		public float[] decode(byte[] inputData, int param) {
			return ArrayConverters.convertIntsToFloats(
					ArrayDecoders.runlengthDecode(
							ArrayConverters.convertFourByteToIntegers(inputData)),
					param);
		}
 		
 	};
	
	
	

	private final int codecId;
	private final String codecName;
	
	/**
	 * Constructor for the float codec Enum.
	 * @param codecId the byte encoding this codec strategy
	 * @param codecName the name of this encoding strategy
	 */
	private FloatCodecs(int codecId, String codecName) {
		this.codecId = codecId;
		this.codecName = codecName;
	}

	/**
	 * Get the codec from an input byte. 
	 * @param codecId the integer defining the coding
	 * @return the enum of the codec
	 */
	public static FloatCodecs getCodec(int codecId){
		for(FloatCodecs codecs : FloatCodecs.values())
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
	 * @return the decoded array as a float array
	 */
	public static float[] decodeArr(byte[] inputData){
		OptionParser optionParser = new OptionParser(inputData);
		FloatCodecs codecs = getCodec(optionParser.methodNumber);
		return codecs.decode(optionParser.data,optionParser.param);
	}
	

	
	/**
	 * @return the codec name. A string naming the codec
	 */
	public String getCodecName() {
		return codecName;
	}

	/**
	 * @return the codecId. A byte describing the codec.
	 */
	public int getCodecId() {
		return codecId;
	}


	

	
}
