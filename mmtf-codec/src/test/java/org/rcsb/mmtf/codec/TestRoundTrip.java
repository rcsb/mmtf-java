package org.rcsb.mmtf.codec;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;
import static org.junit.Assert.*;
import org.rcsb.mmtf.api.StructureDataInterface;
import org.rcsb.mmtf.dataholders.MmtfStructure;
import org.rcsb.mmtf.decoder.DefaultDecoder;
import org.rcsb.mmtf.decoder.GenericDecoder;
import org.rcsb.mmtf.decoder.ReaderUtils;
import org.rcsb.mmtf.encoder.DefaultEncoder;
import org.rcsb.mmtf.encoder.GenericEncoder;
import org.rcsb.mmtf.encoder.ReducedEncoder;


/**
 * Basic integration tests of the new codec suite.
 * @author Anthony Bradley
 *
 */
public class TestRoundTrip {
	
	/**
	 * Test that a simple roundtripping works - using GenericEncoder and GenericDecoder
	 * @throws IOException error reading the file from the resources
	 */
	@Test
	public void testGenericGeneric() throws IOException {
		StructureDataInterface structureDataInterface = getDefaultFullData();
		compareStructDataInfs(structureDataInterface, new GenericDecoder(new GenericEncoder(structureDataInterface).getMmtfEncodedStructure()));
	}
	
	
	/**
	 * Test that a simple roundtripping works - using DefaultEncoder and GenericDecoder
	 * @throws IOException error reading the file from the resources
	 */
	@Test
	public void testDefaultDefault() throws IOException {
		StructureDataInterface structureDataInterface = getDefaultFullData();
		compareStructDataInfs(structureDataInterface, new DefaultDecoder(new DefaultEncoder(structureDataInterface).getMmtfEncodedStructure()));
	}

	/**
	 * Test that the encoding the reduce format and reading it in works.
	 * @throws IOException error reading the file from the resources
	 */
	@Test
	public void testReducedGeneric() throws IOException {
		StructureDataInterface structureDataInterface = getDefaultFullData();
		ReducedEncoder reducedEncoder = new ReducedEncoder(structureDataInterface);
		MmtfStructure mmtfStructure = reducedEncoder.getMmtfEncodedStructure();
		compareStructDataInfs(new GenericDecoder(mmtfStructure), new GenericDecoder(mmtfStructure));
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
	private StructureDataInterface getDefaultFullData() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		Path inFile = Paths.get(classLoader.getResource("mmtf/4cup.mmtf").getFile());
		return new DefaultDecoder(ReaderUtils.getDataFromFile(inFile));
	}
	

	/**
	 * Compare the data in two {@link StructureDataInterface}s.
	 * @param interfaceOne the first {@link StructureDataInterface}
	 * @param interfaceTwo the second {@link StructureDataInterface}
	 */
	private void compareStructDataInfs(StructureDataInterface interfaceOne, StructureDataInterface interfaceTwo) {
		assertArrayEquals(interfaceOne.getAtomIds(), interfaceTwo.getAtomIds());
		assertArrayEquals(interfaceOne.getxCoords(), interfaceTwo.getxCoords(),0.0009f);
		assertArrayEquals(interfaceOne.getbFactors(), interfaceTwo.getbFactors(), 0.009f);
		assertArrayEquals(interfaceOne.getAltLocIds(), interfaceTwo.getAltLocIds());		
	}
	
}
