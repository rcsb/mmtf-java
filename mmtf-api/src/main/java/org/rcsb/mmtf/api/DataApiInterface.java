package org.rcsb.mmtf.api;

import java.util.List;

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
	 * Returns the group name for the group specified in {@link #getGroupIndices()}.
	 * to link groups to the 3 letter group name, e.g. HIS.
	 * @param groupInd The index of the group specified in {@link #getGroupIndices()}.
	 * @return a 3 letter string specifiying the group name.
	 */
	String getGroupName(int groupInd);
	
	/**
	 * Returns the number of atoms in the group specified in {@link #getGroupIndices()}.
	 * @param groupInd The index of the group specified in {@link #getGroupIndices()}.
	 * @return The number of atoms in the group
	 */	
	int getNumAtomsInGroup(int groupInd);
	

	/** Returns the atom names (e.g. CB) for the group specified in {@link #getGroupIndices()}.
	 * Atom names are unique for each unique atom in a group.
	 * @param groupInd The index of the group specified in {@link #getGroupIndices()}.
	 * @return A list of strings for the atom names. 
	 * */
	String[] getGroupAtomNames(int groupInd);

	/** Returns the element names (e.g. C is carbon) for the group specified in {@link #getGroupIndices()}.
	 * @param groupInd The index of the group specified in {@link #getGroupIndices()}.
	 * @return A list of strings for the element information. 
	 * */
	String[] getGroupElementNames(int groupInd);	

	/** Returns the bond orders for the group specified in {@link #getGroupIndices()}.
	 * A list of integers indicating the bond orders
	 * @param groupInd The index of the group specified in {@link #getGroupIndices()}.
	 * @return A list of integers (1,2 or 3) indicating the bond orders. 
	 * */
	int[] getGroupBondOrders(int groupInd);

	/** Returns the zero-indexed bond indices (in pairs) for the group specified in {@link #getGroupIndices()}.
	 * (e.g. 0,1 means there is bond between atom 0 and 1).
	 * @param groupInd The index of the group specified in {@link #getGroupIndices()}.
	 * @return A list of integers specifying the bond indices (within the group). Indices are zero indexed.
	 * */
	int[] getGroupBondIndices(int groupInd);

	/** Returns the atom charges for the group specified in {@link #getGroupIndices()}.
	 * @param groupInd The index of the group specified in {@link #getGroupIndices()}.
	 * @return A list of integers indicating the atomic charge for each atom in the group.
	 */
	int[] getGroupAtomCharges(int groupInd);

	/** Returns the single letter amino acid code for the group specified in {@link #getGroupIndices()}.
	 * @param groupInd The index of the group specified in {@link #getGroupIndices()}.
	 * @return A string indicating the single letter amino acid
	 */
	String getGroupSingleLetterCode(int groupInd);

	/** Returns the chemical componenet type for the group specified in {@link #getGroupIndices()}.
	 * @param groupInd The index of the group specified in {@link #getGroupIndices()}.
	 * @return A string (taken from the chemical component dictionary) indicating 
	 * the type of the group. Corresponds to -> http://mmcif.wwpdb.org/dictionaries/mmcif_pdbx.dic/Items/_chem_comp.type.html
	 */
	String getGroupChemCompType(int groupInd);

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
	 * Returns the number of bioassemblies in this structure.
	 * @return an integer specifying the number of bioassemblies.
	 */
	int getNumBioassemblies();

	/**
	 * Returns the number of transformations in a given bioassembly.
	 * @param an integer specifying the bioassembly index (zero indexed).
	 * @return an integer specifying of transformations in a given bioassembly.
	 */
	int getNumTransInBioassembly(int bioassemblyIndex);
	
	/**
	 * Returns the list of chain ids for the given transformation for the given bioassembly.
	 * @param an integer specifying the bioassembly index (zero indexed).
	 * @param an integer specifying the  index (zero indexed) for the desired transformation.
	 * @return a list of strings showing the chains involved in this transformation.
	 */
	String[] getChainIdListForTrans(int bioassemblyIndex, int transformationIndex);

	
	/**
	 * Returns the transformation matrix for the given transformation for the given bioassembly.
	 * @param an integer specifying the bioassembly index (zero indexed).
	 * @param an integer specifying the  index (zero indexed) for the desired transformation.
	 * @return the transformation matrix for this transformation.
	 */
	double[] getTransMatrixForTrans(int bioassemblyIndex, int transformationIndex);
	
	
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
	 * @return The number of entities in the Structure 
	 */
	int getNumEntities();
	
	/**
	 * Returns the entity description for the entity specified by the index.
	 * @param The index of this entity.
	 * @return The description based on the PDBx model.
	 */
    String getEntityDescription(int entityInd);
    
	/**
	 * Returns the the type (polymer, non-polymer, water) for the entity specified by the index.
	 * @param The index of this entity.
	 * @return The type (polymer, non-polymer, water)
	 */ 
    String getEntityType(int entityInd);
    
	/**
	 * Returns the chain indices for the entity specified by the index.
	 * @param The index of this entity.
	 * @return The chain index list - referencing the entity to the chains.
	 */    
    int[] getEntityChainIndexList(int entityInd);
    
	/**
	 * Returns the sequence for the entity specified by the index.
	 * @param The index of this entity.
	 * @return The one letter sequence for this entity. Empty string if no sequence is applicable.
	 */
    String getEntitySequence(int entityInd);

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