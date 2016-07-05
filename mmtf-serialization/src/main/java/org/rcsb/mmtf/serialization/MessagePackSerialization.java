package org.rcsb.mmtf.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.rcsb.mmtf.dataholders.MmtfStructure;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A message pack implementation of the {@link MmtfStructure} serializer / deserializer.
 * @author Anthony Bradley
 *
 */
public class MessagePackSerialization implements MmtfStructureSerializationInterface {
	
	private ObjectMapper objectMapper;
	
	/**
	 * Constructor for the {@link MessagePackSerialization} class.
	 * Generates {@link ObjectMapper} and sets to include non-null.
	 */
	public MessagePackSerialization() {
		objectMapper = new ObjectMapper(new MessagePackFactory());
		objectMapper.setSerializationInclusion(Include.NON_NULL);
	}
	
	@Override
	public MmtfStructure deserialize(InputStream inputStream){
		MmtfStructure mmtfBean = null;
		try {
			mmtfBean = objectMapper.readValue(inputStream, MmtfStructure.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mmtfBean;
	}
	
	@Override
	public void serialize(MmtfStructure mmtfStructure, OutputStream outputStream) {
		try {
			objectMapper.writeValue(outputStream, mmtfStructure);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
