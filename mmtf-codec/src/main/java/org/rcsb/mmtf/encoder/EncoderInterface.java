package org.rcsb.mmtf.encoder;

import org.rcsb.mmtf.api.StructureDataInterface;
import org.rcsb.mmtf.dataholders.MmtfStructure;

/**
 * The interface all encoders must implement.
 * @author Anthony Bradley
 *
 */
public interface EncoderInterface {

	/**
	 * Get the {@link MmtfStructure} of encoded data.
	 * @return the encoded data as an {@link MmtfStructure}
	 */
	public MmtfStructure getMmtfEncodedStructure();
	
	
	/**
	 * Get the {@link MmtfStructure} of encoded data.
	 * @param structureDataInterface the input {@link StructureDataInterface} 
	 * @return the encoded data as an {@link MmtfStructure}
	 */
	public MmtfStructure getMmtfEncodedStructure(StructureDataInterface structureDataInterface);
}
