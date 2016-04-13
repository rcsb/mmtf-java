package org.rcsb.mmtf.dataholders;


import java.io.Serializable;

/**
 * The transformation needed for generation of biological assemblies
 * from the contents of a PDB/mmCIF file. It contains both the actual
 * transformation (rotation+translation) and the chain identifier to
 * which it should be applied.
 *
 * @author Anthony Bradley
 */
public class BioAssemblyTrans {


  /** The indices of the chains this bioassembly references. */
  private int[] chainIndexList;

  /** The 4x4 matrix transformation specifying a rotation and a translation. */
  private double[] matrix;


  /**
   * Gets the 4x4 matrix transformation specifying a rotation and a translation.
   * 
   * FIXME is this row-packed or column-packed?
   *
   * @return the transformation
   */
  public double[] getMatrix() {
    return matrix;
  }

  /**
   * Sets the 4x4 matrix transformation specifying a rotation and a translation.
   *
   * FIXME is this row-packed or column-packed?
   * 
   * @param inputTransformation the new transformation
   */
  public void setMatrix(double[] transformation) {
    this.matrix = transformation;
  }

  /**
   * Gets the indices of the chains this bioassembly refers to.
   *
   * @return a list of integers indicating the indices (zero indexed) of the chains this bioassembly refers to.
   */
  public int[] getChainIndexList() {
    return chainIndexList;
  }

  /**
   * Sets the chain id.
   *
   * @param  a list of integers indicating the indices (zero indexed) of the chains this bioassembly refers to.
   */
  public void setChainIndexList(int[] inputChainId) {
    this.chainIndexList = inputChainId;
  }

}
