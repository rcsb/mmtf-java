package org.rcsb.mmtf.encoder;

import java.util.ArrayList;
import java.util.List;

import org.rcsb.mmtf.api.StructureDataInterface;
import org.rcsb.mmtf.decoder.DecoderUtils;

/**
 * Convert a full format of the file to a reduced format.
 * @author Anthony Bradley
 *
 */
public class ReducedEncoder {

	private static final String CALPHA_NAME = "CA";
	private static final String CARBON_ELEMENT = "C";
	private static final String PHOSPHATE_NAME = "P";
	private static final String PHOSPHATE_ELEMENT = "P";

	/**
	 * Get the reduced form of the input {@link StructureDataInterface}.
	 * @param structureDataInterface the input {@link StructureDataInterface} 
	 * @return the reduced form of the {@link StructureDataInterface} as another {@link StructureDataInterface}
	 */
	public static StructureDataInterface getReduced(StructureDataInterface structureDataInterface) {
		// The transmission of the data goes through this
		AdapterToStructureData adapterToStructureData = new AdapterToStructureData();
		SummaryData dataSummary = getDataSummaryData(structureDataInterface);
		adapterToStructureData.initStructure(dataSummary.numBonds, dataSummary.numAtoms, dataSummary.numGroups, 
				dataSummary.numChains, structureDataInterface.getNumModels(), structureDataInterface.getStructureId());
		DecoderUtils.addXtalographicInfo(structureDataInterface, adapterToStructureData);
		DecoderUtils.addHeaderInfo(structureDataInterface, adapterToStructureData);
		DecoderUtils.generateBioAssembly(structureDataInterface, adapterToStructureData);		
		DecoderUtils.addEntityInfo(structureDataInterface, adapterToStructureData);
		// Loop through the Structure data interface this with the appropriate data
		int atomCounter=-1;
		int groupCounter=-1;
		int chainCounter=-1;
		for (int i=0; i<structureDataInterface.getNumModels(); i++){
			int numChains = structureDataInterface.getChainsPerModel()[i];
			adapterToStructureData.setModelInfo(i, numChains);
			for(int j=0; j<numChains; j++){
				chainCounter++;
				String chainType = EncoderUtils.getTypeFromChainId(structureDataInterface, chainCounter);
				int numGroups=0;
				for(int k=0; k<structureDataInterface.getGroupsPerChain()[chainCounter]; k++){
					groupCounter++;
					int groupType = structureDataInterface.getGroupTypeIndices()[groupCounter];
					List<Integer> atomIndicesToAdd = getIndicesToAdd(structureDataInterface, groupType, chainType);
					int bondsToAdd = findBondsToAdd(atomIndicesToAdd, structureDataInterface, groupType);
					// If there's an atom to add in this group - add it
					if(atomIndicesToAdd.size()>0){
						adapterToStructureData.setGroupInfo(structureDataInterface.getGroupName(groupType), structureDataInterface.getGroupIds()[groupCounter], 
								structureDataInterface.getInsCodes()[groupCounter], structureDataInterface.getGroupChemCompType(groupType), atomIndicesToAdd.size(),
								bondsToAdd, structureDataInterface.getGroupSingleLetterCode(groupType), structureDataInterface.getGroupSequenceIndices()[groupCounter], 
								structureDataInterface.getSecStructList()[groupCounter],structureDataInterface.getGroupChemCompName(groupType));
						numGroups++;
					}
					for(int l=0; l<structureDataInterface.getNumAtomsInGroup(groupType);l++){
						atomCounter++;
						if(atomIndicesToAdd.contains(l)){
							adapterToStructureData.setAtomInfo(structureDataInterface.getGroupAtomNames(groupType)[l], structureDataInterface.getAtomIds()[atomCounter], structureDataInterface.getAltLocIds()[atomCounter], 
									structureDataInterface.getxCoords()[atomCounter], structureDataInterface.getyCoords()[atomCounter], structureDataInterface.getzCoords()[atomCounter], 
									structureDataInterface.getOccupancies()[atomCounter], structureDataInterface.getbFactors()[atomCounter], structureDataInterface.getGroupElementNames(groupType)[l], structureDataInterface.getGroupAtomCharges(groupType)[l]);
						}
					}
					if(bondsToAdd>0){
						for(int l=0; l<structureDataInterface.getGroupBondOrders(groupType).length; l++){
							int bondOrder = structureDataInterface.getGroupBondOrders(groupType)[l];
							int bondIndOne = structureDataInterface.getGroupBondIndices(groupType)[l*2];
							int bondIndTwo = structureDataInterface.getGroupBondIndices(groupType)[l*2+1];
							adapterToStructureData.setGroupBond(bondIndOne, bondIndTwo, bondOrder);
						}
					}
				}
				adapterToStructureData.setChainInfo(structureDataInterface.getChainIds()[chainCounter],
						structureDataInterface.getChainNames()[chainCounter], numGroups);
			}
		}
		adapterToStructureData.finalizeStructure();
		// Return the AdapterToStructureData
		return adapterToStructureData;
	}

	/**
	 * Find if bonds need adding - to be used in later processing.
	 * @param indicesToAdd the indices of the atoms to add
	 * @param structureDataInterface the {@link StructureDataInterface} of the total structure
	 * @param groupType the index of the groupType
	 * @return the integer number of bonds to add
	 */
	private static int findBondsToAdd(List<Integer> indicesToAdd, StructureDataInterface structureDataInterface, int groupType) {
		// Add the bonds if we've copied all the elements
		if(indicesToAdd.size()>1){
			if(indicesToAdd.size()==structureDataInterface.getNumAtomsInGroup(groupType)){
				return structureDataInterface.getGroupBondOrders(groupType).length;
			}
		}
		return 0;
	}

	/**
	 * Get the number of bonds, atoms and groups as a map.
	 * @param structureDataInterface the input {@link StructureDataInterface}
	 * @return the {@link SummaryData} object describing the data
	 */
	private static SummaryData getDataSummaryData(StructureDataInterface structureDataInterface) {
		SummaryData summaryData = new SummaryData();
		summaryData.numChains = 0;
		summaryData.numGroups = 0;
		summaryData.numAtoms = 0;
		summaryData.numBonds = 0;
		int groupCounter = -1;
		int chainCounter=-1;
		for (int i=0; i<structureDataInterface.getNumModels(); i++){
			int numChains = structureDataInterface.getChainsPerModel()[i];
			for(int j=0; j<numChains; j++){
				chainCounter++;
				summaryData.numChains++;
				String chainType = EncoderUtils.getTypeFromChainId(structureDataInterface, chainCounter);
				for(int k=0; k<structureDataInterface.getGroupsPerChain()[chainCounter]; k++){
					groupCounter++;
					int groupType = structureDataInterface.getGroupTypeIndices()[groupCounter];
					List<Integer> indicesToAdd = getIndicesToAdd(structureDataInterface, groupType, chainType);
					// If there's an atom to add in this group - add it
					if(indicesToAdd.size()>0){
						summaryData.numGroups++;
					}
					for(int l=0; l<structureDataInterface.getNumAtomsInGroup(groupType);l++){
						if(indicesToAdd.contains(l)){
							summaryData.numAtoms++;
						}
					}
					// Add the bonds if we've copied all the elements
					if (findBondsToAdd(indicesToAdd, structureDataInterface, groupType)>0){
						summaryData.numBonds+=structureDataInterface.getGroupBondOrders(groupType).length;
					}
				}
			}
		}
		return summaryData;
	}

	/**
	 * Get the indices of atoms to add in this group. This is C-alpha, phosphate (DNA) and ligand atoms
	 * @param structureDataInterface the input {@link StructureDataInterface}
	 * @param groupType the index of this group in the groupList
	 * @param chainType the type of the chain (polymer, non-polymer, water).
	 * @return the list of indices (within the group) of atoms to consider
	 */
	private static List<Integer> getIndicesToAdd(StructureDataInterface structureDataInterface, int groupType,
			String chainType) {
		// The list to return
		List<Integer> outList = new ArrayList<>();
		// Get chain type
		if(chainType.equals("polymer")){
			for(int i=0; i<structureDataInterface.getNumAtomsInGroup(groupType); i++){
				String atomName = structureDataInterface.getGroupAtomNames(groupType)[i];
				String elementName = structureDataInterface.getGroupElementNames(groupType)[i];
				// Check if it's a Protein C-alpha
				if(atomName.equals(CALPHA_NAME) && elementName.equals(CARBON_ELEMENT)){
					outList.add(i);
				}
				// Check if it's a DNA phosphate
				if(atomName.equals(PHOSPHATE_NAME) && elementName.equals(PHOSPHATE_ELEMENT)){
					outList.add(i);
				}
			}
		}
		// Check if it's a non-polymer 
		else if (chainType.equals("non-polymer")){
			for(int i=0; i<structureDataInterface.getNumAtomsInGroup(groupType); i++){
				outList.add(i);
			}
		}
		else if(chainType.equals("water")){
			// We skip water
		}
		else{
			System.err.println("Unrecoginised entity type: "+chainType);
		}
		return outList;
	}




}
