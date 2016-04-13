package org.rcsb.mmtf.sedeserializers;

import java.io.InputStream;
import java.io.OutputStream;

import org.rcsb.mmtf.dataholders.MmtfBean;

/**
 * An interface to carry out serializing / deserializing to mmtfBean
 * @author Anthony Bradley
 *
 */
public interface MmtfBeanSeDerializerInterface {

	public void serialize(MmtfBean mmtfBean, OutputStream outputStream);
	
	public MmtfBean deserialize(InputStream dataInputStream);

}
