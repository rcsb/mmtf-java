package org.rcsb.mmtf.integrationtest;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.StructureIO;
import org.biojava.nbio.structure.align.util.AtomCache;
import org.biojava.nbio.structure.io.FileParsingParameters;
import org.junit.Test;
import org.rcsb.mmtf.biojavaencoder.EncoderUtils;
import org.rcsb.mmtf.update.IntegrationTestUtils;
import org.rcsb.mmtf.update.TestingUtils;

public class TestParseMMCif {

  private AtomCache cache;
  private FileParsingParameters params;
  private TestingUtils testingUtils = new TestingUtils();

  public TestParseMMCif(){
		// Set up the atom cache etc
	  	EncoderUtils encoderUtils = new EncoderUtils();
	  	cache = encoderUtils.setUpBioJava();
	  	params = cache.getFileParsingParams();
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
