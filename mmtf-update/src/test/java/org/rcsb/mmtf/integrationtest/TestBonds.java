package org.rcsb.mmtf.integrationtest;

import java.io.IOException;

import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.StructureIO;
import org.junit.Test;
import org.rcsb.mmtf.biojavaencoder.EncoderUtils;
import org.rcsb.mmtf.testutils.CheckBonds;
import org.rcsb.mmtf.update.IntegrationTestUtils;

public class TestBonds {


	/**
	 * Test whether all atoms (other than waters) have at least one bond.
	 * They should.
	 * @throws StructureException 
	 * @throws IOException 
	 */
	@Test
	public void testBondConsistency() throws IOException, StructureException {
		CheckBonds testUtils = new CheckBonds();
		// Set up biojava
		EncoderUtils encoderUtils = new EncoderUtils();
		encoderUtils.setUpBioJava();
		for (String testCase : IntegrationTestUtils.TEST_CASES) {
			Structure structure = StructureIO.getStructure(testCase);
			testUtils.checkIfBondsExist(structure);
		}
	}	
}
