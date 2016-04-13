package org.rcsb.mmtf.sedeserializers;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.Test;
import org.rcsb.mmtf.dataholders.MmtfBean;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public class TestMessagePack {

	
	@Test
	public void testDeserialize()  {
		MmtfBeanSeDeMessagePackImpl mmtfBeanSeDeMessagePackImpl = new MmtfBeanSeDeMessagePackImpl();
		byte[] source = new byte[] {(byte) (char) 129, (byte) (char)162, (byte) (char)100, (byte) (char)111, (byte) (char)1};
		ByteArrayInputStream bis = new ByteArrayInputStream(source);
		MmtfBean mmtfBean = mmtfBeanSeDeMessagePackImpl.deserialize(bis);
		assertNotNull(mmtfBean);
	}
	
	@Test
	public void testSerialize() {
		MmtfBeanSeDeMessagePackImpl mmtfBeanSeDeMessagePackImpl = new MmtfBeanSeDeMessagePackImpl();
		PodamFactory factory = new PodamFactoryImpl();
		MmtfBean mmtfBean = factory.manufacturePojo(MmtfBean.class);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		mmtfBeanSeDeMessagePackImpl.serialize(mmtfBean, bos);
		assertNotNull(bos.toByteArray());
	}
}
