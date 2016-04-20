package org.rcsb.mmtf.sedeserializers;

import java.io.InputStream;
import java.io.OutputStream;

import org.rcsb.mmtf.dataholders.MmtfEncodedStructure;

/**
 * An interface to carry out serializing / deserializing to mmtfBean.
 * @author Anthony Bradley
 *
 */
public interface MmtfBeanSeDerializerInterface {

	/**
	 * Serialize an mmtfBean to a generic output stream.
	 * @param mmtfBean the compressed data
	 * @param outputStream the ouput stream to write to
	 */
	public void serialize(MmtfEncodedStructure mmtfBean, OutputStream outputStream);
	
	/**
	 * Deserialize and input stream from an input stream.
	 * @param dataInputStream the inputstream to deserialize
	 * @return the compressed structure data.
	 */
	public MmtfEncodedStructure deserialize(InputStream dataInputStream);

}
