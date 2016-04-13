package org.rcsb.mmtf.serializer;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.rcsb.mmtf.dataholders.MmtfBean;
import org.rcsb.mmtf.serializers.MessagePackSerializer;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public class TestMessagePack {

	
	@Test
	public void testBasic() {
		MessagePackSerializer messagePackSerializer = new MessagePackSerializer();
		PodamFactory factory = new PodamFactoryImpl();
		MmtfBean mmtfBean = factory.manufacturePojo(MmtfBean.class);
		byte[] outArr = messagePackSerializer.serialize(mmtfBean);
		assertNotNull(outArr);
	}
}
