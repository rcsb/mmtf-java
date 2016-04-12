package org.rcsb.mmtf.integrationtest;

import static org.junit.Assert.assertNotEquals;

import java.io.IOException;

import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.io.mmtf.MmtfUtils;
import org.junit.Test;
import org.rcsb.mmtf.dataholders.MmtfBean;
import org.rcsb.mmtf.testutils.IntegrationTestUtils;
import org.rcsb.mmtf.testutils.Utils;
import org.rcsb.mmtf.update.TestingUtils;
import org.unitils.reflectionassert.ReflectionAssert;

/**
 * Tests to see if parsing using Biojava using mmCIF and mmtf produces the same data structure.
 * @author Anthony Bradley
 *
 */
public class TestParseMMCif {

	@Test
	public void testAll() throws IOException, StructureException{
		testDataComplete("4cup");
		TestingUtils.testList(IntegrationTestUtils.TEST_CASES);
	}
	
	/**
	 * A specific mmtf test - to make sure none of the fields are empty when the thing is encoded
	 * @throws StructureException 
	 * @throws IOException 
	 */
	private void testDataComplete(String pdbId) throws IOException, StructureException {
		MmtfUtils.setUpBioJava();
		// Get an mmtfBean 
		MmtfBean mmtfBean = Utils.getBean(pdbId);
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
