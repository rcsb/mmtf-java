package org.rcsb.mmtf.decoder;

import java.io.IOException;

import org.junit.Test;
import org.rcsb.mmtf.api.DataTransferInterface;
import org.rcsb.mmtf.dataholders.MmtfBean;
import org.unitils.reflectionassert.ReflectionAssert;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public class TestDefaultDecoder {

	@Test
	public void testDecodeAllFields() throws IOException {
		PodamFactory factory = new PodamFactoryImpl();
		MmtfBean mmtfBean = factory.manufacturePojo(MmtfBean.class);
		DefaultDecoder defaultDecoder = new DefaultDecoder(mmtfBean);
		ReflectionAssert.assertPropertiesNotNull("Some properties null after decoding", defaultDecoder);
	}


	@Test
	public void testReader() {
		DummyApiImpl dummyApiImpl = new DummyApiImpl();
		DecoderToReader decoderToReader = new DecoderToReader();
		DataTransferInterface inputInflator = new DummyTransferImpl();
		decoderToReader.read(dummyApiImpl, inputInflator);
	}


}
