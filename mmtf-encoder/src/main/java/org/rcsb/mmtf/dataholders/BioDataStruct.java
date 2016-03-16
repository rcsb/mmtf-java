package org.rcsb.mmtf.dataholders;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

/**
 * Class to store the basic biological data from an MMCIF file.
 *
 * @author Anthony Bradley
 */
public class BioDataStruct extends BioDataStructBean implements CoreSingleStructure {
	
	/**
	 * Instantiates a new bio data struct.
	 */
	public BioDataStruct() {
	}
	
	
	/* (non-Javadoc)
	 * @see org.rcsb.mmtf.dataholders.CoreSingleStructure#findDataAsBean()
	 */
	public BioDataStructBean findDataAsBean() {
		// Cast this to the pure data
		BioDataStructBean newData = new BioDataStructBean();
		try {
      BeanUtils.copyProperties(newData, this);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
		return newData;
	}	


	/* (non-Javadoc)
	 * @see org.rcsb.mmtf.dataholders.CoreSingleStructure#findNumAtoms()
	 */
	public int findNumAtoms() {
		return get_atom_site_Cartn_x().size();
	}

	/* (non-Javadoc)
	 * @see org.rcsb.mmtf.dataholders.CoreSingleStructure#findStructureCode()
	 */
	@Override
	public String findStructureCode() {
		// Get the PDB code
		return this.getPdbCode();
	}


}
