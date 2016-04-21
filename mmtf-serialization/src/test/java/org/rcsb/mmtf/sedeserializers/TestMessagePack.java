package org.rcsb.mmtf.sedeserializers;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.Test;
import org.rcsb.mmtf.dataholders.MmtfStructure;
import org.rcsb.mmtf.serialization.MmtfStructureMessagePackSerialization;

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
		MmtfStructureMessagePackSerialization mmtfBeanSeDeMessagePackImpl = new MmtfStructureMessagePackSerialization();
		byte[] source = new byte[] {(byte) (char) 129, (byte) (char)162, (byte) (char)100, (byte) (char)111, (byte) (char)1};
		ByteArrayInputStream bis = new ByteArrayInputStream(source);
		MmtfStructure mmtfBean = mmtfBeanSeDeMessagePackImpl.deserialize(bis);
		assertNotNull(mmtfBean);
	}
	
	/**
	 * Test the serialize.
	 */
	@Test
	public void testSerialize() {
		MmtfStructureMessagePackSerialization mmtfBeanSeDeMessagePackImpl = new MmtfStructureMessagePackSerialization();
		PodamFactory factory = new PodamFactoryImpl();
		MmtfStructure mmtfBean = factory.manufacturePojo(MmtfStructure.class);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		mmtfBeanSeDeMessagePackImpl.serialize(mmtfBean, bos);
		assertNotNull(bos.toByteArray());
	}
}
