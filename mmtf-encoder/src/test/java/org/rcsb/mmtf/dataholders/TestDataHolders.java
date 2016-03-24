package org.rcsb.mmtf.dataholders;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.align.util.AtomCache;
import org.biojava.nbio.structure.io.FileParsingParameters;
import org.biojava.nbio.structure.io.LocalPDBDirectory.FetchBehavior;
import org.biojava.nbio.structure.io.mmcif.ChemCompGroupFactory;
import org.biojava.nbio.structure.io.mmcif.DownloadChemCompProvider;
import org.biojava.nbio.structure.quaternary.BioAssemblyInfo;
import org.junit.Test;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.rcsb.mmtf.biojavaencoder.EncoderUtils;
import org.rcsb.mmtf.biojavaencoder.ParseFromBiojava;
import org.unitils.reflectionassert.ReflectionAssert;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public class TestDataHolders {

  private EncoderUtils encoderUtils;
  private PodamFactory factory;
  private AtomCache cache;
  private FileParsingParameters params;

  public TestDataHolders() {
    encoderUtils = new EncoderUtils();
    factory = new PodamFactoryImpl(); 
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
  }

  @Test
  public void testSerializable() throws JsonParseException, JsonMappingException, IOException {
    // MmtfBean
    assertTrue(testClass(MmtfBean.class));
    // This one fails - make sure it still does
    assertFalse(testClass(BioAssemblyInfo.class));
    // The bean to store calpha data
    assertTrue(testClass(CalphaDistBean.class));
    // The bean to store the calphaalign data in
    assertTrue(testClass(CalphaAlignBean.class));
    // Now consider the no float data stucuture class
    assertTrue(testClass(NoFloatDataStruct.class));
    // Now consider the no bio data structure bean
    assertTrue(testClass(BioDataStructBean.class));
    // And the calpha bean
    assertTrue(testClass(CalphaBean.class));
    // The old matrix data - this should fail
    assertFalse(testClass(Matrix4d.class));
    // Now test round tripping data
    testDataRoundTrip(MmtfBean.class);
    // Now test if all fields in the mmtf are generated
    testDataComplete("4cup");
    // Now check that the failure bean fails this
    // Now test round tripping data
    assertFalse(testDataRoundTrip(FailureBean.class));
  }

  @SuppressWarnings("unchecked")
  private boolean testClass(@SuppressWarnings("rawtypes") Class class1) throws IOException {


    Object inBean = null;
    try {
      inBean = class1.newInstance();
    } catch (InstantiationException | IllegalAccessException e2) {
      // Weirdness
      org.junit.Assert.fail("Weirdness in generating instance of generic class");
    }
    byte[] outArr = null;

    outArr = encoderUtils.getMessagePack(inBean);

    // 
    Object  outBean = null;
    try {
      outBean = new ObjectMapper(new  MessagePackFactory()).readValue(outArr, class1);
    } catch( JsonMappingException jsonE){
      System.out.println("Error reading messagepack - is part of test if test doesn't fail");
      return false;
    }

    // Now check they're the same
    ReflectionAssert.assertReflectionEquals(inBean, outBean); 
    return true;
  }

  @SuppressWarnings("unchecked")
  /**
   * Test round tripping dummy data
   * @param class1
   */
  private  boolean testDataRoundTrip(@SuppressWarnings("rawtypes") Class class1) throws JsonParseException, JsonMappingException, IOException {
    Object inBean = factory.manufacturePojo(class1);
    byte[] outArr = null;

    outArr = encoderUtils.getMessagePack(inBean);


    // 
    Object  outBean = null;
    outBean = new ObjectMapper(new  MessagePackFactory()).readValue(outArr, class1);

    // Make the failure bean fail
    try{
      ReflectionAssert.assertPropertyReflectionEquals("fieldWithNoGetters",null, outBean);
      ReflectionAssert.assertPropertyReflectionEquals("fieldWithRefactoredGetters",null, outBean);
      return false;
    }
    catch(Exception e){

    }
    // Make sure all fields are re-populated
    ReflectionAssert.assertPropertiesNotNull("Some properties are null in re-read object", outBean);

    // Now check they're the same
    ReflectionAssert.assertReflectionEquals(inBean, outBean); 
    return true;
  } 

  /**
   * A specific mmtf test - to make sure none of the fields are empty when the thing is encoded
   * @throws StructureException 
   * @throws IOException 
   */
  private void testDataComplete(String pdbId) throws IOException {

    // Utility functions for encoding stuff
    EncoderUtils eu = new EncoderUtils();
    // Get the utility class to get the structures
    ParseFromBiojava parsedDataStruct = new ParseFromBiojava();
    Map<Integer, PDBGroup> totMap = new HashMap<Integer, PDBGroup>();
    // Parse the data into the basic data structure
    parsedDataStruct.createFromJavaStruct(pdbId, totMap);
    MmtfBean mmtfBean = null;
    // Compress the data and get it back out
    mmtfBean = eu.compressMainData(parsedDataStruct.getBioStruct(), parsedDataStruct.getHeaderStruct());
    // Make sure all fields are re-populated
    ReflectionAssert.assertPropertiesNotNull("Some properties are null in mmtf generated from biojava object",  mmtfBean);
    // Now check the standard ones have been set
    assertNotEquals(mmtfBean.getResolution(), (float) -1.0);
    assertNotEquals(mmtfBean.getrFree(), (float) -1.0);
    // Check that these integer values are set
    assertNotEquals(mmtfBean.getNumAtoms(), -1);
    assertNotEquals(mmtfBean.getNumBonds(), -1);
    // And finally - check this is working
    assertNotEquals(mmtfBean.getMmtfProducer(), "NA");
  }
}



