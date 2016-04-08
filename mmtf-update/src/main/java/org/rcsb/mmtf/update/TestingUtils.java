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
import org.biojava.nbio.structure.io.mmtf.MmtfStructureReader;
import org.rcsb.mmtf.decoder.GetToInflator;
import org.rcsb.mmtf.encoder.GetToBean;
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
		    StructureIO.setAtomCache(cache);
		    for (String pdbId : inputList){
		    	testOne(pdbId, params, cache);
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
	  public void testOne(String pdbId, FileParsingParameters params, AtomCache cache) throws IllegalAccessException, InvocationTargetException, IOException, StructureException {
	      CheckOnBiojava checkEquiv = new CheckOnBiojava();
	      Structure structure = StructureIO.getStructure(pdbId);
	      checkEquiv.checkIfStructuresSame(structure,roundTripStruct(pdbId, params, cache));
	  }

	  /**
	   * 
	   * @return
	   * @throws IOException 
	   * @throws StructureException 
	   * @throws InvocationTargetException 
	   * @throws IllegalAccessException 
	   */
	  public Structure roundTripStruct(String pdbId, FileParsingParameters params, AtomCache cache) throws IOException, IllegalAccessException, InvocationTargetException, StructureException{
	    // We need to set the parsing params to this
	    boolean oldValue = params.isUseInternalChainId();
	    params.setUseInternalChainId(true);
	    cache.setFileParsingParams(params);
	    StructureIO.setAtomCache(cache);
	    GetToBean es = new GetToBean();
	    Structure mmcifStruct = StructureIO.getStructure(pdbId);
	    FileUtils.writeByteArrayToFile(new File("pathname"), es.encodeFromPdbId(pdbId, new BiojavaEncoderImpl()));
	    byte[] inArr = FileUtils.readFileToByteArray(new File("pathname"));
	    // Now do the checks on the Raw data
	    CheckOnRawApi checkRaw = new CheckOnRawApi(inArr);
	    checkRaw.checkRawDataConsistency(mmcifStruct, params);
	    // Now decode the data and return this new structure
	    MmtfStructureReader bjsi = new MmtfStructureReader();
	    GetToInflator decodeStructure = new GetToInflator(inArr);
	    decodeStructure.getStructFromByteArray(bjsi);
	    Structure struct = bjsi.getStructure();
	    // Revert back
	    params.setUseInternalChainId(oldValue);
	    cache.setFileParsingParams(params);
	    StructureIO.setAtomCache(cache);
	    return struct;
	  }
	  

}
