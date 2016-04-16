package org.rcsb.mmtf.decoder;

import static org.junit.Assert.assertNotNull;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.rcsb.mmtf.api.DataTransferInterface;
import org.rcsb.mmtf.dataholders.MmtfBean;
import org.unitils.reflectionassert.ReflectionAssert;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Test that the defualt decoder works.
 * @author Anthony Bradley
 *
 */
public class TestDefaultDecoder {

	/**
	 * Check that we decode all fields and that all getters are not null.
	 * @throws IOException an error reading in data
	 * @throws IntrospectionException an error doing reflection
	 * @throws IllegalAccessException an error doing reflection
	 * @throws IllegalArgumentException an error doing reflection
	 * @throws InvocationTargetException an error doing reflection
	 */
	@Test
	public void testDecodeAllFields() throws IOException, IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		PodamFactory factory = new PodamFactoryImpl();
		MmtfBean mmtfBean = factory.manufacturePojo(MmtfBean.class);
		DefaultDecoder defaultDecoder = new DefaultDecoder(mmtfBean);
		ReflectionAssert.assertPropertiesNotNull("Some properties null after decoding", defaultDecoder);
		for(PropertyDescriptor propertyDescriptor : 
			Introspector.getBeanInfo(MmtfBean.class).getPropertyDescriptors()){
			assertNotNull(propertyDescriptor.getReadMethod().invoke(mmtfBean));
		}
		// Check the decoder has been populated to
		for(PropertyDescriptor propertyDescriptor :
			Introspector.getBeanInfo(DefaultDecoder.class).getPropertyDescriptors()){
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
		DecoderToReader decoderToReader = new DecoderToReader();
		DataTransferInterface inputInflator = new DummyTransferImpl();
		decoderToReader.read(dummyApiImpl, inputInflator);
	}

}
