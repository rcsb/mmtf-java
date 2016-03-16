package org.rcsb.mmtf.dataholders;

import java.util.ArrayList;
import java.util.List;

/**
 * A bean to store the information about the protein structure.
 *
 * @author Anthony Bradley
 */
public class BioDataStructBean extends NoCoordDataStruct implements BioBean {

  
  // Coordinate infroramtion
	/** The _atom_site_cartn_x. */
	protected List<Double> _atom_site_Cartn_x = new ArrayList<Double>();
	
	/** The _atom_site_ cartn_y. */
	protected List<Double> _atom_site_Cartn_y = new ArrayList<Double>();
	
	/** The _atom_site_ cartn_z. */
	protected List<Double> _atom_site_Cartn_z = new ArrayList<Double>();
	
	/** The _atom_site_ b_iso_or_equiv. */
	// Isotropic atomic displacement parameter
	protected List<Float> _atom_site_B_iso_or_equiv= new ArrayList<Float>();
	
	/** The _atom_site_occupancy. */
	// The fraction of the atom present at this atom position_
	protected List<Float> _atom_site_occupancy= new ArrayList<Float>();
	
	/** The secondary  structure list. */
	// An array to store the secondary structure data
	private List<Integer> secStruct = new ArrayList<Integer>();
	
	/** The residue order list. */
	// An array to store the sequence of residues
	private List<Integer> resOrder = new ArrayList<Integer>();
	
	/** The inter-group bond indicess. */
	// Arrays to store the indices and bond orders of inter residue bonds
	private List<Integer> interGroupBondInds = new ArrayList<Integer>();
	
	/** The inter-group bond orders. */
	private List<Integer> interGroupBondOrders = new ArrayList<Integer>();
	
	
	/**
	 * Gets the _atom_site_id.
	 *
	 * @return the _atom_site_id
	 */
	public List<Integer> get_atom_site_id() {
		return _atom_site_id;
	}
	
	/**
	 * Sets the _atom_site_id.
	 *
	 * @param _atom_site_id the new _atom_site_id
	 */
	public void set_atom_site_id(List<Integer> _atom_site_id) {
		this._atom_site_id = _atom_site_id;
	}
	
	/** The _atom_site_id. */
	private List<Integer> _atom_site_id =  new ArrayList<Integer>();
	
	/**
	 * Gets the _atom_site_ cartn_x.
	 *
	 * @return the _atom_site_ cartn_x
	 */
	public List<Double> get_atom_site_Cartn_x() {
		return _atom_site_Cartn_x;
	}
	
	/**
	 * Sets the _atom_site_ cartn_x.
	 *
	 * @param _atom_site_Cartn_x the new _atom_site_ cartn_x
	 */
	public void set_atom_site_Cartn_x(ArrayList<Double> _atom_site_Cartn_x) {
		this._atom_site_Cartn_x = _atom_site_Cartn_x;
	}
	
	/**
	 * Gets the _atom_site_ cartn_y.
	 *
	 * @return the _atom_site_ cartn_y
	 */
	public List<Double> get_atom_site_Cartn_y() {
		return _atom_site_Cartn_y;
	}
	
	/**
	 * Sets the _atom_site_ cartn_y.
	 *
	 * @param _atom_site_Cartn_y the new _atom_site_ cartn_y
	 */
	public void set_atom_site_Cartn_y(ArrayList<Double> _atom_site_Cartn_y) {
		this._atom_site_Cartn_y = _atom_site_Cartn_y;
	}
	
	/**
	 * Gets the _atom_site_ cartn_z.
	 *
	 * @return the _atom_site_ cartn_z
	 */
	public List<Double> get_atom_site_Cartn_z() {
		return _atom_site_Cartn_z;
	}
	
	/**
	 * Sets the _atom_site_ cartn_z.
	 *
	 * @param _atom_site_Cartn_z the new _atom_site_ cartn_z
	 */
	public void set_atom_site_Cartn_z(ArrayList<Double> _atom_site_Cartn_z) {
		this._atom_site_Cartn_z = _atom_site_Cartn_z;
	}
	
	/**
	 * Gets the _atom_site_ b_iso_or_equiv.
	 *
	 * @return the _atom_site_ b_iso_or_equiv
	 */
	public List<Float> get_atom_site_B_iso_or_equiv() {
		return _atom_site_B_iso_or_equiv;
	}
	
	/**
	 * Sets the _atom_site_ b_iso_or_equiv.
	 *
	 * @param _atom_site_B_iso_or_equiv the new _atom_site_ b_iso_or_equiv
	 */
	public void set_atom_site_B_iso_or_equiv(ArrayList<Float> _atom_site_B_iso_or_equiv) {
		this._atom_site_B_iso_or_equiv = _atom_site_B_iso_or_equiv;
	}
	
	/**
	 * Gets the _atom_site_occupancy.
	 *
	 * @return the _atom_site_occupancy
	 */
	public List<Float> get_atom_site_occupancy() {
		return _atom_site_occupancy;
	}
	
	/**
	 * Sets the _atom_site_occupancy.
	 *
	 * @param _atom_site_occupancy the new _atom_site_occupancy
	 */
	public void set_atom_site_occupancy(ArrayList<Float> _atom_site_occupancy) {
		this._atom_site_occupancy = _atom_site_occupancy;
	}
	
	/* (non-Javadoc)
	 * @see org.rcsb.mmtf.dataholders.NoCoordDataStruct#getResOrder()
	 */
	public List<Integer> getResOrder() {
		return resOrder;
	}
	
	/* (non-Javadoc)
	 * @see org.rcsb.mmtf.dataholders.NoCoordDataStruct#setResOrder(java.util.List)
	 */
	public void setResOrder(List<Integer> resOrder) {
		this.resOrder = resOrder;
	}
	
	/* (non-Javadoc)
	 * @see org.rcsb.mmtf.dataholders.NoCoordDataStruct#getSecStruct()
	 */
	public List<Integer> getSecStruct() {
		return secStruct;
	}
	
	/* (non-Javadoc)
	 * @see org.rcsb.mmtf.dataholders.NoCoordDataStruct#setSecStruct(java.util.List)
	 */
	public void setSecStruct(List<Integer> secStruct) {
		this.secStruct = secStruct;
	}
	
	/* (non-Javadoc)
	 * @see org.rcsb.mmtf.dataholders.NoCoordDataStruct#getInterGroupBondOrders()
	 */
	public List<Integer> getInterGroupBondOrders() {
		return interGroupBondOrders;
	}
	
	/* (non-Javadoc)
	 * @see org.rcsb.mmtf.dataholders.NoCoordDataStruct#setInterGroupBondOrders(java.util.List)
	 */
	public void setInterGroupBondOrders(List<Integer> interGroupBondOrders) {
		this.interGroupBondOrders = interGroupBondOrders;
	}
	
	/* (non-Javadoc)
	 * @see org.rcsb.mmtf.dataholders.NoCoordDataStruct#getInterGroupBondInds()
	 */
	public List<Integer> getInterGroupBondInds() {
		return interGroupBondInds;
	}
	
	/* (non-Javadoc)
	 * @see org.rcsb.mmtf.dataholders.NoCoordDataStruct#setInterGroupBondInds(java.util.List)
	 */
	public void setInterGroupBondInds(List<Integer> interGroupBondInds) {
		this.interGroupBondInds = interGroupBondInds;
	}
}
