package org.rcsb.mmtf.codec;

import java.io.IOException;

import org.junit.Test;
import static org.junit.Assert.*;
import org.rcsb.mmtf.api.StructureDataInterface;
import org.rcsb.mmtf.dataholders.MmtfStructure;
import org.rcsb.mmtf.decoder.DefaultDecoder;
import org.rcsb.mmtf.decoder.ReaderUtils;


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
		StructureDataInterface structureDataInterface = new DefaultDecoder(ReaderUtils.getDataFromUrl("4cup"));
		GenericEncoder genericEncoder = new GenericEncoder(structureDataInterface);
		MmtfStructure mmtfStructure = genericEncoder.getMmtfEncodedStructure();
		GenericDecoder genericDecoder = new GenericDecoder(mmtfStructure);
		assertArrayEquals(structureDataInterface.getAtomIds(), genericDecoder.getAtomIds());
		assertArrayEquals(structureDataInterface.getxCoords(), genericDecoder.getxCoords(),0.0009f);
		assertArrayEquals(structureDataInterface.getbFactors(), genericDecoder.getbFactors(), 0.009f);
		assertArrayEquals(structureDataInterface.getAltLocIds(), genericDecoder.getAltLocIds());
	}
	
	
	/**
	 * 
	 */
	@Test
	public void testRecursvieRoundTrip() {
		int[] inputArr = new int[] {1,1203,Short.MAX_VALUE, 1202, Short.MIN_VALUE};
		int[] outputArr = CodecUtils.recursiveIndexDecode(CodecUtils.recursiveIndexEncode(inputArr));
		assertArrayEquals(inputArr, outputArr);
	}

}
