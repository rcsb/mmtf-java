package org.rcsb.mmtf.codec;


import java.util.Arrays;

import org.rcsb.mmtf.dataholders.MmtfStructure;
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
	DELTA_SPLIT_3((byte) 2, "Delta") {

		@Override
		public byte[] encode(float[] inputData) {
//			// Encode the coordinate  and B-factor arrays.
//			List<int[]> xCoords = ArrayConverters.splitIntegers(
//					ArrayEncoders.deltaEncode(
//							ArrayConverters.convertFloatsToInts(
//									structureDataInterface.getxCoords(),
//									MmtfStructure.COORD_DIVIDER)));
//			mmtfBean.setxCoordBig(ArrayConverters.convertIntegersToFourByte(xCoords.get(0)));
//			mmtfBean.setxCoordSmall(ArrayConverters.convertIntegersToTwoBytes(xCoords.get(1)));
			return null;
		}

		@Override
		public float[] decode(byte[] inputData) {
			// TODO Auto-generated method stub
			return null;
		}
 	},
	
	/**
	 * Encoding a list of floats (e.g. coordinates) using integer encoding (2DP accuracy) and then delta encoding. Then split the 
	 * results into 2 byte and 4 byte int arrays (for storage). 
	 */
	DELTA_SPLIT_2((byte) 3, "Delta") {

		@Override
		public byte[] encode(float[] inputData) {
//			// Encode the coordinate  and B-factor arrays.
//			List<int[]> xCoords = ArrayConverters.splitIntegers(
//					ArrayEncoders.deltaEncode(
//							ArrayConverters.convertFloatsToInts(
//									structureDataInterface.getxCoords(),
//									MmtfStructure.COORD_DIVIDER)));
//			mmtfBean.setxCoordBig(ArrayConverters.convertIntegersToFourByte(xCoords.get(0)));
//			mmtfBean.setxCoordSmall(ArrayConverters.convertIntegersToTwoBytes(xCoords.get(1)));
			return null;
		}

		@Override
		public float[] decode(byte[] inputData) {
			// TODO Auto-generated method stub
			return null;
		}
 	},
	
	/**
	 * Run length encoding using two decimal place precision.
	 */
	RUN_LENGTH_2((byte) 4, "") {

		@Override
		public byte[] encode(float[] inputData) {
			return ArrayConverters.convertIntegersToFourByte(
					ArrayEncoders.runlengthEncode(
							ArrayConverters.convertFloatsToInts(
									inputData,
									MmtfStructure.OCCUPANCY_BFACTOR_DIVIDER)));
		}

		@Override
		public float[] decode(byte[] inputData) {
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
	private FloatCodecs(byte codecId, String codecName) {
		this.setCodecId(codecId);
		this.setCodecName(codecName);
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
	
	

	
}
