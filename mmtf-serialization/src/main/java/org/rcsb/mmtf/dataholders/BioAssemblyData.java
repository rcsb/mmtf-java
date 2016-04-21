package org.rcsb.mmtf.dataholders;

import java.io.Serializable;
import java.util.List;

/**
 * Data store for the biological assembly annotation as provided by the PDB.
 * Contains all the information required to build the Biological Assembly from
 * the asymmetric unit.
 * Note that the PDB allows for 1 or more Biological Assemblies for a given
 * entry. They are identified by the id field.
 * 
 * @author Anthony Bradley
 */
public class BioAssemblyData implements Serializable {
	
	/** Serial id for this version of the format. */
	private static final long serialVersionUID = -8448351152898393978L;
	
	
	/**
	 * The specific transformations of this bioassembly.
	 */
	private List<BioAssemblyTransformation> transformList;


	/**
	 * Gets the list of transforms.
	 *
	 * @return the transforms
	 */
	public final List<BioAssemblyTransformation> getTransformList() {
		return transformList;
	}

	/**
	 * Sets the transforms.
	 *
	 * @param inputTransforms the new transforms
	 */
	public final void setTransformList(final
			List<BioAssemblyTransformation> inputTransforms) {
		this.transformList = inputTransforms;
	}
}

