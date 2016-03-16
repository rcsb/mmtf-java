package org.rcsb.mmtf.dataholders;

import java.io.Serializable;

import javax.vecmath.Point3d;


/**
 * A class to hold c-alpha coords.
 *
 * @author anthony
 */
public class CalphaAlignBean implements Serializable {

  private static final long serialVersionUID = 2471082879532014760L;

  /** The pdb id. Four letter code. */
  private String pdbId;

  /** The chain id. Specified by the asym id. */
  private String chainId;

  /** The polymer type. DNA/RNA or Amino acid. */
  private String polymerType;

  /** The sequence of all atoms in the construct (not just those resolved by the data). */
  private String sequence;
  
  /** The mapping of group to the sequence. Same length as point 3d id and indicates the position that
   * corresponds to that  position on the sequence. 
   */
  private int[] sequenceToCoordMap;

  /** The coordinate list. */
  private Point3d[] coordList;


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
   * @param inputPdbId the new pdb id
   */
  public final void setPdbId(final String inputPdbId) {
    this.pdbId = inputPdbId;
  }

  /**
   * Gets the chain id.
   *
   * @return the chain id
   */
  public final String getChainId() {
    return chainId;
  }

  /**
   * Sets the chain id.
   *
   * @param inputChainId the new chain id
   */
  public final void setChainId(final String inputChainId) {
    this.chainId = inputChainId;
  }

  /**
   * Gets the polymer type.
   *
   * @return the polymer type
   */
  public final String getPolymerType() {
    return polymerType;
  }

  /**
   * Sets the polymer type.
   *
   * @param inputPolymerType the new polymer type
   */
  public final void setPolymerType(final String inputPolymerType) {
    this.polymerType = inputPolymerType;
  }

  /**
   * Gets the sequence of all atoms in the construct (not just those resolved by the data).
   *
   * @return the sequence of all atoms in the construct (not just those resolved by the data)
   */
  public final String getSequence() {
    return sequence;
  }

  /**
   * Sets the sequence of all atoms in the construct (not just those resolved by the data).
   *
   * @param inputSequence the sequence of all atoms in the construct (not just those resolved by the data).
   * A char[]
   */
  public final void setSequence(final String inputSequence) {
    this.sequence = inputSequence;
  }

  /**
   * Gets the coord list.
   *
   * @return the coord list
   */
  public final Point3d[] getCoordList() {
    return coordList;
  }

  /**
   * Sets the coord list.
   *
   * @param inputCoordList the new coord list
   */
  public final void setCoordList(final Point3d[] inputCoordList) {
    this.coordList = inputCoordList;
  }

  /**
   * @return the sequenceToCoordMap
   */
  public int[] getSequenceToCoordMap() {
    return sequenceToCoordMap;
  }

  /**
   * @param sequenceToCoordMap the mapping of the position in the Point3d array to the position on the sequence. The array
   * is the same length as the point3d array and contains numbers within the range -1 (no position) to the length of the sequence 
   * -1 (the end of the sequence). The list is 0 indexed.
   */
  public void setSequenceToCoordMap(int[] sequenceToCoordMap) {
    this.sequenceToCoordMap = sequenceToCoordMap;
  }


}
