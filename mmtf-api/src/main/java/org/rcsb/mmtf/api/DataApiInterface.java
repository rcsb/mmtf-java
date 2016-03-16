package org.rcsb.mmtf.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.rcsb.mmtf.dataholders.BioAssemblyInfoNew;
import org.rcsb.mmtf.dataholders.Entity;
import org.rcsb.mmtf.dataholders.PDBGroup;

public interface DataApiInterface {

	int[] getCartnX();

	void setCartnX(int[] cartnX);

	int[] getCartnY();

	void setCartnY(int[] cartnY);

	int[] getCartnZ();

	void setCartnZ(int[] cartnZ);

	int[] getbFactor();

	void setbFactor(int[] bFactor);

	int[] getOccupancyArr();

	void setOccupancyArr(int[] occupancyArr);

	int[] getAtomId();

	void setAtomId(int[] atomId);

	char[] getAltId();

	void setAltId(char[] altId);

	char[] getInsCode();

	void setInsCode(char[] insCode);

	int[] getGroupNum();

	void setGroupNum(int[] groupNum);

	Map<Integer, PDBGroup> getGroupMap();

	void setGroupMap(Map<Integer, PDBGroup> groupMap);

	int[] getGroupList();

	void setGroupList(int[] groupList);

	int getLastAtomCount();

	void setLastAtomCount(int lastAtomCount);

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

	int getModelCounter();

	void setModelCounter(int modelCounter);

	int getGroupCounter();

	void setGroupCounter(int groupCounter);

	int getChainCounter();

	void setChainCounter(int chainCounter);

	int getAtomCounter();

	void setAtomCounter(int atomCounter);

	Set<String> getChainIdSet();

	void setChainIdSet(Set<String> chainIdSet);

	List<String> getSequenceInfo();

	void setSequenceInfo(List<String> sequenceInfo);

	String getMmtfVersion();

	void setMmtfVersion(String mmtfVersion);

	String getMmtfProducer();

	void setMmtfProducer(String mmtfProducer);

	List<String> getNucAcidList();

	void setNucAcidList(List<String> nucAcidList);

	String[] getChainDescription();

	void setChainDescription(String[] chainDescription);

	String[] getChainType();

	void setChainType(String[] chainType);

	Entity[] getEntityList();

	void setEntityList(Entity[] entityList);

	String getPdbId();

	void setPdbId(String pdbId);

}