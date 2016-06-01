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

	/** The transformations of this bioassembly. */
	private List<BioAssemblyTransformation> transformList;
	
	/** The name of the Bioassembly. Can be user defined. */
	private String name;

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

	/**
	 * @return the name of the bioassembly - e.g. the assembly_id from mmCIF files.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set - e.g. the assembly_id from mmCIF files
	 */
	public void setName(String name) {
		this.name = name;
	}
}

