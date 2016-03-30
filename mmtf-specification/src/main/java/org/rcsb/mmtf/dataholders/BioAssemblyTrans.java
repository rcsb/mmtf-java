package org.rcsb.mmtf.dataholders;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The transformation needed for generation of biological assemblies
 * from the contents of a PDB/mmCIF file. It contains both the actual
 * transformation (rotation+translation) and the chain identifier to
 * which it should be applied.
 *
 * @author Anthony Bradley
 */
public class BioAssemblyTrans implements Serializable {


  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = -8109941242652091495L;


  /** The chain id. */
  private List<String> chainIdList = new ArrayList<String>();

  /** The 4x4 matrix transformation specifying a rotation and a translation. */
  private double[] transformation;


  /**
   * Gets the 4x4 matrix transformation specifying a rotation and a translation.
   * 
   * FIXME is this row-packed or column-packed?
   *
   * @return the transformation
   */
  public final double[] getTransformation() {
    return transformation;
  }

  /**
   * Sets the 4x4 matrix transformation specifying a rotation and a translation.
   *
   * FIXME is this row-packed or column-packed?
   * 
   * @param inputTransformation the new transformation
   */
  public final void setTransformation(final double[] inputTransformation) {
    this.transformation = inputTransformation;
  }

  /**
   * Gets the chain id list.
   *
   * @return the chain id list
   */
  public final List<String> getChainIdList() {
    return chainIdList;
  }

  /**
   * Sets the chain id.
   *
   * @param inputChainId the new chain id
   */
  public final void setChainIdList(final List<String> inputChainId) {
    this.chainIdList = inputChainId;
  }

}
