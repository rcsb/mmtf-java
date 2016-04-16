package org.rcsb.mmtf.deserializers;

import java.io.IOException;

import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.rcsb.mmtf.dataholders.MmtfBean;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A class to deserialize messaepack data into an mmtfBean
 * @author Anthony Bradley
 *
 */
public class MessagePackDeserializer {

	/**
	 * Deserialize the byte array into an mmtfBean.
	 * @param byteArray the input data as a byte array.
	 * @return the deserialized data as an mmtfbean.
	 * @throws JsonParseException an error converting the byte array to the bean
	 * @throws JsonMappingException an error converting the byte array to the bean
	 * @throws IOException an error working with the byte arrayq
	 */
	public MmtfBean deserialize(byte[] byteArray) throws JsonParseException, JsonMappingException, IOException {
		MmtfBean mmtfBean = null;
		mmtfBean = new ObjectMapper(new MessagePackFactory()).readValue(byteArray, MmtfBean.class);
		return mmtfBean;
	}
}
