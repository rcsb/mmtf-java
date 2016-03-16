package org.rcsb.mmtf.dataholders;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A class to store the data sent in an MMTF data source.
 *
 * @author anthony
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FailureBean {

  /** The mmtf version. */
  private String mmtfVersion = "0.1";

  /** The mmtf producer. */
  private String mmtfProducer;

  /** The number of bonds. */
  private int numBonds;

  /** The pdb id. */
  private String pdbId;

  /** The title. */
  private String title;

  /** The number of atoms. */
  private int numAtoms;

  /** The number of chains per model. */
  private int[] chainsPerModel;

  /** The names of the chains. Each chain is allocated four bytes. Chain names can be up to four characters long. 0 bytes indicate the end of the chain name. These are taken from the auth id. */
  private byte[] chainNameList;

  /** The names of the chains. Each chain is allocated four bytes. Chain names can be up to four characters long. 0 bytes indicate the end of the chain name. These are taken from the asym id. */
  private byte[] chainIdList;

  /** The internal groups per chain. */
  private int[] groupsPerChain;

  /** The space group. */
  private String spaceGroup;

  /** The unit cell. */
  private List<Float> unitCell;

  /** The bio assembly. */
  private Map<Integer, BioAssemblyInfoNew> bioAssembly;

  /** The bond atom (indices) list. */
  private byte[] bondAtomList;

  /** The bond order list. */
  private byte[] bondOrderList;

  /** The group map. */
  // Map of all the data
  private  Map<Integer, PDBGroup> groupMap;

  /** The x coord big. 4 byte integers in pairs. */
  private byte[] xCoordBig;

  /** The y coord big. 4 byte integers in pairs. */
  private byte[] yCoordBig;

  /** The z coord big. 4 byte integers in pairs. */
  private byte[] zCoordBig;

  /** The b factor big. 4 byte integers in pairs. */
  private byte[] bFactorBig;

  /** The x coord small. 2 byte integers. */
  private byte[] xCoordSmall;

  /** The y coord small. 2 byte integers.*/
  private byte[] yCoordSmall;

  /** The z coord small. 2 byte integers.*/
  private byte[] zCoordSmall;

  /** The b factor small. 2 byte integers.*/
  private byte[] bFactorSmall;

  /** The secondary structure list. Stored as 1 byte ints. */
  private byte[] secStructList;

  /** The occupancy list. */
  private byte[] occList;

  /** The alt label list. */
  private List<String> altLabelList;

  /** The insertion code list. */
  private List<String> insCodeList;

  /** The group type list. */
  private byte[] groupTypeList;

  /** The group id list. Identifies each group along the chain. */
  private byte[]  groupIdList;

  /** The atom id list. */
  private byte[] atomIdList;
  
  /** The SEQRES sequence, per asym chain. */
  private List<String> chainSeqList;
  
  /** The SeqRes group ids. */
  private byte[] seqResIdList;
  
  /** The experimental method(s). */
  private List<String> experimentalMethods;
  // ADDED FIELDS TO DELIBERATELY FAIL TESTS
  @SuppressWarnings("unused")
  private List<String> fieldWithNoGetters;
  
  private List<String> fieldWithRefactoredGetters;


  /**
   * Gets the space group.
   *
   * @return the space group
   */
  public final String getSpaceGroup() {
    return spaceGroup;
  }

  /**
   * Sets the space group.
   *
   * @param inputSpaceGroup the new space group
   */
  public final void setSpaceGroup(final String inputSpaceGroup) {
    this.spaceGroup = inputSpaceGroup;
  }

  /**
   * Gets the unit cell.
   *
   * @return the unit cell
   */
  public final List<Float> getUnitCell() {
    return unitCell;
  }

  /**
   * Sets the unit cell.
   *
   * @param inputUnitCell the new unit cell
   */
  public final void setUnitCell(final List<Float> inputUnitCell) {
    this.unitCell = inputUnitCell;
  }

  /**
   * Gets the group num list.
   *
   * @return the group num list
   */
  public final byte[] getGroupIdList() {
    return groupIdList;
  }

  /**
   * Sets the group num list.
   *
   * @param inputGroupNumList the new group num list
   */
  public final void setGroupIdList(final byte[] inputGroupNumList) {
    this.groupIdList = inputGroupNumList;
  }

  /**
   * Gets the x coord big.
   *
   * @return the x coord big
   */
  public final byte[] getxCoordBig() {
    return xCoordBig;
  }

  /**
   * Sets the x coord big.
   *
   * @param inputXCoordBig the new 4 byte integer x coord array
   */
  public final void setxCoordBig(final byte[] inputXCoordBig) {
    this.xCoordBig = inputXCoordBig;
  }

  /**
   * Gets the y coord big.
   *
   * @return the y coord big
   */
  public final byte[] getyCoordBig() {
    return yCoordBig;
  }

  /**
   * Sets the y coord big.
   *
   * @param inputYCoordBig the new 4 byte integer y coord array
   */
  public final void setyCoordBig(final byte[] inputYCoordBig) {
    this.yCoordBig = inputYCoordBig;
  }

  /**
   * Gets the z coord big.
   *
   * @return the z coord big
   */
  public final byte[] getzCoordBig() {
    return zCoordBig;
  }

  /**
   * Sets the z coord big.
   *
   * @param inputZCoordBig the new 4 byte integer z coord array
   */
  public final void setzCoordBig(final byte[] inputZCoordBig) {
    this.zCoordBig = inputZCoordBig;
  }

  /**
   * Gets the x coord small.
   *
   * @return the x coord small
   */
  public final byte[] getxCoordSmall() {
    return xCoordSmall;
  }

  /**
   * Sets the x coord small.
   *
   * @param inputXCoordSmall the new 2 byte integer x coord array
   */
  public final void setxCoordSmall(final byte[] inputXCoordSmall) {
    this.xCoordSmall = inputXCoordSmall;
  }

  /**
   * Gets the y coord small.
   *
   * @return the y coord small
   */
  public final byte[] getyCoordSmall() {
    return yCoordSmall;
  }

  /**
   * Sets the y coord small.
   *
   * @param inputYCoordSmall the new 2 byte integer y coord array
   */
  public final void setyCoordSmall(final byte[] inputYCoordSmall) {
    this.yCoordSmall = inputYCoordSmall;
  }

  /**
   * Gets the z coord small.
   *
   * @return the z coord small
   */
  public final byte[] getzCoordSmall() {
    return zCoordSmall;
  }

  /**
   * Sets the z coord small.
   *
   * @param inputZCoordSmall the new 2 byte integer z coord array
   */
  public final void setzCoordSmall(final byte[] inputZCoordSmall) {
    this.zCoordSmall = inputZCoordSmall;
  }

  /**
   * Gets the b factor big.
   *
   * @return the b factor big
   */
  public final byte[] getbFactorBig() {
    return bFactorBig;
  }

  /**
   * Sets the b factor big.
   *
   * @param inputBigBFactor the new b factor big
   */
  public final void setbFactorBig(final byte[] inputBigBFactor) {
    this.bFactorBig = inputBigBFactor;
  }

  /**
   * Gets the b factor small.
   *
   * @return the b factor small
   */
  public final byte[] getbFactorSmall() {
    return bFactorSmall;
  }

  /**
   * Sets the b factor small.
   *
   * @param inputSmallBFactor the new b factor 2 byte array
   */
  public final void setbFactorSmall(final byte[] inputSmallBFactor) {
    this.bFactorSmall = inputSmallBFactor;
  }

  /**
   * Gets the alt label list.
   *
   * @return the alt label list
   */
  public final List<String> getAltLabelList() {
    return altLabelList;
  }

  /**
   * Sets the alt label list.
   *
   * @param inputAltIdList the new alt id label list
   */
  public final void setAltLabelList(final List<String> inputAltIdList) {
    this.altLabelList = inputAltIdList;
  }

  /**
   * Gets the bio assembly.
   *
   * @return the bio assembly
   */
  public final Map<Integer, BioAssemblyInfoNew> getBioAssembly() {
    return bioAssembly;
  }

  /**
   * Gets the chain names. The byte array indicating the (up to four characters) name of the chain. This is taken from the auth id.
   *
   * @return the chain list
   */
  public final byte[] getChainNameList() {
    return chainNameList;
  }

  /**
   * Sets the chain names. The byte array indicating the (up to four characters) name of the chain. This is taken from the auth id.
   *
   * @param inputChainList the new chain list
   */
  public final void setChainNameList(final byte[] inputChainList) {
    this.chainNameList = inputChainList;
  }

  /**
   * Sets the bioassembly information.
   *
   * @param inputBioAssembly the bio assembly
   */
  public final void setBioAssembly(final Map<Integer,
      BioAssemblyInfoNew> inputBioAssembly) {
    this.bioAssembly = inputBioAssembly;
  }

  /**
   * Gets the num atoms.
   *
   * @return the num atoms
   */
  public final int getNumAtoms() {
    return numAtoms;
  }

  /**
   * Sets the num atoms.
   *
   * @param inputNumAtoms the new num atoms
   */
  public final void setNumAtoms(final int inputNumAtoms) {
    this.numAtoms = inputNumAtoms;
  }

  /**
   * Gets the occ list.
   *
   * @return the occ list
   */
  public final byte[] getOccList() {
    return occList;
  }

  /**
   * Sets the occ list.
   *
   * @param occupancy the new occ list
   */
  public final void setOccList(final byte[] occupancy) {
    this.occList = occupancy;
  }

  /**
   * Gets the insertion code list.
   *
   * @return the insertion code list
   */
  public final List<String> getInsCodeList() {
    return insCodeList;
  }

  /**
   * Sets the ins code list.
   *
   * @param inputInsertionCodeList the new insertion code list
   */
  public final void setInsCodeList(final List<String> inputInsertionCodeList) {
    this.insCodeList = inputInsertionCodeList;
  }

  /**
   * Gets the group map.
   *
   * @return the group map
   */
  public final Map<Integer, PDBGroup> getGroupMap() {
    return groupMap;
  }

  /**
   * Sets the group map.
   *
   * @param inputGroupMap the group map
   */
  public final void setGroupMap(final Map<Integer, PDBGroup> inputGroupMap) {
    this.groupMap = inputGroupMap;
  }

  /**
   * Gets the sec struct list.
   *
   * @return the sec struct list
   */
  public final byte[] getSecStructList() {
    return secStructList;
  }

  /**
   * Sets the sec struct list.
   *
   * @param secStruct the new sec struct list
   */
  public final void setSecStructList(final byte[] secStruct) {
    this.secStructList = secStruct;
  }

  /**
   * Gets the group type list.
   *
   * @return the group type list
   */
  public final byte[] getGroupTypeList() {
    return groupTypeList;
  }

  /**
   * Sets the group type list.
   *
   * @param resOrder the new group type list
   */
  public final void setGroupTypeList(final byte[] resOrder) {
    this.groupTypeList = resOrder;
  }

  /**
   * Gets the atom id list.
   *
   * @return the atom id list
   */
  public final byte[] getAtomIdList() {
    return atomIdList;
  }

  /**
   * Sets the atom id list.
   *
   * @param inputAtomIdList the new atom id list
   */
  public final void setAtomIdList(final byte[] inputAtomIdList) {
    this.atomIdList = inputAtomIdList;
  }

  /**
   * Gets the title.
   *
   * @return the title
   */
  public final String getTitle() {
    return title;
  }

  /**
   * Sets the title.
   *
   * @param inputTitle the new title
   */
  public final void setTitle(final String inputTitle) {
    this.title = inputTitle;
  }

  /**
   * Gets the pdb id.
   *
   * @return the pdb id
   */
  public final String getPdbId() {
    return pdbId;
  }

  /**
   * Sets the pdb id.
   *
   * @param pdbCode the new pdb id
   */
  public final void setPdbId(final String pdbCode) {
    this.pdbId = pdbCode;
  }

  /**
   * Gets the mmtf producer.
   *
   * @return the mmtf producer
   */
  public final String getMmtfProducer() {
    return mmtfProducer;
  }

  /**
   * Sets the mmtf producer.
   *
   * @param inputMmtfProducer the new mmtf producer
   */
  public final void setMmtfProducer(final String inputMmtfProducer) {
    this.mmtfProducer = inputMmtfProducer;
  }

  /**
   * Gets the mmtf version.
   *
   * @return the mmtf version
   */
  public final String getMmtfVersion() {
    return mmtfVersion;
  }

  /**
   * Sets the mmtf version.
   *
   * @param inputMmtfVersion the new mmtf version
   */
  public final void setMmtfVersion(final String inputMmtfVersion) {
    this.mmtfVersion = inputMmtfVersion;
  }

  /**
   * Gets the num bonds.
   *
   * @return the num bonds
   */
  public final int getNumBonds() {
    return numBonds;
  }

  /**
   * Sets the number of bonds.
   *
   * @param inputNumBonds the new num bonds
   */
  public final void setNumBonds(final int inputNumBonds) {
    this.numBonds = inputNumBonds;
  }

  /**
   * Gets the bond atom list.
   *
   * @return the bond atom list
   */
  public final byte[] getBondAtomList() {
    return bondAtomList;
  }

  /**
   * Sets the bond atom list.
   *
   * @param inputBondAtomList the new bond atom list
   */
  public final void setBondAtomList(final byte[] inputBondAtomList) {
    this.bondAtomList = inputBondAtomList;
  }

  /**
   * Gets the bond order list.
   *
   * @return the bond order list
   */
  public final byte[] getBondOrderList() {
    return bondOrderList;
  }

  /**
   * Sets the bond order list.
   *
   * @param inputBondOrderList the new bond order list
   */
  public final void setBondOrderList(final byte[] inputBondOrderList) {
    this.bondOrderList = inputBondOrderList;
  }

  /**
   * Gets the number of chains per model. Chains are currently specified by asym (internal) chain ids.
   *
   * @return the list of chains per model.
   */
  public final int[] getChainsPerModel() {
    return chainsPerModel;
  }

  /**
   * Sets the number of chains per model. Currently specified by asy (internal) chain ids.
   *
   * @param inputInternalChainsPerModel the new list of chains per model.
   */
  public final void setChainsPerModel(final int[]
      inputInternalChainsPerModel) {
    this.chainsPerModel = inputInternalChainsPerModel;
  }

  /**
   * Gets the number of groups per chain.
   *
   * @return the internal groups per chain
   */
  public final int[] getGroupsPerChain() {
    return groupsPerChain;
  }

  /**
   * Sets the number of groups in a chain.
   *
   * @param inputGroupsPerChain the new internal groups per chain
   */
  public final void setGroupsPerChain(final int[]
      inputGroupsPerChain) {
    this.groupsPerChain = inputGroupsPerChain;
  }

  /**
   * Gets the internal chain list.
   *
   * @return the internal chain list
   */
  public final byte[] getChainIdList() {
    return chainIdList;
  }

  /**
   * Sets the internal chain list.
   *
   * @param inputInternalChainList the new internal chain list
   */
  public final void setChainIdList(final byte[] inputInternalChainList) {
    this.chainIdList = inputInternalChainList;
  }

  /**
   * @return the experimental methods
   */
  public List<String> getExperimentalMethods() {
    return experimentalMethods;
  }

  /**
   * @param experimentalMethods the experimental methods to set
   */
  public void setExperimentalMethods(List<String> experimentalMethods) {
    this.experimentalMethods = experimentalMethods;
  }

  /**
   * @return the sequence on a per (asym) chain level.
   */
  public List<String> getChainSeqList() {
    return chainSeqList;
  }

  /**
   * @param sequence the list of strings (sequences per asym chain) to set.
   */
  public void setChainSeqList(List<String> sequence) {
    this.chainSeqList = sequence;
  }

  /**
   * @return the seqResGroupIds
   */
  public byte[] getSeqResIdList() {
    return seqResIdList;
  }

  /**
   * @param seqResGroupIds the seqResGroupIds to set
   */
  public void setSeqResIdList(byte[] seqResGroupIds) {
    this.seqResIdList = seqResGroupIds;
  }

  /**
   * @return the fieldWithGetters
   */
  public List<String> getFieldWithGetters() {
    return fieldWithRefactoredGetters;
  }

  /**
   * @param fieldWithGetters the fieldWithGetters to set
   */
  public void setFieldWithGetters(List<String> fieldWithGetters) {
    this.fieldWithRefactoredGetters = fieldWithGetters;
  } 

}
