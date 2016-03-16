package org.rcsb.mmtf.dataholders;

import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.BeanUtils;


/**
 * Class to extend the NoFloatDataStructBean with functions
 *
 * @author Anthony Bradley
 */
public class NoFloatDataStruct extends NoFloatDataStructBean implements CoreSingleStructure {

	/* (non-Javadoc)
	 * @see org.rcsb.mmtf.dataholders.CoreSingleStructure#findDataAsBean()
	 */
	@SuppressWarnings("static-access")
	public BioBean findDataAsBean()  {
		// Cast this to the pure data
		NoFloatDataStructBean newData = new NoFloatDataStructBean();
		BeanUtils bu = new BeanUtils();
    try {
      bu.copyProperties(newData, this);
    } catch (IllegalAccessException e) {
      System.err.println("Unknown bug - copying bean data. Report as bug.");
      e.printStackTrace();
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      System.err.println("Unknown bug - copying bean data. Report as bug.");
      e.printStackTrace();
      throw new RuntimeException(e);
    }
		return newData;
	}

	/* (non-Javadoc)
	 * @see org.rcsb.mmtf.dataholders.CoreSingleStructure#findStructureCode()
	 */
	@Override
	public String findStructureCode() {
		return this.findStructureCode();
	}

	/* (non-Javadoc)
	 * @see org.rcsb.mmtf.dataholders.CoreSingleStructure#findNumAtoms()
	 */
	@Override
	public int findNumAtoms() {
		return this.findNumAtoms();
	}


}
