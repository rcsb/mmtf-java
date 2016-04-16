package org.rcsb.mmtf.deserializers;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;
import org.rcsb.mmtf.dataholders.MmtfBean;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * Test the messagepack deserializer implementation.
 * @author Anthony Bradley
 *
 */
public class TestMessagePack {

	
	/**
	 * Test that we can deserialize dummy data.
	 * @throws JsonParseException an error in the JSON parser
	 * @throws JsonMappingException a JSON mapping error. (Jackson)
	 * @throws IOException an error with the byte array.
	 */
	@Test
	public void testBasic() throws JsonParseException, JsonMappingException, IOException {
		MessagePackDeserializer messagePackDeserializer = new  MessagePackDeserializer();
		MmtfBean mmtfBean = messagePackDeserializer.deserialize(new byte[] {(byte) (char) 129, (byte) (char)162, (byte) (char)100, (byte) (char)111, (byte) (char)1});
		assertNotNull(mmtfBean);
	}
}
