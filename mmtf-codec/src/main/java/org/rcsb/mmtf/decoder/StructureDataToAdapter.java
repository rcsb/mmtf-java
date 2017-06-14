package org.rcsb.mmtf.decoder;

import org.rcsb.mmtf.api.StructureDataInterface;

import java.io.Serializable;

import org.rcsb.mmtf.api.StructureAdapterInterface;

/**
 * Pass data from a {@link StructureDataInterface} into a {@link StructureAdapterInterface}.
 * @author Anthony Bradley
 *
 */
public class StructureDataToAdapter implements Serializable {
	private static final long serialVersionUID = 1304850018382353781L;

	/** The struct inflator. */
	private StructureAdapterInterface structInflator;

	/** The api to the data */
	private StructureDataInterface dataApi;

	// Intialises the counters.
	private int modelCounter;
	private int chainCounter;
	private int groupCounter;
	private int atomInGroupCounter;
	private int atomInStructureCounter;
	

	/**
	 * Passes data from the data interface to the inflator interface.
	 * @param inputApi the interface to the decoded data
	 * @param inputInflator the interface to put the data into the client object
	 */
	public StructureDataToAdapter(StructureDataInterface inputApi, StructureAdapterInterface inputInflator){
		// Set the counters to zero
		modelCounter = 0;
		chainCounter = 0;
		groupCounter = 0;
		atomInGroupCounter = 0;
		atomInStructureCounter = 0;
		// Set the api and the inflator
		dataApi = inputApi;
		structInflator = inputInflator;
		// Do any required preparation
		structInflator.initStructure(dataApi.getNumBonds(), dataApi.getNumAtoms(), dataApi.getNumGroups(), 
				dataApi.getNumChains(), dataApi.getNumModels(), dataApi.getStructureId());
		// Now add the atom information
		addAtomicInformation();
		// Now add the header information.
		DecoderUtils.addHeaderInfo(dataApi, structInflator);
		// Now set the crystallographic  information
		DecoderUtils.addXtalographicInfo(dataApi, structInflator);
		/// Now get the bioassembly information - only if parsing using AsymId
		DecoderUtils.generateBioAssembly(dataApi, structInflator);
		// Now add the other bonds between groups
		DecoderUtils.addInterGroupBonds(dataApi, structInflator);
		// Now add the entity info
		DecoderUtils.addEntityInfo(dataApi, structInflator);
		// Now do any required cleanup
		structInflator.finalizeStructure();
	}

	/**
	 * Add the main atomic information to the data model
	 */
	private void addAtomicInformation() {
		for (int modelChains: dataApi.getChainsPerModel()) {
			structInflator.setModelInfo(modelCounter, modelChains);
			int totChainsThisModel = chainCounter + modelChains;
			int lastChainCounter = chainCounter;
			for (int chainIndex = lastChainCounter; chainIndex < totChainsThisModel;  chainIndex++) {
				addOrUpdateChainInfo(chainIndex);
			}
			modelCounter++;
		}		
	}

	/**
	 * Set the chain level information and then loop through the groups
	 * @param chainIndex the chain index to be created or updated.
	 */
	private void addOrUpdateChainInfo(int chainIndex) {
		// Get the current c
		String currentChainId = dataApi.getChainIds()[chainIndex];
		String currentChainName = dataApi.getChainNames()[chainIndex];
		int groupsThisChain = dataApi.getGroupsPerChain()[chainIndex];
		// If we've already seen this chain -> just update it
		structInflator.setChainInfo(currentChainId, currentChainName, groupsThisChain);
		int nextInd = groupCounter + groupsThisChain;
		int lastGroupCount = groupCounter;
		// Now iteratr over the group
		for (int currentGroupNumber = lastGroupCount; currentGroupNumber < nextInd; currentGroupNumber++) {
			addGroup(currentGroupNumber);
			groupCounter++;
		}    
		chainCounter++;
	}

	/**
	 * Add a group to the structure - return the number of atoms in the structure.
	 * @param currentGroupIndex the integer indicating the index of the group to be added.
	 * @return an integer for the number of atoms in the structure.
	 */
	private int addGroup(int currentGroupIndex) {
		// Now get the group
		int groupInd = dataApi.getGroupTypeIndices()[currentGroupIndex];
		// Get this info
		int atomCount = dataApi.getNumAtomsInGroup(groupInd);
		int currentGroupNumber = dataApi.getGroupIds()[currentGroupIndex];
		char insertionCode = dataApi.getInsCodes()[currentGroupIndex];
		structInflator.setGroupInfo(dataApi.getGroupName(groupInd), currentGroupNumber, insertionCode,
				dataApi.getGroupChemCompType(groupInd), atomCount, dataApi.getNumBonds(), dataApi.getGroupSingleLetterCode(groupInd),
				dataApi.getGroupSequenceIndices()[currentGroupIndex], dataApi.getSecStructList()[currentGroupIndex]);
		// A counter for the atom information
		atomInGroupCounter = 0;
		// Now read the next atoms
		for (int i = 0; i < atomCount; i++) {
			addAtomData(dataApi.getGroupAtomNames(groupInd), dataApi.getGroupElementNames(groupInd), dataApi.getGroupAtomCharges(groupInd), atomInStructureCounter);  
			atomInStructureCounter++;
			// Now increment the atom counter for this group
			atomInGroupCounter++;
		}
		addGroupBonds(dataApi.getGroupBondIndices(groupInd), dataApi.getGroupBondOrders(groupInd));
		return atomCount;
	}


	/**
	 * Add atom level data for a given atom.
	 * @param currentPdbGroup the group being considered.
	 * @param atomInfo the list of strings containing atom level information.
	 * @param currentAtomIndex the index of the current Atom
	 */
	private void addAtomData(String[] atomNames, String[] elementNames, int[] atomCharges, 
			int currentAtomIndex) {
		// Now get all the relevant atom level information here
		String atomName = atomNames[atomInGroupCounter];
		String element = elementNames[atomInGroupCounter];
		int charge = atomCharges[atomInGroupCounter];
		char alternativeLocationId = dataApi.getAltLocIds()[currentAtomIndex];
		int serialNumber = dataApi.getAtomIds()[currentAtomIndex];
		float x = dataApi.getxCoords()[currentAtomIndex];
		float z = dataApi.getzCoords()[currentAtomIndex];
		float y = dataApi.getyCoords()[currentAtomIndex];
		float occupancy = dataApi.getOccupancies()[currentAtomIndex];
		float temperatureFactor = dataApi.getbFactors()[currentAtomIndex];
		structInflator.setAtomInfo(atomName, serialNumber, alternativeLocationId,
				x, y, z, occupancy, temperatureFactor, element, charge);
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
			int thisBondIndTwo = bondInds[thisBond * 2 + 1];
			structInflator.setGroupBond(thisBondIndOne, thisBondIndTwo,
					thisBondOrder);
		}    
	}



}
