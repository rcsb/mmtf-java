package org.rcsb.mmtf.codec;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.Test;
import static org.junit.Assert.*;
import org.rcsb.mmtf.api.StructureDataInterface;
import org.rcsb.mmtf.dataholders.MmtfStructure;
import org.rcsb.mmtf.decoder.DefaultDecoder;
import org.rcsb.mmtf.decoder.GenericDecoder;
import org.rcsb.mmtf.decoder.ReaderUtils;
import org.rcsb.mmtf.encoder.GenericEncoder;


/**
 * Basic integration tests of the new codec suite.
 * @author Anthony Bradley
 *
 */
public class BasicTest {
	
	/**
	 * Test that a simple roundtripping works.
	 * @throws IOException
	 */
	@Test
	public void testRoundtrip() throws IOException {
		StructureDataInterface structureDataInterface = new DefaultDecoder(ReaderUtils.getDataFromFile(Paths.get("/Users/anthony/mmtf-webapp/testdata/4CUP.mmtf")));
		GenericEncoder genericEncoder = new GenericEncoder(structureDataInterface);
		MmtfStructure mmtfStructure = genericEncoder.getMmtfEncodedStructure();
		GenericDecoder genericDecoder = new GenericDecoder(mmtfStructure);
		assertArrayEquals(structureDataInterface.getAtomIds(), genericDecoder.getAtomIds());
		assertArrayEquals(structureDataInterface.getxCoords(), genericDecoder.getxCoords(),0.0009f);
		assertArrayEquals(structureDataInterface.getbFactors(), genericDecoder.getbFactors(), 0.009f);
		assertArrayEquals(structureDataInterface.getAltLocIds(), genericDecoder.getAltLocIds());
	}
	
	
	/**
	 * Test that roundtripping on the recursive index code owrks.
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

}
