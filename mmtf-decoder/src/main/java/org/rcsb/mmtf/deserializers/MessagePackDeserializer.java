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

	public MmtfBean deserialize(byte[] byteArray) throws JsonParseException, JsonMappingException, IOException {
		MmtfBean mmtfBean = null;
		mmtfBean = new ObjectMapper(new MessagePackFactory()).readValue(byteArray, MmtfBean.class);
		return mmtfBean;
	}
}
