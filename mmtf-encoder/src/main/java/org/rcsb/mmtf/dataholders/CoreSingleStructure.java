package org.rcsb.mmtf.dataholders;

import java.lang.reflect.InvocationTargetException;

/**
 * An interface required for core single structure types
 * @author abradley
 *
 */
public interface CoreSingleStructure {
	// Interface to deal with core structural data
	// Return the data as a HashMap
	
	/**
	 * Function to return the data as a bean
	 * @return the data as a bean
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	BioBean findDataAsBean();

	/**
	 * Function to return the structure code
	 * @return
	 */
	String findStructureCode();

	/**
	 * Function to return the number of atoms
	 * @return
	 */
	int findNumAtoms();
}
