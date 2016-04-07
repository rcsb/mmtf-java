package org.rcsb.mmtf.api;

/**
 *
 * @author Anthony Bradley
 * @author Jose Duarte
 */
public interface MmtfInitialDataInterface {

	/**
	 * Sets an array containing the X coordinates of the atoms in Angstroms.
	 * @param an array of length the number of atoms in the structure
	 */
	void setxCoords(float[] xCoords);

	/**
	 * Sets an array containing the Y coordinates of the atoms in Angstroms.
	 * @param an array of length the number of atoms in the structure
	 */
	void setyCoords(float[] yCoords);

	/**
	 * Sets an array containing the Z coordinates of the atoms in Angstroms.
	 * @param an array of length the number of atoms in the structure
	 */
	void setzCoords(float[] zCoords);

	/**
	 * Sets an array containing the B-factors (temperature factors) of the atoms in Angstroms^2.
	 * @param an array of length the number of atoms in the structure
	 */
	void setbFactors(float[] bFactors);

	/**
	 * Sets an array containing the occupancy values of the atoms.
	 * @param an array of length the number of atoms in the structure
	 */	
	void setOccupancies(float[] occupancies);

	/**
	 * Sets an array of atom serial ids (_atom_site.id in mmCIF dictionary).
	 * @param an array of length the number of atoms in the structure
	 */
	void setAtomIds(int[] atomIds);

	/**
	 * Sets an array of location ids of the atoms.
	 * '\0' specifies a lack of alt id.
	 * @param an array of length the number of atoms in the structure 
	 */
	void setAltLocIds(char[] altLocIds);

	/**
	 * Sets an array containing the insertion codes (pdbx_PDB_ins_code in mmCIF dictionary) for each residue (group). 
	 * '\0' specifies a lack of insertion code.
	 * @param an array with insertion codes
	 * @see #setGroupIds()
	 */	
	void setInsCodes(char[] insCodes);

	/**
	 * Sets an array containing residue numbers (auth_seq_id in mmCIF dictionary) for each residue (group).
	 * @param an array with with residue numbers
	 * @see #setInsCodes()
	 */
	void setGroupIds(int[] groupIds);


	/**
	 * Sets the group name for the group specified in {@link #setGroupTypeIndices()}.
	 * to link groups to the 3 letter group name, e.g. HIS.
	 * @param groupInd The index of the group specified in {@link #setGroupTypeIndices()}.
	 * @param a 3 letter string specifiying the group name.
	 */
	void setGroupName(int groupInd, String groupName);
	
	/**
	 * Sets the number of atoms in the group specified in {@link #setGroupTypeIndices()}.
	 * @param groupInd The index of the group specified in {@link #setGroupTypeIndices()}.
	 * @param numAtomsInGroup The number of atoms in the group
	 */	
	void setNumAtomsInGroup(int groupInd, int numAtomsInGroup);
	

	/** 
	 * Sets the atom names (e.g. CB) for the group specified in {@link #setGroupTypeIndices()}.
	 * Atom names are unique for each unique atom in a group.
	 * @param groupInd The index of the group specified in {@link #setGroupTypeIndices()}.
	 * @param groupAtomNames A list of strings for the atom names. 
	 * */
	void setGroupAtomNames(int groupInd, String[] groupAtomNames);

	/** 
	 * Sets the IUPAC element names (e.g. Ca is calcium) for the group specified in {@link #setGroupTypeIndices()}.
	 * @param groupInd the index of the group specified in {@link #setGroupTypeIndices()}.
	 * @param groupElements an array of strings for the element information. 
	 * */
	void setGroupElementNames(int groupInd, String[] groupElements);	

	/** 
	 * Sets the bond orders for the group specified in {@link #setGroupTypeIndices()}.
	 * A list of integers indicating the bond orders
	 * @param groupInd the index of the group specified in {@link #setGroupTypeIndices()}.
	 * @param groupBondOrders an array of integers (1,2 or 3) indicating the bond orders. 
	 * */
	void setGroupBondOrders(int groupInd, int[] groupBondOrders);

	/** 
	 * Sets the zero-based bond indices (in pairs) for the group specified in {@link #setGroupTypeIndices()}.
	 * (e.g. 0,1 means there is bond between atom 0 and 1).
	 * @param groupInd the index of the group specified in {@link #setGroupTypeIndices()}.
	 * @param groupBondIndices an array of integers specifying the bond indices (within the group). Indices are zero indexed.
	 * */
	void setGroupBondIndices(int groupInd, int[] groupBondIndices);

	/** 
	 * Sets the atom charges for the group specified in {@link #setGroupTypeIndices()}.
	 * @param groupInd the index of the group specified in {@link #setGroupTypeIndices()}.
	 * @param groupAtomCharges an array of integers indicating the atomic charge for each atom in the group.
	 */
	void setGroupAtomCharges(int groupInd, int[] groupAtomCharges);

	/** 
	 * Sets the single letter amino acid code or nucleotide code for the 
	 * group specified in {@link #setGroupTypeIndices()}.
	 * @param groupInd the index of the group specified in {@link #setGroupTypeIndices()}.
	 * @param groupSingleLetterCode the single letter amino acid or nucleotide, 'X' if non-standard amino acid or nucleotide
	 */
	void setGroupSingleLetterCode(int groupInd, char groupSingleLetterCode);

	/** 
	 * Sets the chemical component type for the group specified in {@link #setGroupTypeIndices()}.
	 * @param groupInd The index of the group specified in {@link #setGroupTypeIndices()}.
	 * @param a string (taken from the chemical component dictionary) indicating 
	 * the type of the group. Corresponds to 
	 * <a href="http://mmcif.wwpdb.org/dictionaries/mmcif_pdbx.dic/Items/_chem_comp.type.html">http://mmcif.wwpdb.org/dictionaries/mmcif_pdbx.dic/Items/_chem_comp.type.html</a>
	 */
	void setGroupChemCompType(int groupInd, String groupChemCompType);

	/**
	 * Sets an array containing indices to be used to obtain group level information
	 * @param an array of length the number of groups (residues) in the structure
	 */	
	void setGroupTypeIndices(int[] groupTypeIndices);

	/**
	 * Sets an array containing the indices of groups (residues) in their corresponding sequences.
	 * The indices are 0-based and specified per entity, -1 indicates the group is not present in the sequence.
	 * @param groupSequenceIndices an array of length the number of groups (residues) in the structure
	 */
	void setGroupSequenceIndices(int[] groupSequenceIndices);

	/**
	 * Sets an array of internal chain identifiers (asym_ids in mmCIF dictionary), of length the 
	 * number of chains (polymeric, non-polymeric and water) in the structure. 
	 * @param chainIds an array of length the number of chains in the structur
	 * @see #setChainNames()
	 */
	void setChainIds(String[] chainIds);

	/**
	 * Sets an array of public chain identifiers (auth_ids in mmCIF dictionary), of length the 
	 * number of chains (polymeric, non-polymeric and water) in the structure. 
	 * @param chainNames an array of length the number of chains in the structur
	 * @see #setChainIds()
	 */	
	void setChainNames(String[] chainNames);

	/**
	 * Sets an array containing the number of chains (polymeric/non-polymeric/water) in each model.
	 * @param chainsPerModel an array of length the number of models in the structure
	 */
	void setChainsPerModel(int[] chainsPerModel);

	/**
	 * Sets an array containing the number of groups (residues) in each chain.
	 * @param groupsPerChain an array of length the number of chains in the structur
	 */	
	void setGroupsPerChain(int[] groupsPerChain);

	/**
	 * Sets the space group of the structure.
	 * @param spaceGroup the space group name (e.g. "P 21 21 21") or null if the structure is not crystallographic
	 */
	void setSpaceGroup(String spaceGroup);

	/**
	 * Sets the 6 floats that describe the unit cell.
	 * @param unitCell an array of size 6 with the unit cell parameters in order: a, b, c, alpha, beta, gamma
	 */
	void setUnitCell(float[] unitCell);

	/**
	 * Sets the number of bioassemblies in this structure.
	 * @param numBioassemblies the number of bioassemblies.
	 */
	void setNumBioassemblies(int numBioassemblies);

	/**
	 * Sets the number of transformations in a given bioassembly.
	 * @param bioassemblyIndex an integer specifying the bioassembly index (zero indexed).
	 * @param numTransInBioassembly an integer specifying of transformations in a given bioassembly.
	 */
	void setNumTransInBioassembly(int bioassemblyIndex, int numTransInBioassembly);
	
	/**
	 * Sets the list of chain indices for the given transformation for the given bioassembly.
	 * @param bioassemblyIndex an integer specifying the bioassembly index (zero indexed).
	 * @param transformationIndex an integer specifying the  index (zero indexed) for the desired transformation.
	 * @param transChainIndexList a list of indices showing the chains involved in this transformation.
	 */
	void setChainIndexListForTransform(int bioassemblyIndex, int transformationIndex, int[] transChainIndexList);

	
	/**
	 * Sets a 4x4 transformation matrix for the given transformation for the given bioassembly.
	 * It is row-packed as per the convention of vecmath. (The first four elements are in the first row of the
	 * overall matrix).
	 * @param bioassemblyIndex an integer specifying the bioassembly index (zero indexed).
	 * @param transformationIndex an integer specifying the  index for the desired transformation (zero indexed).
	 * @param transformationMatrix the transformation matrix for this transformation.
	 */
	void setMatrixForTransform(int bioassemblyIndex, int transformationIndex, double[] transformationMatrix);
	
	
	/**
	 * Sets the zero-based bond indices (in pairs) for the structure.
	 * (e.g. 0,1 means there is bond between atom 0 and 1).
	 * @param interGroupBondIndices an array of integers specifying the bond indices (within the structure). Indices are zero-based.
	 */
	void setInterGroupBondIndices(int[] interGroupBondIndices);

	/**
	 * Sets an array of bond orders (1,2,3) of inter-group bonds with length <em>number of inter-group bonds</em>
	 * @param interGroupBondOrders the bond orders for bonds within a group
	 */
	void setInterGroupBondOrders(int[] interGroupBondOrders);

	/**
	 * Sets the MMTF version number (from the specification).
	 * @param mmtfVersion the version of the file.
	 */
	void setMmtfVersion(String mmtfVersion);

	/**
	 * Sets a string describing the producer of the MMTF file.
	 * e.g. "RCSB-PDB Generator---version: 6b8635f8d319beea9cd7cc7f5dd2649578ac01a0"
	 * @param mmtfProducer a string describing the producer
	 */
	void setMmtfProducer(String mmtfProducer);

	/**
	 * Sets the number of entities (as defined in mmCIF dictionary) in the structure
	 * @param numEntities the number of entities in the structure 
	 */
	void setNumEntities(int numEntities);
	
	/**
	 * Sets the entity description (as defined in mmCIF dictionary) 
	 * for the entity specified by the index.
	 * @param entityInd the index of the specified entity.
	 * @param entityDescription the description of the entity
	 */
    void setEntityDescription(int entityInd, String entityDescription);
    
	/**
	 * Sets the entity type (polymer, non-polymer, water) for the entity specified by the index.
	 * @param entityInd the index of the specified entity.
	 * @param entityType the entity type (polymer, non-polymer, water)
	 */ 
    void setEntityType(int entityInd, String entityType);
    
	/**
	 * Sets the chain indices for the entity specified by the index.
	 * @param entityInd the index of the specified entity.
	 * @param chainIndexList the chain index list - referencing the entity to the chains.
	 */    
    void setEntityChainIndexList(int entityInd,int[] chainIndexList);
    
	/**
	 * Sets the sequence for the entity specified by the index.
	 * @param entityInd the index of the specified entity.
	 * @param sequence the one letter sequence for this entity. Empty string if no sequence is applicable.
	 */
    void setEntitySequence(int entityInd, String sequence);

	/**
	 * Sets the identifier of the structure.
	 * For instance the 4-letter PDB id
	 * @param structureId the identifier for the structure
	 */
	void setStructureId(String structureId);

	/**
	 * Sets the number of models in the structure.
	 * @param numModels the number of models
	 */
	void setNumModels(int numModels);

	/**
	 * Sets the number of chains (for all models) in the structure.
	 * @param numChains the number of chains for all models
	 * @see #setChainsPerModel()
	 */
	void setNumChains(int numChains);

	/**
	 * Sets the number of groups (residues) in the structure that have
	 * experimentally determined 3D coordinates.
	 * @param numGroups the number of residues in the structure, for all models and chains
	 */
	void setNumGroups(int numGroups);


	/**
	 * Sets the number of atoms in the structure.
	 * @param numAtoms the number of atoms in the structure, for all models and chains
	 */
	void setNumAtoms(int numAtoms);


	/**
	 * Sets the Rfree of the dataset.
	 * @param rFree the R-free value
	 */
	void setRfree(float rFree);
	
	/**
	 * Sets the Rwork of the dataset.
	 * @param rWork the R-work value
	 */
	void setRwork(float rWork);

	/**
	 * Sets the resolution of the dataset.
	 * @param resolution the resolution value in Angstroms
	 */
	void setResolution(float resolution);

	/**
	 * Sets the title of the structure.
	 * @param title the title of the structure.
	 */
	void setTitle(String title);

	/**
	 * Sets the experimental methods as an array of strings. Normally only one 
	 * experimental method is available, but structures solved with hybrid methods will
	 * have more than one method. 
	 * The possible experimental method values are described in 
	 * <a href="http://mmcif.wwpdb.org/dictionaries/mmcif_pdbx_v40.dic/Items/_exptl.method.html">data item <em>_exptl.method</em> of the mmCIF dictionary</a>
	 * @param experimentalMethods the list of experimental methods 
	 */
	void setExperimentalMethods(String[] experimentalMethods);
	
	/**
	 * Sets the deposition date of the structure as a string
	 * in ISO time standard format. https://www.cl.cam.ac.uk/~mgk25/iso-time.html
	 * @param depositionDate the deposition date of the structure.
	 */
	void setDepositionDate(String depositionDate);
	
	
}