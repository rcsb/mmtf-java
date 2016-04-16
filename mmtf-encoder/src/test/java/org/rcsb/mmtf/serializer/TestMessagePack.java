package org.rcsb.mmtf.serializer;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.rcsb.mmtf.dataholders.MmtfBean;
import org.rcsb.mmtf.serializers.MessagePackSerializer;

import com.fasterxml.jackson.core.JsonProcessingException;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * A class to test the messagepack serializer.
 * @author Anthony Bradley
 *
 */
public class TestMessagePack {

	
	/**
	 * Test if we can serialize dummy data.
	 * @throws JsonProcessingException related to converting dummy data to JSON
	 */
	@Test
	public void testBasic() throws JsonProcessingException {
		MessagePackSerializer messagePackSerializer = new MessagePackSerializer();
		PodamFactory factory = new PodamFactoryImpl();
		MmtfBean mmtfBean = factory.manufacturePojo(MmtfBean.class);
		byte[] outArr = messagePackSerializer.serialize(mmtfBean);
		assertNotNull(outArr);
	}
}
