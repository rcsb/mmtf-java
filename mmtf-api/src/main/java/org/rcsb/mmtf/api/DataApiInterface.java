package org.rcsb.mmtf.api;

import java.util.List;
import java.util.Map;

import org.rcsb.mmtf.dataholders.BioAssemblyData;
import org.rcsb.mmtf.dataholders.Entity;
import org.rcsb.mmtf.dataholders.MmtfBean;
import org.rcsb.mmtf.dataholders.PDBGroup;

/**
 * An interface describing the data API.
 * 
 * @author Anthony Bradley
 *
 */
public interface DataApiInterface {

	/**
	 * Returns an array of length N atoms of the X coordinates of the atoms.
	 * @return
	 */
	float[] getXcoords();

	/**
	 * Returns an array of length N atoms of the Y coordinates of the atoms.
	 * @return
	 */
	float[] getYcoords();

	/**
	 * Returns an array of length N atoms of the Z coordinates of the atoms.
	 * @return
	 */
	float[] getZcoords();

	/**
	 * Returns an array of length N atoms of the B-factors of the atoms.
	 * @return
	 */
	float[] getBfactors();

	/**
	 * Returns an array of length N atoms of the Occupancy of the atoms.
	 * @return
	 */	
	float[] getOccupancies();

	/**
	 * Returns an array of atom serial ids (_atom_site.id in mmCIF dictionary) of length N atoms.
	 * @return
	 */
	int[] getAtomIds();

	/**
	 * Returns an array of length N atoms of the alternate location ids of the atoms as characters.
	 * '?' specifies a lack of alt id.
	 * @return
	 */
	char[] getAltLocIds();

	/**
	 * Returns an array containing the insertion codes (pdbx_PDB_ins_code in mmCIF dictionary) for each residue (group). 
	 * '?' specifies a lack of insertion code.
	 * @return an array with insertion codes, of size {@link #getNumResidues()}
	 * @see #getResidueNums()
	 */	
	char[] getInsCodes();

	/**
	 * Returns an array containing residue numbers (auth_seq_id in mmCIF dictionary) for each residue (group).
	 * @return an array with with residue numbers, of size {@link #getNumResidues()} 
	 * @see #getInsCodes()
	 */
	int[] getResidueNums();

	/**
	 * Returns the group map, mapping the numbers from indices specified in {@link #getGroupIndices()} 
	 * to {@link PDBGroup} objects, which specify the atom names, 
	 * elements, bonds and charges for each group.
	 * @return
	 */
	Map<Integer, PDBGroup> getGroupMap();

	/**
	 * Returns an array of length N groups indicating the index in {@link #getGroupMap()} for each group.
	 * @return
	 */	
	int[] getGroupIndices();

	/**
	 * Returns an array of length N groups indicating the index in the Sequence for each group.
	 * -1 indicates the group is not present in the sequence. Indices are specified per chain.
	 * @return
	 */
	int[] getSeqResGroupIndices();

	/**
	 * Returns an array of internal chain identifiers (asym_ids in mmCIF dictionary), of length the 
	 * number of chains (polymeric, non-polymeric and water) in the structure.
	 * 
	 * The ids have a maximum of 4 chars.
	 * @return
	 */
	String[] getChainIds();

	/**
	 * Returns an array of public chain identifiers (auth_ids in mmCIF dictionary), of length the 
	 * number of internal chains (polymeric, non-polymeric and water) in the structure.
	 * 
	 * @return
	 */	
	String[] getChainNames();

	/**
	 * Returns an array of length N models, indicating the number of chains 
	 * (polymeric/non-polymeric/water) in each model.
	 * @return
	 */
	int[] getChainsPerModel();

	/**
	 * Returns an array of length N chains, indicating the number of groups (residues) in each chain.
	 * @return
	 */	
	int[] getGroupsPerChain();

	/**
	 * Returns the space group of the structure.
	 *
	 * @return the space group name (e.g. "P 21 21 21") or null if the structure is not crystallographic
	 */
	String getSpaceGroup();

	/**
	 * Returns the 6 floats that describe the unit cell.
	 * @return an array of size 6 with the unit cell parameters in order: a, b, c, alpha, beta, gamma
	 */
	float[] getUnitCell();

	/**
	 * Returns a list of {@link BioAssemblyData}s corresponding to the structure.
	 * @return
	 */
	List<BioAssemblyData> getBioAssemblyList();

	/**
	 * Returns an array of inter-group bonds represented with 2 consecutive atom 
	 * indices in the array, with length 2 * <em>number of inter-group bonds</em>.
	 * @return
	 */
	int[] getInterGroupBondIndices();

	/**
	 * Returns an array of bond orders (1,2,3) of inter-group bonds with length <em>number of inter-group bonds</em>
	 * @return
	 */
	int[] getInterGroupBondOrders();

	/**
	 * Returns an array of length N chains for the internal chain ids (asym ids).
	 * Each string is of length up to 4.
	 * @return
	 */
	String[] getChainList();

	/**
	 * Returns the MMTF version number (from the specification).
	 * @return
	 */
	String getMmtfVersion();

	/**
	 * Returns a string describing the producer of the MMTF process.
	 * @return
	 */
	String getMmtfProducer();

	/**
	 * Returns an array with all {@link Entity} objects for the structure.
	 * @return
	 */
	Entity[] getEntityList();

	/**
	 * Returns the four character PDB id of the structure.
	 * @return
	 */
	String getPdbId();

	/**
	 * Returns the number of models in the structure.
	 * @return
	 */
	int getNumModels();
	
	/**
	 * Returns the number of chains in the structure.
	 * @return
	 */
	int getNumChains();
	
	/**
	 * Returns the number of groups (residues) in the structure.
	 * @return
	 */
	int getNumResidues();
	

	/**
	 * Returns the number of atoms in the structure.
	 * @return
	 */
	int getNumAtoms();
	
	
	/**
	 * Returns the Rfree (if available) of the dataset.
	 * @return the Rfree value or {@value MmtfBean#UNAVAILABLE_R_VALUE} if unavailable
	 */
	float getRfree();
	
	/**
	 * Returns the Resolution (if available) of the dataset.
	 * @return the resolution value in Angstroms or {@value MmtfBean#UNAVAILABLE_R_VALUE} if unavailable
	 */
	float getResolution();
	
	/**
	 * Returns the Rwork (if available) of the dataset.
	 * @return the Rwork value or {@value MmtfBean#UNAVAILABLE_R_VALUE} if unavailable
	 */
	float getRwork();
	
	
	/**
	 * Returns the title of the structure.
	 * @return
	 */
	String getTitle();
	
	/**
	 * Returns the experimental methods as a list of strings. The experimental method values 
	 * are described in <a href="http://mmcif.wwpdb.org/dictionaries/mmcif_pdbx_v40.dic/Items/_exptl.method.html">data item <em>_exptl.method</em> of the mmCIF dictionary</a>
	 * @return
	 */
	List<String> getExperimentalMethods();
}