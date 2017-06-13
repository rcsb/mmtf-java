package org.rcsb.mmtf.codec;

import static org.junit.Assert.assertArrayEquals;

import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;

import org.junit.Test;
import org.rcsb.mmtf.api.StructureDataInterface;
import org.rcsb.mmtf.dataholders.MmtfStructure;
import org.rcsb.mmtf.decoder.GenericDecoder;
import org.rcsb.mmtf.decoder.ReaderUtils;

/**
 * Basic integration tests of the codec suite.
 * @author Anthony Bradley
 *
 */
public class TestRoundTrip {

	/**
	 * Test that a simple roundtripping works - using GenericEncoder and GenericDecoder
	 * @throws IOException error reading the file from the resource
	 */
	@Test
	public void testGenericGeneric() throws IOException, ParseException {
		Utils.compare(getDefaultFullData());
	}
	
	/**
	 * Test that roundtripping on the recursive index code works.
	 */
	@Test
	public void testRecursvieRoundTrip() {
		int[] inputArr = new int[] {1,1203,Short.MAX_VALUE, 1202, Short.MIN_VALUE};
		int[] outputArr = ArrayConverters.recursiveIndexDecode(ArrayConverters.recursiveIndexEncode(inputArr));
		assertArrayEquals(inputArr, outputArr);
	}

	/**
	 * Test that round tripping how it is currently done works as expected.
	 */
	@Test
	public void testArrayRoundTrip() {
		String[] inputStrings = new String[] {"1.3554545","2.9999999","3.939393"};
		float[] inArray  = new float[inputStrings.length];
		for(int i=0; i<inputStrings.length; i++){
			double x = Double.parseDouble (inputStrings[i]);
			inArray[i] = (float) x;
		}
		float[] outArray = ArrayConverters.convertIntsToFloats(ArrayConverters.convertFloatsToInts(inArray,MmtfStructure.COORD_DIVIDER), MmtfStructure.COORD_DIVIDER);
		assertArrayEquals(inArray, outArray, 0.00099999f);
	}

	/**
	 * Get the default data for the full format.
	 * @return a {@link StructureDataInterface} for the full data.
	 * @throws IOException
	 */
	private StructureDataInterface getDefaultFullData() throws IOException, ParseException {
		Path p = Utils.getResource("/mmtf/4CUP.mmtf");
		return new GenericDecoder(ReaderUtils.getDataFromFile(p));
	}
}
