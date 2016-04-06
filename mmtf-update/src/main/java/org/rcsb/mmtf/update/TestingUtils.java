package org.rcsb.mmtf.update;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.io.FileUtils;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.StructureIO;
import org.biojava.nbio.structure.align.util.AtomCache;
import org.biojava.nbio.structure.io.FileParsingParameters;
import org.biojava.nbio.structure.io.mmtf.MmtfStructureDecoder;
import org.rcsb.mmtf.decoder.DecodeStructure;
import org.rcsb.mmtf.decoder.ParsingParams;
import org.rcsb.mmtf.encoder.EncodeStructure;
import org.rcsb.mmtf.testutils.CheckOnBiojava;
import org.rcsb.mmtf.testutils.CheckOnRawApi;

public class TestingUtils {
	  /**
	   * Function to round trip everything based on the current params
	   * @throws IllegalAccessException
	   * @throws InvocationTargetException
	   * @throws IOException
	   * @throws StructureException
	   */
	  public void testAll(String[] inputList, FileParsingParameters params, AtomCache cache) throws IllegalAccessException, InvocationTargetException, IOException, StructureException{
		    ParsingParams mmtfParams = new ParsingParams();
		    mmtfParams.setParseInternal(params.isUseInternalChainId());
		    StructureIO.setAtomCache(cache);
		    for (String pdbId : inputList){
		    	testOne(pdbId, params, cache, mmtfParams);
		    }
	    
	  }
	  
	  /**
	   * Function just to test one structure.
	   * @param pdbId
	   * @param params
	   * @param cache
	   * @param mmtfParams
	   * @throws IllegalAccessException
	   * @throws InvocationTargetException
	   * @throws IOException
	   * @throws StructureException
	   */
	  public void testOne(String pdbId, FileParsingParameters params, AtomCache cache, ParsingParams mmtfParams) throws IllegalAccessException, InvocationTargetException, IOException, StructureException {
	      CheckOnBiojava checkEquiv = new CheckOnBiojava();
	      Structure structure = StructureIO.getStructure(pdbId);
	      checkEquiv.checkIfStructuresSame(structure,roundTripStruct(pdbId, mmtfParams, params, cache),mmtfParams);
	  }

	  /**
	   * 
	   * @return
	   * @throws IOException 
	   * @throws StructureException 
	   * @throws InvocationTargetException 
	   * @throws IllegalAccessException 
	   */
	  public Structure roundTripStruct(String pdbId, ParsingParams pp, FileParsingParameters params, AtomCache cache) throws IOException, IllegalAccessException, InvocationTargetException, StructureException{
	    // We need to set the parsing params to this
	    boolean oldValue = params.isUseInternalChainId();
	    params.setUseInternalChainId(true);
	    cache.setFileParsingParams(params);
	    StructureIO.setAtomCache(cache);
	    EncodeStructure es = new EncodeStructure();
	    Structure mmcifStruct  = StructureIO.getStructure(pdbId);
	    FileUtils.writeByteArrayToFile(new File("pathname"), es.encodeFromBiojava(mmcifStruct));
	    byte[] inArr = FileUtils.readFileToByteArray(new File("pathname"));
	    // Now do the checks on the Raw data
	    CheckOnRawApi checkRaw = new CheckOnRawApi(inArr);
	    checkRaw.checkRawDataConsistency(mmcifStruct, params);
	    // Now decode the data and return this new structure
	    MmtfStructureDecoder bjsi = new MmtfStructureDecoder();
	    DecodeStructure decodeStructure = new DecodeStructure(inArr);
	    decodeStructure.getStructFromByteArray(bjsi, pp);
	    Structure struct = bjsi.getStructure();
	    // Revert back
	    params.setUseInternalChainId(oldValue);
	    cache.setFileParsingParams(params);
	    StructureIO.setAtomCache(cache);
	    return struct;
	  }
	  

}
