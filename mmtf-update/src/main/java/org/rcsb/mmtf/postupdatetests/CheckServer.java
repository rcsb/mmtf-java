package org.rcsb.mmtf.postupdatetests;

import java.io.File;
import java.io.IOException;

import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.StructureIO;
import org.biojava.nbio.structure.align.util.AtomCache;
import org.biojava.nbio.structure.io.FileParsingParameters;
import org.biojava.nbio.structure.io.mmtf.ParseUsingBioJava;
import org.junit.Test;
import org.rcsb.mmtf.biojavaencoder.EncoderUtils;
import org.rcsb.mmtf.decoder.ParsingParams;
import org.rcsb.mmtf.examples.HandleIO;
import org.rcsb.mmtf.testutils.CheckOnBiojava;
import org.rcsb.mmtf.testutils.CheckOnRawApi;
import org.rcsb.mmtf.update.IntegrationTestUtils;

public class CheckServer {

  private HandleIO handleIo;
  private ParseUsingBioJava parseUsingBioJava;
  private FileParsingParameters params;
  private CheckOnBiojava checkEquiv;

  public static void main(String[] args) throws IOException {
	  String outPutFile = args[0];
	  CheckServer checkServer = new CheckServer();
	  checkServer.basicParsingTest();
		File f = new File(outPutFile);
		f.getParentFile().mkdirs(); 
		f.createNewFile();
  }


  /**
   * Basic test to go through a series of PDBs and make sure they are the same.
   * Should be run at the end of the weekly update to ensure data integrity.
 * @throws IOException 
   */
  @Test
  public void basicParsingTest() throws IOException {
	  
	    // Get the class to parse and get data
	    parseUsingBioJava = new ParseUsingBioJava();
	    handleIo = new HandleIO();
	    checkEquiv = new CheckOnBiojava();
	    EncoderUtils encoderUtils = new EncoderUtils();
	    AtomCache cache = encoderUtils.setUpBioJava();
	    params = cache.getFileParsingParams();
    // Test it for a series of structures
	  for (String pdbId : IntegrationTestUtils.TEST_CASES) {
	  testParsing(pdbId);
	  }
   }

  /**
   * This tests whether the data on the website can be decoded to produce the same
   * data as parsing the mmcif data.
   * @param inputPdb
 * @throws IOException 
   */
  private void testParsing(String inputPdb) throws IOException {
    System.out.println("TESTING: "+inputPdb);
    byte[] inputByteArr = handleIo.getFromUrl(inputPdb);
    ParsingParams parsingParms = new ParsingParams();
    parsingParms.setParseInternal(params.isUseInternalChainId());
    Structure mmtfStruct = parseUsingBioJava.getBiojavaStruct(inputByteArr, parsingParms);
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
