package org.rcsb.mmtf.dataholders;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to store the data after removal of floats.
 *
 * @author Anthony Bradley
 */
public class NoFloatDataStructBean extends NoCoordDataStruct implements BioBean {
	
	/** The _atom_site_cartn_x_int. */
	// All this information is stored as Integer arrays
	private List<Integer> _atom_site_Cartn_xInt = new ArrayList<Integer>();
	
	/** The _atom_site_cartn_y_int. */
	private List<Integer> _atom_site_Cartn_yInt  = new ArrayList<Integer>();
	
	/** The _atom_site_cartn_z_int. */
	private List<Integer>  _atom_site_Cartn_zInt  = new ArrayList<Integer>();
	
	/** The _atom_site_b_iso_or_equiv_int. */
	// Isotropic atomic displacement parameter
	private List<Integer>  _atom_site_B_iso_or_equivInt  = new ArrayList<Integer>();
	
	/** The _atom_site_occupancy_int. */
	// The fraction of the atom present at this atom position_
	private List<Integer>  _atom_site_occupancyInt  = new ArrayList<Integer>();
	
	/**
	 * Gets the atom serial ids.
	 *
	 * @return the atom serial ids
	 */
	public List<Integer> get_atom_site_id() {
		return _atom_site_id;
	}
	
	/**
	 * Sets the atom serial ids.
	 *
	 * @param _atom_site_id the new atom serial ids.
	 */
	public void set_atom_site_id(List<Integer> _atom_site_id) {
		this._atom_site_id = _atom_site_id;
	}
	
	/** The the atom serial ids. */
	private List<Integer> _atom_site_id =  new ArrayList<Integer>();
	
	/**
	 * Gets the _atom_site_cartn_x_int.
	 *
	 * @return the _atom_site_cartn_x_int
	 */
	public List<Integer> get_atom_site_Cartn_xInt() {
		return _atom_site_Cartn_xInt;
	}
	
	/**
	 * Sets the X coordinates stored as integers.
	 *
	 * @param _atom_site_Cartn_xInt the new X coordinates stored as integers.
	 */
	public void set_atom_site_Cartn_xInt(List<Integer> _atom_site_Cartn_xInt) {
		this._atom_site_Cartn_xInt = _atom_site_Cartn_xInt;
	}
	
	/**
	 * Gets the Y coordinates stored as integers.
	 *
	 * @return the Y coordinates stored as integers.
	 */
	public List<Integer> get_atom_site_Cartn_yInt() {
		return _atom_site_Cartn_yInt;
	}
	
	/**
	 * Sets the Y coordinates stored as integers.
	 *
	 * @param _atom_site_Cartn_yInt the new Y coordinates stored as integers.
	 */
	public void set_atom_site_Cartn_yInt(List<Integer> _atom_site_Cartn_yInt) {
		this._atom_site_Cartn_yInt = _atom_site_Cartn_yInt;
	}
	
	/**
	 * Gets the Z coordinates stored as integers.
	 *
	 * @return the Z coordinates stored as integers.
	 */
	public List<Integer> get_atom_site_Cartn_zInt() {
		return _atom_site_Cartn_zInt;
	}
	
	/**
	 * Sets the Z coordinates stored as integers.
	 *
	 * @param _atom_site_Cartn_zInt the new Z coordinates stored as integers.
	 */
	public void set_atom_site_Cartn_zInt(List<Integer> _atom_site_Cartn_zInt) {
		this._atom_site_Cartn_zInt = _atom_site_Cartn_zInt;
	}
	
	/**
	 * Gets the B factor stored as integers.
	 *
	 * @return the B factor stored as integers.
	 */
	public List<Integer> get_atom_site_B_iso_or_equivInt() {
		return _atom_site_B_iso_or_equivInt;
	}
	
	/**
	 * Sets the B factor data stored as integers.
	 *
	 * @param _atom_site_B_iso_or_equivInt the new B factor data stored as integers.
	 */
	public void set_atom_site_B_iso_or_equivInt(List<Integer> _atom_site_B_iso_or_equivInt) {
		this._atom_site_B_iso_or_equivInt = _atom_site_B_iso_or_equivInt;
	}
	
	/**
	 * Gets the occupancy data stored as integers.
	 *
	 * @return the occupancy data stored as integers
	 */
	public List<Integer> get_atom_site_occupancyInt() {
		return _atom_site_occupancyInt;
	}
	
	/**
	 * Sets the occupancy data stored as integers.
	 *
	 * @param _atom_site_occupancyInt the new the occupancy data stored as integers.
	 */
	public void set_atom_site_occupancyInt(List<Integer> _atom_site_occupancyInt) {
		this._atom_site_occupancyInt = _atom_site_occupancyInt;
	}


}
