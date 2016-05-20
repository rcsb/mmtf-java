package org.rcsb.mmtf.codec;

import java.util.Arrays;

import org.rcsb.mmtf.encoder.ArrayConverters;
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
			return ArrayConverters.convertIntegersToFourByte(
					ArrayEncoders.runlengthEncode(
							ArrayEncoders.deltaEncode(inputData)));

		}

		@Override
		public int[] decode(byte[] inputData) {
			// TODO Auto-generated method stub
			return null;
		}
	},
	/**
	 * Run length encode integers, for repeated numbers, e.g. alt locations.
	 */
	RUN_LENGTH_ENCODE((byte) 6,"Run lenght"){

		@Override
		public byte[] encode(int[] inputData) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int[] decode(byte[] inputData) {
			// TODO Auto-generated method stub
			return null;
		}
		
	},
	/**
	 * Convert integers to four byte integers.
	 */
	CONVERT_4_BYTE((byte) 7, "Convert to bytes as 4 byte integers."){

		@Override
		public byte[] encode(int[] inputData) {
			return ArrayConverters.convertIntegersToFourByte(inputData);
		}

		@Override
		public int[] decode(byte[] inputData) {
			// TODO Auto-generated method stub
			return null;
		}
		
	},
	/**
	 * Convert integers to  byte integers.
	 */
	CONVERT_BYTE((byte) 8, "Convert to bytes as  byte integers."){

		@Override
		public byte[] encode(int[] inputData) {
			return ArrayConverters.convertIntegersToBytes(inputData);
		}

		@Override
		public int[] decode(byte[] inputData) {
			// TODO Auto-generated method stub
			return null;
		}
		
	};
	
	
	private byte codecId;
	private String codecName;

	/**
	 * Constructor sets the codec type from a short.
	 * @param codecId the input short (byte) indicating the strategy
	 */
	private IntCodecs(byte codecId, String codecName) {
		this.setCodecId(codecId);
		this.setCodecName(codecName);
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
		// Return a null entry.
		return  null;
	}
	
	/**
	 * Decode a byte array from an input array.
	 * @param inputData the byte array of data
	 * @return the decoded array as a int array
	 */
	public static int[] decodeArr(byte[] inputData){
		for(IntCodecs codecs : IntCodecs.values())
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



}
