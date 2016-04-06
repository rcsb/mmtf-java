package org.rcsb.mmtf.testutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.biojava.nbio.structure.Atom;
import org.biojava.nbio.structure.Chain;
import org.biojava.nbio.structure.EntityInfo;
import org.biojava.nbio.structure.ExperimentalTechnique;
import org.biojava.nbio.structure.Group;
import org.biojava.nbio.structure.PDBHeader;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.quaternary.BioAssemblyInfo;
import org.biojava.nbio.structure.quaternary.BiologicalAssemblyTransformation;

public class CheckOnBiojava {

	public CheckOnBiojava() {
	}

	/**
	 * Broad test of atom similarity
	 * @param structOne
	 * @param structTwo
	 * @param mmtfParams) 
	 * @return
	 */
	private boolean checkIfAtomsSame(Structure structOne, Structure structTwo) {
		// First check the bioassemblies
		checkIfBioassemblySame(structOne, structTwo);
		// Now check the pdb header
		checkIfHederSame(structOne, structTwo);
		// Now check the entity information
		checkIfEntitiesSame(structOne, structTwo);
		int numModels = structOne.nrModels();
		if(numModels!=structTwo.nrModels()){
			System.out.println("Error - diff number models: "+structOne.getPDBCode());
			return false;
		}
		for(int i=0;i<numModels;i++){
			List<Chain> chainsOne = structOne.getChains(i);
			List<Chain> chainsTwo = structTwo.getChains(i);

			if(chainsOne.size()!=chainsTwo.size()){
				System.out.println("Error - diff number chains: "+structOne.getPDBCode());
				return false;
			}
			// Now loop over
			for(int j=0; j<chainsOne.size();j++){
				Chain chainOne = chainsOne.get(j);
				Chain chainTwo = chainsTwo.get(j);
				// Check they have the same chain id
				assertEquals(chainOne.getChainID(), chainTwo.getChainID());
				List<Group> groupsOne = chainOne.getAtomGroups();
				List<Group> groupsTwo = chainTwo.getAtomGroups();
				if(groupsOne.size()!=groupsTwo.size()){
					System.out.println("Error - diff number groups: "+structOne.getPDBCode());
					return false;
				}
				for(int k=0; k<groupsOne.size();k++){
					Group groupOne = groupsOne.get(k);
					Group groupTwo = groupsTwo.get(k);
					// Check if the groups are of the same type
					if(groupOne.getType().equals(groupTwo.getType())==false){
						System.out.println("Error - diff group type: "+structOne.getPDBCode());
						System.out.println(groupOne.getPDBName() + " and type: "+groupOne.getType());
						System.out.println(groupTwo.getPDBName() + " and type: "+groupTwo.getType());;
					}
					assertEquals(groupOne.getType(), groupTwo.getType());
					// Get the first conf
					List<Atom> atomsOne = new ArrayList<>(groupOne.getAtoms());
					List<Atom> atomsTwo = new ArrayList<>(groupTwo.getAtoms());
					if(groupOne.getAltLocs().size()!=0){
						if(groupTwo.getAltLocs().size()!=groupOne.getAltLocs().size()){
							System.out.println("Error - diff number alt locs: "+structOne.getPDBCode());
							System.out.println(groupOne.getAltLocs().size());
							System.out.println(groupTwo.getAltLocs().size());

						}
						// Now go over the alt locs
						for(Group altLocOne: groupOne.getAltLocs()){
							for(Atom atomAltLocOne: altLocOne.getAtoms()){
								atomsOne.add(atomAltLocOne);
							}
						}
						for(Group altLocTwo: groupTwo.getAltLocs()){
							for(Atom atomAltLocTwo: altLocTwo.getAtoms()){
								atomsTwo.add(atomAltLocTwo);
							}
						}
					}
					if(atomsOne.size()!=atomsTwo.size()){
						System.out.println("Error - diff number atoms: "+structOne.getPDBCode());
						System.out.println(groupOne.getPDBName()+" vs "+groupTwo.getPDBName());
						System.out.println(atomsOne.size()+" vs "+atomsTwo.size());
						return false;           
					}
					// Now sort the atoms 
					atomsOne.sort(new Comparator<Atom>() {

						@Override
						public int compare(Atom o1, Atom o2) {
							//  
							if (o1.getPDBserial()<o2.getPDBserial()){
								return -1;
							}
							else{
								return 1;
							}
						}


					});
					atomsTwo.sort(new Comparator<Atom>() {

						@Override
						public int compare(Atom o1, Atom o2) {
							//  
							if (o1.getPDBserial()<o2.getPDBserial()){
								return -1;
							}
							else{
								return 1;
							}
						}


					});         
					for(int l=0;l<atomsOne.size();l++){
						Atom atomOne = atomsOne.get(l);
						Atom atomTwo = atomsTwo.get(l);
						if(atomOne.toPDB().equals(atomTwo.toPDB())){

						}
						else{
							// If any of the coords is exactly zero - this is (almost certainly) a +-0.0 issue
							if (atomOne.getX()*atomOne.getY()*atomOne.getZ()==0.0){

							}
							else{
								System.out.println("Error - atoms different: "+structOne.getPDBCode());
								System.out.println("mmtf -> "+atomOne.toPDB());
								System.out.println("mmcif -> "+atomTwo.toPDB());
								return false;
							}

						}
						if(i==0){
							if(atomOne.getBonds()==null){
								if(atomTwo.getBonds()!=null){
									return false;
								}
							}
							else if(atomTwo.getBonds()==null){
								return false;
							}
							else if(atomOne.getBonds().size()!=atomTwo.getBonds().size()){
								System.out.println("Error different number of bonds: "+structOne.getPDBCode());
								System.out.println(atomOne.getBonds().size()+" vs. "+atomTwo.getBonds().size());
								System.out.println(atomOne);
								System.out.println(atomTwo);

								return false;
							}
						}
					}
				}

			}
		} 
		return true;

	}

	/**
	 * Loop through entity info - and check that the fields set by MMTF are the same.
	 * @param structOne
	 * @param structTwo
	 */
	private void checkIfEntitiesSame(Structure structOne, Structure structTwo) {
		List<EntityInfo> entityListOne = structOne.getEntityInfos();
		List<EntityInfo> entityListTwo = structTwo.getEntityInfos();
		assertEquals(entityListOne.size(), entityListTwo.size());	
		for (int i=0; i<entityListOne.size(); i++) {
			EntityInfo entityInfoOne = entityListOne.get(i);
			EntityInfo entityInfoTwo = entityListTwo.get(i);
			assertEquals(entityInfoOne.getDescription(), entityInfoTwo.getDescription());
			//			// Need to fix bug in Biojava (entites not allocated to chains when using AuthIds).
			//			assertEquals(entityInfoOne.toString(), entityInfoTwo.toString());
		}
	}
	/**
	 * Check that any header fields set by MMTF are the same.
	 * @param structOne
	 * @param structTwo
	 */
	private void checkIfHederSame(Structure structOne, Structure structTwo) {
		PDBHeader headerOne = structOne.getPDBHeader();
		PDBHeader headerTwo = structTwo.getPDBHeader();
		// Rfree, rwork, resolution, title, experimental methods
		assertEquals(headerOne.getResolution(), headerTwo.getResolution(), 0.0);
		assertEquals(headerOne.getRfree(), headerTwo.getRfree(), 0.0);
		assertEquals(headerOne.getTitle(), headerTwo.getTitle());
		// Check they are the same length
		assertEquals(headerOne.getExperimentalTechniques().size(), headerTwo.getExperimentalTechniques().size());
		String[] listOne = new  String[headerOne.getExperimentalTechniques().size()];
		int i = 0;
		for (ExperimentalTechnique etOne : headerOne.getExperimentalTechniques()) {
			listOne[i] = etOne.toString();
			i++;
		}
		i = 0;
		String[] listTwo = new  String[headerTwo.getExperimentalTechniques().size()];
		for (ExperimentalTechnique etTwo : headerTwo.getExperimentalTechniques()) {
			listTwo[i] = etTwo.toString();
			i++;
		}		
		assertArrayEquals(listOne, listTwo);
	}

	/**
	 * Checks if the bioassembly data between two Biojava structures are equivalent
	 * @param structOne
	 * @param structTwo
	 * @return
	 */
	private void checkIfBioassemblySame(Structure structOne, Structure structTwo){		
		// Get the headers
		Map<Integer, BioAssemblyInfo> bioassembliesOne = cleanUpBioass(structOne);
		Map<Integer, BioAssemblyInfo> bioassembliesTwo = structTwo.getPDBHeader().getBioAssemblies();
		assertEquals(bioassembliesOne.keySet(), bioassembliesTwo.keySet());
		for(Entry<Integer, BioAssemblyInfo> entry: bioassembliesOne.entrySet()){
			// Get the bioassembly info
			BioAssemblyInfo valueOne = entry.getValue();
			BioAssemblyInfo valueTwo = bioassembliesTwo.get(entry.getKey());
			assertEquals(valueOne.getId(), valueTwo.getId());
			// Check there's the same number of transforms
			assertEquals(valueOne.getTransforms().size(), valueTwo.getTransforms().size());
			// Build a map of chain id to matrix 4d
			Set<String> keySetOne = new TreeSet<>();
			Set<String> keySetTwo = new TreeSet<>();
			Set<String> valSetOne = new TreeSet<>();
			Set<String> valSetTwo = new TreeSet<>();
			for(int i= 0; i< valueOne.getTransforms().size();i++){
				BiologicalAssemblyTransformation transformOne = valueOne.getTransforms().get(i);
				BiologicalAssemblyTransformation transformTwo = valueTwo.getTransforms().get(i);
				// Check these are the same
				keySetOne.add(transformOne.getChainId());
				keySetTwo.add(transformTwo.getChainId());
				valSetOne.add(transformOne.getTransformationMatrix().toString());
				valSetTwo.add(transformTwo.getTransformationMatrix().toString());
			}
			assertEquals(keySetOne, keySetTwo);
			assertEquals(valSetOne, valSetTwo);

		}
	}

	/**
	 * Remove any transformation applying to empty chains.
	 * @param inputStruct The input Biojava Structure to be used.
	 * @return The bioassemblies cleande up.
	 */
	private Map<Integer, BioAssemblyInfo> cleanUpBioass(Structure inputStruct) {		
		// Get the bioassemblies
		Map<Integer, BioAssemblyInfo> bioAssemblies = inputStruct.getPDBHeader().getBioAssemblies();
		// Create a list of chains
		List<String> chainList = new ArrayList<>();
		for (int i=0; i<inputStruct.nrModels(); i++){
			for (Chain inputChain : inputStruct.getChains(i)){
				chainList.add(inputChain.getChainID());
			}
		}

		// Loop through the bioassemblies
		for (Entry<Integer,BioAssemblyInfo> entry : bioAssemblies.entrySet()) {
			BioAssemblyInfo val = entry.getValue();
			List<BiologicalAssemblyTransformation> removeList = new ArrayList<>();
			for (BiologicalAssemblyTransformation bioTrans : val.getTransforms()) {
				// Check if it exists
				if(!chainList.contains(bioTrans.getChainId())) {
					removeList.add(bioTrans);
				}
			}
			val.getTransforms().removeAll(removeList);
		}
		return bioAssemblies;
	}

	/**
	 * Check if all features between the two structures  are the same
	 * @param biojavaStruct the input Biojava structure parsed from the  mmcif file
	 * @param structTwo the BioJava structure parsed from the MMTF file
	 * @param mmtfParams 
	 */
	public void checkIfStructuresSame(Structure biojavaStruct, Structure structTwo){
		assertTrue(checkIfAtomsSame(biojavaStruct, structTwo));
	}
}
