package org.rcsb.mmtf.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;

import org.rcsb.mmtf.dataholders.MmtfStructure;

/**
 * An interface to carry out serializing / deserializing to
 * {@link MmtfStructure}.
 *
 * @author Anthony Bradley
 *
 */
public interface MmtfStructureSerializationInterface {

	/**
	 * Serialize an {@link MmtfStructure} to a generic output stream.
	 *
	 * @param mmtfStructure the compressed data
	 * @param outputStream the output stream to write to
	 */
	public void serialize(MmtfStructure mmtfStructure,
		OutputStream outputStream) throws IOException;

	/**
	 * Deserialize an {@link MmtfStructure} from an input stream.
	 *
	 * @param inputStream the inputstream to deserialize
	 * @return the compressed structure data.
	 */
	public MmtfStructure deserialize(InputStream inputStream)
		throws ParseException, IOException;

}
