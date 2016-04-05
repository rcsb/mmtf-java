package org.rcsb.mmtf.biocompressors;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.rcsb.mmtf.dataholders.BioDataStruct;
import org.rcsb.mmtf.dataholders.CoreSingleStructure;
import org.rcsb.mmtf.dataholders.NoFloatDataStruct;

/**
 * Class to compress a structure by turning doubles to integers.
 * @author Anthony Bradley
 *
 */
public class CompressDoubles implements BioCompressor, Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8942639615818134183L;

	/* (non-Javadoc)
	 * @see org.rcsb.mmtf.biocompressors.BioCompressor#compresStructure(org.rcsb.mmtf.dataholders.CoreSingleStructure)
	 */
	public final CoreSingleStructure compresStructure(CoreSingleStructure coress) {
		//  Take in the appropriate arrays 
		BioDataStruct bioDataS = (BioDataStruct) coress;
		NoFloatDataStruct noFloatDataS = new NoFloatDataStruct();

		try {
			BeanUtils.copyProperties(noFloatDataS, bioDataS);
		} catch (IllegalAccessException e) {
			System.err.println("Unknown bug - copying bean data. Report as bug.");
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			System.err.println("Unknown bug - copying bean data. Report as bug.");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		// Get all the arrays we want to compress
		// Set the coordinates
		noFloatDataS.set_atom_site_Cartn_xInt(getIntArrayFromDouble(bioDataS.get_atom_site_Cartn_x(),1000.0));
		noFloatDataS.set_atom_site_Cartn_yInt(getIntArrayFromDouble(bioDataS.get_atom_site_Cartn_y(),1000.0));
		noFloatDataS.set_atom_site_Cartn_zInt(getIntArrayFromDouble(bioDataS.get_atom_site_Cartn_z(),1000.0));
		// Now set the temperature factors and occupancy
		noFloatDataS.set_atom_site_B_iso_or_equivInt(getIntArrayFromFloat(bioDataS.get_atom_site_B_iso_or_equiv(),(float) 100.0));
		noFloatDataS.set_atom_site_occupancyInt(getIntArrayFromFloat(bioDataS.get_atom_site_occupancy(),(float) 100.0));
		// Now assign these to the new dataStructure
		return noFloatDataS;
	}

	/**
	 * Function to return an int array from a float array.
	 *
	 * @param inArray the input array of floats
	 * @param multiplier - the multiplication factor for conversion
	 * @return the integer array after conversion
	 */
	public final List<Integer> getIntArrayFromFloat(List<Float> inArray, float multiplier) {
		// Initialise the out array
		List<Integer> outArray = new ArrayList<Integer>(inArray.size());
		for(Float oldDouble: inArray){
			Integer newInt = (int) Math.round(oldDouble * multiplier);
			outArray.add(newInt);
		}
		return outArray;

	}

	/**
	 * Function to return an int array from a double array.
	 *
	 * @param inArray the input array of doubles
	 * @param multiplier the multiplier
	 * @return the int array from double
	 */
	public final List<Integer> getIntArrayFromDouble(List<Double> inArray, Double multiplier){
		// Initialise the out array
		List<Integer> outArray = new ArrayList<Integer>(inArray.size());
		for(Double oldDouble: inArray){
			Integer newInt = (int) Math.round(oldDouble * multiplier);
			outArray.add(newInt);
		}
		return outArray;

	}

}
