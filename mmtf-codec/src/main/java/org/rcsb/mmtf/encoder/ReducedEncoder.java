package org.rcsb.mmtf.encoder;

import java.util.ArrayList;
import java.util.List;

import org.rcsb.mmtf.api.StructureDataInterface;
import org.rcsb.mmtf.dataholders.MmtfStructure;

/**
 * Convert a full format of the file to a reduced format.
 * @author Anthony Bradley
 *
 */
public class ReducedEncoder implements EncoderInterface {
	
	private MmtfStructure mmtfStructure;

	private static final String CALPHA_NAME = "CA";
	private static final String CARBON_ELEMENT = "C";
	private static final String PHOSPHATE_NAME = "P";
	private static final String PHOSPHATE_ELEMENT = "P";

	/**
	 * Constructor to implement the reduced encoder of the {@link StructureDataInterface}.
	 * @param structureDataInterface the input {@link StructureDataInterface}
	 */
	public ReducedEncoder(StructureDataInterface structureDataInterface) {
		// First convert to a reduced data type
		structureDataInterface = ReducedEncoder.getReduced(structureDataInterface);
		// Now just apply the default encoder on top of it
		DefaultEncoder defaultEncoder = new DefaultEncoder(structureDataInterface);
		this.mmtfStructure = defaultEncoder.getMmtfEncodedStructure();
	}
	
	
	/**
	 * Get the reduced form of the input {@link StructureDataInterface}.
	 * @param structureDataInterface the input {@link StructureDataInterface} 
	 * @return the reduced form of the {@link StructureDataInterface} as another {@link StructureDataInterface}
	 */
	public static StructureDataInterface getReduced(StructureDataInterface structureDataInterface) {
		// The transmission of the data goes through this
		AdapterToStructureData adapterToStructureData = new AdapterToStructureData();
		adapterToStructureData.initStructure(structureDataInterface.getNumBonds(), structureDataInterface.getNumAtoms(), structureDataInterface.getNumGroups(), 
				structureDataInterface.getNumChains(), structureDataInterface.getNumModels(), structureDataInterface.getStructureId());
		// Add the header and crystallographic information
		adapterToStructureData.setXtalInfo(structureDataInterface.getSpaceGroup(), structureDataInterface.getUnitCell());
		adapterToStructureData.setHeaderInfo(structureDataInterface.getRfree(), structureDataInterface.getRfree(),structureDataInterface.getResolution(), 
				structureDataInterface.getTitle(), structureDataInterface.getDepositionDate(), structureDataInterface.getReleaseDate(), structureDataInterface.getExperimentalMethods());
		// Transfer the bioassembly info
		for(int i=0; i<structureDataInterface.getNumBioassemblies(); i++) {
			for(int j =0; j<structureDataInterface.getNumTransInBioassembly(i); j++){
				adapterToStructureData.setBioAssemblyTrans(i, 
						structureDataInterface.getChainIndexListForTransform(i, j), 
						structureDataInterface.getMatrixForTransform(i, j));
			}
		}
		// Transfer the entity info
		for(int i=0; i<structureDataInterface.getNumEntities(); i++){
			adapterToStructureData.setEntityInfo(structureDataInterface.getEntityChainIndexList(i), structureDataInterface.getEntitySequence(i), 
					structureDataInterface.getEntityDescription(i), structureDataInterface.getEntityType(i));
		}
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
					List<Integer> indicesToAdd = getIndicesToAdd(structureDataInterface, groupType, chainType);
					// If there's an atom to add in this group - add it
					if(indicesToAdd.size()>0){
						adapterToStructureData.setGroupInfo(structureDataInterface.getGroupName(groupType), structureDataInterface.getGroupIds()[groupCounter], 
								structureDataInterface.getInsCodes()[groupCounter], structureDataInterface.getGroupChemCompType(groupType), structureDataInterface.getGroupAtomCharges(groupType).length,
								structureDataInterface.getGroupBondOrders(groupType).length, structureDataInterface.getGroupSingleLetterCode(groupType), structureDataInterface.getGroupSequenceIndices()[groupCounter], 
								structureDataInterface.getSecStructList()[groupCounter]);
						numGroups++;
					}
					for(int l=0; l<structureDataInterface.getNumAtomsInGroup(groupType);l++){
						atomCounter++;
						if(indicesToAdd.contains(l)){
							adapterToStructureData.setAtomInfo(structureDataInterface.getGroupAtomNames(groupType)[l], structureDataInterface.getAtomIds()[atomCounter], structureDataInterface.getAltLocIds()[atomCounter], 
									structureDataInterface.getxCoords()[atomCounter], structureDataInterface.getyCoords()[atomCounter], structureDataInterface.getzCoords()[atomCounter], 
									structureDataInterface.getOccupancies()[atomCounter], structureDataInterface.getbFactors()[atomCounter], structureDataInterface.getGroupElementNames(groupType)[l], structureDataInterface.getGroupAtomCharges(groupType)[l]);
						}
					}

					// Add the bonds if we've copied all the elements
					if(indicesToAdd.size()>1 && indicesToAdd.size()==structureDataInterface.getGroupBondOrders(groupType).length){
						for(int l=0; l<structureDataInterface.getGroupBondOrders(groupType).length; l++){
							int bondOrder = structureDataInterface.getGroupBondOrders(groupType)[l];
							int bondIndOne = structureDataInterface.getGroupBondIndices(groupType)[l*2];
							int bondIndTwo = structureDataInterface.getGroupBondIndices(groupType)[l*2+1];
							adapterToStructureData.setInterGroupBond(bondIndOne, bondIndTwo, bondOrder);
						}
					}
				}
				adapterToStructureData.setChainInfo(structureDataInterface.getChainIds()[i],
						structureDataInterface.getChainNames()[i], numGroups);
			}
		}
		adapterToStructureData.finalizeStructure();
		// Return the AdapterToStructureData
		return adapterToStructureData;
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



	@Override
	public MmtfStructure getMmtfEncodedStructure() {
		return mmtfStructure;
	}


}
