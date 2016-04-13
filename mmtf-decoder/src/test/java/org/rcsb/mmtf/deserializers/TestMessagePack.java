package org.rcsb.mmtf.deserializers;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;
import org.rcsb.mmtf.dataholders.MmtfBean;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class TestMessagePack {

	
	@Test
	public void testBasic() throws JsonParseException, JsonMappingException, IOException {
		MessagePackDeserializer messagePackDeserializer = new  MessagePackDeserializer();
		MmtfBean mmtfBean = messagePackDeserializer.deserialize(new byte[] {(byte) (char) 129, (byte) (char)162, (byte) (char)100, (byte) (char)111, (byte) (char)1});
		assertNotNull(mmtfBean);
	}
}
