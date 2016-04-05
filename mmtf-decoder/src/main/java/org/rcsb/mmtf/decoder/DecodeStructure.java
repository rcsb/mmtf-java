package org.rcsb.mmtf.decoder;

import java.util.HashSet;
import java.util.Set;

import org.rcsb.mmtf.api.DataApiInterface;
import org.rcsb.mmtf.api.StructureDecoderInterface;

/**
 * Decode an MMTF structure using a structure inflator. The class also allows access to the unconsumed but parsed and inflated underlying data.
 * 
 * @author Anthony Bradley
 *
 */
public class DecodeStructure {



	/** The struct inflator. */
	private StructureDecoderInterface structInflator;

	/** The api to the data */
	private DataApiInterface dataApi;

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
		inputStructInflator.prepareStructure(dataApi.getNumAtoms(), dataApi.getNumResidues(), dataApi.getNumChains(), dataApi.getNumModels(), dataApi.getPdbId());
		// Now get the parsing parameters to do their thing
		useParseParams(parsingParams);
		// Now add the atom information
		addAtomicInformation();
		// Now add the header information.
		addHeaderInfo();
		// Now set the crystallographic  information
		addXtalographicInfo();
		/// Now get the bioassembly information - only if parsing using AsymId
		if (parsingParams.isParseInternal()){
			generateBioAssembly();
		}
		// Now add the other bonds between groups
		addInterGroupBonds();
		// Now add the entity info
		addEntityInfo();
		// Now do any required cleanup
		structInflator.cleanUpStructure();
	}

	/**
	 * Add the main atomic information to the data model
	 */
	private final void addAtomicInformation() {
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
	}


	/**
	 * Add the entity information to a structure.
	 */
	private final void addEntityInfo() {
		for (int i=0; i<dataApi.getNumEntities(); i++) {
			String[] chainIdList = new String[dataApi.getEntityChainIndexList(i).length];
			int counter = 0;
			for (int chainInd : dataApi.getEntityChainIndexList(i)) {
				chainIdList[counter] = chainList[chainInd];
				counter++;
			}
			structInflator.setEntityInfo(chainIdList, dataApi.getEntitySequence(i), dataApi.getEntityDescription(i), dataApi.getEntityType(i));
		}		
	}


	/**
	 * Function to add ancilliary header information to the structure
	 */
	private final void addHeaderInfo() {
		structInflator.setHeaderInfo(dataApi.getRfree(),dataApi.getRwork(), dataApi.getResolution(), dataApi.getTitle(), dataApi.getExperimentalMethods());		
	}


	/**
	 * Use the parsing parameters to set the scene.
	 * @param parsingParams
	 */
	private final void useParseParams(ParsingParams parsingParams) {
		if (parsingParams.isParseInternal()) {
			System.out.println("Using asym ids");
			chainList = dataApi.getChainIds();
		} else {
			System.out.println("Using auth ids");
			chainList = dataApi.getChainNames();
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
		int groupInd = dataApi.getGroupIndices()[thisGroupNum];
		// Get this info
		int atomCount = dataApi.getNumAtomsInGroup(groupInd);
		int currentGroupNumber = dataApi.getResidueNums()[thisGroupNum];
		char insertionCode = dataApi.getInsCodes()[thisGroupNum];
		structInflator.setGroupInfo(dataApi.getGroupName(groupInd), currentGroupNumber, insertionCode,
				dataApi.getGroupChemCompType(groupInd), atomCount);
		// A counter for the atom information
		atomCounter = 0;
		// Now read the next atoms
		for (int i = lastAtomCount; i < lastAtomCount + atomCount; i++) {
			addAtomData(dataApi.getGroupAtomNames(groupInd), dataApi.getGroupElementNames(groupInd), dataApi.getGroupAtomCharges(groupInd), i);  
		}
		addGroupBonds(dataApi.getGroupBondIndices(groupInd), dataApi.getGroupBondOrders(groupInd));
		return atomCount;
	}


	/**
	 * Add atom level data for a given atom.
	 * @param currentPdbGroup The group being considered.
	 * @param atomInfo The list of strings containing atom level information.
	 * @param currentAtomIndex The index of the current Atom
	 */
	private void addAtomData(String[] atomNames, String[] elementNames, int[] atomCharges, int currentAtomIndex) {
		// Now get all the relevant atom level information here
		String atomName = atomNames[atomCounter];
		String element = elementNames[atomCounter];
		int charge = atomCharges[atomCounter];
		int serialNumber = dataApi.getAtomIds()[currentAtomIndex];
		char alternativeLocationId = dataApi.getAltLocIds()[currentAtomIndex];
		float x = dataApi.getXcoords()[currentAtomIndex];
		float z = dataApi.getZcoords()[currentAtomIndex];
		float y = dataApi.getYcoords()[currentAtomIndex];
		float occupancy = dataApi.getOccupancies()[currentAtomIndex];
		float temperatureFactor = dataApi.getBfactors()[currentAtomIndex];
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
	private void addGroupBonds(int[] bondInds, int[] bondOrders) {
		// Now add the bond information for this group
		for (int thisBond = 0; thisBond < bondOrders.length; thisBond++) {
			int thisBondOrder = bondOrders[thisBond];
			int thisBondIndOne = bondInds[thisBond * 2];
			int thisBondIndTwo = bondInds[(thisBond * 2 + 1)];
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
		for (int i=0; i<dataApi.getNumBioassemblies(); i++) {
			for(int j=0; j<dataApi.getNumTransInBioassembly(i); j++) {
				structInflator.setBioAssemblyTrans(i+1, dataApi.getChainIndexListForTrans(i, j), dataApi.getTransMatrixForTrans(i,j));    
			}
		}
	}



}
