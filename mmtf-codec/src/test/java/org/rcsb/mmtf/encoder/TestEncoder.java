package org.rcsb.mmtf.encoder;

import static org.junit.Assert.assertNotNull;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.rcsb.mmtf.api.StructureAdapterInterface;
import org.unitils.reflectionassert.ReflectionAssert;

/**
 * A class to test the encoding of data using the {@link DefaultEncoder}.
 * @author Anthony Bradley
 *
 */
public class TestEncoder {
	
    /**
     * A test folder for testing writing files.
     */
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
	

	/**
	 * Test whether calling all the set methods gives a none null get.
	 * @throws IOException an error converting byte arrays
	 */
	@Test
	public void testDefaultEncoder() throws IOException {
		DummyApiImpl dummyApiImpl = new DummyApiImpl();
		EncoderInterface encoder = new DefaultEncoder(dummyApiImpl);
		ReflectionAssert.assertPropertiesNotNull("Some properties null after encoding",  encoder.getMmtfEncodedStructure());
	}

	
	/**
	 * Test whether calling all the set methods gives a none null get.
	 * @throws IOException an error converting byte arrays
	 */
	@Test
	public void testReducedEncoder() throws IOException {
		DummyApiImpl dummyApiImpl = new DummyApiImpl();
		EncoderInterface encoder = new ReducedEncoder(dummyApiImpl);
		ReflectionAssert.assertPropertiesNotNull("Some properties null after encoding",  encoder.getMmtfEncodedStructure());
	}
	
	
	/**
	 * Test whether calling all the set methods gives a none null get.
	 * @throws IOException an error converting byte arrays
	 */
	@Test
	public void testGenricEncoder() throws IOException {
		DummyApiImpl dummyApiImpl = new DummyApiImpl();
		EncoderInterface encoder = new GenericEncoder(dummyApiImpl);
		ReflectionAssert.assertPropertiesNotNull("Some properties null after encoding",  encoder.getMmtfEncodedStructure());
	}


	/**
	 * Test the writer of data.
	 * @throws IllegalAccessException reflection based error
	 * @throws IllegalArgumentException reflection based error
	 * @throws InvocationTargetException reflection based error
	 * @throws IntrospectionException reflection based error
	 */
	@Test
	public void testWriter() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		AdapterToStructureData adapterToStructureData = getWriterEncoder();
		ReflectionAssert.assertPropertiesNotNull("Some properties null after writing", adapterToStructureData);
		for(PropertyDescriptor propertyDescriptor : 
			Introspector.getBeanInfo(AdapterToStructureData.class).getPropertyDescriptors()){
			if(propertyDescriptor.getReadMethod()!=null){
				assertNotNull(propertyDescriptor.getReadMethod().invoke(adapterToStructureData));
			}
		}
	}


	/**
	 * Test writing the data to a file.
	 * @throws IOException an error accessing the file system
	 */
	@Test
	public void testWriteToFile() throws IOException {
		AdapterToStructureData AdapterToStructureData = getWriterEncoder();
		File tempFile = testFolder.newFile("tmpfile");
		WriterUtils.writeDataToFile(AdapterToStructureData, tempFile.toPath());
	}
	
	
	/**
	 * Utility function for getting the base data into the {@link AdapterToStructureData} 
	 * implementation of the {@link StructureAdapterInterface}.
	 * @return the {@link AdapterToStructureData} instance
	 */
	private AdapterToStructureData getWriterEncoder() {
		AdapterToStructureData adapterToStructureData = new AdapterToStructureData();
		adapterToStructureData.initStructure(1, 1, 1, 1, 1, "ABC");
		adapterToStructureData.setModelInfo(0, 1);
		adapterToStructureData.setChainInfo("A","A",10);
		adapterToStructureData.setGroupInfo("HET", 1, 'a', "D", 1, 1, 'A', 0, -1);
		adapterToStructureData.setAtomInfo("A", 1, 'a', 1.0f, 1.0f, 1.0f,1.0f, 1.0f, "A", 1);
		adapterToStructureData.setEntityInfo(new int[1], "A", "A", "A");
		adapterToStructureData.setGroupBond(0, 0, 1);
		adapterToStructureData.setHeaderInfo(1.0f, 1.0f, 1.0f, "A", "A", "A", new String[1]);
		adapterToStructureData.setInterGroupBond(0, 0, 1);
		adapterToStructureData.setBioAssemblyTrans(0, new int[1], new double[6]);
		adapterToStructureData.setXtalInfo("A", new float[6]);
		adapterToStructureData.finalizeStructure();
		return adapterToStructureData;
	}
}
