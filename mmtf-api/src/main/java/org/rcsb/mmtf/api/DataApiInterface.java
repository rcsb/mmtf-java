package org.rcsb.mmtf.api;

import java.util.List;
import java.util.Map;

import org.rcsb.mmtf.dataholders.BioAssemblyData;
import org.rcsb.mmtf.dataholders.Entity;
import org.rcsb.mmtf.dataholders.PDBGroup;

/**
 * An interface describing the data API.
 * One can implement their  own implementation of this interface.
 * @author Anthony Bradley
 *
 */
public interface DataApiInterface {

	/**
	 * Returns an array of length N atoms of the X coordinates of the atoms as integers.
	 * They must be divided by 1000.0 to be in float form.
	 * @return
	 */
	int[] getCartnX();

	void setCartnX(int[] cartnX);


	/**
	 * Returns an array of length N atoms of the Y coordinates of the atoms as integers.
	 * They must be divided by 1000.0 to be in float form.
	 * @return
	 */
	int[] getCartnY();

	void setCartnY(int[] cartnY);


	/**
	 * Returns an array of length N atoms of the Z coordinates of the atoms as integers.
	 * They must be divided by 1000.0 to be in float form.
	 * @return
	 */
	int[] getCartnZ();

	void setCartnZ(int[] cartnZ);


	/**
	 * Returns an array of length N atoms of the B-factors of the atoms as integers.
	 * They must be divided by 100.0 to be in float form.
	 * @return
	 */
	int[] getbFactor();

	void setbFactor(int[] bFactor);

	/**
	 * Returns an array of length N atoms of the Occupancy of the atoms as integers.
	 * They must be divided by 100.0 to be in float form.
	 * @return
	 */	
	int[] getOccupancyArr();

	void setOccupancyArr(int[] occupancyArr);


	/**
	 * Returns an array of length N atoms of the serial ids of the atoms as integers.
	 * @return
	 */
	int[] getAtomId();

	void setAtomId(int[] atomId);


	/**
	 * Returns an array of length N atoms of the alternate location ids of the atoms as characters.
	 * '?' specifies a lack of alt id.
	 * @return
	 */
	char[] getAltId();

	void setAltId(char[] altId);


	/**
	 * Returns an array of length N atoms of the insertion codes of the atoms as characters.
	 * "?" specifies a lack of alt id.
	 * @return
	 */	
	char[] getInsCode();

	void setInsCode(char[] insCode);


	/**
	 * Returns a list of length N groups indicating the residue number for  each group.
	 * @return
	 */
	int[] getGroupNum();

	void setGroupNum(int[] groupNum);


	/**
	 * Returns the group map, mapping the numbers from getGroupNum to PDBGroup objects, which specify the atom names, 
	 * elements, bonds and charges for each group.
	 * @return
	 */
	Map<Integer, PDBGroup> getGroupMap();

	void setGroupMap(Map<Integer, PDBGroup> groupMap);


	/**
	 * Returns an array of length N groups indicating the index in the group map for each group.
	 * @return
	 */	
	int[] getGroupList();

	void setGroupList(int[] groupList);


	/**
	 * Returns an array of length N groups indicating the index in the Sequence for each group.
	 * -1 indicates the group is not present in the sequence. Indices are specified per chain.
	 * @return
	 */
	int[] getSeqResGroupList();

	void setSeqResGroupList(int[] seqResGroupList);




	/**
	 * Returns an array of Strings (length number of chains) for the public facing chain ids (auth ids).
	 * Each string is of length up to 4.
	 * @return
	 */	
	String[] getPublicChainIds();

	void setPublicChainIds(String[] publicChainIds);


	/**
	 * Returns an array of length N models, indicating the number of (internal) chains in each model.
	 * @return
	 */
	int[] getChainsPerModel();

	void setChainsPerModel(int[] chainsPerModel);


	/**
	 * Returns an array of length N chains, indicating the number of groups (residues) in each chain.
	 * @return
	 */	
	int[] getGroupsPerChain();

	void setGroupsPerChain(int[] groupsPerChain);


	/**
	 * Returns the space group of the structure. //TODO WHAT IS IT FOR NMR??
	 *
	 * @return
	 */
	String getSpaceGroup();

	void setSpaceGroup(String spaceGroup);


	/**
	 * Returns the 6 floats that describe the unit cell.
	 * @return
	 */
	List<Float> getUnitCell();

	void setUnitCell(List<Float> unitCell);


	// TODO CLEAN UP THIS WHOLE THING AND THEN DOCUMENT IT CORRECTLY
	/**
	 * Returns the bioassmebly information as a map
	 * @return
	 */
	List<BioAssemblyData> getBioAssembly();

	void setBioAssembly(List<BioAssemblyData> bioAssembly);


	/**
	 * Returns an array of length 2 * intergroup bonds of the bond indices.
	 * Each index corresponds to a the index of the atom in the total structure.
	 * @return
	 */
	int[] getInterGroupBondIndices();

	void setInterGroupBondIndices(int[] interGroupBondIndices);


	/**
	 * Returns an array of length intergroup bonds of the bond orders (1,2,3) for the bonds between groups as a list of integers.
	 * @return
	 */
	int[] getInterGroupBondOrders();

	void setInterGroupBondOrders(int[] interGroupBondOrders);


	/**
	 * Returns an array of length N chains for the internal chain ids (asym ids).
	 * Each string is of length up to 4.
	 * @return
	 */
	String[] getChainList();

	void setChainList(String[] chainList);



	/**
	 * Returns a list of length N chains indicating the sequences for each chain.
	 * Sequences are described using standard single letter codes. 
	 * @return
	 */
	List<String> getSequenceInfo();

	void setSequenceInfo(List<String> sequenceInfo);


	/**
	 * Returns the MMTF version number (from the specification).
	 * @return
	 */
	String getMmtfVersion();

	void setMmtfVersion(String mmtfVersion);


	/**
	 * Returns a string describing the producer of the MMTF process.
	 * @return
	 */
	String getMmtfProducer();

	void setMmtfProducer(String mmtfProducer);

	/**
	 * Returns the list of Entity objects for this structure.
	 * @return
	 */
	Entity[] getEntityList();

	void setEntityList(Entity[] entityList);


	/**
	 * Returns the four character string pdb id of the structure.
	 * @return
	 */
	String getPdbId();

	void setPdbId(String pdbId);
	


	/**
	 * Returns the number of models in the total structure
	 */
	int getNumModels();
	
	/**
	 * Returns the number of chains in the total structure
	 */
	int getNumChains();
	
	/**
	 * Returns the number of groups (residues) in the file
	 */
	int getNumResidues();
	

	/**
	 * Returns the number of atoms in the total structure
	 */
	int getNumAtoms();
}