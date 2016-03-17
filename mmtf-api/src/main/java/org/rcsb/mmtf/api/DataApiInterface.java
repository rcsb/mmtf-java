package org.rcsb.mmtf.api;

import java.util.List;
import java.util.Map;

import org.rcsb.mmtf.dataholders.BioAssemblyInfoNew;
import org.rcsb.mmtf.dataholders.Entity;
import org.rcsb.mmtf.dataholders.PDBGroup;

public interface DataApiInterface {

	// TODO Needs a test to ensure that none of the fields are empty using a standard converson
	/**
	 * Returns a list of length N atoms of the X coords of the atoms as Integers.
	 * They must be divided by 1000.0 to be in float form.
	 * @return
	 */
	int[] getCartnX();

	void setCartnX(int[] cartnX);

	
	/**
	 * Returns a list of length N atoms of the Y coords of the atoms as Integers.
	 * They must be divided by 1000.0 to be in float form.
	 * @return
	 */
	int[] getCartnY();

	void setCartnY(int[] cartnY);

	
	/**
	 * Returns a list of length N atoms of the Z coords of the atoms as Integers.
	 * They must be divided by 1000.0 to be in float form.
	 * @return
	 */
	int[] getCartnZ();

	void setCartnZ(int[] cartnZ);

	
	/**
	 * Returns a list of length N atoms of the B-factors of the atoms as Integers.
	 * They must be divided by 100.0 to be in float form.
	 * @return
	 */
	int[] getbFactor();

	void setbFactor(int[] bFactor);

	/**
	 * Returns a list of length N atoms of the Occupancy of the atoms as Integers.
	 * They must be divided by 100.0 to be in float form.
	 * @return
	 */	
	int[] getOccupancyArr();

	void setOccupancyArr(int[] occupancyArr);

	
	/**
	 * Returns a list of length N atoms of the serial ids of the atoms as Integers.
	 * @return
	 */
	int[] getAtomId();

	void setAtomId(int[] atomId);

	
	/**
	 * Returns a list of length N atoms of the alternate location ids of the atoms as characters.
	 * "?" specifies a lack of alt id.
	 * @return
	 */
	char[] getAltId();

	void setAltId(char[] altId);

	
	/**
	 * Returns a list of length N atoms of the insertion codes of the atoms as characters.
	 * "?" specifies a lack of alt id.
	 * @return
	 */	
	char[] getInsCode();

	void setInsCode(char[] insCode);

	
	// TODO COMPLETE THE DOCUMENTATION
	int[] getGroupNum();

	void setGroupNum(int[] groupNum);

	
	
	Map<Integer, PDBGroup> getGroupMap();

	void setGroupMap(Map<Integer, PDBGroup> groupMap);

	
	
	int[] getGroupList();

	void setGroupList(int[] groupList);

	
	
	int[] getSeqResGroupList();

	void setSeqResGroupList(int[] seqResGroupList);

	
	
	String[] getInternalChainIds();

	void setInternalChainIds(String[] internalChainIds);

	
	
	String[] getPublicChainIds();

	void setPublicChainIds(String[] publicChainIds);

	
	
	int[] getChainsPerModel();

	void setChainsPerModel(int[] chainsPerModel);

	
	
	int[] getGroupsPerChain();

	void setGroupsPerChain(int[] groupsPerChain);

	
	
	String getSpaceGroup();

	void setSpaceGroup(String spaceGroup);

	
	
	List<Float> getUnitCell();

	void setUnitCell(List<Float> unitCell);

	
	
	Map<Integer, BioAssemblyInfoNew> getBioAssembly();

	void setBioAssembly(Map<Integer, BioAssemblyInfoNew> bioAssembly);

	
	
	int[] getInterGroupBondIndices();

	void setInterGroupBondIndices(int[] interGroupBondIndices);

	
	
	int[] getInterGroupBondOrders();

	void setInterGroupBondOrders(int[] interGroupBondOrders);

	
	
	String[] getChainList();

	void setChainList(String[] chainList);

	
	

	List<String> getSequenceInfo();

	void setSequenceInfo(List<String> sequenceInfo);
	
	

	String getMmtfVersion();

	void setMmtfVersion(String mmtfVersion);

	
	
	String getMmtfProducer();

	void setMmtfProducer(String mmtfProducer);
	

	Entity[] getEntityList();

	void setEntityList(Entity[] entityList);

	
	
	String getPdbId();

	void setPdbId(String pdbId);

}