package org.rcsb.mmtf.decoder;

import static org.junit.Assert.assertNotNull;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;

import org.junit.Test;
import org.rcsb.mmtf.dataholders.MmtfStructure;
import org.unitils.reflectionassert.ReflectionAssert;

/**
 * Test that the {@link DefaultDecoder} works.
 * @author Anthony Bradley
 *
 */
public class TestDefaultDecoder {

	/**
	 * Check that we decode all fields and that all getters are not null.
	 * @throws IOException an error reading in data
	 * @throws IntrospectionException an error with introspection
	 * @throws InvocationTargetException  an error with introspection
	 * @throws IllegalArgumentException  an error with introspection
	 * @throws IllegalAccessException  an error with introspection
	 */
	@Test
	public void testDecodeAllFields() throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		ClassLoader classLoader = getClass().getClassLoader();
		MmtfStructure mmtfBean = ReaderUtils.getDataFromFile(Paths.get(classLoader.getResource("mmtf/4cup.mmtf").getFile()));
		GenericDecoder defaultDecoder = new GenericDecoder(mmtfBean);
		ReflectionAssert.assertPropertiesNotNull("Some properties null after decoding", defaultDecoder);
		for(PropertyDescriptor propertyDescriptor : 
			Introspector.getBeanInfo(MmtfStructure.class).getPropertyDescriptors()){
			assertNotNull(propertyDescriptor.getReadMethod().invoke(mmtfBean));
		}
		// Check the decoder has been populated to
		for(PropertyDescriptor propertyDescriptor :
			Introspector.getBeanInfo(GenericDecoder.class).getPropertyDescriptors()){
			if(propertyDescriptor.getReadMethod()!=null){
				assertNotNull(propertyDescriptor.getReadMethod().invoke(defaultDecoder));
			}
		}
	}

	/**
	 * Test that we can pass data into the inflator.
	 */
	@Test
	public void testReader() {
		DummyApiImpl dummyApiImpl = new DummyApiImpl();
		new StructureDataToAdapter(dummyApiImpl, new DummyTransferImpl());
	}

}
