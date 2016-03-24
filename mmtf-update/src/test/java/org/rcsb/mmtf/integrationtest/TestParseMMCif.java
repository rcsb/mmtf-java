package org.rcsb.mmtf.integrationtest;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.StructureIO;
import org.biojava.nbio.structure.align.util.AtomCache;
import org.biojava.nbio.structure.io.FileParsingParameters;
import org.biojava.nbio.structure.io.mmcif.ChemCompGroupFactory;
import org.biojava.nbio.structure.io.mmcif.DownloadChemCompProvider;
import org.junit.Test;
import org.rcsb.mmtf.update.TestingUtils;

public class TestParseMMCif {

  private AtomCache cache;
  private FileParsingParameters params;
  private TestingUtils testingUtils = new TestingUtils();

  public TestParseMMCif(){
		// Set up the atom cache etc
	  
	  // TODO once https://github.com/rcsb/mmtf-java/issues/2 has been resolved use the encoder utils instead
		cache = new AtomCache();
		cache.setUseMmCif(true);
		params = cache.getFileParsingParams();
		params.setCreateAtomBonds(true);
		params.setAlignSeqRes(true);
		params.setParseBioAssembly(true);
		params.setUseInternalChainId(true);
		DownloadChemCompProvider cc = new DownloadChemCompProvider();
		ChemCompGroupFactory.setChemCompProvider(cc);
		cc.checkDoFirstInstall();
		cache.setFileParsingParams(params);
		StructureIO.setAtomCache(cache);
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


}
