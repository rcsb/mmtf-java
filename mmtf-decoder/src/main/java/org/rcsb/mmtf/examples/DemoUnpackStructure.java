package org.rcsb.mmtf.examples;

import org.rcsb.mmtf.api.MmtfDecodedDataInterface;

/**
 * Demonstration of how to unpack a given structure using the dataApi.
 * @author Anthony Bradley
 *
 */
public class DemoUnpackStructure {


	public static void main(String[] args) {
		// Create an instance of the handle IO class
		HandleIO handleIO = new HandleIO();
		// The index of a given chain
		int chainIndex = 0;
		int groupIndex = 0;
		int atomIndex = 0;
		// Create the instance of this API
		MmtfDecodedDataInterface dataApi = handleIO.getDataApiFromUrlOrFile("4cup");
		// Now loop through the models
		for (int modelIndex=0; modelIndex<dataApi.getNumModels(); modelIndex++) {
			for (int chainCounter=0; chainCounter<dataApi.getChainsPerModel()[modelIndex]; chainCounter++){
				for (int groupCounter=0; groupCounter<dataApi.getGroupsPerChain()[chainIndex]; groupCounter++){
					int currentGroupTypeIndex = dataApi.getGroupTypeIndices()[groupIndex];
					for (int atomCounter=0; atomCounter<dataApi.getNumAtomsInGroup(currentGroupTypeIndex); atomCounter++) {
						// Use the atomIndex to get total structure info.
						float xCoord = dataApi.getxCoords()[atomIndex];
						int serialId = dataApi.getAtomIds()[atomIndex];
						float bFactor = dataApi.getbFactors()[atomIndex];
						// Use the atomCounter to get group level info
						String element = dataApi.getGroupElementNames(currentGroupTypeIndex)[atomCounter];
						System.out.println("Serial id: "+serialId+" X coordinate: "+xCoord+", B-factor: "+bFactor+", element: "+element);
						// Incremenet the atom index
						atomIndex++;
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