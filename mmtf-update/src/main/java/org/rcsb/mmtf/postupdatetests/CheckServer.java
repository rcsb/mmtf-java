package org.rcsb.mmtf.postupdatetests;

import java.io.File;
import java.io.IOException;

import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.StructureIO;
import org.biojava.nbio.structure.align.util.AtomCache;
import org.biojava.nbio.structure.io.FileParsingParameters;
import org.biojava.nbio.structure.io.mmtf.MmtfStructureReader;
import org.rcsb.mmtf.biojavaencoder.BiojavaUtils;
import org.rcsb.mmtf.examples.HandleIO;
import org.rcsb.mmtf.testutils.CheckOnBiojava;
import org.rcsb.mmtf.testutils.CheckOnRawApi;
import org.rcsb.mmtf.update.IntegrationTestUtils;

public class CheckServer {

	private HandleIO handleIo;
	private FileParsingParameters params;
	private CheckOnBiojava checkEquiv;
	
	/**
	 * Java class to check if data can be parsed from the server.
	 * 1) Arg one is the server 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String baseUrl = args[0];
		String outPutFile = args[1];
		CheckServer checkServer = new CheckServer();
		checkServer.basicParsingTest(baseUrl);
		File f = new File(outPutFile);
		f.getParentFile().mkdirs(); 
		f.createNewFile();
	}


	/**
	 * Basic test to go through a series of PDBs and make sure they are the same.
	 * Should be run at the end of the weekly update to ensure data integrity.
	 * @throws IOException 
	 */
	public void basicParsingTest(String baseUrl) throws IOException {
		// Get the class to parse and get data
		handleIo = new HandleIO();
		checkEquiv = new CheckOnBiojava();
		BiojavaUtils biojavaUtils = new BiojavaUtils();
		AtomCache cache = biojavaUtils.setUpBioJava();
		params = cache.getFileParsingParams();
		// Test it for a series of structures
		for (String pdbId : IntegrationTestUtils.TEST_CASES) {
			testParsing(pdbId, HandleIO.BASE_URL);
		}
	}

	/**
	 * This tests whether the data on the website can be decoded to produce the same
	 * data as parsing the mmcif data.
	 * @param inputPdb
	 * @throws IOException 
	 */
	private void testParsing(String inputPdb, String inputUrl) throws IOException {
		System.out.println("TESTING: "+inputPdb);
		byte[] inputByteArr = handleIo.getFromUrl(inputPdb, inputUrl);
		Structure mmtfStruct = MmtfStructureReader.getBiojavaStruct(inputByteArr);
		// Now parse from the MMCIF file
		Structure mmcifStruct;
		try {
			mmcifStruct = StructureIO.getStructure(inputPdb);
		} catch (IOException e) {
			// Error accessing mmcif
			System.err.println("Error accessing MMCIF");
			e.printStackTrace();
			throw new RuntimeException();
		} catch (StructureException e) {
			System.err.println("Error parsing/consuming MMCIF");
			e.printStackTrace();
			throw new RuntimeException();
		}
		checkEquiv.checkIfStructuresSame(mmtfStruct, mmcifStruct);
		// Now do the checks on the Raw data
		CheckOnRawApi checkRaw = new CheckOnRawApi(inputByteArr);
		checkRaw.checkRawDataConsistency(mmcifStruct, params);

	}
}
