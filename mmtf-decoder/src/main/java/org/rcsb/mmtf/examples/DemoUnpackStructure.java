package org.rcsb.mmtf.examples;

import org.rcsb.mmtf.api.MmtfDecodedDataInterface;

/**
 * Demonstration of how to unpack a given structure using the dataApi.
 * @author Anthony Bradley
 *
 */
public class DemoUnpackStructure {

	/**
	 * Main function to run the demo class
	 * @param args no args required
	 */
	public static void main(String[] args) {
		// Create an instance of the handle IO class
		HandleIO handleIO = new HandleIO();
		// The index of a given chain
		int chainIndex = 0;
		int groupIndex = 0;
		int structureAtomIndex = 0;
		// Create the instance of this API - in this case for the 4cup structure's data.
		MmtfDecodedDataInterface dataApi = handleIO.getDataApiFromUrlOrFile("4cup");
		// Now loop through the models
		for (int modelIndex=0; modelIndex<dataApi.getNumModels(); modelIndex++) {
			// Loop through the chains in this model
			for (int modelChainCounter=0; modelChainCounter<dataApi.getChainsPerModel()[modelIndex]; modelChainCounter++){
				// Loop through the groups in this chain
				for (int chainGroupCounter=0; chainGroupCounter<dataApi.getGroupsPerChain()[chainIndex]; chainGroupCounter++){
					// Get the indices mapping the groups to their types
					int currentGroupTypeIndex = dataApi.getGroupTypeIndices()[groupIndex];
					// Loop through the atoms in every group
					for (int groupAtomIndex=0; groupAtomIndex<dataApi.getNumAtomsInGroup(currentGroupTypeIndex); groupAtomIndex++) {
						// Use the structureAtomIndex to get total structure info.
						float xCoord = dataApi.getxCoords()[structureAtomIndex];
						int serialId = dataApi.getAtomIds()[structureAtomIndex];
						float bFactor = dataApi.getbFactors()[structureAtomIndex];
						// Use the atomCounter to get group level info
						String element = dataApi.getGroupElementNames(currentGroupTypeIndex)[groupAtomIndex];
						System.out.println("Serial id: "+serialId+" X coordinate: "+xCoord+", B-factor: "+bFactor+", element: "+element);
						// Increment the atom index
						structureAtomIndex++;
					}
					// Increment the groupIndex
					groupIndex++;
				}
				// Increment the chain index
				chainIndex++;
			}
		}
	}
}