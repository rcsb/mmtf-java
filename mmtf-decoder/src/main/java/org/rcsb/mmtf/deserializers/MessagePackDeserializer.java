package org.rcsb.mmtf.deserializers;

import java.io.IOException;

import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.rcsb.mmtf.api.DeserializerInterface;
import org.rcsb.mmtf.dataholders.MmtfBean;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A class to deserialize messaepack data into an mmtfBean
 * @author Anthony Bradley
 *
 */
public class MessagePackDeserializer implements DeserializerInterface {

	@Override
	public MmtfBean deserialize(byte[] byteArray) {
		MmtfBean mmtfBean = null;
		try {
			mmtfBean = new ObjectMapper(new MessagePackFactory()).readValue(byteArray, MmtfBean.class);
		} catch (IOException e) {
			System.err.println("Error converting Byte array to message pack. IOError");
			e.printStackTrace();
			throw new RuntimeException();
		}
		return mmtfBean;
	}
}
