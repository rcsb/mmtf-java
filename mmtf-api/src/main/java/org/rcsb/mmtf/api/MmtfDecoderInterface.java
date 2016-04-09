package org.rcsb.mmtf.api;

/**
 * Interface to inflate a given MMTF data source.
 *
 * @author Anthony Bradley
 */
public interface MmtfDecoderInterface {

	/**
	 * Used before any additions to do any required pre-processing.
	 * For example the user could use this to specify the amount of memory to be allocated.
	 * @param totalNumBonds the total number of bonds in the structure
	 * @param totalNumAtoms the total number of atoms found in the data.
	 * @param totalNumGroups the total number of groups found in the data.
	 * @param totalNumChains the total number of chains found in the data.
	 * @param totalNumModels the total number of models found in the data.
	 * @param structureId an identifier for the structure (e.g. PDB id).
	 */
	void initStructure(int totalNumBonds, int totalNumAtoms, int totalNumGroups, int totalNumChains, 
			int totalNumModels, String structureId);

	/**
	 * A generic function to be used at the end of all data addition to do required cleanup on the structure
	 */
	void finalizeStructure();

	/**
	 * Sets the number of chains for a given model.
	 * @param modelId identifier of the model within the structure
	 * @param chainCount total number of chains within this model
	 */
	void setModelInfo(int modelId, int chainCount);

	/**
	 * Sets the information for a given chain.
	 * @param chainId chain identifier - length of one to four
	 * @param chainName chain name - public chain id
	 * @param groupCount number of groups/residues in chain
	 */
	void setChainInfo(String chainId, String chainName, int groupCount);

	/**
	 * Sets the entity level annotation for a chain(s). ChainIds is a list of integers that indicate the chains this information
	 * refers to. Sequence is the one letter amino acid sequence. Description and title are both free forms strings describing the entity and 
	 * acting as a title for the entity.
	 * @param chainIndices the indices of the chain this refers to.
	 * @param sequence the full sequence of the entity
	 * @param description the text description of the entity
	 * @param title
	 */
	void setEntityInfo(int[] chainIndices, String sequence, String description, String title);

	/**
	 * Sets the information for a given group / residue with atomic data.
	 * @param groupName 3 letter code name of this group/residue
	 * @param groupNumber sequence position of this group
	 * @param insertionCode the one letter insertion code
	 * @param groupType a string indicating the type of group (as found in the chemcomp dictionary. Empty string if none available.
	 * @param atomCount the number of atoms in the group
	 * @param bondCount the number of unique bonds in the group
	 * @param singleLetterCode the single letter code of the group
	 * @param sequenceIndex the index of this group in the sequence
	 */
	void setGroupInfo(String groupName, int groupNumber, char insertionCode,
			String groupType, int atomCount, int boundCount, char singleLetterCode, int sequenceIndex);


	/**
	 * Sets the atom level information for a given atom.
	 * @param atomName 1-3 long string of the unique name of the atom
	 * @param serialNumber a number counting atoms in a structure
	 * @param alternativeLocationId a character indicating the alternate
	 * location of the atom
	 * @param x the x cartesian coordinate
	 * @param y the y cartesian coordinate
	 * @param z the z cartesian coordinate
	 * @param occupancy the atomic occupancy
	 * @param temperatureFactor the B factor (temperature factor)
	 * @param element a 1-3 long string indicating the chemical element of the atom
	 * @param charge the atomic charge
	 */
	void setAtomInfo(String atomName, int serialNumber, char alternativeLocationId, 
			float x, float y, float z, float occupancy, float temperatureFactor, String element, int charge);

	/**
	 * Sets a single Bioassembly transformation to a structure. bioAssemblyId indicates the index of the bioassembly.
	 * @param bioAssemblyIndex An integer index of this bioassembly.
	 * @param inputChainIndices The integer indices of the chains involved in this bioassembly. 
	 * @param inputTransform A list of doubles indicating the transform for this bioassembly.
	 */
	void setBioAssemblyTrans(int bioAssemblyIndex, int[] inputChainIndices, double[] inputTransform);

	/**
	 * Sets the space group and unit cell information.
	 *
	 * @param spaceGroup the space group name, e.g. "P 21 21 21"
	 * @param unitCell an array of length 6 with the unit cell parameters in order: a, b, c, alpha, beta, gamma
	 */
	void setXtalInfo(String spaceGroup, float[] unitCell);

	/**
	 * Sets an intra-group bond.
	 *
	 * @param thisBondIndOne the atom index of the first partner in the bond
	 * @param thisBondIndTwo the atom index of the second partner in the bond
	 * @param thisBondOrder the bond order
	 */
	void setGroupBond(int thisBondIndOne, int thisBondIndTwo, int thisBondOrder);

	/**
	 * Sets an inter-group bond.
	 *
	 * @param thisBondIndOne the atom index of the first partner in the bond
	 * @param thisBondIndTwo the atom index of the second partner in the bond
	 * @param thisBondOrder the bond order
	 */
	void setInterGroupBond(int thisBondIndOne, int thisBondIndTwo, int thisBondOrder);


	/**
	 * Sets the header information.
	 * @param rFree
	 * @param rWork
	 * @param resolution
	 * @param title
	 * @param depositionDate 
	 * @param experimnetalMethods
	 */
	void setHeaderInfo(float rFree, float rWork, float resolution, String title, String depositionDate, String[] experimnetalMethods);



}
