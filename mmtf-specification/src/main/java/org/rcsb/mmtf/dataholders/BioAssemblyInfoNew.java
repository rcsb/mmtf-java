package org.rcsb.mmtf.dataholders;

import java.util.List;

/**
 * Representation of a Biological Assembly annotation as provided by the PDB.
 * Contains all the information required to build the Biological Assembly from
 * the asymmetric unit.
 * Note that the PDB allows for 1 or more Biological Assemblies for a given
 * entry.
 * They are identified by the id field.
 * Modified by Anthony Bradley for message pack.
 * @author duarte_j
 * @author Anthony Bradley
 */
public class BioAssemblyInfoNew {

  /**
   * The id of this bioassembly.
   */
  private int id;
  /**
   * The specific transformations of this bioassembly.
   */
  private List<BiologicalAssemblyTransformationNew> transforms;
  /**
   * Getter for the id.
   * @return The id of the bioassebly
   */
  public final int getId() {
    return id;
  }

  /**
   * Sets the id.
   *
   * @param inputId the new id
   */
  public final void setId(final int inputId) {
    this.id = inputId;
  }

  /**
   * Gets the transforms.
   *
   * @return the transforms
   */
  public final List<BiologicalAssemblyTransformationNew> getTransforms() {
    return transforms;
  }

  /**
   * Sets the transforms.
   *
   * @param inputTransforms the new transforms
   */
  public final void setTransforms(final
      List<BiologicalAssemblyTransformationNew> inputTransforms) {
    this.transforms = inputTransforms;
  }

  /**
   * Gets the macromolecular size.
   *
   * @return the macromolecular size
   */
  public final int getMacromolecularSize() {
    return macromolecularSize;
  }

  /**
   * Sets the macromolecular size.
   *
   * @param inputMacromolecularSize the new macromolecular size
   */
  public final void setMacromolecularSize(final int inputMacromolecularSize) {
    this.macromolecularSize = inputMacromolecularSize;
  }

  /** The macromolecular size. */
  private int macromolecularSize;

}

