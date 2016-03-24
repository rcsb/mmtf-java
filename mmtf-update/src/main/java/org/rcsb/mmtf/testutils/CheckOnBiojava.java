package org.rcsb.mmtf.testutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.biojava.nbio.structure.Atom;
import org.biojava.nbio.structure.Chain;
import org.biojava.nbio.structure.Group;
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
   * @return
   */
  private boolean checkIfAtomsSame(Structure structOne, Structure structTwo) {
    // First check the bioassemblies
    checkIfBioassemblySame(structOne, structTwo);
    // Now check the pdb header
    // Now check the sequence
    int numModels = structOne.nrModels();
    if(numModels!=structTwo.nrModels()){
      System.out.println("ERROR - diff number models");
      return false;
    }
    for(int i=0;i<numModels;i++){
      List<Chain> chainsOne = structOne.getChains(i);
      List<Chain> chainsTwo = structTwo.getChains(i);

      if(chainsOne.size()!=chainsTwo.size()){
        System.out.println("ERROR - diff number chains");
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
          System.out.println("ERROR - diff number groups");
          return false;
        }
        for(int k=0; k<groupsOne.size();k++){
          Group groupOne = groupsOne.get(k);
          Group groupTwo = groupsTwo.get(k);
          // Check if the groups are of the same type
          if(groupOne.getType().equals(groupTwo.getType())==false){
            System.out.println(groupOne.getPDBName());
            System.out.println(groupTwo.getPDBName());
          }
          assertEquals(groupOne.getType(), groupTwo.getType());
          // Get the first conf
          List<Atom> atomsOne = groupOne.getAtoms();
          List<Atom> atomsTwo = groupTwo.getAtoms();
          if(groupOne.getAltLocs().size()!=0){
            if(groupTwo.getAltLocs().size()!=groupOne.getAltLocs().size()){
              System.out.println("ERROR - diff number alt locs");
            }
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
            System.out.println("ERROR - diff number atoms");
            System.out.println(groupOne.getPDBName()+" vs "+groupTwo.getPDBName());
            System.out.println(atomsOne.size()+" vs "+atomsTwo.size());
            return false;           
          }
          for(int l=0;l<atomsOne.size();l++){
            Atom atomOne = atomsOne.get(l);
            Atom atomTwo = atomsTwo.get(l);
            if(atomOne.toPDB().equals(atomTwo.toPDB())){
            	
            }
            else{
            	// If any of the coords is exactly zero - this is a +-0.0 issue
            	if (atomOne.getX()*atomOne.getY()*atomOne.getZ()==0.0){
            		
            	}
            	else{
              System.out.println("MINE"+atomOne.toPDB());
              System.out.println("BIOJAVA"+atomTwo.toPDB());
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
                System.out.println("DIFFERENT NUMBER OF BONDS: "+atomOne.getBonds().size()+" VS "+atomTwo.getBonds().size());
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
   * Checks if the bioassembly data between two Biojava structures are equivalent
   * @param structOne
   * @param structTwo
   * @return
   */
  private void checkIfBioassemblySame(Structure structOne, Structure structTwo){

    // Get the headers
    Map<Integer, BioAssemblyInfo> bioassembliesOne = structOne.getPDBHeader().getBioAssemblies();
    Map<Integer, BioAssemblyInfo> bioassembliesTwo = structTwo.getPDBHeader().getBioAssemblies();
    assertEquals(bioassembliesOne.keySet(), bioassembliesTwo.keySet());
    for(Entry<Integer, BioAssemblyInfo> entry: bioassembliesOne.entrySet()){
      // Get the bioassembly info
      BioAssemblyInfo valueOne = entry.getValue();
      BioAssemblyInfo valueTwo = bioassembliesTwo.get(entry.getKey());
      assertEquals(valueOne.getId(), valueTwo.getId());
      assertEquals(valueOne.getMacromolecularSize(), valueTwo.getMacromolecularSize());
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
   * Check if all features between the two structures  are the same
   * @param biojavaStruct the input biojava structure parsed from the  mmcif file
   * @param structTwo the BioJava structure parsed from the MMTF file
   */
  public void checkIfStructuresSame(Structure biojavaStruct, Structure structTwo){
	  checkIfBioassemblySame(biojavaStruct, structTwo);
    assertTrue(checkIfAtomsSame(biojavaStruct, structTwo));
  }
}
