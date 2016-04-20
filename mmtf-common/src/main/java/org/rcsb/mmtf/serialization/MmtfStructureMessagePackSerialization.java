package org.rcsb.mmtf.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.rcsb.mmtf.dataholders.MmtfStructure;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A message pack implementation of the mmtf serializer / deserializer.
 * @author Anthony Bradley
 *
 */
public class MmtfStructureMessagePackSerialization implements MmtfStructureSerializationInterface {
	
	@Override
	public MmtfStructure deserialize(InputStream byteArray){
		MmtfStructure mmtfBean = null;
		try {
			mmtfBean = new ObjectMapper(new MessagePackFactory()).readValue(byteArray, MmtfStructure.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mmtfBean;
	}
	
	@Override
	public void serialize(MmtfStructure object, OutputStream outputStream) {
		ObjectMapper objectMapper = new ObjectMapper(new MessagePackFactory());
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		try {
			objectMapper.writeValue(outputStream, object);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
