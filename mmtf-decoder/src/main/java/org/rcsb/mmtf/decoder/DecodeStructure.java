package org.rcsb.mmtf.decoder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rcsb.mmtf.api.StructureDecoderInterface;
import org.rcsb.mmtf.dataholders.Entity;
import org.rcsb.mmtf.dataholders.PDBGroup;

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


	/** The struct inflator. */
	private StructureDecoderInterface structInflator;

	/** The api to the data */
	private SimpleDataApi dataApi;

	/* 
	 * Initialise the counters
	 */
	private int modelCounter = 0;
	private int chainCounter = 0;
	private int groupCounter = 0;
	private int atomCounter = 0;
	private int lastAtomCount = 0;
	private Set<String> chainIdSet;
	private String[] chainList;

	/**
	 * The constructor requires a byte array to fill the data. This will decompress the arrays using our bespoke methods.
	 * @param inputByteArr An unentropy encoded byte array with the data as found in the MMTF format
	 */
	public DecodeStructure(byte[] inputByteArr) {

		// Data api
		dataApi = new SimpleDataApi(inputByteArr);

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
		// Do any required preparation
		inputStructInflator.prepareStructure(dataApi.getNumAtoms(), dataApi.getNumResidues(),
				dataApi.getNumChains(), dataApi.getNumModels());
		// Now get the parsing parameters to do their thing
		useParseParams(parsingParams);
		// Now get the group map
		for (int modelChains: dataApi.getChainsPerModel()) {
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
		for (Entity entity : dataApi.getEntityList()) {
			String[] chainIdList = new String[entity.getChainIndexList().length];
			int counter = 0;
			for (int chainInd : entity.getChainIndexList()) {
				chainIdList[counter] = chainList[chainInd];
				counter++;
			}
			inputStructInflator.setEntityInfo(chainIdList, entity.getSequence(), entity.getDescription(), entity.getType());
		}
		// Now do any required cleanup
		inputStructInflator.cleanUpStructure();
	}

	/**
	 * Use the parsing parameters to set the scene.
	 * @param parsingParams
	 */
	private void useParseParams(ParsingParams parsingParams) {
		if (parsingParams.isParseInternal()) {
			System.out.println("Using asym ids");
			chainList = dataApi.getChainList();
		} else {
			System.out.println("Using auth ids");
			chainList = dataApi.getPublicChainIds();
		}    
	}


	/**
	 * Set the chain level information and then loop through the groups
	 * @param chainIndex
	 */
	private void addOrUpdateChainInfo(int chainIndex) {
		// Get the current c
		String currentChainId = chainList[chainIndex];
		int groupsThisChain = dataApi.getGroupsPerChain()[chainIndex];
		// If we've already seen this chain -> just update it
		if (chainIdSet.contains(currentChainId)) {
			structInflator.setChainInfo(currentChainId, groupsThisChain);
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
		int g = dataApi.getGroupList()[thisGroupNum];
		// Get this info
		PDBGroup currentGroup = dataApi.getGroupMap().get(g);
		List<String> atomInfo = currentGroup.getAtomInfo();
		int atomCount = atomInfo.size() / 2;
		int currentGroupNumber = dataApi.getGroupNum()[thisGroupNum];
		char insertionCode = dataApi.getInsCode()[thisGroupNum];
		structInflator.setGroupInfo(currentGroup.getGroupName(), currentGroupNumber, insertionCode,
				currentGroup.getChemCompType(), atomCount);
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
		int serialNumber = dataApi.getAtomId()[totalAtomCount];
		char alternativeLocationId = dataApi.getAltId()[totalAtomCount];
		float x = dataApi.getCartnX()[totalAtomCount] / COORD_B_FACTOR_DIVIDER;
		float z = dataApi.getCartnZ()[totalAtomCount] / COORD_B_FACTOR_DIVIDER;
		float y = dataApi.getCartnY()[totalAtomCount] / COORD_B_FACTOR_DIVIDER;
		float occupancy = dataApi.getOccupancyArr()[totalAtomCount] / OCCUPANCY_DIVIDER;
		float temperatureFactor = dataApi.getbFactor()[totalAtomCount] / OCCUPANCY_DIVIDER;
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
			structInflator.setGroupBond(thisBondIndOne, thisBondIndTwo,
					thisBondOrder);
		}    
	}

	/**
	 * Generate inter group bonds
	 */
	private void addInterGroupBonds() {
		for (int i = 0; i < dataApi.getInterGroupBondOrders().length; i++) {
			structInflator.setInterGroupBond(dataApi.getInterGroupBondIndices()[i * 2],
					dataApi.getInterGroupBondIndices()[i * 2 + 1], dataApi.getInterGroupBondOrders()[i]);
		}    
	}

	/**
	 * Adds the crystallographic info to the structure
	 */
	private void addXtalographicInfo() {
		if(dataApi.getUnitCell()!=null){
			structInflator.setXtalInfo(dataApi.getSpaceGroup(), dataApi.getUnitCell());    
		}
	}

	/**
	 * Parses the bioassembly data and inputs it to the structure inflator
	 */
	private void generateBioAssembly() {
		structInflator.setBioAssemblyList(dataApi.getBioAssemblyList());    
	}



}
