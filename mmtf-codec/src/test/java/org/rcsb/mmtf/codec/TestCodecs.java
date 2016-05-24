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

		List<float[]> floatData = new ArrayList<>();
		floatData.add(new float[]{1.0f,2.0f,Short.MAX_VALUE});
		
		for(float[] inputData : floatData){
			for (FloatCodecs floatCodecs : FloatCodecs.values()){
				byte[] encoded = floatCodecs.encode(inputData);
				assertNotNull(encoded);
				float[] decoded = floatCodecs.decode(encoded);
				assertArrayEquals(decoded, inputData, getPrecision(floatCodecs.getAccuracy()));
			}
		}
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
