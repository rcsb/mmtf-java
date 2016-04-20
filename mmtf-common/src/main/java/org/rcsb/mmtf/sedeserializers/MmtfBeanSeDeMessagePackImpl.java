package org.rcsb.mmtf.sedeserializers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.rcsb.mmtf.dataholders.MmtfBean;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A message pack implementation of the mmtf serializer / deserializer
 * @author Anthony Bradley
 *
 */
public class MmtfBeanSeDeMessagePackImpl implements MmtfBeanSeDerializerInterface {
	
	@Override
	public MmtfBean deserialize(InputStream byteArray){
		MmtfBean mmtfBean = null;
		try {
			mmtfBean = new ObjectMapper(new MessagePackFactory()).readValue(byteArray, MmtfBean.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mmtfBean;
	}
	
	@Override
	public void serialize(MmtfBean object, OutputStream outputStream) {
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
