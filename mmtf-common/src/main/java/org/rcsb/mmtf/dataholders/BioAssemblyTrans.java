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
public class BioAssemblyTrans implements Serializable {


  /** Serial id for this version of the format. */
  private static final long serialVersionUID = -8109941242652091495L;


  /** The indices of the chains this bioassembly references. */
  private int[] chainIndexList;

  /** The 4x4 matrix transformation specifying a rotation and a translation. */
  private double[] transformation;


  /**
   * Gets the 4x4 matrix transformation specifying a rotation and a translation.
   * 
   * FIXME is this row-packed or column-packed?
   *
   * @return the transformation
   */
  public final double[] getMatrix() {
    return transformation;
  }

  /**
   * Sets the 4x4 matrix transformation specifying a rotation and a translation.
   *
   * FIXME is this row-packed or column-packed?
   * 
   * @param inputTransformation the new transformation
   */
  public final void setMatrix(final double[] transformation) {
    this.transformation = transformation;
  }

  /**
   * Gets the indices of the chains this bioassembly refers to.
   *
   * @return a list of integers indicating the indices (zero indexed) of the chains this bioassembly refers to.
   */
  public final int[] getChainIndexList() {
    return chainIndexList;
  }

  /**
   * Sets the chain id.
   *
   * @param  a list of integers indicating the indices (zero indexed) of the chains this bioassembly refers to.
   */
  public final void setChainIndexList(final int[] inputChainId) {
    this.chainIndexList = inputChainId;
  }

}
