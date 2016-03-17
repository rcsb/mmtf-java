package org.rcsb.mmtf.decoder;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.rcsb.mmtf.api.DataApiInterface;
import org.rcsb.mmtf.api.StructureDecoderInterface;
import org.rcsb.mmtf.arraydecompressors.DeltaDeCompress;
import org.rcsb.mmtf.arraydecompressors.RunLengthDecodeInt;
import org.rcsb.mmtf.arraydecompressors.RunLengthDecodeString;
import org.rcsb.mmtf.arraydecompressors.RunLengthDelta;
import org.rcsb.mmtf.dataholders.BioAssemblyData;
import org.rcsb.mmtf.dataholders.Entity;
import org.rcsb.mmtf.dataholders.MmtfBean;
import org.rcsb.mmtf.dataholders.PDBGroup;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Decode an MMTF structure using a structure inflator. The class also allows access to the unconsumed but parsed and inflated underlying data.
 * 
 * @author Anthony Bradley
 *
 */
public class DecodeStructure {

	/** The number to divide coordinate and b factor values by. */
	private static final float COORD_B_FACTOR_DIVIDER = (float) 1000.0;
	/** The number to divide occupancy values by. */
	private static final float OCCUPANCY_DIVIDER = (float) 100.0;


	/** The cartn x. */
	private int[] cartnX;

	/** The cartn y. */
	private int[] cartnY;

	/** The cartn z. */
	private int[] cartnZ;

	/** The b factor. */
	private int[] bFactor;

	/** The occupancy arr. */
	private int[] occupancyArr;

	/** The atom id. */
	private int[] atomId;

	/** The alt id. */
	private char[] altId;

	/** The ins code. */
	private char[] insCode;

	/** The group num. */
	private int[] groupNum;

	/** The group map. */
	private Map<Integer, PDBGroup> groupMap;

	/** The group list. */
	private int[] groupList;

	/** The last count. */
	private int lastAtomCount;

	/** The sequence ids of the groups */
	private int[] seqResGroupList;

	/** The internal chain ids*/
	private String[] internalChainIds;

	/** The public facing chain ids*/
	private String[] publicChainIds;

	/** The number of chains per model*/
	private int[] chainsPerModel;

	/** The number of groups per (internal) chain*/
	private int[] groupsPerChain;

	/** The space group of the structure*/
	private String spaceGroup;

	/** The unit cell of the structure*/
	private List<Float> unitCell;

	/** The bioassembly information for the structure*/
	private Map<Integer, BioAssemblyData> bioAssembly;

	/** The bond indices for bonds between groups*/
	private int[] interGroupBondIndices;

	/** The bond orders for bonds between groups*/
	private int[] interGroupBondOrders;

	/** The chosen list of chain ids */
	private String[] chainList;

	/** The counter for the models */
	private int modelCounter;

	/** The counter for the groups (residues) */
	private int groupCounter;

	/** The counter for the chains */
	private int chainCounter;

	/** The counter for atoms in a group */
	private int atomCounter;

	/** A unique set of lists for each model */
	private Set<String> chainIdSet;

	/** The sequence information. An entry for each chain. In a list.  */
	private List<String> sequenceInfo;

	/** The mmtf version */
	private String mmtfVersion;

	/** The mmtf prodcuer */
	private String mmtfProducer;

	/** A list containing pdb group names for nucleic acids */
	List<String> nucAcidList = new ArrayList<>();

	/** The pdbx_description of a given chain (entity) */
	private String[] chainDescription;

	/** The type of a given chain (entity) */
	private String[] chainType;

	/** The list of entities in this structure. */
	private Entity[] entityList;

	/** The PDB id	 */
	private String pdbId;


	/** The struct inflator. */
	private StructureDecoderInterface structInflator;


	/**
	 * The constructor requires a byte array to fill the data. This will decompress the arrays using our bespoke methods.
	 * @param inputByteArr An unentropy encoded byte array with the data as found in the MMTF format
	 */
	public DecodeStructure(byte[] inputByteArr) {
		// Get the decompressors to build in the data structure
		DeltaDeCompress deltaDecompress = new DeltaDeCompress();
		RunLengthDelta intRunLengthDelta = new RunLengthDelta();
		RunLengthDecodeInt intRunLength = new RunLengthDecodeInt();
		RunLengthDecodeString stringRunlength = new RunLengthDecodeString();
		DecoderUtils decoderUtils = new DecoderUtils();
		MmtfBean inputData = null;
		try {
			inputData = new ObjectMapper(new MessagePackFactory()).readValue(inputByteArr, MmtfBean.class);
		} catch (IOException e) {
			// 
			System.out.println("Error converting Byte array to message pack. IOError");
			e.printStackTrace();
			throw new RuntimeException();
		}
		// Get the data
		try {
			groupList = decoderUtils.bytesToInts(inputData.getGroupTypeList());
			// Read the byte arrays as int arrays
			cartnX = deltaDecompress.decompressByteArray(inputData.getxCoordBig(),
					inputData.getxCoordSmall());
			cartnY = deltaDecompress.decompressByteArray(inputData.getyCoordBig(),
					inputData.getyCoordSmall());
			cartnZ = deltaDecompress.decompressByteArray(inputData.getzCoordBig(),
					inputData.getzCoordSmall());
			bFactor =  deltaDecompress.decompressByteArray(inputData.getbFactorBig(),
					inputData.getbFactorSmall());
			occupancyArr = intRunLength.decompressByteArray(inputData.getOccList());
			atomId = intRunLengthDelta.decompressByteArray(inputData.getAtomIdList());
			altId = stringRunlength.stringArrayToChar(
					(ArrayList<String>) inputData.getAltLabelList());
			// Get the insertion code
			insCode = stringRunlength.stringArrayToChar(
					(ArrayList<String>) inputData.getInsCodeList());
			// Get the groupNumber
			groupNum = intRunLengthDelta.decompressByteArray(
					inputData.getGroupIdList());
			groupMap = inputData.getGroupMap();
			// Get the seqRes groups
			seqResGroupList = intRunLengthDelta.decompressByteArray(inputData.getSeqResIdList());
			// Get the number of chains per model
			chainsPerModel = inputData.getChainsPerModel();
			groupsPerChain = inputData.getGroupsPerChain();
			// Get the internal and public facing chain ids
			publicChainIds = decoderUtils.decodeChainList(inputData.getChainNameList());
			internalChainIds = decoderUtils.decodeChainList(inputData.getChainIdList());
			spaceGroup = inputData.getSpaceGroup();
			unitCell = inputData.getUnitCell();
			bioAssembly  = inputData.getBioAssembly();
			interGroupBondIndices = decoderUtils.bytesToInts(inputData.getBondAtomList());
			interGroupBondOrders = decoderUtils.bytesToByteInts(inputData.getBondOrderList());
			sequenceInfo = inputData.getChainSeqList();
			mmtfVersion = inputData.getMmtfVersion();
			mmtfProducer = inputData.getMmtfProducer();
			entityList = inputData.getEntityList();

		}
		catch (IOException ioException){
			System.out.println("Error reading in byte arrays from message pack");
			ioException.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * Fill the API to the data.
	 * @param dataApi
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public final void fillDataApi(DataApiInterface dataApi) throws IllegalAccessException, InvocationTargetException {
		// Currently we have a one to one mapping (for setter functions). Getter functions can then alter
		BeanUtils.copyProperties(dataApi, this);
	}


	/**
	 * Generate a structure from bytes using a structure inflator.
	 *
	 * @param myInBytes the my in bytes
	 * @param inputStructInflator the input struct inflator
	 * @param parsingParams the parsing params
	 */
	public final void getStructFromByteArray(final StructureDecoderInterface inputStructInflator, final ParsingParams parsingParams) {    
		// Set the inflator
		structInflator = inputStructInflator;
		// Now get the parsing parameters to do their thing
		useParseParams(parsingParams);
		// Now get the group map
		for (int modelChains: chainsPerModel) {
			structInflator.setModelInfo(modelCounter, modelChains);
			// A list to check if we need to set or update the chains
			chainIdSet = new HashSet<>();
			int totChainsThisModel = chainCounter + modelChains;
			for (int chainIndex = chainCounter; chainIndex < totChainsThisModel;  chainIndex++) {
				addOrUpdateChainInfo(chainIndex);
			}
			modelCounter++;
		}
		// Now set the crystallographic  information
		addXtalographicInfo();
		/// Now get the bioassembly information
		generateBioAssembly();
		// Now add the other bonds between groups
		addInterGroupBonds();
	}

	/**
	 * Use the parsing parameters to set the scene.
	 * @param parsingParams
	 */
	private void useParseParams(ParsingParams parsingParams) {
		if (parsingParams.isParseInternal()) {
			System.out.println("Using asym ids");
			chainList = internalChainIds;
		} else {
			System.out.println("Using auth ids");
			chainList = publicChainIds;
		}    
	}


	/**
	 * Set the chain level information and then loop through the groups
	 * @param chainIndex
	 */
	private void addOrUpdateChainInfo(int chainIndex) {
		// Get the current c
		String currentChainId = chainList[chainIndex];
		int groupsThisChain = groupsPerChain[chainIndex];
		// If we've already seen this chain -> just update it
		if (chainIdSet.contains(currentChainId)) {
			structInflator.updateChainInfo(currentChainId, groupsThisChain);
		} else {
			structInflator.setChainInfo(currentChainId, groupsThisChain);
			chainIdSet.add(currentChainId);
		}
		int nextInd = groupCounter + groupsThisChain;
		// Now iteratr over the group
		for (int currentGroupNumber = groupCounter; currentGroupNumber < nextInd; currentGroupNumber++) {
			groupCounter++;
			int atomCount = addGroup(currentGroupNumber);
			lastAtomCount += atomCount;
		}    
		chainCounter++;
	}

	/**
	 * Adds the group.
	 *
	 * @param thisGroupNum the this group num
	 * @param nucAcidList the nuc acid list
	 * @return the int
	 */
	private int addGroup(final int thisGroupNum) {
		// Now get the group
		int g = groupList[thisGroupNum];
		// Get this info
		PDBGroup currentGroup = groupMap.get(g);
		List<String> atomInfo = currentGroup.getAtomInfo();
		int atomCount = atomInfo.size() / 2;
		int thsG = groupNum[thisGroupNum];
		char thsIns = insCode[lastAtomCount];
		structInflator.setGroupInfo(currentGroup.getGroupName(), thsG, thsIns, currentGroup.getChemCompType(), atomCount);
		// A counter for the atom information
		atomCounter = 0;
		// Now read the next atoms
		for (int i = lastAtomCount; i < lastAtomCount + atomCount; i++) {
			addAtomData(currentGroup, atomInfo, i);  
		}
		addGroupBonds(currentGroup.getBondIndices(), currentGroup.getBondOrders());
		return atomCount;
	}


	/**
	 * Add atom level data for a given atom.
	 * @param currentPdbGroup The group being considered.
	 * @param atomInfo The list of strings containing atom level information.
	 * @param totalAtomCount The total count of atoms
	 */
	private void addAtomData(PDBGroup currentPdbGroup, List<String> atomInfo, int totalAtomCount) {
		// Now get all the relevant atom level information here
		String atomName = atomInfo.get(atomCounter * 2 + 1);
		String element = atomInfo.get(atomCounter * 2);
		int charge = currentPdbGroup.getAtomCharges().get(atomCounter);
		int serialNumber = atomId[totalAtomCount];
		char alternativeLocationId = altId[totalAtomCount];
		float x = cartnX[totalAtomCount] / COORD_B_FACTOR_DIVIDER;
		float z = cartnZ[totalAtomCount] / COORD_B_FACTOR_DIVIDER;
		float y = cartnY[totalAtomCount] / COORD_B_FACTOR_DIVIDER;
		float occupancy = occupancyArr[totalAtomCount] / OCCUPANCY_DIVIDER;
		float temperatureFactor = bFactor[totalAtomCount] / OCCUPANCY_DIVIDER;
		structInflator.setAtomInfo(atomName, serialNumber, alternativeLocationId,
				x, y, z, occupancy, temperatureFactor, element, charge);
		// Now increment the atom counter for this group
		atomCounter++;
	}

	/**
	 * Adds bond information for a group (residue).
	 * @param bondInds A list of integer pairs. Each pair indicates the indices for the bonds.
	 * Bond indices are specified internally within the group and start at 0.
	 * @param bondOrders A list of integers specifying the bond orders for each bond.
	 */
	private void addGroupBonds(List<Integer> bondInds, List<Integer> bondOrders) {
		// Now add the bond information for this group
		for (int thisBond = 0; thisBond < bondOrders.size(); thisBond++) {
			int thisBondOrder = bondOrders.get(thisBond);
			int thisBondIndOne = bondInds.get(thisBond * 2);
			int thisBondIndTwo = bondInds.get(thisBond * 2 + 1);
			structInflator.setGroupBonds(thisBondIndOne, thisBondIndTwo,
					thisBondOrder);
		}    
	}

	/**
	 * Generate inter group bonds
	 */
	private void addInterGroupBonds() {
		for (int i = 0; i < interGroupBondOrders.length; i++) {
			structInflator.setInterGroupBonds(interGroupBondIndices[i * 2],
					interGroupBondIndices[i * 2 + 1], interGroupBondOrders[i]);
		}    
	}

	/**
	 * Adds the crystallographic info to the structure
	 */
	private void addXtalographicInfo() {
		structInflator.setXtalInfo(spaceGroup, unitCell);    
	}

	/**
	 * Parses the bioassembly data and inputs it to the structure inflator
	 */
	private void generateBioAssembly() {
		structInflator.setBioAssembly(bioAssembly);    
	}



	public int[] getCartnX() {
		return cartnX;
	}

	public void setCartnX(int[] cartnX) {
		this.cartnX = cartnX;
	}

	public int[] getCartnY() {
		return cartnY;
	}

	public void setCartnY(int[] cartnY) {
		this.cartnY = cartnY;
	}

	public int[] getCartnZ() {
		return cartnZ;
	}

	public void setCartnZ(int[] cartnZ) {
		this.cartnZ = cartnZ;
	}

	public int[] getbFactor() {
		return bFactor;
	}

	public void setbFactor(int[] bFactor) {
		this.bFactor = bFactor;
	}

	public int[] getOccupancyArr() {
		return occupancyArr;
	}

	public void setOccupancyArr(int[] occupancyArr) {
		this.occupancyArr = occupancyArr;
	}

	public int[] getAtomId() {
		return atomId;
	}

	public void setAtomId(int[] atomId) {
		this.atomId = atomId;
	}

	public char[] getAltId() {
		return altId;
	}

	public void setAltId(char[] altId) {
		this.altId = altId;
	}

	public char[] getInsCode() {
		return insCode;
	}

	public void setInsCode(char[] insCode) {
		this.insCode = insCode;
	}

	public int[] getGroupNum() {
		return groupNum;
	}

	public void setGroupNum(int[] groupNum) {
		this.groupNum = groupNum;
	}

	public Map<Integer, PDBGroup> getGroupMap() {
		return groupMap;
	}

	public void setGroupMap(Map<Integer, PDBGroup> groupMap) {
		this.groupMap = groupMap;
	}

	public int[] getGroupList() {
		return groupList;
	}

	public void setGroupList(int[] groupList) {
		this.groupList = groupList;
	}

	public int getLastAtomCount() {
		return lastAtomCount;
	}

	public void setLastAtomCount(int lastAtomCount) {
		this.lastAtomCount = lastAtomCount;
	}

	public int[] getSeqResGroupList() {
		return seqResGroupList;
	}

	public void setSeqResGroupList(int[] seqResGroupList) {
		this.seqResGroupList = seqResGroupList;
	}

	public String[] getInternalChainIds() {
		return internalChainIds;
	}

	public void setInternalChainIds(String[] internalChainIds) {
		this.internalChainIds = internalChainIds;
	}

	public String[] getPublicChainIds() {
		return publicChainIds;
	}

	public void setPublicChainIds(String[] publicChainIds) {
		this.publicChainIds = publicChainIds;
	}

	public int[] getChainsPerModel() {
		return chainsPerModel;
	}

	public void setChainsPerModel(int[] chainsPerModel) {
		this.chainsPerModel = chainsPerModel;
	}

	public int[] getGroupsPerChain() {
		return groupsPerChain;
	}

	public void setGroupsPerChain(int[] groupsPerChain) {
		this.groupsPerChain = groupsPerChain;
	}

	public String getSpaceGroup() {
		return spaceGroup;
	}

	public void setSpaceGroup(String spaceGroup) {
		this.spaceGroup = spaceGroup;
	}

	public List<Float> getUnitCell() {
		return unitCell;
	}

	public void setUnitCell(List<Float> unitCell) {
		this.unitCell = unitCell;
	}

	public Map<Integer, BioAssemblyData> getBioAssembly() {
		return bioAssembly;
	}

	public void setBioAssembly(Map<Integer, BioAssemblyData> bioAssembly) {
		this.bioAssembly = bioAssembly;
	}

	public int[] getInterGroupBondIndices() {
		return interGroupBondIndices;
	}

	public void setInterGroupBondIndices(int[] interGroupBondIndices) {
		this.interGroupBondIndices = interGroupBondIndices;
	}

	public int[] getInterGroupBondOrders() {
		return interGroupBondOrders;
	}

	public void setInterGroupBondOrders(int[] interGroupBondOrders) {
		this.interGroupBondOrders = interGroupBondOrders;
	}

	public String[] getChainList() {
		return chainList;
	}

	public void setChainList(String[] chainList) {
		this.chainList = chainList;
	}

	public int getModelCounter() {
		return modelCounter;
	}

	public void setModelCounter(int modelCounter) {
		this.modelCounter = modelCounter;
	}

	public int getGroupCounter() {
		return groupCounter;
	}

	public void setGroupCounter(int groupCounter) {
		this.groupCounter = groupCounter;
	}

	public int getChainCounter() {
		return chainCounter;
	}

	public void setChainCounter(int chainCounter) {
		this.chainCounter = chainCounter;
	}

	public int getAtomCounter() {
		return atomCounter;
	}

	public void setAtomCounter(int atomCounter) {
		this.atomCounter = atomCounter;
	}

	public Set<String> getChainIdSet() {
		return chainIdSet;
	}

	public void setChainIdSet(Set<String> chainIdSet) {
		this.chainIdSet = chainIdSet;
	}

	public List<String> getSequenceInfo() {
		return sequenceInfo;
	}

	public void setSequenceInfo(List<String> sequenceInfo) {
		this.sequenceInfo = sequenceInfo;
	}

	public String getMmtfVersion() {
		return mmtfVersion;
	}

	public void setMmtfVersion(String mmtfVersion) {
		this.mmtfVersion = mmtfVersion;
	}

	public String getMmtfProducer() {
		return mmtfProducer;
	}

	public void setMmtfProducer(String mmtfProducer) {
		this.mmtfProducer = mmtfProducer;
	}

	public List<String> getNucAcidList() {
		return nucAcidList;
	}

	public void setNucAcidList(List<String> nucAcidList) {
		this.nucAcidList = nucAcidList;
	}

	public String[] getChainDescription() {
		return chainDescription;
	}

	public void setChainDescription(String[] chainDescription) {
		this.chainDescription = chainDescription;
	}

	public String[] getChainType() {
		return chainType;
	}

	public void setChainType(String[] chainType) {
		this.chainType = chainType;
	}

	public Entity[] getEntityList() {
		return entityList;
	}

	public void setEntityList(Entity[] entityList) {
		this.entityList = entityList;
	}

	public String getPdbId() {
		return pdbId;
	}

	public void setPdbId(String pdbId) {
		this.pdbId = pdbId;
	}

}
