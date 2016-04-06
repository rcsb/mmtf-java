package org.rcsb.mmtf.encoder;

import java.util.Map;

import org.rcsb.mmtf.dataholders.BioDataStruct;
import org.rcsb.mmtf.dataholders.CalphaBean;
import org.rcsb.mmtf.dataholders.HeaderBean;
import org.rcsb.mmtf.dataholders.PDBGroup;

/**
 * An interface to be used in encoding structures
 * @author Anthony Bradley
 *
 */
public interface EncoderInterface {

	/**
	 * Gets the bio struct.
	 *
	 * @return the bio struct
	 */
	BioDataStruct getBioStruct();

	/**
	 * Sets the bio struct.
	 *
	 * @param bioStruct the new bio struct
	 */
	void setBioStruct(BioDataStruct bioStruct);

	/**
	 * Gets the calpha struct.
	 *
	 * @return the calpha struct
	 */
	CalphaBean getCalphaStruct();

	/**
	 * Sets the calpha struct.
	 *
	 * @param calphaStruct the new calpha struct
	 */
	void setCalphaStruct(CalphaBean calphaStruct);

	/**
	 * Gets the header struct.
	 *
	 * @return the header struct
	 */
	HeaderBean getHeaderStruct();

	/**
	 * Sets the header struct.
	 *
	 * @param headerStruct the new header struct
	 */
	void setHeaderStruct(HeaderBean headerStruct);

	/**
	 * Helper function to generate a main, calpha and header data form a PDB id.
	 *
	 * @param pdbId the pdb id
	 * @param bioStructMap the bio struct map
	 */
	void generateDataStructuresFromPdbId(String pdbId, Map<Integer, PDBGroup> bioStructMap);

}