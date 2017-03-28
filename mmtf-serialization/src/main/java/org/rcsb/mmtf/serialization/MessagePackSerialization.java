package org.rcsb.mmtf.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.rcsb.mmtf.dataholders.MmtfStructure;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.DataInputStream;
import java.util.Map;
import org.rcsb.mmtf.dataholders.MmtfStructureFactory;
import org.rcsb.mmtf.serialization.quickmessagepackdeserialization.MessagePackReader;
import org.rcsb.mmtf.serialization.quickmessagepackdeserialization.ObjectTree;

/**
 * A message pack implementation of the {@link MmtfStructure} serializer / deserializer.
 *
 * @author Anthony Bradley
 * @author Antonin Pavelka
 *
 */
public class MessagePackSerialization implements MmtfStructureSerializationInterface {

	private ObjectMapper objectMapper;
	private static boolean useJackson = false;

	/**
	 * Constructor for the {@link MessagePackSerialization} class. Generates {@link ObjectMapper}
	 * and sets to include non-null.
	 */
	public MessagePackSerialization() {
		objectMapper = new ObjectMapper(new MessagePackFactory());
		objectMapper.setSerializationInclusion(Include.NON_NULL);
	}

	public static void setJackson(boolean allowed) {
		useJackson = allowed;
	}

	@Override
	public MmtfStructure deserialize(InputStream inputStream)
		throws IOException {
		if (useJackson) {
			return deserializeByJackson(inputStream);
		} else {
			return deserializeQuick(inputStream);
		}
	}

	/**
	 * Elegant, but slow (comparable to unzipping).
	 */
	private MmtfStructure deserializeByJackson(InputStream inputStream) throws IOException {
		return objectMapper.readValue(inputStream, MmtfStructure.class);
	}

	/**
	 * Several times faster.
	 */
	private MmtfStructure deserializeQuick(InputStream inputStream)
		throws IOException {
		MessagePackReader mpr = new MessagePackReader(new DataInputStream(inputStream), true);
		Map<String, Object> map = mpr.readMap();
		MmtfStructureFactory f = new MmtfStructureFactory();
		MmtfStructure s = f.create(new ObjectTree(map));
		return s;
	}

	@Override
	public void serialize(MmtfStructure mmtfStructure,
		OutputStream outputStream) throws IOException {
		objectMapper.writeValue(outputStream, mmtfStructure);
	}

}
