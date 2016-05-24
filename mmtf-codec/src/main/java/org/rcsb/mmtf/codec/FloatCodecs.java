package org.rcsb.mmtf.codec;


import java.util.Arrays;

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
	INT_DELTA_RECURSIVE_3((byte) 2, 1000.0f, "Delta") {

		@Override
		public byte[] encode(float[] inputData) {
			return ArrayConverters.convertIntegersToTwoBytes(ArrayConverters.recursiveIndexEncode(
					ArrayEncoders.deltaEncode(
							ArrayConverters.convertFloatsToInts(
									inputData,
									this.getMultiplier()))));
			}

		@Override
		public float[] decode(byte[] inputData) {
			return ArrayConverters.convertIntsToFloats(
					ArrayDecoders.deltaDecode(
							ArrayConverters.recursiveIndexDecode(
									ArrayConverters.convertTwoByteToIntegers(inputData))),
					this.getMultiplier());
			}
 	},
	
	/**
	 * Encoding a list of floats (e.g. coordinates) using integer encoding (2DP accuracy)
	 * delta encoding and then recursive indexing.
	 */
	INT_DELTA_RECURSIVE_2((byte) 3, 100.0f, "Delta") {

		@Override
		public byte[] encode(float[] inputData) {
			return ArrayConverters.convertIntegersToTwoBytes(ArrayConverters.recursiveIndexEncode(
					ArrayEncoders.deltaEncode(
							ArrayConverters.convertFloatsToInts(
									inputData,
									this.getMultiplier()))));
			}

		@Override
		public float[] decode(byte[] inputData) {
			return ArrayConverters.convertIntsToFloats(
					ArrayDecoders.deltaDecode(
							ArrayConverters.recursiveIndexDecode(
									ArrayConverters.convertTwoByteToIntegers(inputData))),
					this.getMultiplier());
		}
 	},
	
	/**
	 * Encoding a list of floats (e.g. occupancy) using integer encoding (2DP accuracy) 
	 * and then run length encoding.
	 */
	INT_RUNLENGTH_2((byte) 4, 100.0f, "Run length") {

		@Override
		public byte[] encode(float[] inputData) {
			return ArrayConverters.convertIntegersToFourByte(
					ArrayEncoders.runlengthEncode(
							ArrayConverters.convertFloatsToInts(inputData, this.getMultiplier())));
		}


		@Override
		public float[] decode(byte[] inputData) {
			return ArrayConverters.convertIntsToFloats(
					ArrayDecoders.runlengthDecode(
							ArrayConverters.convertFourByteToIntegers(inputData)),
					this.getMultiplier());
		}
 		
 	};
	
	
	

	private final byte codecId;
	private final String codecName;
	private final float multiplier;
	
	/**
	 * Constructor for the float codec Enum.
	 * @param codecId the byte encoding this codec strategy
	 * @param multiplier the accuracy of this float encoding strategy
	 * @param codecName the name of this encoding strategy
	 */
	private FloatCodecs(byte codecId, float multiplier, String codecName) {
		this.codecId = codecId;
		this.multiplier = multiplier;
		this.codecName = codecName;
	}

	/**
	 * Get the codec from an input byte. 
	 * @param inputByte the byte defining the coding
	 * @return the enum of the codec
	 */
	public static FloatCodecs getCodecFromByte(byte inputByte){
		for(FloatCodecs codecs : FloatCodecs.values())
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
	 * @return the decoded array as a float array
	 */
	public static float[] decodeArr(byte[] inputData){
		FloatCodecs codecs = getCodecFromByte(inputData[0]);
		return codecs.decode(Arrays.copyOfRange(inputData, 1, inputData.length));
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
	public byte getCodecId() {
		return codecId;
	}


	/**
	 * @return the multiplier for converting between integers and floats. 
	 * Encodes the precision.
	 */
	public float getMultiplier() {
		return multiplier;
	}
	

	

	
}
