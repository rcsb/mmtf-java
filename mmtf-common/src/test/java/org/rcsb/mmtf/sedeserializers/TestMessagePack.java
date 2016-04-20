package org.rcsb.mmtf.sedeserializers;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.Test;
import org.rcsb.mmtf.dataholders.MmtfEncodedStructure;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Class to test the message pack serializer and deserializer.
 * @author Anthony Bradley
 *
 */
public class TestMessagePack {

	
	/**
	 * Test the deserialize.
	 */
	@Test
	public void testDeserialize()  {
		MmtfBeanSeDeMessagePackImpl mmtfBeanSeDeMessagePackImpl = new MmtfBeanSeDeMessagePackImpl();
		byte[] source = new byte[] {(byte) (char) 129, (byte) (char)162, (byte) (char)100, (byte) (char)111, (byte) (char)1};
		ByteArrayInputStream bis = new ByteArrayInputStream(source);
		MmtfEncodedStructure mmtfBean = mmtfBeanSeDeMessagePackImpl.deserialize(bis);
		assertNotNull(mmtfBean);
	}
	
	/**
	 * Test the serialize.
	 */
	@Test
	public void testSerialize() {
		MmtfBeanSeDeMessagePackImpl mmtfBeanSeDeMessagePackImpl = new MmtfBeanSeDeMessagePackImpl();
		PodamFactory factory = new PodamFactoryImpl();
		MmtfEncodedStructure mmtfBean = factory.manufacturePojo(MmtfEncodedStructure.class);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		mmtfBeanSeDeMessagePackImpl.serialize(mmtfBean, bos);
		assertNotNull(bos.toByteArray());
	}
}
