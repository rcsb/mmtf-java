package org.rcsb.mmtf.codec;

import org.rcsb.mmtf.decoder.ArrayDecoders;
import org.rcsb.mmtf.encoder.ArrayEncoders;

/**
 * Codecs for Integer encoding and decoding.
 * @author Anthony Bradley
 *
 */
public enum IntCodecs implements IntCodecInterface, CodecInterface {


	/**
	 * Encoding first performs delta encoding and then run length
	 * encoding on top. This is appropriate for serial numbers. 
	 * 1,2,3,4,5,6 -> 1,1,1,1,1,1 -> 1,6
	 */
	RUN_LENGTH_DELTA(8, "Run length delta") {

		@Override
		public byte[] encode(int[] inputData, int param){
			return ArrayConverters.convertIntegersToFourByte(
					ArrayEncoders.runlengthEncode(
							ArrayEncoders.deltaEncode(inputData)));

		}

		@Override
		public int[] decode(byte[] inputData, int param){
			return ArrayDecoders.deltaDecode(
					ArrayDecoders.runlengthDecode(
							ArrayConverters.convertFourByteToIntegers(inputData)));
		}
	},
	/**
	 * Convert integers to a byte array - encoding each integer as a four byte integer.
	 */
	CONVERT_4_BYTE(4, "Convert to bytes as 4 byte integers."){

		@Override
		public byte[] encode(int[] inputData, int param){
			return ArrayConverters.convertIntegersToFourByte(inputData);
		}

		@Override
		public int[] decode(byte[] inputData, int param){
			return ArrayConverters.convertFourByteToIntegers(inputData);
		}
		
	},
	/**
	 * Convert integers to  a byte array - encoding each integer as a one byte integer.
	 */
	CONVERT_BYTE(2, "Convert to bytes as  byte integers."){

		@Override
		public byte[] encode(int[] inputData, int param){
			return ArrayConverters.convertIntegersToBytes(inputData);
		}

		@Override
		public int[] decode(byte[] inputData, int param){
			return ArrayConverters.convertByteToIntegers(inputData);
		}
		
	};
	
	private final int codecId;
	private final String codecName;

	/**
	 * Constructor sets the codec type from a short.
	 * @param codecId the input integer indicating the strategy
	 */
	private IntCodecs(int codecId, String codecName) {
		this.codecId = codecId;
		this.codecName = codecName;
	}

	/**
	 * Get the codec from an input byte. 
	 * @param codecId the byte defining the coding
	 * @return the enum of the codec
	 */
	public static IntCodecs getCodec(int codecId){
		for(IntCodecs codecs : IntCodecs.values())
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
	public static int[] decodeArr(byte[] inputData){
		OptionParser optionParser = new OptionParser(inputData);
		IntCodecs codecs = getCodec(optionParser.methodNumber);
		return codecs.decode(optionParser.data, optionParser.param);
	}

	/**
	 * @return the codec name - a string naming the codec
	 */
	public String getCodecName() {
		return codecName;
	}


	/**
	 * @return the codecId a short for the codec
	 */
	public int getCodecId() {
		return codecId;
	}


}
