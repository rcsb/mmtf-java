package org.rcsb.mmtf.dataholders;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Group (residue) level data store.
 * @author Anthony Bradley
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PDBGroup implements Serializable {

  /** Serial id for this version of the format. */
  private static final long serialVersionUID = 2880633780569899800L;

  /** The group name. (e.g. HIS) */
  private String groupName;

  /** The atom names. A list of strings indicating
   * the atom names (e.g. CA for C-alpha). */
  private List<String> atomNameList;

  /** The element names. A list of strings indicating
   * the element names (e.g. Ca for Calcium). */
  private List<String> elementList;
  
  /** The bond orders. A list of integers indicating the bond orders*/
  private List<Integer> bondOrderList;

  /** The bond indices (in pairs).
   * (e.g. 0,1 means there is bond between atom 0 and 1).*/
  private List<Integer> bondAtomList;

  /** The atom charges. */
  private List<Integer> atomChargeList;

  /** The single letter code. */
  private char singleLetterCode;

  /** A string (taken from the chemical component dictionary) indicating 
   * the type of the group. Corresponds to -> http://mmcif.wwpdb.org/dictionaries/mmcif_pdbx.dic/Items/_chem_comp.type.html
   */
  private String chemCompType;
  
  /**
   * Constructor for the PDB group. Makes empty lists.
   */
  public PDBGroup(){
    groupName = new String();
    atomNameList = new ArrayList<String>();
    bondOrderList = new ArrayList<Integer>();
    bondAtomList = new ArrayList<Integer>();
    atomChargeList = new ArrayList<Integer>();
  }
  
  /**
   * Gets the atom info.
   *
   * @return the atom info
   */
  // Generic getter and setter functions
  public final List<String> getAtomNameList() {
    return atomNameList;
  }

  /**
   * Sets the atom info.
   *
   * @param inputAtomInfo the new atom info
   */
  public final void setAtomNameList(final List<String> inputAtomInfo) {
    this.atomNameList = inputAtomInfo;
  }

  /**
   * Gets the bond orders.
   *
   * @return the bond orders
   */
  public final List<Integer> getBondOrderList() {
    return bondOrderList;
  }

  /**
   * Sets the bond orders.
   *
   * @param inputBondOrders the new bond orders
   */
  public final void setBondOrderList(final List<Integer> inputBondOrders) {
    this.bondOrderList = inputBondOrders;
  }

  /**
   * Gets the bond indices.
   *
   * @return the bond indices
   */
  public final List<Integer> getBondAtomList() {
    return bondAtomList;
  }

  /**
   * Sets the bond indices.
   *
   * @param inputBondIndices the new bond indices
   */
  public final void setBondAtomList(final List<Integer> inputBondIndices) {
    this.bondAtomList = inputBondIndices;
  }

  /**
   * Gets the group name.
   *
   * @return the group name
   */
  public final String getGroupName() {
    return groupName;
  }

  /**
   * Sets the group name.
   *
   * @param resName the new group name
   */
  public final void setGroupName(final String resName) {
    this.groupName = resName;
  }

  /**
   * Gets the atom charges.
   *
   * @return the atom charges
   */
  public final List<Integer> getAtomChargeList() {
    return atomChargeList;
  }

  /**
   * Sets the atom charges.
   *
   * @param inputAtomCharges the new atom charges
   */
  public final void setAtomChargeList(final List<Integer> inputAtomCharges) {
    this.atomChargeList = inputAtomCharges;
  }

  /**
   * Gets the single letter code.
   *
   * @return the single letter code
   */
  public final char getSingleLetterCode() {
    return singleLetterCode;
  }

  /**
   * Sets the single letter code.
   *
   * @param inputSingleLetterCode the new single letter code
   */
  public final void setSingleLetterCode(final char inputSingleLetterCode) {
    this.singleLetterCode = inputSingleLetterCode;
  }

  /**
   * @return the groupType - corresponds to _chem_comp.type
   */
  public String getChemCompType() {
    return chemCompType;
  }

  /**
   * @param groupType the groupType (corresponds to _chem_comp.type) to set
   */
  public void setChemCompType(String groupType) {
    this.chemCompType = groupType;
  }

/**
 * @return the elementList
 */
public List<String> getElementList() {
	return elementList;
}

/**
 * @param elementList the elementList to set
 */
public void setElementList(List<String> elementList) {
	this.elementList = elementList;
}
}
