package org.rcsb.mmtf.testutils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import org.biojava.nbio.structure.Chain;
import org.biojava.nbio.structure.EntityInfo;
import org.biojava.nbio.structure.Group;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.io.FileParsingParameters;
import org.rcsb.mmtf.api.DataApiInterface;
import org.rcsb.mmtf.decoder.SimpleDataApi;

/**
 * Class to test the raw API
 * @author Anthony Bradley
 *
 */
public class CheckOnRawApi {
	DataApiInterface dataApi;
	public CheckOnRawApi(byte[] inputData) {
		dataApi = new SimpleDataApi(inputData);
	}

	/**
	 * Check that required data is available the way we would expect.
	 * @param biojavaStruct The input structure (parsed from MMCIF) that can be used to compare.
	 * @param params The input file parsing parameters.
	 */
	public void checkRawDataConsistency(Structure biojavaStruct, FileParsingParameters params) {
		// Series of tests on expected values from the raw API
		assertNotEquals(dataApi.getMmtfProducer(), null);
		assertNotEquals(dataApi.getMmtfVersion(), null);
		checkIfSeqResInfoSame(biojavaStruct, params);
		checkIfEntityInfoSame(biojavaStruct);
		// Check other features in the data
	}


	/**
	 * Test to see if the roundtripped entity data is the same as is found in the MMCIF
	 */
	public void checkIfEntityInfoSame(Structure biojavaStruct) {    

		// Fist check it's not null
		assertNotEquals(dataApi.getNumEntities(), null);
		// Second check it's the same length
		assertEquals(dataApi.getNumEntities(), biojavaStruct.getEntityInfos().size());
		List<Chain> totChains = new ArrayList<>();
		totChains.addAll(biojavaStruct.getChains());
		// Now check it has the same information as BioJava
		for(int i=0; i<dataApi.getNumEntities(); i++) {
			EntityInfo biojavaEntity = biojavaStruct.getEntityInfos().get(i);
			assertEquals(dataApi.getEntityDescription(i), biojavaEntity.getDescription());
			assertEquals(dataApi.getEntityType(i), biojavaEntity.getType().toString());
			// Now check it maps onto the correct chains
			List<Chain> bioJavaChains = biojavaEntity.getChains();
			int[] mmtfList = dataApi.getEntityChainIndexList(i);
			assertEquals(mmtfList.length, bioJavaChains.size());
			int[] testList = new int[bioJavaChains.size()];
			for(int j=0; j<bioJavaChains.size();j++) {
				testList[j] = totChains.indexOf(bioJavaChains.get(j));
			}
			assertArrayEquals(testList, mmtfList);

		}
	}


	/**
	 * Test of sequence and seqres group level information. At the moment the decoder does not parse this data.
	 * This test checks to see if the underlying data is how one would expect.
	 * @param biojavaStruct
	 * @param params
	 */
	public void checkIfSeqResInfoSame(Structure biojavaStruct, FileParsingParameters params){
		if(params.isUseInternalChainId()){
			// Get the seqres group list
			int[] decodedSeqResGroupList = dataApi.getSeqResGroupIndices();
			// Get the string sequences
			int groupCounter = 0;
			int chainCounter = 0;
			// Get the sequence information - only for the first model
			String sequence = null;
			for(Chain currentChain : biojavaStruct.getChains()){
				for (int i=0; i<dataApi.getNumEntities(); i++) {
					for (int chainInd : dataApi.getEntityChainIndexList(i)) {
						if (chainInd==chainCounter) {
							sequence = dataApi.getEntitySequence(i);
							break;
						}
					}
				}
				assertEquals(sequence, currentChain.getSeqResSequence());
				List<Group> thisChainSeqResList = new ArrayList<>();
				for(Group seqResGroup : currentChain.getSeqResGroups()){
					thisChainSeqResList.add(seqResGroup);
				}
				// Now go through and check the indices line up
				for(int i = 0; i < currentChain.getAtomGroups().size(); i++){
					// Get the group
					Group testGroup = currentChain.getAtomGroup(i);
					int testGroupInd = thisChainSeqResList.indexOf(testGroup);
					assertEquals(testGroupInd, decodedSeqResGroupList[groupCounter]);
					groupCounter++;
				}
				chainCounter++;
			}
		}
		// Otherwise we need to parse in a different
		else{
			System.out.println("Using public facing chain ids -> seq res not tested");
		}

	}
}
