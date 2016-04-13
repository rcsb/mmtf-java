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

public class TestDefaultDecoder {

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
	}


	@Test
	public void testReader() {
		DummyApiImpl dummyApiImpl = new DummyApiImpl();
		DecoderToReader decoderToReader = new DecoderToReader();
		DataTransferInterface inputInflator = new DummyTransferImpl();
		decoderToReader.read(dummyApiImpl, inputInflator);
	}


}
