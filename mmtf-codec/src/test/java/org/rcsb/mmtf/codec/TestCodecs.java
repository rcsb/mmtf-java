package org.rcsb.mmtf.codec;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Class to test the codecs (anything that implements {@link CodecInterface}).
 * @author Anthony Bradley
 *
 */
public class TestCodecs {

	/**
	 * Test all of the float codecs work on a range of input data.
	 */
	@Test
	public void testFloatCodecs() {
		
		for(float[] inputData : getFloatData()){
			for (FloatCodecs floatCodecs : FloatCodecs.values()){
				byte[] encoded = floatCodecs.encode(inputData);
				assertNotNull(encoded);
				float[] decoded = floatCodecs.decode(encoded);
				assertArrayEquals(decoded, inputData, getPrecision(floatCodecs.getMultiplier()));
			}
		}
	}
	
	
	/**
	 * Test all of the int codecs work on a range of input data.
	 */
	@Test
	public void testIntCodecs() {
		
		for(int[] inputData : getIntData()){
			for (IntCodecs codec : IntCodecs.values()){
				byte[] encoded = codec.encode(inputData);
				assertNotNull(encoded);
				int[] decoded = codec.decode(encoded);
				assertArrayEquals(decoded, inputData);
			}
		}
	}

	/**
	 * Test all of the char codecs work on a range of input data.
	 */
	@Test
	public void testCharCodecs() {
		
		for(char[] inputData : getCharData()){
			for (CharCodecs codec : CharCodecs.values()){
				byte[] encoded = codec.encode(inputData);
				assertNotNull(encoded);
				char[] decoded = codec.decode(encoded);
				assertArrayEquals(decoded, inputData);
			}
		}
	}
	
	
	/**
	 * Test all of the String codecs work on a range of input data.
	 */
	@Test
	public void testStringCodecs() {
		
		for(String[] inputData : getStringData()){
			for (StringCodecs codec : StringCodecs.values()){
				byte[] encoded = codec.encode(inputData);
				assertNotNull(encoded);
				String[] decoded = codec.decode(encoded);
				assertArrayEquals(decoded, inputData);
			}
		}
	}
	
	/**
	 * Get the character array data to test all the methods with.
	 * @return a list of character arrays to be used as test data.
	 */
	private List<char[]> getCharData() {
		List<char[]> data = new ArrayList<>();
		data.add(new char[]{'A','B','?'});
		return data;
	}

	
	/**
	 * Get the String array data to test all the methods with.
	 * @return a list of String arrays to be used as test data.
	 */
	private List<String[]> getStringData() {
		List<String[]> data = new ArrayList<>();
		data.add(new String[]{"A","BDDD","?"});
		return data;
	}

	/**
	 * Get the integer array data to test all the methods with.
	 * @return a list of integer arrays to be used as test data.
	 */
	private List<int[]> getIntData() {
		List<int[]> data = new ArrayList<>();
		// TODO Must be 8 bit (1 byte) ints
		data.add(new int[]{1,2,12});
		return data;
	}


	/**
	 * Get the floating point array data to test all the methods with.
	 * @return a list of float arrays to be used as test data.
	 */
	private List<float[]> getFloatData() {
		List<float[]> data = new ArrayList<>();
		data.add(new float[]{1.0f,2.0f,Short.MAX_VALUE});
		return data;
	}

	/**
	 * Get the precision of a float codec.
	 * @param accuracy the accuracy (e.g. 1000.0f - means 3dp accuracy).
	 * @return the precision required (e.g. 0.0009f for 1000.0f)
	 */
	private float getPrecision(float accuracy) {
		float maxPrecision = 1.0f/accuracy;
		maxPrecision = maxPrecision / 10;
		maxPrecision = maxPrecision * 0.9999999999999999999f;
		return maxPrecision;
	}

}
