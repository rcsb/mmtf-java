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
 * Modified for message pack
 * @author Peter Rose
 * @author Andreas Prlic
 * @author rickb
 * @author duarte_j
 * @author Anthony Bradley
 */
public class BiologicalAssemblyTransformationNew implements Cloneable,
Serializable {


  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = -8109941242652091495L;

  /** The id. */
  private String id;

  /** The chain id. */
  private List<String> chainId = new ArrayList<String>();

  /** The transformation. */
  private double[] transformation;

  /**
   * Gets the id.
   *
   * @return the id
   */
  public final String getId() {
    return id;
  }

  /**
   * Sets the id.
   *
   * @param inputId the new id
   */
  public final void setId(final String inputId) {
    this.id = inputId;
  }

  /**
   * Gets the transformation.
   *
   * @return the transformation
   */
  public final double[] getTransformation() {
    return transformation;
  }

  /**
   * Sets the transformation.
   *
   * @param inputTransformation the new transformation
   */
  public final void setTransformation(final double[] inputTransformation) {
    this.transformation = inputTransformation;
  }

  /**
   * Gets the chain id.
   *
   * @return the chain id
   */
  public final List<String> getChainId() {
    return chainId;
  }

  /**
   * Sets the chain id.
   *
   * @param inputChainId the new chain id
   */
  public final void setChainId(final List<String> inputChainId) {
    this.chainId = inputChainId;
  }
}
