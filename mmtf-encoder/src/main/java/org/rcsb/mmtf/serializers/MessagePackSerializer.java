package org.rcsb.mmtf.serializers;

import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.rcsb.mmtf.api.SerializerInterface;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A class to serialize an object to a byte array.
 * The byte array accords to message pack.
 * @author Anthony Bradley
 *
 */
public class MessagePackSerializer implements SerializerInterface {

	@Override
	public byte[] serialize(Object object) {
		ObjectMapper objectMapper = new ObjectMapper(new MessagePackFactory());
		byte[] byteArray;
		try {
			byteArray = objectMapper.writeValueAsBytes(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			byteArray = new byte[0];
		}
		return byteArray;
	}
}
