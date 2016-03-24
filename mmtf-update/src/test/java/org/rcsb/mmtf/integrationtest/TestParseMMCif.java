package org.rcsb.mmtf.integrationtest;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.io.FileUtils;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.StructureIO;
import org.biojava.nbio.structure.align.util.AtomCache;
import org.biojava.nbio.structure.io.FileParsingParameters;
import org.biojava.nbio.structure.io.LocalPDBDirectory.FetchBehavior;
import org.biojava.nbio.structure.io.mmcif.ChemCompGroupFactory;
import org.biojava.nbio.structure.io.mmcif.DownloadChemCompProvider;
import org.biojava.nbio.structure.io.mmtf.BioJavaStructureDecoder;
import org.junit.Test;
import org.rcsb.mmtf.biojavaencoder.EncodeStructure;
import org.rcsb.mmtf.decoder.DecodeStructure;
import org.rcsb.mmtf.decoder.ParsingParams;
import org.rcsb.mmtf.testutils.CheckOnBiojava;
import org.rcsb.mmtf.testutils.CheckOnRawApi;

public class TestParseMMCif {

  private DecodeStructure decodeStructure;
  private AtomCache cache;
  private FileParsingParameters params;
  private CheckOnBiojava checkEquiv;

  public TestParseMMCif(){
    cache = new AtomCache();
    cache.setUseMmCif(true);
    cache.setFetchBehavior(FetchBehavior.FETCH_FILES);
    params = cache.getFileParsingParams();
    params.setCreateAtomBonds(true);
    params.setAlignSeqRes(true);
    params.setParseBioAssembly(true);
    DownloadChemCompProvider dcc = new DownloadChemCompProvider();
    ChemCompGroupFactory.setChemCompProvider(dcc);
    dcc.setDownloadAll(true);
    dcc.checkDoFirstInstall();
    params.setUseInternalChainId(true);
    checkEquiv = new CheckOnBiojava();
  }


  @Test
  public void testAsymChainIds() throws IOException, StructureException, IllegalAccessException, InvocationTargetException {
    // Set the params
    params.setUseInternalChainId(true);
    cache.setFileParsingParams(params);
    StructureIO.setAtomCache(cache);
    testAll();
  }

  @Test
  public void testAuthChainIds() throws IOException, StructureException, IllegalAccessException, InvocationTargetException {
    // Set the params
    params.setUseInternalChainId(false);
    cache.setFileParsingParams(params);
    StructureIO.setAtomCache(cache);
    testAll();
  }


  /**
   * Function to round trip everything based on the current params
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   * @throws IOException
   * @throws StructureException
   */
  private void testAll() throws IllegalAccessException, InvocationTargetException, IOException, StructureException{

    ParsingParams pp = new ParsingParams();
    pp.setParseInternal(params.isUseInternalChainId());
    StructureIO.setAtomCache(cache);
    for (String pdbId : IntegrationTestUtils.TEST_CASES){
      checkEquiv = new CheckOnBiojava();
      checkEquiv.checkIfStructuresSame(StructureIO.getStructure(pdbId),roundTripStruct(pdbId, pp));
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
  private Structure roundTripStruct(String pdbId, ParsingParams pp) throws IOException, IllegalAccessException, InvocationTargetException, StructureException{
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
    decodeStructure = new DecodeStructure(inArr);
    decodeStructure.getStructFromByteArray(bjsi, pp);
    Structure struct = bjsi.getStructure();
    // Revert back
    params.setUseInternalChainId(oldValue);
    cache.setFileParsingParams(params);
    StructureIO.setAtomCache(cache);
    return struct;
  }


}
