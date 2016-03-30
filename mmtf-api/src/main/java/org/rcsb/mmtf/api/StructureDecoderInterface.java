package org.rcsb.mmtf.api;

import java.util.List;

import org.rcsb.mmtf.dataholders.BioAssemblyData;

/**
 * Interface to inflate a given mmtf data source.
 *
 * @author Anthony Bradley
 */
public interface StructureDecoderInterface {

  /**
   * Sets the number of models.
   * @param modelCount Number of models
   */
  void setModelCount(int modelCount);

  /**
   * Sets the number of chains for a given model.
   * @param modelId Identifier of the model within the structure
   * @param chainCount Total number of chains within this model
   */
  void setModelInfo(int modelId, int chainCount);

  /**
   * Sets the information for a given chain.
   * @param chainId chain identifier - length of one to four
   * @param groupCount number of groups/residues in chain
   */
  void setChainInfo(String chainId, int groupCount);


  /**
   * Updates the information for this chain.
   *
   * @param chainId the chain id
   * @param groupCount the group count
   */
  void updateChainInfo(String chainId, int groupCount);


  /**
   * Sets the information for a given group / residue with atomic data.
   * @param groupName 3 letter code name of this group/residue
   * @param groupNumber sequence position of this group
   * @param insertionCode the one letter insertion code
   * @param polymerType A string indicating the type of group (as found in the chemcomp dictionary. Empty string if none available.
   * @param atomCount the number of atoms in the group
   */
  void setGroupInfo(String groupName, int groupNumber, char insertionCode,
      String polymerType, int atomCount);

  
  /**
   * Sets the atom level information for a given atom.
   * @param atomName 1-3 long string of the unique name of the atom
   * @param serialNumber a number counting atoms in a structure
   * @param alternativeLocationId a character indicating the alternate
   * location of the atom
   * @param x the x cartesian coordinate
   * @param y the y cartesian coordinate
   * @param z the z cartesian coordinate
   * @param occupancy the atomic occupancy
   * @param temperatureFactor the B factor (temperature factor)
   * @param element a 1-3 long string indicating the element of the atom
   * @param charge the atomic charge
   */
  void setAtomInfo(String atomName, int serialNumber, char alternativeLocationId, 
      float x, float y, float z, float occupancy, float temperatureFactor, String element, int charge);


  /**
   * Sets the Bioassembly information for the structure.
   *
   * @param keyToIndexMap mapping the id of the bioassembly to its name
   * @param sizeMap mapping the id of the bioassembly to it's size
   * @param bioAssemblyToIdsMap maps the bioassembly to the
   * ids of the transformations
   * @param bioassemblyToChainIdsMap the map of bioassembly
   * to the list of chain ids considered
   * @param bioassemblyToTransformationsMap the list of bioassemblies
   */
  void setBioAssemblyList(List<BioAssemblyData> inputBioAssemblies);

  /**
   * Sets the space group and unit cell information.
   *
   * @param spaceGroup the space group
   * @param list the list
   */
  void setXtalInfo(String spaceGroup, List<Float> list);

  /**
   * Sets the intra bonds for groups.
   *
   * @param thisBondIndOne the this bond ind one
   * @param thisBondIndTwo the this bond ind two
   * @param thisBondOrder the this bond order
   */
  void setGroupBonds(int thisBondIndOne, int thisBondIndTwo, int thisBondOrder);

  /**
   * Sets the inter group bonds between atoms.
   *
   * @param thisBondIndOne the this bond ind one
   * @param thisBondIndTwo the this bond ind two
   * @param thisBondOrder the this bond order
   */
  void setInterGroupBonds(int thisBondIndOne,
      int thisBondIndTwo, int thisBondOrder);
  
  
  /**
   * A generic function to be used before any additions to do any required pre-processing.
   * For example the user could use this to specify the amount of memory to be allocated.
   * @param totalNumAtoms The total number of atoms found in the data.
   * @param totalNumGroups The total number of groups found in the data.
   * @param totalNumChains The total number of chains found in the data.
   * @param totalNumModels The total number of models found in the data.
   */
  void prepareStructure(int totalNumAtoms, int totalNumGroups, int totalNumChains, int totalNumModels);
  
  /**
   * A generic function to be used at the end of all data addition to do required cleanup on the structure
   */
  void cleanUpStructure();
  
}
