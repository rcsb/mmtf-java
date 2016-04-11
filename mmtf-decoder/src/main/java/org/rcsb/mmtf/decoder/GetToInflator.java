package org.rcsb.mmtf.decoder;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.rcsb.mmtf.api.MmtfDecodedDataInterface;
import org.rcsb.mmtf.api.MmtfDecoderInterface;
import org.rcsb.mmtf.api.MmtfReader;

/**
 * Decode an MMTF structure using a structure inflator. 
 * The class also allows access to the unconsumed but parsed and inflated underlying data.
 * 
 * @author Anthony Bradley
 *
 */
public class GetToInflator implements MmtfReader {

	/** The struct inflator. */
	private MmtfDecoderInterface structInflator;

	/** The api to the data */
	private MmtfDecodedDataInterface dataApi;

	// Intialises the counters.
	private int modelCounter = 0;
	private int chainCounter = 0;
	private int groupCounter = 0;
	private int atomCounter = 0;
	private int currentAtomIndex = 0;
	private Set<String> chainIdSet;

	/**
	 * The constructor requires a byte array to fill the data. This will decompress the arrays using our bespoke methods.
	 * @param byteArray An unentropy encoded byte array with the data as found in the MMTF format
	 * @throws IOException 
	 */
	public GetToInflator() throws IOException {

	}

	/**
	 * Passes data from the data interface to the inflator interface.
	 */
	public void read(MmtfDecodedDataInterface inputApi, MmtfDecoderInterface inputInflator){
		// Set the api and the inflator
		dataApi = inputApi;
		structInflator = inputInflator;
		// Do any required preparation
		structInflator.initStructure(dataApi.getNumBonds(), dataApi.getNumAtoms(), dataApi.getNumGroups(), 
				dataApi.getNumChains(), dataApi.getNumModels(), dataApi.getStructureId());
		// Now add the atom information
		addAtomicInformation();
		// Now add the header information.
		addHeaderInfo();
		// Now set the crystallographic  information
		addXtalographicInfo();
		/// Now get the bioassembly information - only if parsing using AsymId
		generateBioAssembly();
		// Now add the other bonds between groups
		addInterGroupBonds();
		// Now add the entity info
		addEntityInfo();
		// Now do any required cleanup
		structInflator.finalizeStructure();
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
			int lastChainCounter = chainCounter;
			for (int chainIndex = lastChainCounter; chainIndex < totChainsThisModel;  chainIndex++) {
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
				chainIdList[counter] = dataApi.getChainIds()[chainInd];
				counter++;
			}
			structInflator.setEntityInfo(dataApi.getEntityChainIndexList(i), dataApi.getEntitySequence(i), dataApi.getEntityDescription(i), dataApi.getEntityType(i));
		}		
	}


	/**
	 * Add ancilliary header information to the structure
	 */
	private final void addHeaderInfo() {
		structInflator.setHeaderInfo(dataApi.getRfree(),dataApi.getRwork(), dataApi.getResolution(), 
				dataApi.getTitle(), dataApi.getDepositionDate(), dataApi.getExperimentalMethods());		
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
		if (chainIdSet.contains(currentChainId)) {
			structInflator.setChainInfo(currentChainId, currentChainName, groupsThisChain);
		} else {
			structInflator.setChainInfo(currentChainId, currentChainName, groupsThisChain);
			chainIdSet.add(currentChainId);
		}
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
		atomCounter = 0;
		// Now read the next atoms
		for (int i = 0; i < atomCount; i++) {
			addAtomData(dataApi.getGroupAtomNames(groupInd), dataApi.getGroupElementNames(groupInd), dataApi.getGroupAtomCharges(groupInd), currentAtomIndex);  
			currentAtomIndex++;
			// Now increment the atom counter for this group
			atomCounter++;
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
		String atomName = atomNames[atomCounter];
		String element = elementNames[atomCounter];
		int charge = atomCharges[atomCounter];
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

	/**
	 * Generate inter group bonds
	 * Bond indices are specified within the whole structure and start at 0.
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
				structInflator.setBioAssemblyTrans(i+1, dataApi.getChainIndexListForTransform(i, j), dataApi.getMatrixForTransform(i,j));    
			}
		}
	}




}
