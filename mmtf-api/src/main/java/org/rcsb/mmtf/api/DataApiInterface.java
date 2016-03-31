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
 * <p>
 * The structural data is accessible through this interface via 
 * a flat structure, instead of the usual hierarchical 
 * data encountered in PDB structures: structure -> model -> chain -> group -> atom.
 * Going back to a hierarchical view of the structure can be achieved by 
 * using the {@link #getChainsPerModel()}, {@link #getGroupsPerChain()} and 
 * {@link #getGroupMap()} methods so that the flat arrays can be reconstructed into
 * a hierarchy.   
 * </p>
 * 
 * <p>
 * Please refer to the full MMTF specification available at 
 * <a href="http://mmtf.rcsb.org">http://mmtf.rcsb.org</a>.
 * Further reference can be found in the <a href="http://mmcif.wwpdb.org/">mmCIF dictionary</a>.
 * </p>
 * 
 * @author Anthony Bradley
 * @author Jose Duarte
 */
public interface DataApiInterface {

	/**
	 * Returns an array containing the X coordinates of the atoms in Angstroms.
	 * @return an array of length the number of atoms in the structure, obtainable with {@link #getNumAtoms()}
	 */
	float[] getXcoords();

	/**
	 * Returns an array containing the Y coordinates of the atoms in Angstroms.
	 * @return an array of length the number of atoms in the structure, obtainable with {@link #getNumAtoms()}
	 */
	float[] getYcoords();

	/**
	 * Returns an array containing the Z coordinates of the atoms in Angstroms.
	 * @return an array of length the number of atoms in the structure, obtainable with {@link #getNumAtoms()}
	 */
	float[] getZcoords();

	/**
	 * Returns an array containing the B-factors (temperature factors) of the atoms in Angstroms^2.
	 * @return an array of length the number of atoms in the structure, obtainable with {@link #getNumAtoms()}
	 */
	float[] getBfactors();

	/**
	 * Returns an array containing the occupancy values of the atoms.
	 * @return an array of length the number of atoms in the structure, obtainable with {@link #getNumAtoms()}
	 */	
	float[] getOccupancies();

	/**
	 * Returns an array of atom serial ids (_atom_site.id in mmCIF dictionary).
	 * @return an array of length the number of atoms in the structure, obtainable with {@link #getNumAtoms()}
	 */
	int[] getAtomIds();

	/**
	 * Returns an array of location ids of the atoms.
	 * '?' specifies a lack of alt id.
	 * @return an array of length the number of atoms in the structure, obtainable with {@link #getNumAtoms()} 
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
	 * @return a map of group indices to {@link PDBGroup} objects
	 */
	Map<Integer, PDBGroup> getGroupMap();

	/**
	 * Returns an array containing indices of all groups in the structure as used in {@link #getGroupMap()}.
	 * @return an array of length the number of groups (residues) in the structure, obtainable with {@link #getNumResidues()}
	 */	
	int[] getGroupIndices();

	/**
	 * Returns an array containing the indices of groups (residues) in their corresponding sequences,
	 * obtainable through {@link #getEntityList()} from the {@link Entity} objects.
	 * The indices are 0-based and specified per entity, -1 indicates the group is not present in the sequence.
	 * @return an array of length the number of groups (residues) in the structure, obtainable with {@link #getNumResidues()}
	 */
	int[] getSeqResGroupIndices();

	/**
	 * Returns an array of internal chain identifiers (asym_ids in mmCIF dictionary), of length the 
	 * number of chains (polymeric, non-polymeric and water) in the structure.
	 * 
	 * The ids have a maximum of 4 chars.
	 * @return an array of length the number of chains in the structure, obtainable with {@link #getNumChains()}
	 */
	String[] getChainIds();

	/**
	 * Returns an array of public chain identifiers (auth_ids in mmCIF dictionary), of length the 
	 * number of chains (polymeric, non-polymeric and water) in the structure.
	 * 
	 * @return an array of length the number of chains in the structure, obtainable with {@link #getNumChains()}
	 */	
	String[] getChainNames();

	/**
	 * Returns an array containing the number of chains (polymeric/non-polymeric/water) in each model.
	 * @return an array of length the number of models in the structure, obtainable with {@link #getNumModels()}
	 */
	int[] getChainsPerModel();

	/**
	 * Returns an array containing the number of groups (residues) in each chain.
	 * @return an array of length the number of chains in the structure, obtainable with {@link #getNumChains()}
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
	 * indices in the array.
	 * @return an array of length 2 * <em>number of inter-group bonds</em>
	 */
	int[] getInterGroupBondIndices();

	/**
	 * Returns an array of bond orders (1,2,3) of inter-group bonds with length <em>number of inter-group bonds</em>
	 * @return
	 */
	int[] getInterGroupBondOrders();

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
	 * The sequences can be obtained from the Entities.
	 * @return
	 */
	Entity[] getEntityList();

	/**
	 * Returns the four character PDB id of the structure.
	 * @return the PDB identifier
	 */
	String getPdbId();

	/**
	 * Returns the number of models in the structure.
	 * @return the number of models
	 */
	int getNumModels();
	
	/**
	 * Returns the number of chains (for all models) in the structure.
	 * @return the number of chains for all models
	 * @see #getChainsPerModel()
	 */
	int getNumChains();
	
	/**
	 * Returns the number of groups (residues) in the structure that have
	 * experimentally determined 3D coordinates.
	 * @return the number of residues in the structure, counting all models and chains
	 */
	int getNumResidues();
	

	/**
	 * Returns the number of atoms in the structure.
	 * @return the number of atoms in the structure, counting all models and chains
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
	 * Returns the experimental methods as a list of strings. Normally only one 
	 * experimental method is available, but structures solved with hybrid methods will
	 * have more than one method. 
	 * The possible experimental method values are described in 
	 * <a href="http://mmcif.wwpdb.org/dictionaries/mmcif_pdbx_v40.dic/Items/_exptl.method.html">data item <em>_exptl.method</em> of the mmCIF dictionary</a>
	 * @return the list of experimental methods 
	 */
	List<String> getExperimentalMethods();
}