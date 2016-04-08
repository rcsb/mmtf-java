package org.rcsb.mmtf.integrationtest;

import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.StructureIO;
import org.biojava.nbio.structure.align.util.AtomCache;
import org.biojava.nbio.structure.io.FileParsingParameters;
import org.junit.Test;
import org.rcsb.mmtf.biojavaencoder.BiojavaUtils;
import org.rcsb.mmtf.dataholders.BiojavaEncoderImpl;
import org.rcsb.mmtf.dataholders.EncoderInterface;
import org.rcsb.mmtf.dataholders.MmtfBean;
import org.rcsb.mmtf.dataholders.PDBGroup;
import org.rcsb.mmtf.encoder.EncoderUtils;
import org.rcsb.mmtf.update.IntegrationTestUtils;
import org.rcsb.mmtf.update.TestingUtils;
import org.unitils.reflectionassert.ReflectionAssert;

/**
 * Tests to see if parsing using Biojava using mmCIF and mmtf produces the same data structure.
 * @author Anthony Bradley
 *
 */
public class TestParseMMCif {

	private AtomCache cache;
	private FileParsingParameters params;
	private TestingUtils testingUtils = new TestingUtils();

	public TestParseMMCif(){
		// Set up biojava
		BiojavaUtils biojavaUtils = new BiojavaUtils();
		cache = biojavaUtils.setUpBioJava();
		params = cache.getFileParsingParams();
	}


	@Test
	public void testAsymChainIds() throws IOException, StructureException, IllegalAccessException, InvocationTargetException {
		// Set the params
		params.setUseInternalChainId(true);
		cache.setFileParsingParams(params);
		StructureIO.setAtomCache(cache);
		testingUtils.testAll(IntegrationTestUtils.TEST_CASES, params, cache);
	}

	@Test
	public void testAuthChainIds() throws IOException, StructureException, IllegalAccessException, InvocationTargetException {
		// Set the param
		params.setUseInternalChainId(false);
		cache.setFileParsingParams(params);
		StructureIO.setAtomCache(cache);
		testingUtils.testAll(IntegrationTestUtils.TEST_CASES, params, cache);
	}
	
	/**
	 * A specific mmtf test - to make sure none of the fields are empty when the thing is encoded
	 * @throws StructureException 
	 * @throws IOException 
	 */
	private void testDataComplete(String pdbId) throws IOException {

		// Utility functions for encoding stuff
		EncoderUtils eu = new EncoderUtils();
		// Get the utility class to get the structures
		EncoderInterface parsedDataStruct = new BiojavaEncoderImpl();
		Map<Integer, PDBGroup> totMap = new HashMap<Integer, PDBGroup>();
		// Parse the data into the basic data structure
		parsedDataStruct.generateDataStructuresFromPdbId(pdbId, totMap);
		MmtfBean mmtfBean = null;
		// Compress the data and get it back out
		mmtfBean = eu.compressToMmtfBean(parsedDataStruct.getBioStruct(), parsedDataStruct.getHeaderStruct());
		// Make sure all fields are re-populated
		ReflectionAssert.assertPropertiesNotNull("Some properties are null in mmtf generated from biojava object",  mmtfBean);
		// Now check the standard ones have been set
		assertNotEquals(mmtfBean.getResolution(), (float) -1.0);
		assertNotEquals(mmtfBean.getrFree(), (float) -1.0);
		// Check that these integer values are set
		assertNotEquals(mmtfBean.getNumAtoms(), -1);
		assertNotEquals(mmtfBean.getNumBonds(), -1);
		// And finally - check this is working
		assertNotEquals(mmtfBean.getMmtfProducer(), "NA");
	}

}
