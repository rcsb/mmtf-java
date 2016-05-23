package org.rcsb.mmtf.codec;


import java.util.Arrays;

import org.rcsb.mmtf.dataholders.MmtfStructure;
import org.rcsb.mmtf.decoder.ArrayDecoders;
import org.rcsb.mmtf.encoder.ArrayConverters;
import org.rcsb.mmtf.encoder.ArrayEncoders;

/**
 * An enum defining the encoding and decoding strategies.
 * @author Anthony Bradley
 *
 */
public enum FloatCodecs implements FloatCodecInterface, CodecInterface {
	
	/**
	 * Encoding a list of floats (e.g. coordinates) using integer encoding (3DP accuracy) and then delta encoding. Then split the 
	 * results into 2 byte and 4 byte int arrays (for storage). 
	 */
	DELTA_SPLIT_3((byte) 2, 1000.0f, "Delta") {

		@Override
		public byte[] encode(float[] inputData) {
			return CodecUtils.prependByteArr(deltaEncode(inputData, this.getAccuracy()),this.getCodecId());
		}

		@Override
		public float[] decode(byte[] inputData) {
			return deltaDecode(inputData, this.getAccuracy());
		}
 	},
	
	/**
	 * Encoding a list of floats (e.g. coordinates) using integer encoding (2DP accuracy) and then delta encoding. Then split the 
	 * results into 2 byte and 4 byte int arrays (for storage). 
	 */
	DELTA_SPLIT_2((byte) 3, 100.0f, "Delta") {

		@Override
		public byte[] encode(float[] inputData) {
			return CodecUtils.prependByteArr(deltaEncode(inputData, this.getAccuracy()),this.getCodecId());
		}

		@Override
		public float[] decode(byte[] inputData) {
			return deltaDecode(inputData, this.getAccuracy());
		}
 	},
	
	/**
	 * Run length encoding using two decimal place precision.
	 */
	RUN_LENGTH_2((byte) 4, 100.0f, "Run length") {

		@Override
		public byte[] encode(float[] inputData) {
			return CodecUtils.prependByteArr(runLengthEncode(inputData, this.getAccuracy()),this.getCodecId());
		}


		@Override
		public float[] decode(byte[] inputData) {
			return runLengthDecode(inputData, this.getAccuracy());
		}
 		
 	};
	
	
	

	private byte codecId;
	private String codecName;
	private float accuracy;
	
	/**
	 * Constructor for the float codec Enum.
	 * @param codecId the byte encoding this codec strategy
	 * @param accuracy the accuracy of this float encoding strategy
	 * @param codecName the name of this encoding strategy
	 */
	private FloatCodecs(byte codecId, float accuracy, String codecName) {
		this.setCodecId(codecId);
		this.setCodecName(codecName);
		this.setAccuracy(accuracy);
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
		// Return a null entry.
		return  null;
	}
	
	/**
	 * Decode a byte array from an input array.
	 * @param inputData the byte array of data
	 * @return the decoded array as a float array
	 */
	public static float[] decodeArr(byte[] inputData){
		for(FloatCodecs codecs : FloatCodecs.values())
		{
			if(inputData[0]==codecs.codecId)
			{
				return codecs.decode(Arrays.copyOfRange(inputData, 1, inputData.length));
			}
		}
		// Return a null entry.
		return  null;
	}
	

	
	/**
	 * @return the codec name - a string naming the codec
	 */
	public String getCodecName() {
		return codecName;
	}

	/**
	 * @param codecName the codec name - a string naming the codec
	 */
	public void setCodecName(String codecName) {
		this.codecName = codecName;
	}

	/**
	 * @return the codecId a short for the codec
	 */
	public byte getCodecId() {
		return codecId;
	}

	/**
	 * @param codecId the codec id - a short for the codec
	 */
	public void setCodecId(byte codecId) {
		this.codecId = codecId;
	}

	/**
	 * @return the accuracy
	 */
	public float getAccuracy() {
		return accuracy;
	}

	/**
	 * @param accuracy the accuracy to set
	 */
	public void setAccuracy(float accuracy) {
		this.accuracy = accuracy;
	}
	

	/**
	 * 
	 * @param inputData
	 * @param accuracy
	 * @return
	 */
	private static byte[] deltaEncode(float[] inputData, float accuracy) {
		return ArrayConverters.convertShortsToTwoBytes(ArrayConverters.bitPack(
				ArrayEncoders.deltaEncode(
						ArrayConverters.convertFloatsToInts(
								inputData,
								MmtfStructure.COORD_DIVIDER))));
	}
	
	/**
	 * 
	 * @param inputData
	 * @param accuracy
	 * @return
	 */
	private static float[] deltaDecode(byte[] inputData, float accuracy) {
		return org.rcsb.mmtf.decoder.ArrayConverters.convertIntsToFloats(
				ArrayDecoders.deltaDecode(
						ArrayConverters.bitUnpack(
								org.rcsb.mmtf.decoder.ArrayConverters.convertTwoBytesToShorts(inputData))),
				accuracy);
	}
	
	/**
	 * 
	 * @param inputData
	 * @param accuracy
	 * @return
	 */
	private static byte[] runLengthEncode(float[] inputData, float accuracy) {
		return ArrayConverters.convertIntegersToFourByte(
				ArrayEncoders.runlengthEncode(
						ArrayConverters.convertFloatsToInts(inputData, accuracy)));
	}
	
	
	/**
	 * 
	 * @param inputData
	 * @param accuracy
	 * @return
	 */
	private static float[] runLengthDecode(byte[] inputData, float accuracy) {
		return org.rcsb.mmtf.decoder.ArrayConverters.convertIntsToFloats(
				ArrayDecoders.runlengthDecode(
						org.rcsb.mmtf.decoder.ArrayConverters.convertFourByteToIntegers(inputData)),
				accuracy);
	}
	
}
