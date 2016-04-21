package org.rcsb.mmtf.serialization;

import java.io.InputStream;
import java.io.OutputStream;

import org.rcsb.mmtf.dataholders.MmtfStructure;

/**
 * An interface to carry out serializing / deserializing to {@link MmtfStructure}.
 * @author Anthony Bradley
 *
 */
public interface MmtfStructureSerializationInterface {

	/**
	 * Serialize an {@link MmtfStructure} to a generic output stream.
	 * @param mmtfStructure the compressed data
	 * @param outputStream the output stream to write to
	 */
	public void serialize(MmtfStructure mmtfStructure, OutputStream outputStream);
	
	/**
	 * Deserialize an {@link MmtfStructure} from an input stream.
	 * @param inputStream the inputstream to deserialize
	 * @return the compressed structure data.
	 */
	public MmtfStructure deserialize(InputStream inputStream);

}
