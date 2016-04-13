package org.rcsb.mmtf.api;

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
public interface DecodedDataInterface {

	/**
	 * Returns an array containing the X coordinates of the atoms in Angstroms.
	 * @return an array of length the number of atoms in the structure, obtainable with {@link #getNumAtoms()}
	 */
	float[] getxCoords();

	/**
	 * Returns an array containing the Y coordinates of the atoms in Angstroms.
	 * @return an array of length the number of atoms in the structure, obtainable with {@link #getNumAtoms()}
	 */
	float[] getyCoords();

	/**
	 * Returns an array containing the Z coordinates of the atoms in Angstroms.
	 * @return an array of length the number of atoms in the structure, obtainable with {@link #getNumAtoms()}
	 */
	float[] getzCoords();

	/**
	 * Returns an array containing the B-factors (temperature factors) of the atoms in Angstroms^2.
	 * @return an array of length the number of atoms in the structure, obtainable with {@link #getNumAtoms()}
	 */
	float[] getbFactors();

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
	 * '\0' specifies a lack of alt id.
	 * @return an array of length the number of atoms in the structure, obtainable with {@link #getNumAtoms()} 
	 */
	char[] getAltLocIds();

	/**
	 * Returns an array containing the insertion codes (pdbx_PDB_ins_code in mmCIF dictionary) for each residue (group). 
	 * '\0' specifies a lack of insertion code.
	 * @return an array with insertion codes, of size {@link #getNumGroups()}
	 * @see #getGroupIds()
	 */	
	char[] getInsCodes();

	/**
	 * Returns an array containing residue numbers (auth_seq_id in mmCIF dictionary) for each residue (group).
	 * @return an array with with residue numbers, of size {@link #getNumGroups()} 
	 * @see #getInsCodes()
	 */
	int[] getGroupIds();

	/**
	 * Returns the group name for the group specified in {@link #getGroupTypeIndices()}.
	 * to link groups to the 3 letter group name, e.g. HIS.
	 * @param groupInd The index of the group specified in {@link #getGroupTypeIndices()}.
	 * @return a 3 letter string specifiying the group name.
	 */
	String getGroupName(int groupInd);
	
	/**
	 * Returns the number of atoms in the group specified in {@link #getGroupTypeIndices()}.
	 * @param groupInd The index of the group specified in {@link #getGroupTypeIndices()}.
	 * @return The number of atoms in the group
	 */	
	int getNumAtomsInGroup(int groupInd);
	

	/** 
	 * Returns the atom names (e.g. CB) for the group specified in {@link #getGroupTypeIndices()}.
	 * Atom names are unique for each unique atom in a group.
	 * @param groupInd The index of the group specified in {@link #getGroupTypeIndices()}.
	 * @return A list of strings for the atom names. 
	 * */
	String[] getGroupAtomNames(int groupInd);

	/** 
	 * Returns the IUPAC element names (e.g. Ca is calcium) for the group specified in {@link #getGroupTypeIndices()}.
	 * @param groupInd the index of the group specified in {@link #getGroupTypeIndices()}.
	 * @return an array of strings for the element information. 
	 * */
	String[] getGroupElementNames(int groupInd);	

	/** 
	 * Returns the bond orders for the group specified in {@link #getGroupTypeIndices()}.
	 * A list of integers indicating the bond orders
	 * @param groupInd the index of the group specified in {@link #getGroupTypeIndices()}.
	 * @return an array of integers (1,2 or 3) indicating the bond orders. 
	 * */
	int[] getGroupBondOrders(int groupInd);

	/** 
	 * Returns the zero-based bond indices (in pairs) for the group specified in {@link #getGroupTypeIndices()}.
	 * (e.g. 0,1 means there is bond between atom 0 and 1).
	 * @param groupInd the index of the group specified in {@link #getGroupTypeIndices()}.
	 * @return an array of integers specifying the bond indices (within the group). Indices are zero indexed.
	 * */
	int[] getGroupBondIndices(int groupInd);

	/** 
	 * Returns the atom charges for the group specified in {@link #getGroupTypeIndices()}.
	 * @param groupInd the index of the group specified in {@link #getGroupTypeIndices()}.
	 * @return an array of integers indicating the atomic charge for each atom in the group.
	 */
	int[] getGroupAtomCharges(int groupInd);

	/** 
	 * Returns the single letter amino acid code or nucleotide code for the 
	 * group specified in {@link #getGroupTypeIndices()}.
	 * @param groupInd the index of the group specified in {@link #getGroupTypeIndices()}.
	 * @return the single letter amino acid or nucleotide, 'X' if non-standard amino acid or nucleotide
	 */
	char getGroupSingleLetterCode(int groupInd);

	/** 
	 * Returns the chemical component type for the group specified in {@link #getGroupTypeIndices()}.
	 * @param groupInd The index of the group specified in {@link #getGroupTypeIndices()}.
	 * @return a string (taken from the chemical component dictionary) indicating 
	 * the type of the group. Corresponds to 
	 * <a href="http://mmcif.wwpdb.org/dictionaries/mmcif_pdbx.dic/Items/_chem_comp.type.html">http://mmcif.wwpdb.org/dictionaries/mmcif_pdbx.dic/Items/_chem_comp.type.html</a>
	 */
	String getGroupChemCompType(int groupInd);

	/**
	 * Returns an array containing indices to be used to obtain group level information, 
	 * e.g. through {@link #getGroupAtomCharges(int)}.
	 * @return an array of length the number of groups (residues) in the structure, obtainable with {@link #getNumGroups()}
	 */	
	int[] getGroupTypeIndices();

	/**
	 * Returns an array containing the indices of groups (residues) in their corresponding sequences,
	 * obtainable through {@link #getEntityList()} from the {@link Entity} objects.
	 * The indices are 0-based and specified per entity, -1 indicates the group is not present in the sequence.
	 * @return an array of length the number of groups (residues) in the structure, obtainable with {@link #getNumGroups()}
	 */
	int[] getGroupSequenceIndices();

	/**
	 * Returns an array of internal chain identifiers (asym_ids in mmCIF dictionary), of length the 
	 * number of chains (polymeric, non-polymeric and water) in the structure. 
	 * @return an array of length the number of chains in the structure, obtainable with {@link #getNumChains()}
	 * @see #getChainNames()
	 */
	String[] getChainIds();

	/**
	 * Returns an array of public chain identifiers (auth_ids in mmCIF dictionary), of length the 
	 * number of chains (polymeric, non-polymeric and water) in the structure. 
	 * @return an array of length the number of chains in the structure, obtainable with {@link #getNumChains()}
	 * @see #getChainIds()
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
	 * @return the number of bioassemblies.
	 */
	int getNumBioassemblies();

	/**
	 * Returns the number of transformations in a given bioassembly.
	 * @param bioassemblyIndex an integer specifying the bioassembly index (zero indexed).
	 * @return an integer specifying of transformations in a given bioassembly.
	 */
	int getNumTransInBioassembly(int bioassemblyIndex);
	
	/**
	 * Returns the list of chain indices for the given transformation for the given bioassembly.
	 * @param bioassemblyIndex an integer specifying the bioassembly index (zero indexed).
	 * @param transformationIndex an integer specifying the  index (zero indexed) for the desired transformation.
	 * @return a list of indices showing the chains involved in this transformation.
	 */
	int[] getChainIndexListForTransform(int bioassemblyIndex, int transformationIndex);

	
	/**
	 * Returns a 4x4 transformation matrix for the given transformation for the given bioassembly.
	 * It is row-packed as per the convention of vecmath. (The first four elements are in the first row of the
	 * overall matrix).
	 * @param bioassemblyIndex an integer specifying the bioassembly index (zero indexed).
	 * @param transformationIndex an integer specifying the  index for the desired transformation (zero indexed).
	 * @return the transformation matrix for this transformation.
	 */
	double[] getMatrixForTransform(int bioassemblyIndex, int transformationIndex);
	
	
	/**
	 * Returns the zero-based bond indices (in pairs) for the structure.
	 * (e.g. 0,1 means there is bond between atom 0 and 1).
	 * @return an array of integers specifying the bond indices (within the structure). Indices are zero-based.
	 */
	int[] getInterGroupBondIndices();

	/**
	 * Returns an array of bond orders (1,2,3) of inter-group bonds with length <em>number of inter-group bonds</em>
	 * @return the bond orders for bonds within a group
	 */
	int[] getInterGroupBondOrders();

	/**
	 * Returns the MMTF version number (from the specification).
	 * @return the version
	 */
	String getMmtfVersion();

	/**
	 * Returns a string describing the producer of the MMTF file.
	 * e.g. "RCSB-PDB Generator---version: 6b8635f8d319beea9cd7cc7f5dd2649578ac01a0"
	 * @return a string describing the producer
	 */
	String getMmtfProducer();

	/**
	 * Returns the number of entities (as defined in mmCIF dictionary) in the structure
	 * @return the number of entities in the structure 
	 */
	int getNumEntities();
	
	/**
	 * Returns the entity description (as defined in mmCIF dictionary) 
	 * for the entity specified by the index.
	 * @param entityInd the index of the specified entity.
	 * @return the description of the entity
	 */
    String getEntityDescription(int entityInd);
    
	/**
	 * Returns the entity type (polymer, non-polymer, water) for the entity specified by the index.
	 * @param entityInd the index of the specified entity.
	 * @return the entity type (polymer, non-polymer, water)
	 */ 
    String getEntityType(int entityInd);
    
	/**
	 * Returns the chain indices for the entity specified by the index.
	 * @param entityInd the index of the specified entity.
	 * @return the chain index list - referencing the entity to the chains.
	 */    
    int[] getEntityChainIndexList(int entityInd);
    
	/**
	 * Returns the sequence for the entity specified by the index.
	 * @param entityInd the index of the specified entity.
	 * @return the one letter sequence for this entity. Empty string if no sequence is applicable.
	 */
    String getEntitySequence(int entityInd);

	/**
	 * Returns the identifier of the structure.
	 * For instance the 4-letter PDB id
	 * @return the identifier
	 */
	String getStructureId();

	/**
	 * Returns the number of models in the structure.
	 * @return the number of models
	 */
	int getNumModels();

	/**
	 * Returns the total number of bonds in the structure
	 * @resturn the number of bonds
	 */
	int getNumBonds();
	
	/**
	 * Returns the number of chains (for all models) in the structure.
	 * @return the number of chains for all models
	 * @see #getChainsPerModel()
	 */
	int getNumChains();

	/**
	 * Returns the number of groups (residues) in the structure that have
	 * experimentally determined 3D coordinates.
	 * @return the number of residues in the structure, for all models and chains
	 */
	int getNumGroups();


	/**
	 * Returns the number of atoms in the structure.
	 * @return the number of atoms in the structure, for all models and chains
	 */
	int getNumAtoms();


	/**
	 * Returns the Rfree of the dataset.
	 * @return the Rfree value
	 */
	float getRfree();
	
	/**
	 * Returns the Rwork of the dataset.
	 * @return the Rwork value
	 */
	float getRwork();

	/**
	 * Returns the resolution of the dataset.
	 * @return the resolution value in Angstroms
	 */
	float getResolution();

	/**
	 * Returns the title of the structure.
	 * @return the title of the structure.
	 */
	String getTitle();

	/**
	 * Returns the experimental methods as an array of strings. Normally only one 
	 * experimental method is available, but structures solved with hybrid methods will
	 * have more than one method. 
	 * The possible experimental method values are described in 
	 * <a href="http://mmcif.wwpdb.org/dictionaries/mmcif_pdbx_v40.dic/Items/_exptl.method.html">data item <em>_exptl.method</em> of the mmCIF dictionary</a>
	 * @return the list of experimental methods 
	 */
	String[] getExperimentalMethods();
	
	/**
	 * Returns the deposition date of the structure as a string
	 * in ISO time standard format. https://www.cl.cam.ac.uk/~mgk25/iso-time.html
	 * @return the deposition date of the structure.
	 */
	String getDepositionDate();

	/**
	 * Returns the release date of the structure as a string
	 * in ISO time standard format. https://www.cl.cam.ac.uk/~mgk25/iso-time.html
	 * @return the release date of the structure.
	 */
	String getReleaseDate();
	
	/**
	 * The secondary structure information for the structure as a list of integers
	 * @return the array of secondary structure informations
	 */
	int[] getSecStructList();
	
}