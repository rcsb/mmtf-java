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
import org.biojava.nbio.structure.io.mmtf.BioJavaStructureDecoder;
import org.rcsb.mmtf.biojavaencoder.EncodeStructure;
import org.rcsb.mmtf.decoder.DecodeStructure;
import org.rcsb.mmtf.decoder.ParsingParams;
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

	    ParsingParams pp = new ParsingParams();
	    pp.setParseInternal(params.isUseInternalChainId());
	    StructureIO.setAtomCache(cache);
	    for (String pdbId : inputList){
	      CheckOnBiojava checkEquiv = new CheckOnBiojava();
	      checkEquiv.checkIfStructuresSame(StructureIO.getStructure(pdbId),roundTripStruct(pdbId, pp, params, cache));
	    }
	    
	  }

	  /**
	   * 
	   * @return
	   * @throws IOException 
	   * @throws StructureException 
	   * @throws InvocationTargetException 
	   * @throws IllegalAccessException 
	   */
	  private Structure roundTripStruct(String pdbId, ParsingParams pp, FileParsingParameters params, AtomCache cache) throws IOException, IllegalAccessException, InvocationTargetException, StructureException{
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
	    BioJavaStructureDecoder bjsi = new BioJavaStructureDecoder();
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
