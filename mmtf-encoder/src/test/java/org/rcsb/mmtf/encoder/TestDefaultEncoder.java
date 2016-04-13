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
import org.unitils.reflectionassert.ReflectionAssert;

public class TestDefaultEncoder {
	
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
	

	/**
	 * Test whether calling all the set methods gives a none null get
	 * @throws IOException
	 */
	@Test
	public void testEncodeAllFields() throws IOException {

		DummyApiImpl dummyApiImpl = new DummyApiImpl();
		DefaultEncoder defaultEncoder = new DefaultEncoder(dummyApiImpl);
		ReflectionAssert.assertPropertiesNotNull("Some properties null after encoding", defaultEncoder.getMmtfBean());
	}


	@Test
	public void testWriter() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		WriterToEncoder writerToEncoder = getWriterEncoder();
		ReflectionAssert.assertPropertiesNotNull("Some properties null after writing", writerToEncoder);
		for(PropertyDescriptor propertyDescriptor : 
			Introspector.getBeanInfo(WriterToEncoder.class).getPropertyDescriptors()){
			if(propertyDescriptor.getReadMethod()!=null){
				assertNotNull(propertyDescriptor.getReadMethod().invoke(writerToEncoder));
			}
		}
	}


	@Test
	public void testWriteToFile() throws IOException {
		WriterToEncoder writerToEncoder = getWriterEncoder();
		File tempFile = testFolder.newFile("tmpfile");
		WriterUtils.writeDataToFile(writerToEncoder, tempFile.getAbsolutePath());
	}
	
	
	
	private WriterToEncoder getWriterEncoder() {
		WriterToEncoder writerToEncoder = new WriterToEncoder();
		writerToEncoder.initStructure(1, 1, 1, 1, 1, "ABC");
		writerToEncoder.setModelInfo(0, 1);
		writerToEncoder.setChainInfo("A","A",10);
		writerToEncoder.setGroupInfo("HET", 1, 'a', "D", 1, 1, 'A', 0, -1);
		writerToEncoder.setAtomInfo("A", 1, 'a', 1.0f, 1.0f, 1.0f,1.0f, 1.0f, "A", 1);
		writerToEncoder.setEntityInfo(new int[1], "A", "A", "A");
		writerToEncoder.setGroupBond(0, 0, 1);
		writerToEncoder.setHeaderInfo(1.0f, 1.0f, 1.0f, "A", "A", "A", new String[1]);
		writerToEncoder.setInterGroupBond(0, 0, 1);
		writerToEncoder.setBioAssemblyTrans(0, new int[1], new double[6]);
		writerToEncoder.setXtalInfo("A", new float[6]);
		writerToEncoder.finalizeStructure();
		return writerToEncoder;
	}
}
