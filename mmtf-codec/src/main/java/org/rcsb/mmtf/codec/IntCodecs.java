package org.rcsb.mmtf.codec;

import java.util.Arrays;

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
	RUN_LENGTH_DELTA((byte) 5, "Run length delta") {

		@Override
		public byte[] encode(int[] inputData) {
			return CodecUtils.prependByteArr(ArrayConverters.convertIntegersToFourByte(
					ArrayEncoders.runlengthEncode(
							ArrayEncoders.deltaEncode(inputData))),
					this.getCodecId());

		}

		@Override
		public int[] decode(byte[] inputData) {
			return ArrayDecoders.deltaDecode(
					ArrayDecoders.runlengthDecode(
							org.rcsb.mmtf.codec.ArrayConverters.convertFourByteToIntegers(inputData)));
		}
	},
	/**
	 * Run length encode integers, for repeated numbers, e.g. alt locations.
	 */
	RUN_LENGTH((byte) 6,"Run length"){

		@Override
		public byte[] encode(int[] inputData) {
			return  CodecUtils.prependByteArr(ArrayConverters.convertIntegersToFourByte(
					ArrayEncoders.runlengthEncode(inputData)),
					this.getCodecId());
		}

		@Override
		public int[] decode(byte[] inputData) {
			return ArrayDecoders.runlengthDecode(
					org.rcsb.mmtf.codec.ArrayConverters.convertFourByteToIntegers(inputData));
		}
		
	},
	/**
	 * Convert integers to four byte integers.
	 */
	CONVERT_4_BYTE((byte) 7, "Convert to bytes as 4 byte integers."){

		@Override
		public byte[] encode(int[] inputData) {
			return CodecUtils.prependByteArr(ArrayConverters.convertIntegersToFourByte(inputData),
					this.getCodecId());
		}

		@Override
		public int[] decode(byte[] inputData) {
			return org.rcsb.mmtf.codec.ArrayConverters.convertFourByteToIntegers(inputData);
		}
		
	},
	/**
	 * Convert integers to  byte integers.
	 */
	CONVERT_BYTE((byte) 8, "Convert to bytes as  byte integers."){

		@Override
		public byte[] encode(int[] inputData) {
			return CodecUtils.prependByteArr(ArrayConverters.convertIntegersToBytes(inputData),
					this.getCodecId());
		}

		@Override
		public int[] decode(byte[] inputData) {
			return org.rcsb.mmtf.codec.ArrayConverters.convertByteToIntegers(inputData);
		}
		
	};
	
	private final byte codecId;
	private final String codecName;

	/**
	 * Constructor sets the codec type from a short.
	 * @param codecId the input short (byte) indicating the strategy
	 */
	private IntCodecs(byte codecId, String codecName) {
		this.codecId = codecId;
		this.codecName = codecName;
	}

	/**
	 * Get the codec from an input byte. 
	 * @param inputByte the byte defining the coding
	 * @return the enum of the codec
	 */
	public static IntCodecs getCodecFromByte(byte inputByte){
		for(IntCodecs codecs : IntCodecs.values())
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
	 * @return the decoded array as a int array
	 */
	public static int[] decodeArr(byte[] inputData){
		IntCodecs intCodecs = getCodecFromByte(inputData[0]);
		return intCodecs.decode(Arrays.copyOfRange(inputData, 1, inputData.length));
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
	public byte getCodecId() {
		return codecId;
	}


}
