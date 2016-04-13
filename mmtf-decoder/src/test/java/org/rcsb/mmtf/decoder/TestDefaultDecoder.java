package org.rcsb.mmtf.decoder;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.rcsb.mmtf.api.DataTransferInterface;
import org.rcsb.mmtf.dataholders.MmtfBean;
import org.unitils.reflectionassert.ReflectionAssert;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public class TestDefaultDecoder {

	private MmtfBean mmtfBean;
	/**
	 * Build the bean before any tests
	 */
	@Before
	public void beforeTests() {
		PodamFactory factory = new PodamFactoryImpl();
		mmtfBean = factory.manufacturePojo(MmtfBean.class);
	}
	
	
	@Test
	public void testDecodeAllFields() throws IOException {
		DefaultDecoder defaultDecoder = new DefaultDecoder(mmtfBean);
		ReflectionAssert.assertPropertiesNotNull("Some properties null after decoding", defaultDecoder);
	}
	
	
	@Test
	public void testReader() throws IOException {
		DummyApiImpl dummyApiImpl = new DummyApiImpl();
		DecoderToReader decoderToReader = new DecoderToReader();
		DataTransferInterface inputInflator = new DummyTransferImpl();
		decoderToReader.read(dummyApiImpl, inputInflator);
	}
	
	
}
