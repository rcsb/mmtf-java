package org.rcsb.mmtf.encoder;

import org.rcsb.mmtf.dataholders.MmtfStructure;

/**
 * The interface all encoders must implement.
 * @author Anthony Bradley
 *
 */
public interface EncoderInterface {

	/**
	 * Get the MmtfBean of encoded data.
	 * @return the encoded data as an MmtfBean
	 */
	public MmtfStructure getMmtfEncodedStructure();
}
