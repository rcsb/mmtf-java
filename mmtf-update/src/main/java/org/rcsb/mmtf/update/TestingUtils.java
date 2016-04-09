package org.rcsb.mmtf.update;

import java.io.IOException;

import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.StructureIO;
import org.biojava.nbio.structure.io.mmtf.MmtfActions;
import org.biojava.nbio.structure.io.mmtf.MmtfUtils;
import org.rcsb.mmtf.testutils.CheckOnBiojava;

public class TestingUtils {

	/**
	 * Test a set of PDB ids found in the string list
	 * @param inputList the list of PDB ids to test
	 * @throws IOException
	 * @throws StructureException
	 */
	public static void testList(String[] inputList) throws IOException, StructureException{
		MmtfUtils.setUpBioJava();
		for (String pdbId : inputList){
			testSingleStructure(pdbId);
		}
	}

	/**
	 * Round trip and check a structure given by its PDB id
	 * @param pdbId the string PDB id for the structure
	 * @throws IOException
	 * @throws StructureException
	 */
	public static void testSingleStructure(String pdbId) throws IOException, StructureException {
		CheckOnBiojava checkEquiv = new CheckOnBiojava();
		Structure structure = StructureIO.getStructure(pdbId);
		checkEquiv.checkIfStructuresSame(structure,MmtfActions.roundTrip(structure));
	}



}
